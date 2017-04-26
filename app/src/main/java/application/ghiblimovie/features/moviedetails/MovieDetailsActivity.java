package application.ghiblimovie.features.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import javax.inject.Inject;

import application.ghiblimovie.base.Movie;
import application.ghiblimovie.R;
import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.base.BasePresenter;
import butterknife.BindView;

public class MovieDetailsActivity extends BaseActivity implements MovieDetailsContract.MovieDetailsView {

    private static final String MOVIE_EXTRA = "movie";

    @Inject
    MovieDetailsPresenterImpl mMovieDetailsPresenter;

    @BindView(R.id.movie_details_activity_textview_title)
    TextView mTitleTextView;
    @BindView(R.id.movie_details_activity_textview_director)
    TextView mDirectorTextView;
    @BindView(R.id.movie_details_activity_textview_producer)
    TextView mProducerTextView;
    @BindView(R.id.movie_details_activity_textview_relese_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.movie_details_activity_textview_score)
    TextView mScoreTextView;
    @BindView(R.id.movie_details_activity_textview_description)
    TextView mDescriptionTextView;

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
    public void initMovie(final Movie movie) {
        mTitleTextView.setText(movie.getTitle());
        mProducerTextView.setText(movie.getProducer());
        mDirectorTextView.setText(movie.getDirector());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mScoreTextView.setText(movie.getScore());
        mDescriptionTextView.setText(movie.getDescription());
    }
}
