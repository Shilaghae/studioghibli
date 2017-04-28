package application.ghiblimovie.features.home;

import java.util.List;

import application.ghiblimovie.base.Movie;
import application.ghiblimovie.base.BaseView;
import io.reactivex.Observable;

/**
 * @author anna
 */

public class HomeContract {

    public interface HomeView extends BaseView {

        Observable<Movie> onMovieItemClicked();

        void showGhibliMovies(List<Movie> movies);

        void showErrorMessage();

        void showNoConnectionMessage();

        void hideNoConnectionMessage();

        void showMovieDetails(Movie movie);

        void showNoMovie();
    }

    public interface HomePresenter {

    }
}
