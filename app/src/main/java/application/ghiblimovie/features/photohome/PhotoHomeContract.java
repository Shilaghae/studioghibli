package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;

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

        void takeAPicture() throws IOException;

        void showErrorMessage();

        void updatePhotoList(Bitmap photo);

        void updatePhotoList(List<Bitmap> photos);
    }

    interface PhotoPresenter {

    }
}
