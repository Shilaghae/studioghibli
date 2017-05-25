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
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;

/**
 * @author anna
 */

public class PhotoHomePresenterImpl extends BasePresenter<PhotoHomeContract.PhotoHomeView>
        implements PhotoHomeContract.PhotoPresenter {

    private final Scheduler ioScheduler;
    private final Scheduler androidMainScheduler;
    private final PhotoRepository photoRepository;
    private final float photoWidthSize;

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
                .subscribeOn(ioScheduler)
                .flatMap(photoPaths -> Observable.create((ObservableOnSubscribe<List<Bitmap>>) e -> {
                    System.out.println("What thread am I in 2? " + Thread.currentThread().getName());
                    List<Bitmap> photoBitmaps = new ArrayList<>();
                    for (String photoPath : photoPaths) {
                        photoBitmaps.add(getThumbnailBitmap(photoPath));
                    }
                    e.onNext(photoBitmaps);
                    e.onComplete();
                }))
                .subscribeOn(androidMainScheduler)
                .observeOn(androidMainScheduler)
                .subscribe(bitmaps -> {
                    view.updatePhotoList(bitmaps);
                }));

        subscribe(view.onTakeAPicture()
                .subscribeOn(androidMainScheduler)
                .doOnError(e -> view.showErrorMessage())
                .subscribe(ignored -> {
                    view.takeAPicture();
                }));

        subscribe(view.onAddPhoto()
                .subscribeOn(androidMainScheduler)
                .doOnNext(photoPath -> photoRepository.addPhoto(new Photo(photoPath)))
                .subscribeOn(ioScheduler)
                .map(photoPath -> {
                    System.out.println("What thread am I in 3? " + Thread.currentThread().getName());
                    return getThumbnailBitmap(photoPath);
                })
                .subscribeOn(androidMainScheduler)
                .observeOn(androidMainScheduler)
                .subscribe(photoBitmap -> view.updatePhotoList(photoBitmap)));
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
