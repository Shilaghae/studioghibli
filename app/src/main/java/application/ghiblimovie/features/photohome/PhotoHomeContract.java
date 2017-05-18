package application.ghiblimovie.features.photohome;

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

        void takeAPicture() throws IOException;

        void showErrorMessage();

        void updatePhotoList(Photo photo);

        void updatePhotoList(List<Photo> photos);
    }

    interface PhotoPresenter {

    }
}