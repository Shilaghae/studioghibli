package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.photorepository.Photo;
import application.ghiblimovie.photorepository.PhotoRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * @author anna
 */

public class PhotoHomePresenterImpl extends BasePresenter<PhotoHomeContract.PhotoHomeView>
        implements PhotoHomeContract.PhotoPresenter {

    private final Scheduler ioScheduler;
    private final Scheduler uiScheduler;
    private final PhotoRepository photoRepository;
    private final float photoWidthSize;

    PhotoHomePresenterImpl(final Scheduler ioScheduler,
                           final Scheduler mainScheduler,
                           final PhotoRepository photoRepository,
                           final float photoWidthSize) {
        this.ioScheduler = ioScheduler;
        this.uiScheduler = mainScheduler;
        this.photoRepository = photoRepository;
        this.photoWidthSize = photoWidthSize;
    }

    @Override
    public void onAttach(@NonNull final PhotoHomeContract.PhotoHomeView view) {
        super.onAttach(view);

        subscribe(photoRepository.getAllPhotos()
                .subscribe(photos -> Observable.fromIterable(photos).subscribe(photo ->view.updatePhotoList(photo))));

        subscribe(view.onTakeAPicture()
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takeAPicture();
                }));

        subscribe(view.onAddPhoto()
                .subscribe(photo -> {Photo p = new Photo(photo); photoRepository.addPhoto(p);view.updatePhotoList(p);}));

        subscribe(view.onClickPhotoItem().subscribe(view::showAddDetails));

        subscribe(view
                .onFineLocationPermissionRequest()
                .subscribe(accepted -> {
                    if (accepted) {
                        view.getLocation();
                    } else {
                        view.showPermissionDeniedMessage();
                    }
                }));

        subscribe(view.onGetLocation()
                .subscribe(s -> {
                    view.showLocation(s);
                }));
    }
}
