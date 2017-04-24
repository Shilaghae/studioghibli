package application.daggerttest.features;

import android.widget.TextView;

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
    TextView mInitMessage;

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
    public void inject() {
        getAppComponent().getHomeComponent(new HomeModule()).inject(this);
    }

    @Override
    public void showGhibliMovies(final List<Movie> movies) {
        String movie = "";
        for(Movie m: movies) {
            movie = movie + m.getTitle() + "\n";
        }
        mInitMessage.setText(movie);
    }
}
