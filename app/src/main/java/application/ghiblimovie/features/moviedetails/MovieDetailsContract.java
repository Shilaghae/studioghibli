package application.ghiblimovie.features.moviedetails;

import application.ghiblimovie.base.BaseView;
import application.ghiblimovie.repositories.Movie;

/**
 * @author anna
 */

public class MovieDetailsContract {

    public interface MovieDetailsView extends BaseView {
        void initMovie(Movie movie);
    }

    public interface MovieDetailsPresenter {

    }
}
