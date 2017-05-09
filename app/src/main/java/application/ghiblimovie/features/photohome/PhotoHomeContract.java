package application.ghiblimovie.features.photohome;

import java.io.IOException;

import application.ghiblimovie.base.BaseView;
import application.ghiblimovie.photorepository.Photo;
import io.reactivex.Observable;

/**
 * @author anna
 */

class PhotoHomeContract {

    interface PhotoHomeView extends BaseView {

        Observable<Object> clickOnTakeAPicture();

        Observable<String> onAddPhoto();

        void takePictures() throws IOException;

        void showErrorMessage();

        void updatePhotoList(Photo photo);
    }

    interface PhotoPresenter {

    }
}
