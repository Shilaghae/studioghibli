package application.ghiblimovie.features.photodetails;

import dagger.Subcomponent;

/**
 * @author anna
 */
@PhotoDetailsScope
@Subcomponent(modules = PhotoDetailsModule.class)
public interface PhotoDetailsComponent {
    void inject(PhotoDetailsActivity activity);
}
