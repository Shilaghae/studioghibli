package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

import application.ghiblimovie.base.BaseView;
import application.ghiblimovie.photorepository.Photo;
import io.reactivex.Observable;

/**
 * @author anna
 */

class PhotoHomeContract {

    interface PhotoHomeView extends BaseView {

        Observable<Object> onTakeAPicture();

        Observable<String> onAddPhoto();

        Observable<Photo> onClickPhotoItem();

        Observable<Boolean> onFineLocationPermissionRequest();

        Observable<String> onGetLocation();

        void takeAPicture() throws IOException;

        void showErrorMessage();

        void updatePhotoList(Photo photo, Bitmap bitmap);

        void updatePhotoList(List<Photo> photos);

        void showAddDetails(Photo photoItem);

        void getLocation();

        void showPermissionDeniedMessage();

        void showLocation(String location);
    }

    interface PhotoPresenter {

    }
}
