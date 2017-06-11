package application.ghiblimovie.features.photodetails;

import android.support.annotation.NonNull;

import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.features.photodetails.PhotoDetailsContractor.PhotoDetailsView;

class PhotoDetailsPresenterImpl extends BasePresenter<PhotoDetailsView> {

    @Override public void onAttach(@NonNull final PhotoDetailsView view) {
        super.onAttach(view);
    }
}
