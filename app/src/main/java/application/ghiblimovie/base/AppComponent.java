package application.ghiblimovie.base;

import javax.inject.Singleton;

import application.ghiblimovie.features.photodetails.PhotoDetailsComponent;
import application.ghiblimovie.features.photodetails.PhotoDetailsModule;
import application.ghiblimovie.features.photohome.PhotoHomeComponent;
import application.ghiblimovie.features.photohome.PhotoHomeModule;
import dagger.Component;

/**
 * @author anna
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    PhotoHomeComponent getPhotoHomeComponent(PhotoHomeModule photoHomeModule);

    PhotoDetailsComponent getPhotoDetailsComponent(PhotoDetailsModule photoDetailsModule);

}
