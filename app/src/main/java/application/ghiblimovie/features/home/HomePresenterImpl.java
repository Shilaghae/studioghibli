package application.ghiblimovie.features.home;

import android.support.annotation.NonNull;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import application.ghiblimovie.base.AppPreferences;
import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.base.Movie;
import application.ghiblimovie.base.MovieRepositoryRealm;
import application.ghiblimovie.services.GhibliService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * @author anna
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    public static final long RETRY_ON_ERROR = 10000L;
    private final GhibliService mGhibliService;
    private final AppPreferences mAppPreferences;
    private final MovieRepositoryRealm mMovieRepositoryRealm;
    private final Scheduler mIoScheduler;
    private final Scheduler mMainScheduler;

    HomePresenterImpl(final GhibliService ghibliService, final AppPreferences appPreferences, final MovieRepositoryRealm movieRepositoryRealm, final Scheduler ioScheduler, final Scheduler mainScheduler) {
        mGhibliService = ghibliService;
        mAppPreferences = appPreferences;
        mMovieRepositoryRealm = movieRepositoryRealm;
        mIoScheduler = ioScheduler;
        mMainScheduler = mainScheduler;
    }

    @Override
    public void onAttach(@NonNull final HomeContract.HomeView view) {
        super.onAttach(view);

        subscribe(mGhibliService.getMovies()
                .subscribeOn(mIoScheduler)
                .observeOn(mMainScheduler)
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
                .subscribeOn(mIoScheduler)
                .observeOn(mMainScheduler)
                .subscribe(movie -> {
                    view.showMovieDetails(movie);
                })
        );
    }
}
