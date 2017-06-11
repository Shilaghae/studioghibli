package application.ghiblimovie.features.photodetails;

import application.ghiblimovie.base.BaseView;
import io.reactivex.Observable;

class PhotoDetailsContractor {

    public interface PhotoDetailsView extends BaseView {

        Observable<Object> onClickAddDetails();
    }
}
