package application.ghiblimovie.repositories;

import android.content.Context;

import java.util.List;

import io.realm.Realm;

/**
 * @author anna
 */

public class MovieRepositoryRealm {

    private Realm mRealm;

    public MovieRepositoryRealm(final Context context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
    }

    public List<Movie> getAllMovies() {
        return mRealm.where(Movie.class).findAll();
    }

    public void addMovie(final Movie movie) {
        mRealm.executeTransaction(realm -> realm.copyToRealm(movie));
    }

    public void addAllMovies(List<Movie> movies) {
        mRealm.executeTransaction(realm -> realm.copyToRealm(movies));
    }

    public void removeAllMovies() {
        mRealm.executeTransaction(realm -> realm.deleteAll());
    }
}
