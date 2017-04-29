package application.ghiblimovie.features.home;

import java.util.List;

import application.ghiblimovie.base.BaseView;
import application.ghiblimovie.base.Movie;
import io.reactivex.Observable;

/**
 * @author anna
 */

public class HomeContract {

    public interface HomeView extends BaseView {

        Observable<Boolean> onCheckConnection();

        Observable<Movie> onMovieItemClicked();

        void showGhibliMovies(List<Movie> movies);

        void showErrorMessage();

        void showNoConnectionMessage();

        void hideNoConnectionMessage();

        void showMovieDetails(Movie movie);

        void showNoMoviesToShowMessage();
    }

    public interface HomePresenter {

    }
}
