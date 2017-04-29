package application.ghiblimovie.features.moviedetails;

import application.ghiblimovie.repositories.Movie;
import dagger.Module;
import dagger.Provides;

/**
 * @author anna
 */

@Module
public class MovieDetailsModule {
    private final Movie mMovie;

    public MovieDetailsModule(final Movie movie) {
        mMovie = movie;
    }

    @MovieDetailsScope
    @Provides
    MovieDetailsPresenterImpl provideMovieDetailsPresenter() {
        return new MovieDetailsPresenterImpl(mMovie);
    }
}
