package application.daggerttest.features;

import java.util.List;

import application.daggerttest.Movie;
import application.daggerttest.base.BaseView;

/**
 * @author anna
 */

public class HomeContract {

    public interface HomeView extends BaseView {

        void showGhibliMovies(List<Movie> movies);

        void showErrorMessage();

        void showNoConnectionMessage();

        void hideNoConnectionMessage();
    }

    public interface HomePresenter {

    }
}
