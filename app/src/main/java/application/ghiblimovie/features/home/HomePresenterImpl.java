package application.ghiblimovie.features.home;

import android.support.annotation.NonNull;

import application.ghiblimovie.base.AppPreferences;
import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.repositories.MovieRepositoryFactory;
import application.ghiblimovie.repositories.MovieStore;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * @author anna
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    private final AppPreferences mAppPreferences;
    private final MovieRepositoryFactory mMovieRepositoryFactory;
    private final Scheduler mIoScheduler;
    private final Scheduler mMainScheduler;
    private boolean isConnected;

    HomePresenterImpl(final AppPreferences appPreferences, final MovieRepositoryFactory movieRepositoryFactory, final Scheduler ioScheduler, final Scheduler mainScheduler) {
        mAppPreferences = appPreferences;
        mMovieRepositoryFactory = movieRepositoryFactory;
        mIoScheduler = ioScheduler;
        mMainScheduler = mainScheduler;
    }

    @Override
    public void onAttach(@NonNull final HomeContract.HomeView view) {
        super.onAttach(view);
        subscribe(Observable.merge(mMovieRepositoryFactory.onMovieStore(), mMovieRepositoryFactory.onMovieStoreUpdated())
                .observeOn(mMainScheduler)
                .flatMap(MovieStore::getMovies)
                .observeOn(mMainScheduler)
                .subscribeOn(mIoScheduler)
                .subscribe(movies -> {
                    if (movies.isEmpty()) {
                        view.showNoMoviesToShowMessage();
                    } else {
                        view.showGhibliMovies(movies);
                        if (isConnected) {
                            if (!mAppPreferences.isMovieListUpToDate()) {
                                mAppPreferences.setMovieListUpToDate(true);
                                mMovieRepositoryFactory.updateAllMovieStore(movies);
                            }
                        } else {
                            mAppPreferences.setMovieListUpToDate(false);
                        }
                    }
                }));

        subscribe(view.onCheckConnection()
                .subscribeOn(mIoScheduler)
                .observeOn(mMainScheduler)
                .retry()
                .subscribe(isConnected -> {
                    this.isConnected = isConnected;
                    if (!isConnected) {
                        view.showNoConnectionMessage();
                    } else {
                        view.hideNoConnectionMessage();
                    }
                    mMovieRepositoryFactory.updateMovieStore();
                }));

        subscribe(view.onMovieItemClicked()
                .subscribeOn(mIoScheduler)
                .observeOn(mMainScheduler)
                .subscribe(movie -> {
                    view.showMovieDetails(movie);
                })
        );
    }
}
