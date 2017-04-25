package application.daggerttest.features;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import javax.inject.Inject;

import application.daggerttest.Movie;
import application.daggerttest.base.AppPreferences;
import application.daggerttest.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author anna
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    private final GhibliService mGhibliService;
    private final AppPreferences mAppPreferences;

    @Inject
    HomePresenterImpl(final GhibliService ghibliService, final AppPreferences appPreferences) {
        mGhibliService = ghibliService;
        mAppPreferences = appPreferences;
    }

    @Override
    public void onAttach(@NonNull final HomeContract.HomeView view) {
        super.onAttach(view);

        subscribe(mGhibliService.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    view.showGhibliMovies(movies);
                    if (!mAppPreferences.isMovieListUpToDate()) {
                        // Change file
                        mAppPreferences.setMovieListUpToDate(true);
                    }
                },throwable -> {
                    // take it from the database
                    ArrayList<Movie> movies = new ArrayList<Movie>();
                    movies.add(new Movie("1", "Anna"));
                    movies.add(new Movie("2", "Beta"));
                    view.showGhibliMovies(movies);
                    mAppPreferences.setMovieListUpToDate(false);
                }));

//        subscribe(mAppPreferences.onMovieListUpToDate()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(isMovieListUpToDate -> {
//                    if (isMovieListUpToDate) {
//                        // take it from the database
//                        ArrayList<Movie> movies = new ArrayList<Movie>();
//                        movies.add(new Movie("1", "Anna"));
//                        movies.add(new Movie("2", "Beta"));
//                        return Observable.just(movies);
//                    } else {
//                        Observable<List<Movie>> userObservable = mGhibliService.getMovies();
//                        return userObservable;
//                    }
//                })
//                .subscribe(movies -> {
//                            view.showGhibliMovies(movies);
//                            if (!mAppPreferences.isMovieListUpToDate()) {
//                                // Change file
//                                mAppPreferences.setMovieListUpToDate(true);
//                            }
//                        },
//                        throwable -> {
//                            // take it from the database
//                            ArrayList<Movie> movies = new ArrayList<Movie>();
//                            movies.add(new Movie("1", "Anna"));
//                            movies.add(new Movie("2", "Beta"));
//                            view.showGhibliMovies(movies);
//                        }));
//
//        Observable<List<Movie>> userObservable = mGhibliService.getMovies();
//        subscribe(userObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(movies -> {
//                    view.showGhibliMovies(movies);
//                }));

    }
}
