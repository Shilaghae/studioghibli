package application.ghiblimovie.repositories;

import java.util.List;

import application.ghiblimovie.base.Movie;
import application.ghiblimovie.base.MovieRepositoryRealm;
import io.reactivex.Observable;

/**
 * @author anna
 */

public class DiskMovieStore implements MovieStore {

    private final MovieRepositoryRealm mMovieRepositoryRealm;

    public DiskMovieStore(final MovieRepositoryRealm movieRepositoryRealm) {
        mMovieRepositoryRealm = movieRepositoryRealm;
    }

    @Override
    public Observable<List<Movie>> getMovies() {
        return Observable.just(mMovieRepositoryRealm.getAllMovies());
    }

    @Override
    public void updateMovies(final List<Movie> movies) {
        mMovieRepositoryRealm.removeAllMovies();
        mMovieRepositoryRealm.addAllMovies(movies);
    }
}
