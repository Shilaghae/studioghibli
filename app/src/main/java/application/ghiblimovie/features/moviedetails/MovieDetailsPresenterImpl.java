package application.ghiblimovie.features.moviedetails;

import android.support.annotation.NonNull;

import application.ghiblimovie.repositories.Movie;
import application.ghiblimovie.base.BasePresenter;

/**
 * @author anna
 */

public class MovieDetailsPresenterImpl extends BasePresenter<MovieDetailsContract.MovieDetailsView> implements MovieDetailsContract.MovieDetailsPresenter {

    private final Movie mMovie;

    public MovieDetailsPresenterImpl(final Movie movie) {
        mMovie = movie;
    }

    @Override
    public void onAttach(@NonNull final MovieDetailsContract.MovieDetailsView view) {
        super.onAttach(view);
        view.initMovie(mMovie);
    }
}
