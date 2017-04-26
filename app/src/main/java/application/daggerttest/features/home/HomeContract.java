package application.daggerttest.features.home;

import java.util.List;

import application.daggerttest.base.Movie;
import application.daggerttest.base.BaseView;
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
    }

    public interface HomePresenter {

    }
}
