package application.ghiblimovie.repositories;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author anna
 */

public interface MovieStore {
    Observable<List<Movie>> getMovies();
    void updateMovies(List<Movie> movies);
}
