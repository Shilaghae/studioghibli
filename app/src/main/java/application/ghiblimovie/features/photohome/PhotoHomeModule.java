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

    @Provides
    @PhotoHomeScope
    public PhotoHomePresenterImpl providePhotoHomePresenterImpl(final PhotoRepository photoRepository) {
        return new PhotoHomePresenterImpl(Schedulers.computation(), AndroidSchedulers.mainThread(), photoRepository);
    }
}
