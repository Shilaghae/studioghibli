package application.ghiblimovie.features.home;

import org.junit.Test;
import org.mockito.Mock;

import java.net.UnknownHostException;
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
import static org.mockito.Mockito.never;
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
    public void getMoviesFromInternet_ShowMovies() {
        mPresenter.onAttach(mView);
        List<Movie> movies = new ArrayList<>();
        onRetrieveMovies.onNext(movies);
        verify(mView).showGhibliMovies(movies);
        verify(mView).hideNoConnectionMessage();
    }

    @Test
    public void getMoviesFromDisk_ShowMovies() {
        List<Movie> movies = new ArrayList<>();
        when(mMockMovieRepositoryRealm.getAllMovies()).thenReturn(movies);
        mPresenter.onAttach(mView);
        onRetrieveMovies.onError(new UnknownHostException());
        verify(mView).showGhibliMovies(movies);
        verify(mView).showNoConnectionMessage();
        verify(mMockAppPreferences).setMovieListUpToDate(false);
    }

    @Test
    public void getMoviesFromInternetAndRepositoryIsNotUpdated_UpdateLocalMovieRepository() {
        when(mMockAppPreferences.isMovieListUpToDate()).thenReturn(false);
        mPresenter.onAttach(mView);
        List<Movie> movies = new ArrayList<>();
        onRetrieveMovies.onNext(movies);
        verify(mView).showGhibliMovies(movies);
        verify(mView).hideNoConnectionMessage();
        verify(mMockMovieRepositoryRealm).removeAllMovies();
        verify(mMockMovieRepositoryRealm).addAllMovies(movies);
        verify(mMockAppPreferences).setMovieListUpToDate(true);
    }

    @Test
    public void getMoviesFromInternetAndRepositoryIsUpdated_DoesNotUpdateLocalMovieRepository() {
        when(mMockAppPreferences.isMovieListUpToDate()).thenReturn(true);
        mPresenter.onAttach(mView);
        List<Movie> movies = new ArrayList<>();
        onRetrieveMovies.onNext(movies);
        verify(mView).showGhibliMovies(movies);
        verify(mView).hideNoConnectionMessage();
        verify(mMockMovieRepositoryRealm, never()).removeAllMovies();
        verify(mMockMovieRepositoryRealm, never()).addAllMovies(movies);
        verify(mMockAppPreferences, never()).setMovieListUpToDate(true);
    }

    @Test
    public void selectMovie_ShowMovieDetails() {
        Movie movie = new Movie();
        mPresenter.onAttach(mView);
        onMovieItemClicked.onNext(movie);
        verify(mView).showMovieDetails(movie);
    }
}
