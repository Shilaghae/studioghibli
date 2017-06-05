package application.ghiblimovie.features.photodetails;

import application.ghiblimovie.base.BaseView;
import io.reactivex.Observable;

/**
 * @author anna
 */

public class PhotoDetailsContractor {

    public interface PhotoDetailsView extends BaseView {

        Observable<Object> onClickAddDetails();

        Observable<String> onGetLocation();

        Observable<Boolean> onFineLocationPermissionRequest();

        void getLocation();

        void requireGetLocationPermission();

        void showPermissionDeniedMessage();

        void showLocation(String location);

        void restart();
    }
}
