package application.ghiblimovie.features.photohome;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

import com.jakewharton.rxbinding2.view.RxView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import application.ghiblimovie.R;
import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.features.photodetails.PhotoDetailsActivity;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.os.Environment.DIRECTORY_PICTURES;

public class PhotoHomeActivity extends BaseActivity<PhotoHomeContract.PhotoHomeView>
        implements PhotoHomeContract.PhotoHomeView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Inject
    PhotoHomePresenterImpl presenter;
    @BindView(R.id.photohome_activity_recyclerview_photos)
    RecyclerView photoListRecyclerView;
    @BindView(R.id.photohome_activity_button_take_picture)
    FloatingActionButton takePictureFloatingButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String APPLICATION_GHIBLIMOVIE_FILEPROVIDER = "application.ghiblimovie.fileprovider";
    private static final int NUM_COLUMNS = 3;
    private static final int ACCESS_FINE_LOCATION_CODE_REQUEST = 1;
    private static final String PHOTO_PATH = "PHOTO_PATH";

    private GoogleApiClient googleApiClient;
    private PublishSubject<String> onAddPicurePublishSubject = PublishSubject.create();
    private PublishSubject<Boolean> onPermissionAccepted = PublishSubject.create();
    private PublishSubject<String> onGetLocation = PublishSubject.create();
    private PhotoAdapter photoAdapter;
    private String pictureAbsolutePath;

    @Override
    public void init() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        photoAdapter = new PhotoAdapter();
        photoListRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        photoListRecyclerView.setAdapter(photoAdapter);
    }

    private float getSpanWidth() {
        final Resources resources = getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        return (metrics.widthPixels / NUM_COLUMNS);
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
        getAppComponent().getPhotoHomeComponent(new PhotoHomeModule(getSpanWidth())).inject(this);
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
            pictureAbsolutePath = picture.getAbsolutePath();
            takeAPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, APPLICATION_GHIBLIMOVIE_FILEPROVIDER, picture));
            startActivityForResult(takeAPictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,
                getString(R.string.photohome_activity_error_impossible_to_take_pictures),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePhotoList(final PhotoItem photo) {
        photoAdapter.addPhoto(photo);
    }

    @Override
    public void updatePhotoList(final List<PhotoItem> photos) {
        photoAdapter.addPhotos(photos);
    }

    @Override
    public void showAddDetails(final PhotoItem photoItem) {
        PhotoDetailsActivity.startActivity(this, photoItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, pictureAbsolutePath, Toast.LENGTH_SHORT).show();
            onAddPicurePublishSubject.onNext(pictureAbsolutePath);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        ACCESS_FINE_LOCATION_CODE_REQUEST);
            } else {
                onPermissionAccepted.onNext(true);
            }
        }
    }
    @Override
    public Observable<Boolean> onFineLocationPermissionRequest() {
        return onPermissionAccepted;
    }

    @SuppressLint("MissingPermission") @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionAccepted.onNext(true);
                } else {
                    onPermissionAccepted.onNext(false);
                }
            }
        }
    }

    @Override
    public Observable<String> onAddPhoto() {
        return onAddPicurePublishSubject;
    }

    @Override
    public Observable<PhotoItem> onClickPhotoItem() {
        return photoAdapter.onClickItem();
    }

    private File createImageFile() throws IOException {
        String pictureFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date()) + "_";
        return File.createTempFile(pictureFileName, ".jpg", getExternalFilesDir(DIRECTORY_PICTURES));
    }

    @Override protected void onSaveInstanceState(final Bundle bundle) {
        bundle.putString(PHOTO_PATH, pictureAbsolutePath);
        super.onSaveInstanceState(bundle);
    }

    @Override protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pictureAbsolutePath = savedInstanceState.getString(PHOTO_PATH, "");
    }

    @Override public void onConnected(@Nullable final Bundle bundle) {
        final FusedLocationProviderApi fusedLocationApi = LocationServices.FusedLocationApi;
        @SuppressLint("MissingPermission") Location lastLocation = fusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            String latitude = String.valueOf(lastLocation.getLatitude());
            String longitude = String.valueOf(lastLocation.getLongitude());
            onGetLocation.onNext(latitude + ":" + longitude);
        }
    }

    @Override public void onConnectionSuspended(final int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
    }

    @Override public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
    }

    @Override public void getLocation() {
        googleApiClient.connect();
    }

    @Override protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override public void showPermissionDeniedMessage() {
        Toast.makeText(this, R.string.no_permission_to_get_location, Toast.LENGTH_SHORT).show();
    }

    @Override public Observable<String> onGetLocation() {
        return onGetLocation;
    }

    @Override public void showLocation(final String location) {
        Toast.makeText(this, location, Toast.LENGTH_LONG).show();
    }
}
