package application.daggerttest.features;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import application.daggerttest.Movie;
import application.daggerttest.R;
import application.daggerttest.base.BaseActivity;
import application.daggerttest.base.BasePresenter;
import butterknife.BindView;

public class HomeActivity extends BaseActivity implements HomeContract.HomeView {

    @Inject
    HomePresenterImpl mHomePresenter;

    @BindView(R.id.init_message)
    TextView mInitMessageTextView;
    @BindView(R.id.offline)
    TextView mUserOfflineTextView;

    public void init() {
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
    public void inject() {
        getAppComponent().getHomeComponent(new HomeModule()).inject(this);
    }

    @Override
    public void showGhibliMovies(final List<Movie> movies) {
        if (movies.isEmpty()) {
            mInitMessageTextView.setText(getString(R.string.home_activity_no_movies_to_show));
            return;
        }

        String movie = "";
        for (Movie m : movies) {
            movie = movie + m.getTitle() + "\n";
        }
        mInitMessageTextView.setText(movie);
    }

}
