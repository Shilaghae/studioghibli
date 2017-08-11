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
    private final List<PhotoItem> photoItems = new ArrayList<>();
    private PhotoItem photoItem;

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
                .map(photos -> {
                    final List<String> paths = new ArrayList<>();
                    Observable.fromIterable(photos).subscribe(photo -> paths.add(photo.getPhotoPath()));
                    return paths;

                })
                .doOnNext(photos -> {
                    System.out.println("What thread am I in 2? " + Thread.currentThread().getName());
                    Observable.fromIterable(photos)
                            .observeOn(ioScheduler)
                            .flatMap(photoPaths -> {
                                System.out.println("What thread am I in 5? " + Thread.currentThread().getName());
                                final Bitmap thumbnailBitmap = getThumbnailBitmap(photoPaths);
                                return Observable.just(new PhotoItem(photoPaths, thumbnailBitmap));
                            })
                            .subscribeOn(uiScheduler)
                            .observeOn(uiScheduler)
                            .subscribe(s -> {
                                System.out.println("What thread am I in 6? " + Thread.currentThread().getName());
                                view.updatePhotoList(s);
                                photoItems.add(s);
                            });
                })
                .subscribe());

        subscribe(view.onTakeAPicture()
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takeAPicture();
                }));

        subscribe(view.onAddPhoto()
                .doOnNext(photoPath -> photoRepository.addPhoto(new Photo(photoPath)))
                .observeOn(ioScheduler)
                .map(photoPath -> {
                    System.out.println("What thread am I in 3? " + Thread.currentThread().getName());
                    final Bitmap thumbnailBitmap = getThumbnailBitmap(photoPath);
                    photoItem = new PhotoItem(photoPath, thumbnailBitmap);
                    photoItems.add(photoItem);
                    return photoItem;
                })
                .observeOn(uiScheduler)
                .subscribeOn(uiScheduler)
                .subscribe(view::updatePhotoList));

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
                    photoItem.setLocation(s);
                    view.showLocation(s);
                }));
    }

    private Bitmap getThumbnailBitmap(final String photoPath) {
        final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        final int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int ratio = (int) (photoWidthSize * 100) / width;
        height = (height * ratio) / 100;
        return ThumbnailUtils.extractThumbnail(bitmap,
                (int) photoWidthSize,
                height);
    }
}
