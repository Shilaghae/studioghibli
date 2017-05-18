package application.ghiblimovie.features.photohome;

import android.content.Intent;
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
import java.util.List;
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
    public static final String APPLICATION_GHIBLIMOVIE_FILEPROVIDER = "application.ghiblimovie.fileprovider";

    @Inject
    PhotoHomePresenterImpl presenter;

    @BindView(R.id.photohome_activity_recyclerview_photos)
    RecyclerView photoListRecyclerView;

    @BindView(R.id.photohome_activity_button_take_picture)
    FloatingActionButton takePictureFloatingButton;

    private PublishSubject<String> onAddPicurePublishSubject = PublishSubject.create();

    private PhotoAdapter photoAdapter;

    private String pictureAbsolutePath;

    @Override
    public void init() {
        photoAdapter = new PhotoAdapter(new ArrayList<>());
        photoListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        photoListRecyclerView.setAdapter(photoAdapter);
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
        return presenter;
    }

    @Override
    public void inject() {
        getAppComponent().getPhotoHomeComponent(new PhotoHomeModule()).inject(this);
    }

    @Override
    public Observable<Object> onTakeAPicture() {
        return RxView.clicks(takePictureFloatingButton);
    }

    @Override
    public void takeAPicture() throws IOException {
        final Intent takeAPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeAPictureIntent.resolveActivity(getPackageManager()) != null) {
            final File picture = createImageFile();
            if (picture != null) {
                pictureAbsolutePath = picture.getAbsolutePath();
                takeAPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, APPLICATION_GHIBLIMOVIE_FILEPROVIDER, picture));
                startActivityForResult(takeAPictureIntent, REQUEST_IMAGE_CAPTURE);
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
    public void updatePhotoList(final List<Photo> photos) {
        photoAdapter.addPhotos(photos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, pictureAbsolutePath, Toast.LENGTH_SHORT).show();
            onAddPicurePublishSubject.onNext(pictureAbsolutePath);
        }
    }

    @Override
    public Observable<String> onAddPhoto() {
        return onAddPicurePublishSubject;
    }

    private File createImageFile() throws IOException {
        String pictureFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date()) + "_";
        return File.createTempFile(pictureFileName, ".jpg", getExternalFilesDir(DIRECTORY_PICTURES));
    }
}
