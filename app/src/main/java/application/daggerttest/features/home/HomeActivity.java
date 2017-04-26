package application.daggerttest.features.home;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import application.daggerttest.base.Movie;
import application.daggerttest.R;
import application.daggerttest.base.BaseActivity;
import application.daggerttest.base.BasePresenter;
import application.daggerttest.features.moviedetails.MovieDetailsActivity;
import butterknife.BindView;
import io.reactivex.Observable;

public class HomeActivity extends BaseActivity implements HomeContract.HomeView {

    @Inject
    HomePresenterImpl mHomePresenter;
    @BindView(R.id.offline)
    TextView mUserOfflineTextView;
    @BindView(R.id.home_activity_movieListRecyclerView)
    RecyclerView mMovieListRecyclerView;

    private MovieListAdapter mMovieListAdapter;

    public void init() {
        mMovieListAdapter = new MovieListAdapter();
        mMovieListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager movieRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mMovieListRecyclerView.setLayoutManager(movieRecyclerViewLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mMovieListRecyclerView.getContext(), movieRecyclerViewLayoutManager.getOrientation());
        mMovieListRecyclerView.addItemDecoration(dividerItemDecoration);
        mMovieListRecyclerView.setAdapter(mMovieListAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public HomeContract.HomeView getView() {
        return this;
    }

    @Override
    public BasePresenter getPresenter() {
        return mHomePresenter;
    }

    @Override
    public Observable<Movie> onMovieItemClicked() {
        return mMovieListAdapter.onMovieItemClicked();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, getString(R.string.home_activity_error_impossible_to_retrieve_movies), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoConnectionMessage() {
        mUserOfflineTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoConnectionMessage() {
        mUserOfflineTextView.setVisibility(View.GONE);
    }

    @Override
    public void showGhibliMovies(final List<Movie> movies) {
        mMovieListAdapter.updateAllMovies(movies);
    }

    @Override
    public void showMovieDetails(final Movie movie) {
        MovieDetailsActivity.startActivity(this, movie);
    }

    @Override
    public void inject() {
        getAppComponent().getHomeComponent(new HomeModule()).inject(this);
    }

}
