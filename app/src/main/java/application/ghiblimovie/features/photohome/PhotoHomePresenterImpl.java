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

    private final Scheduler ioScheduler;
    private final Scheduler androidMainScheduler;
    private final PhotoRepository photoRepository;

    public PhotoHomePresenterImpl(final Scheduler ioScheduler, final Scheduler mainScheduler, final PhotoRepository photoRepository) {
        this.ioScheduler = ioScheduler;
        this.androidMainScheduler = mainScheduler;
        this.photoRepository = photoRepository;
    }

    @Override
    public void onAttach(@NonNull final PhotoHomeContract.PhotoHomeView view) {
        super.onAttach(view);

        subscribe(photoRepository.getAllPhotos()
                .observeOn(androidMainScheduler)
                .subscribe(photos -> view.updatePhotoList(photos)));

        subscribe(view.onTakeAPicture()
                .observeOn(androidMainScheduler)
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takeAPicture();
                }));

        subscribe(view.onAddPhoto()
                .observeOn(androidMainScheduler)
                .subscribe(photoPath -> {
                            Photo photo = new Photo(photoPath);
                            photoRepository.addPhoto(photo);
                            view.updatePhotoList(photo);
                        }
                ));
    }
}
