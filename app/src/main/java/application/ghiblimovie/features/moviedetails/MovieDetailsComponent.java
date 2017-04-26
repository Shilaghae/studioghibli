package application.ghiblimovie.features.moviedetails;

import dagger.Subcomponent;

/**
 * @author anna
 */
@MovieDetailsScope
@Subcomponent(modules = MovieDetailsModule.class)
public interface MovieDetailsComponent {
    void inject(MovieDetailsActivity activity);
}
