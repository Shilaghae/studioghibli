package application.ghiblimovie.features.home;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import application.ghiblimovie.R;
import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.repositories.Movie;
import application.ghiblimovie.base.NetworkStatusChangeReceiver;
import application.ghiblimovie.features.moviedetails.MovieDetailsActivity;
import butterknife.BindView;
import io.reactivex.Observable;

public class HomeActivity extends BaseActivity<HomeContract.HomeView> implements HomeContract.HomeView {

    @Inject
    HomePresenterImpl mHomePresenter;

    @Inject
    NetworkStatusChangeReceiver mNetworkChangeReceiver;

    @BindView(R.id.activity_home_textView_offline)
    TextView mUserOfflineTextView;
    @BindView(R.id.home_activity_recyclerView_movies)
    RecyclerView mMovieListRecyclerView;
    @BindView(R.id.home_activity_textView_no_movies)
    TextView mNoMovieTextView;

    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onResume() {
        registerReceiver(mNetworkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();

    }
    @Override
    protected void onStop() {
        unregisterReceiver(mNetworkChangeReceiver);
        super.onStop();
    }

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
    public Observable<Boolean> onCheckConnection() {
        return mNetworkChangeReceiver.onNetworkChangeStatus();
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
        mNoMovieTextView.setVisibility(View.GONE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        mMovieListAdapter.updateAllMovies(movies);
    }

    @Override
    public void showMovieDetails(final Movie movie) {
        MovieDetailsActivity.startActivity(this, movie);
    }

    @Override
    public void showNoMoviesToShowMessage() {
        mNoMovieTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void inject() {
        getAppComponent().getHomeComponent(new HomeModule()).inject(this);
    }

}
