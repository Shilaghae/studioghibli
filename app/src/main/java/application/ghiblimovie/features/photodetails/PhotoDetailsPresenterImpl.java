package application.ghiblimovie.features.photodetails;

import android.support.annotation.NonNull;

import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.features.photodetails.PhotoDetailsContractor.PhotoDetailsView;

/**
 * @author anna
 */

class PhotoDetailsPresenterImpl extends BasePresenter<PhotoDetailsView> {

    @Override public void onAttach(@NonNull final PhotoDetailsView view) {
        super.onAttach(view);

        subscribe(view
                .onClickAddDetails()
                .subscribe(o -> {
                    view.requireGetLocationPermission();
                }));

        subscribe(view
                .onFineLocationPermissionRequest()
                .subscribe(accepted -> {
                    if (accepted) {
                        view.getLocation();
                    } else {
                        view.showPermissionDeniedMessage();
                    }
                }));

        subscribe(view.onGetLocation()
                .subscribe(location -> {
                    view.restart();
                    view.showLocation(location);
                }));
    }
}
