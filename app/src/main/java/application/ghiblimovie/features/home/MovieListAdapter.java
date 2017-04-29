package application.ghiblimovie.features.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import application.ghiblimovie.repositories.Movie;
import application.ghiblimovie.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author anna
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private ArrayList<Movie> mMovies = new ArrayList<>();

    private PublishSubject<Movie> mOnClickMoviePublishSubject = PublishSubject.create();

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
        holder.setMovieTitle(movie.getTitle());
        holder.setOnClickAction(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public Observable<Movie> onMovieItemClicked() {
        return mOnClickMoviePublishSubject;
    }

    public void updateAllMovies(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_entry_item_title)
        TextView mMovieEntryTitleTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMovieTitle(final String movieTitle) {
            mMovieEntryTitleTextView.setText(movieTitle);
        }

        public void setOnClickAction(final Movie movie) {
            itemView.setOnClickListener(item -> {mOnClickMoviePublishSubject.onNext(movie);});
        }
    }
}
