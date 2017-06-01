package application.ghiblimovie.features.photodetails;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import javax.inject.Inject;

import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.features.photodetails.PhotoDetailsContractor.PhotoDetailsView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author anna
 */

public class PhotoDetailsActivity extends BaseActivity<PhotoDetailsView> implements PhotoDetailsView,
        ConnectionCallbacks, OnConnectionFailedListener {

    @Inject
    PhotoDetailsPresenterImpl presenter;

    private PublishSubject<String> onGetLocation = PublishSubject.create();

    private GoogleApiClient googleApiClient;

    @Override public int getLayoutId() {
        return 0;
    }

    @Override public PhotoDetailsView getView() {
        return this;
    }

    @Override public BasePresenter<PhotoDetailsView> getPresenter() {
        return presenter;
    }

    @Override public void inject() {
        getAppComponent().getPhotoDetailsComponent(new PhotoDetailsModule()).inject(this);
    }

    @Override public void init() {

    }

    @Override public Observable<Object> onClickAddDetails() {
        return null;
    }

    @Override
    public Observable<String> onGetLocation() {
        return onGetLocation;
    }

    @Override public void getLocation() {
        googleApiClient.connect();
    }

    @Override public void onConnected(@Nullable final Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            String latitude = String.valueOf(lastLocation.getLatitude());
            String longitude = String.valueOf(lastLocation.getLongitude());
            onGetLocation.onNext(latitude+":"+longitude);
        }
    }

    @Override public void onConnectionSuspended(final int i) {

    }

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }
}
