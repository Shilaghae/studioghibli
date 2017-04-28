package application.ghiblimovie.features.home;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import application.ghiblimovie.base.AppPreferences;
import application.ghiblimovie.base.BasePresenterTest;
import application.ghiblimovie.base.Movie;
import application.ghiblimovie.base.MovieRepositoryRealm;
import application.ghiblimovie.services.GhibliService;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author anna
 */

public class HomePresenterImplTest extends BasePresenterTest<HomePresenterImpl, HomeContract.HomeView> {

    @Mock
    private GhibliService mMockGhibliService;

    @Mock
    private AppPreferences mMockAppPreferences;

    @Mock
    private MovieRepositoryRealm mMockMovieRepositoryRealm;

    private final PublishSubject<List<Movie>> onRetrieveMovies = PublishSubject.create();
    private final PublishSubject<Movie> onMovieItemClicked = PublishSubject.create();

    @Override
    protected HomePresenterImpl createPresenter() {
        return new HomePresenterImpl(mMockGhibliService, mMockAppPreferences, mMockMovieRepositoryRealm, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Override
    protected HomeContract.HomeView createView() {
        final HomeContract.HomeView mockHomeView = mock(HomeContract.HomeView.class);
        when(mMockGhibliService.getMovies()).thenReturn(onRetrieveMovies);
        when(mockHomeView.onMovieItemClicked()).thenReturn(onMovieItemClicked);
        return mockHomeView;
    }

    @Test
    public void onGetMoviesFromInternet() {
        when(mMockAppPreferences.isMovieListUpToDate()).thenReturn(false);
        mPresenter.onAttach(mView);
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie());
        movies.add(new Movie());
        onRetrieveMovies.onNext(movies);
        verify(mView).showGhibliMovies(movies);
        verify(mView).hideNoConnectionMessage();
        verify(mMockAppPreferences).setMovieListUpToDate(true);
    }
}
