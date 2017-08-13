package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final PhotoRepository repository;
    private final float width;
    private final Map<String, Photo> map = new HashMap<>();

    PhotoHomePresenterImpl(final Scheduler ioScheduler,
                           final Scheduler mainScheduler,
                           final PhotoRepository repository,
                           final float width) {
        this.ioScheduler = ioScheduler;
        this.uiScheduler = mainScheduler;
        this.repository = repository;
        this.width = width;
    }


    @Override
    public void onAttach(@NonNull final PhotoHomeContract.PhotoHomeView view) {
        super.onAttach(view);

//        subscribe(repository.getAllPhotos()
//                .subscribe(photos -> Observable.fromIterable(photos)
//                        .map(photo -> {
//                            map.put(photo.getAbsolutePath(), photo);
//                            return photo.getAbsolutePath();
//                        })
//                        .observeOn(ioScheduler)
//                        .flatMap(photoAbsolutePath -> Observable.just(new BitmapPhoto(photoAbsolutePath, getThumbnailBitmap(photoAbsolutePath))))
//                        .observeOn(uiScheduler)
//                        .subscribe(photoBitmap -> view.updatePhotoList(map.get(photoBitmap.photoAbsolutePath), photoBitmap.bitmapPhoto))
//                ));

        subscribe(repository.getAllPhotos()
                .map(photos -> {
                    List<String> absolutePhotoPaths = new ArrayList<>();
                    for (Photo photo : photos) {
                        absolutePhotoPaths.add(photo.getAbsolutePath());
                        map.put(photo.getAbsolutePath(), photo);
                    }
                    return absolutePhotoPaths;
                })
                .observeOn(ioScheduler)
                .doOnNext(paths -> Observable.fromIterable(paths)
                        .subscribe(photoAbsolutePath -> Observable.just(new BitmapPhoto(photoAbsolutePath, getThumbnailBitmap(photoAbsolutePath)))
                                .subscribeOn(uiScheduler)
                                .subscribe(photoBitmap -> view.updatePhotoList(map.get(photoBitmap.photoAbsolutePath), photoBitmap.bitmapPhoto))))
                .subscribe());

        subscribe(view.onTakeAPicture()
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takeAPicture();
                }));

        subscribe(view.onAddPhoto()
                .observeOn(ioScheduler)
                .flatMap(photoAbsolutePath -> Observable.just(new BitmapPhoto(photoAbsolutePath, getThumbnailBitmap(photoAbsolutePath))))
                .observeOn(uiScheduler)
                .subscribe(bitmapPhoto -> {
                    final Photo photo = new Photo(bitmapPhoto.photoAbsolutePath);
                    repository.addPhoto(photo);
                    view.updatePhotoList(photo, bitmapPhoto.bitmapPhoto);
                }));

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
                .subscribe(view::showLocation));
    }

    class BitmapPhoto {
        private String photoAbsolutePath;
        private final Bitmap bitmapPhoto;

        BitmapPhoto(final String photoAbsolutePath, final Bitmap bitmapPhoto) {
            this.photoAbsolutePath = photoAbsolutePath;
            this.bitmapPhoto = bitmapPhoto;
        }
    }

    private Bitmap getThumbnailBitmap(final String photoAbsolutePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoAbsolutePath);
        final int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int ratio = (int) (this.width * 100) / width;
        height = (height * ratio) / 100;
        return ThumbnailUtils.extractThumbnail(bitmap,
                (int) this.width,
                height);
    }
}
