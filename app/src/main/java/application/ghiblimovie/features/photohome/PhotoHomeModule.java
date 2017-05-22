package application.ghiblimovie.features.photohome;

import application.ghiblimovie.photorepository.PhotoRepository;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author anna
 */

@Module
public class PhotoHomeModule {

    private final float photoWidthSize;

    PhotoHomeModule(final float photoWidthSize) {
        this.photoWidthSize = photoWidthSize;
    }

    @Provides
    @PhotoHomeScope
    PhotoHomePresenterImpl providePhotoHomePresenterImpl(final PhotoRepository photoRepository) {
        return new PhotoHomePresenterImpl(Schedulers.computation(), AndroidSchedulers.mainThread(), photoRepository, photoWidthSize);
    }
}
