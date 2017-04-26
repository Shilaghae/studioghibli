package application.ghiblimovie.base;

import android.content.Context;
import android.content.SharedPreferences;

import io.reactivex.Observable;

/**
 * @author anna
 */

public class AppPreferences {

    private static final String MOVIE_LIST_UPDATE = ".movie_list_update";
    private static final String IS_MOVIE_LIST_UPDATE = ".is_movie_list_up_to_date";

    private final SharedPreferences mMovieListUpdateSharedPreferences;

    public AppPreferences(final Context context) {
        mMovieListUpdateSharedPreferences = context.getSharedPreferences(MOVIE_LIST_UPDATE, Context.MODE_PRIVATE);
    }

    public boolean isMovieListUpToDate() {
        return mMovieListUpdateSharedPreferences.getBoolean(IS_MOVIE_LIST_UPDATE, false);
    }

    public void setMovieListUpToDate(final boolean upToDate) {
        mMovieListUpdateSharedPreferences.edit().putBoolean(IS_MOVIE_LIST_UPDATE, upToDate).commit();
    }

    public Observable<Boolean> onMovieListUpToDate() {
        return Observable.create(subscriber -> subscriber.onNext(isMovieListUpToDate()));
    }
}
