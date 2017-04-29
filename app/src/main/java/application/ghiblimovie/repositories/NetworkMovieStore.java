package application.ghiblimovie.repositories;

import java.util.List;

import application.ghiblimovie.services.GhibliService;
import io.reactivex.Observable;

/**
 * @author anna
 */

public class NetworkMovieStore implements MovieStore {

    private final GhibliService mGhibliService;

    public NetworkMovieStore(GhibliService ghibliService) {
        mGhibliService = ghibliService;
    }

    @Override
    public Observable<List<Movie>> getMovies() {
        return mGhibliService.getMovies();
    }

    @Override
    public void updateMovies(final List<Movie> movies) {
    }
}
