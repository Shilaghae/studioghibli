package application.ghiblimovie.features.home;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import application.ghiblimovie.base.AppPreferences;
import application.ghiblimovie.base.BasePresenterTest;
import application.ghiblimovie.base.Movie;
import application.ghiblimovie.repositories.DiskMovieStore;
import application.ghiblimovie.repositories.MovieRepositoryFactory;
import application.ghiblimovie.repositories.MovieStore;
import application.ghiblimovie.repositories.NetworkMovieStore;
import io.reactivex.Observable;
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
    private AppPreferences mMockAppPreferences;

    @Mock
    private MovieRepositoryFactory mMockMovieRepositoryFactory;

    @Mock
    private NetworkMovieStore mNetworkMovieStore;

    @Mock
    private DiskMovieStore mDiskMovieStore;

    private final PublishSubject<MovieStore> onRetrieveMovieStore = PublishSubject.create();
    private final PublishSubject<MovieStore> onUpdateMovieStore = PublishSubject.create();
    private final PublishSubject<Movie> onMovieItemClicked = PublishSubject.create();
    private final PublishSubject<Boolean> onCheckConnection = PublishSubject.create();

    @Override
    protected HomePresenterImpl createPresenter() {
        return new HomePresenterImpl(mMockAppPreferences, mMockMovieRepositoryFactory, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Override
    protected HomeContract.HomeView createView() {
        final HomeContract.HomeView mockHomeView = mock(HomeContract.HomeView.class);
        when(mMockMovieRepositoryFactory.onMovieStore()).thenReturn(onRetrieveMovieStore);
        when(mMockMovieRepositoryFactory.onMovieStoreUpdated()).thenReturn(onUpdateMovieStore);
        when(mockHomeView.onCheckConnection()).thenReturn(onCheckConnection);
        when(mockHomeView.onMovieItemClicked()).thenReturn(onMovieItemClicked);
        return mockHomeView;
    }

    @Test
    public void getMoviesFromInternet_MoMoviesToShow() {
        List<Movie> movies = new ArrayList<>();
        when(mNetworkMovieStore.getMovies()).thenReturn(Observable.just(movies));
        mPresenter.onAttach(mView);
        onRetrieveMovieStore.onNext(mNetworkMovieStore);
        verify(mView).showNoMoviesToShowMessage();
    }

    @Test
    public void getMoviesFromInternet_ShowTheMoviesRetrieved() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setDescription("This is actually a test!");
        movies.add(movie1);
        when(mNetworkMovieStore.getMovies()).thenReturn(Observable.just(movies));
        mPresenter.onAttach(mView);
        onRetrieveMovieStore.onNext(mNetworkMovieStore);
        verify(mView).showGhibliMovies(movies);
    }

    @Test
    public void getMoviesFromDisk_ShowNoMovies() {
        List<Movie> movies = new ArrayList<>();
        when(mDiskMovieStore.getMovies()).thenReturn(Observable.just(movies));
        mPresenter.onAttach(mView);
        onRetrieveMovieStore.onNext(mDiskMovieStore);
        verify(mView).showNoMoviesToShowMessage();
    }

    @Test
    public void getMoviesFromDisk_ShowTheMoviesRetrieved() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setDescription("This is actually a test!");
        movies.add(movie1);
        when(mNetworkMovieStore.getMovies()).thenReturn(Observable.just(movies));
        mPresenter.onAttach(mView);
        onRetrieveMovieStore.onNext(mNetworkMovieStore);
        verify(mView).showGhibliMovies(movies);
    }

    @Test
    public void getMoviesFromInternetAndRepositoryIsNotUpdated_UpdateLocalMovieRepository() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setDescription("This is actually a test!");
        movies.add(movie1);
        when(mNetworkMovieStore.getMovies()).thenReturn(Observable.just(movies));
        when(mMockAppPreferences.isMovieListUpToDate()).thenReturn(false);
        mPresenter.onAttach(mView);
        onCheckConnection.onNext(true);
        onRetrieveMovieStore.onNext(mNetworkMovieStore);
        verify(mMockMovieRepositoryFactory).updateMovieStore();
        verify(mView).showGhibliMovies(movies);
        verify(mMockMovieRepositoryFactory).updateAllMovieStore(movies);
        verify(mMockAppPreferences).setMovieListUpToDate(true);
    }

    @Test
    public void getMoviesFromInternetAndRepositoryIsUpdated_DoesNotUpdateLocalMovieRepository() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setDescription("This is actually a test!");
        movies.add(movie1);
        when(mNetworkMovieStore.getMovies()).thenReturn(Observable.just(movies));
        when(mMockAppPreferences.isMovieListUpToDate()).thenReturn(true);
        mPresenter.onAttach(mView);
        onCheckConnection.onNext(true);
        onRetrieveMovieStore.onNext(mNetworkMovieStore);
        verify(mMockMovieRepositoryFactory).updateMovieStore();
        verify(mView).showGhibliMovies(movies);
        verify(mMockMovieRepositoryFactory, never()).updateAllMovieStore(movies);
        verify(mMockAppPreferences, never()).setMovieListUpToDate(true);
    }

    @Test
    public void getMoviesWhenThereIsNoConnection_TheRepositoryMustBeUpdated() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setDescription("This is actually a test!");
        movies.add(movie1);
        when(mDiskMovieStore.getMovies()).thenReturn(Observable.just(movies));
        mPresenter.onAttach(mView);
        onCheckConnection.onNext(false);
        onRetrieveMovieStore.onNext(mDiskMovieStore);
        verify(mMockMovieRepositoryFactory).updateMovieStore();
        verify(mView).showNoConnectionMessage();
        verify(mView).showGhibliMovies(movies);
        verify(mMockAppPreferences).setMovieListUpToDate(false);
    }

    @Test
    public void noConnectionAvailable_showNoConnectionMessage() {
        mPresenter.onAttach(mView);
        onCheckConnection.onNext(false);
        verify(mView).showNoConnectionMessage();
    }

    @Test
    public void connectionAvailable_hideConnectionMessage() {
        mPresenter.onAttach(mView);
        onCheckConnection.onNext(true);
        verify(mView).hideNoConnectionMessage();
    }

    @Test
    public void errorWhenRetrievingMovies_showErrorMessage() {
        mPresenter.onAttach(mView);
        onRetrieveMovieStore.onError(new Throwable());
        verify(mView).showErrorMessage();
    }

    @Test
    public void errorWhenRCheckingConnection_showErrorMessage() {
        mPresenter.onAttach(mView);
        onCheckConnection.onError(new Throwable());
        verify(mView).showErrorMessage();
    }

    @Test
    public void selectMovie_ShowMovieDetails() {
        Movie movie = new Movie();
        mPresenter.onAttach(mView);
        onMovieItemClicked.onNext(movie);
        verify(mView).showMovieDetails(movie);
    }
}
