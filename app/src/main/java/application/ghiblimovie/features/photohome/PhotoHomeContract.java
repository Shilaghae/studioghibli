package application.ghiblimovie.features.photohome;

import java.io.IOException;
import java.util.List;

import application.ghiblimovie.base.BaseView;
import io.reactivex.Observable;

/**
 * @author anna
 */

class PhotoHomeContract {

    interface PhotoHomeView extends BaseView {

        Observable<Object> onTakeAPicture();

        Observable<String> onAddPhoto();

        Observable<PhotoItem> onClickPhotoItem();

        Observable<Boolean> onFineLocationPermissionRequest();

        Observable<String> onGetLocation();

        void takeAPicture() throws IOException;

        void showErrorMessage();

        void updatePhotoList(PhotoItem photo);

        void updatePhotoList(List<PhotoItem> photos);

        void showAddDetails(PhotoItem photoItem);

        void getLocation();

        void showPermissionDeniedMessage();

        void showLocation(String location);
    }

    interface PhotoPresenter {

    }
}
