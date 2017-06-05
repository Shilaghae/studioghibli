package application.ghiblimovie.features.photodetails;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import com.jakewharton.rxbinding2.view.RxView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import application.ghiblimovie.R;
import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.features.photodetails.PhotoDetailsContractor.PhotoDetailsView;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author anna
 */

public class PhotoDetailsActivity extends BaseActivity<PhotoDetailsView> implements PhotoDetailsView,
        ConnectionCallbacks, OnConnectionFailedListener {

    public static final int ACCESS_FINE_LOCATION_CODE_REQUEST = 1;
    @Inject
    PhotoDetailsPresenterImpl presenter;

    @BindView(R.id.photodetails_activity_button_getlocation)
    Button getLocationButton;

    private PublishSubject<String> onGetLocation = PublishSubject.create();

    private PublishSubject<Boolean> onPermissionAccepted = PublishSubject.create();

    private GoogleApiClient googleApiClient;

    @Override public int getLayoutId() {
        return R.layout.photodetails_activity;
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
        return RxView.clicks(getLocationButton);
    }

    @Override public Observable<Boolean> onFineLocationPermissionRequest() {
        return onPermissionAccepted;
    }

    @Override
    public Observable<String> onGetLocation() {
        return onGetLocation;
    }

    @Override public void getLocation() {
        googleApiClient.connect();
    }

    @Override public void showPermissionDeniedMessage() {
        Toast.makeText(this, R.string.no_permission_to_get_location, Toast.LENGTH_SHORT).show();
    }

    @Override public void showLocation(final String location) {
        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requireGetLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(this,
                                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                ACCESS_FINE_LOCATION_CODE_REQUEST))
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        ACCESS_FINE_LOCATION_CODE_REQUEST);
            }
        } else {
            onPermissionAccepted.onNext(true);
        }
    }

    @Override public void onConnected(@Nullable final Bundle bundle) {
        @SuppressLint("MissingPermission") Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (lastLocation != null) {
            String latitude = String.valueOf(lastLocation.getLatitude());
            String longitude = String.valueOf(lastLocation.getLongitude());
            onGetLocation.onNext(latitude + ":" + longitude);
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
    public void restart() {
        googleApiClient.disconnect();
    }

    @Override public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }

    public static void startActivity(final Context context) {
        context.startActivity(new Intent(context, PhotoDetailsActivity.class));
    }
}
