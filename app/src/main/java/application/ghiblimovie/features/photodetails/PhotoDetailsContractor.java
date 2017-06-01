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

        void getLocation();
    }
}
