package application.ghiblimovie.features.photohome;

import dagger.Subcomponent;

/**
 * @author anna
 */
@PhotoHomeScope
@Subcomponent(modules = PhotoHomeModule.class)
public interface PhotoHomeComponent {
    void inject(PhotoHomeActivity activity);
}
