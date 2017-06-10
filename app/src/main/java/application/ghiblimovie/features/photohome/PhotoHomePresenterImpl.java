package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.photorepository.Photo;
import application.ghiblimovie.photorepository.PhotoRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.schedulers.Schedulers;

/**
 * @author anna
 */

public class PhotoHomePresenterImpl extends BasePresenter<PhotoHomeContract.PhotoHomeView>
        implements PhotoHomeContract.PhotoPresenter {

    private final Scheduler ioScheduler;
    private final Scheduler androidMainScheduler;
    private final PhotoRepository photoRepository;
    private final float photoWidthSize;
    private ExecutorService executor = Executors.newFixedThreadPool(5, new RxThreadFactory("New Thread"));
    final List<PhotoItem> photoItems = new ArrayList<>();



    public PhotoHomePresenterImpl(final Scheduler ioScheduler,
            final Scheduler mainScheduler,
            final PhotoRepository photoRepository,
            final float photoWidthSize) {
        this.ioScheduler = ioScheduler;
        this.androidMainScheduler = mainScheduler;
        this.photoRepository = photoRepository;
        this.photoWidthSize = photoWidthSize;
    }

    @Override
    public void onAttach(@NonNull final PhotoHomeContract.PhotoHomeView view) {
        super.onAttach(view);

        subscribe(photoRepository.getAllPhotos()
                .subscribeOn(androidMainScheduler)
                .map(photos -> {
                    final List<String> photoPaths = new ArrayList<>();
                    for (Photo photo : photos) {
                        photoPaths.add(photo.getPhotoPath());
                    }
                    System.out.println("What thread am I in 1? " + Thread.currentThread().getName());
                    return photoPaths;
                })
                .observeOn(ioScheduler)
                .flatMap(photoPaths -> {
                    System.out.println("What thread am I in 2? " + Thread.currentThread().getName());
                    final CountDownLatch wait = new CountDownLatch(1);
                    Observable.fromIterable(photoPaths)
                            .subscribeOn(Schedulers.from(executor))
                            .subscribe(photoPath -> {
                                System.out.println("What thread am I in 5? " + Thread.currentThread().getName
                                        ());
                                final Bitmap thumbnailBitmap = getThumbnailBitmap(photoPath);

                                photoItems.add(new PhotoItem(photoPath, thumbnailBitmap));
                            }, e -> {}, wait::countDown);
                    wait.await();
                    return Observable.just(photoItems);
                })
                .observeOn(androidMainScheduler)
                .subscribe(view::updatePhotoList));

        subscribe(view.onTakeAPicture()
                .observeOn(androidMainScheduler)
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takeAPicture();
                }));

        subscribe(view.onAddPhoto()
                .subscribeOn(androidMainScheduler)
                .doOnNext(photoPath -> photoRepository.addPhoto(new Photo(photoPath)))
                .observeOn(ioScheduler)
                .map(photoPath -> {
                    System.out.println("What thread am I in 3? " + Thread.currentThread().getName());
                    final Bitmap thumbnailBitmap = getThumbnailBitmap(photoPath);
                    return new PhotoItem(photoPath, thumbnailBitmap);
                })
                .observeOn(androidMainScheduler)
                .subscribe(view::updatePhotoList));

        subscribe(view.onClickPhotoItem().subscribe(view::showAddDetails));
    }

    private Bitmap getThumbnailBitmap(final String photoPath) {
        final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        final int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int ratio = (int) (photoWidthSize * 100) / width;
        height = (height * ratio) / 100;
        final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap,
                (int) photoWidthSize,
                height);
        return thumbnail;
    }
}
