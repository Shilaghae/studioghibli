package application.ghiblimovie.features.photohome;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import application.ghiblimovie.R;
import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.photorepository.Photo;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * @author anna
 */

public class PhotoHomeActivity extends BaseActivity<PhotoHomeContract.PhotoHomeView> implements PhotoHomeContract.PhotoHomeView {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    PhotoHomePresenterImpl mPresenter;

    @BindView(R.id.photohome_activity_recyclerview_photos)
    RecyclerView mPhotoListRecyclerView;

    @BindView(R.id.photohome_activity_button_take_picture)
    FloatingActionButton mTakePictureFloatingButton;

    private PublishSubject<String> mOnAddPhoto = PublishSubject.create();

    private PhotoAdapter photoAdapter;

    private String mCurrentPhotoPath;

    @Override
    public void init() {
        photoAdapter = new PhotoAdapter(new ArrayList<>());
        mPhotoListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mPhotoListRecyclerView.setAdapter(photoAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.photohome_activity;
    }

    @Override
    public PhotoHomeContract.PhotoHomeView getView() {
        return this;
    }

    @Override
    public PhotoHomePresenterImpl getPresenter() {
        return mPresenter;
    }

    @Override
    public void inject() {
        getAppComponent().getPhotoHomeComponent(new PhotoHomeModule()).inject(this);
    }

    @Override
    public Observable<Object> clickOnTakeAPicture() {
        return RxView.clicks(mTakePictureFloatingButton);
    }

    @Override
    public void takePictures() throws IOException {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            final File photoFile;
                photoFile = createImageFile();
            if (photoFile != null) {
                final Uri photoURI = FileProvider.getUriForFile(this, "application.ghiblimovie.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, getString(R.string.photohome_activity_error_impossible_to_take_pictures), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePhotoList(final Photo photo) {
        photoAdapter.addPhoto(photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
            mOnAddPhoto.onNext(mCurrentPhotoPath);
        }
    }

    @Override
    public Observable<String> onAddPhoto() {
        return mOnAddPhoto;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =  getExternalFilesDir(DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
