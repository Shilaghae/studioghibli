package application.ghiblimovie.features.photodetails;

import dagger.Module;
import dagger.Provides;

/**
 * @author anna
 */

@Module
public class PhotoDetailsModule {

    @Provides
    @PhotoDetailsScope
    PhotoDetailsPresenterImpl providePhotoDetailsPresenterImpl() {
        return new PhotoDetailsPresenterImpl();
    }
}
