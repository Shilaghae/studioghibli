package application.ghiblimovie.features.photohome;


import android.support.annotation.NonNull;

import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.photorepository.Photo;
import application.ghiblimovie.photorepository.PhotoRepository;
import io.reactivex.Scheduler;

/**
 * @author anna
 */

public class PhotoHomePresenterImpl extends BasePresenter<PhotoHomeContract.PhotoHomeView> implements PhotoHomeContract.PhotoPresenter {

    private final Scheduler mIoScheduler;
    private final Scheduler mMainScheduler;
    private final PhotoRepository mPhotoRepository;

    public PhotoHomePresenterImpl(final Scheduler ioScheduler, final Scheduler mainScheduler, final PhotoRepository photoRepository) {
        mIoScheduler = ioScheduler;
        mMainScheduler = mainScheduler;
        mPhotoRepository = photoRepository;
    }

    @Override
    public void onAttach(@NonNull final PhotoHomeContract.PhotoHomeView view) {
        super.onAttach(view);

        subscribe(view.clickOnTakeAPicture()
                .observeOn(mMainScheduler)
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takePictures();
                }));

        subscribe(view.onAddPhoto()
                .observeOn(mMainScheduler)
                .subscribe(photoPath -> {
                            Photo photo = new Photo(photoPath);
                            mPhotoRepository.addPhoto(photo);
                            view.updatePhotoList(photo);
                        }
                ));
    }
}
