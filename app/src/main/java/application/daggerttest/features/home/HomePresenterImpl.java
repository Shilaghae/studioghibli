package application.daggerttest.features.home;

import android.support.annotation.NonNull;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import application.daggerttest.base.Movie;
import application.daggerttest.base.MovieRepositoryRealm;
import application.daggerttest.base.AppPreferences;
import application.daggerttest.base.BasePresenter;
import application.daggerttest.services.GhibliService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author anna
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    public static final long RETRY_ON_ERROR = 10000L;
    private final GhibliService mGhibliService;
    private final AppPreferences mAppPreferences;
    private final MovieRepositoryRealm mMovieRepositoryRealm;

    @Inject
    HomePresenterImpl(final GhibliService ghibliService, final AppPreferences appPreferences, final MovieRepositoryRealm movieRepositoryRealm) {
        mGhibliService = ghibliService;
        mAppPreferences = appPreferences;
        mMovieRepositoryRealm = movieRepositoryRealm;
    }

    @Override
    public void onAttach(@NonNull final HomeContract.HomeView view) {
        super.onAttach(view);

        subscribe(mGhibliService.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    if (error instanceof UnknownHostException) {
                        // take it from the database
                        List<Movie> movies = mMovieRepositoryRealm.getAllMovies();
                        view.showGhibliMovies(movies);
                        view.showNoConnectionMessage();
                        mAppPreferences.setMovieListUpToDate(false);
                    } else {
                        view.showErrorMessage();
                    }
                })
                .retryWhen(throwableObservable -> throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> Observable.timer(RETRY_ON_ERROR, TimeUnit.MILLISECONDS)))
                .subscribe(movies -> {
                    view.showGhibliMovies(movies);
                    view.hideNoConnectionMessage();
                    if (!mAppPreferences.isMovieListUpToDate()) {
                        // Change file
                        mMovieRepositoryRealm.removeAllMovies();
                        mMovieRepositoryRealm.addAllMovies(movies);
                        mAppPreferences.setMovieListUpToDate(true);
                    }
                }, Throwable::printStackTrace));

        subscribe(view.onMovieItemClicked()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie -> {
                    view.showMovieDetails(movie);
                })
        );
    }
}
