package application.daggerttest.features;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import application.daggerttest.Movie;
import application.daggerttest.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author anna
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    private final GhibliService mGhibliService;

    @Inject
    HomePresenterImpl(GhibliService ghibliService) {
        mGhibliService = ghibliService;
    }

    @Override
    public void onAttach(@NonNull final HomeContract.HomeView view) {
        super.onAttach(view);
        Observable<List<Movie>> userObservable = mGhibliService.getMovies();
        subscribe(userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    view.showGhibliMovies(movies);
                }));

    }
}
