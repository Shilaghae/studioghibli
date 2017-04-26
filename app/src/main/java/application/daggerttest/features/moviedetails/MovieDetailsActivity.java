package application.daggerttest.features.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import javax.inject.Inject;

import application.daggerttest.base.Movie;
import application.daggerttest.R;
import application.daggerttest.base.BaseActivity;
import application.daggerttest.base.BasePresenter;
import butterknife.BindView;

public class MovieDetailsActivity extends BaseActivity implements MovieDetailsContract.MovieDetailsView {

    private static final String MOVIE_EXTRA = "movie";

    @Inject
    MovieDetailsPresenterImpl mMovieDetailsPresenter;

    @BindView(R.id.movie_details_activity_title)
    TextView mTitleTextView;

    public void init() {
    }

    @Override
    public MovieDetailsContract.MovieDetailsView getView() {
        return this;
    }

    @Override
    public BasePresenter getPresenter() {
        return mMovieDetailsPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_movie_details;
    }

    @Override
    public void inject() {
        Movie movie = getIntent().getParcelableExtra(MOVIE_EXTRA);
        getAppComponent().getMovieDetailsComponent(new MovieDetailsModule(movie)).inject(this);
    }

    public static void startActivity(final Context context, final Movie movie) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_EXTRA, movie);
        context.startActivity(intent);
    }

    @Override
    public void initTitle(final String title) {
        mTitleTextView.setText(title);
    }
}
