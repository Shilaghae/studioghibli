package application.ghiblimovie.repositories;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import application.ghiblimovie.services.GhibliService;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author anna
 */

public class MovieRepositoryFactory {

    private final GhibliService mGhibliService;
    private final MovieRepositoryRealm mMovieRepositoryRealm;
    private final Context mContext;

    private PublishSubject<MovieStore> merge = PublishSubject.create();

    public MovieRepositoryFactory(final GhibliService ghibliService, final MovieRepositoryRealm movieRepositoryRealm, final Context context) {
        mGhibliService = ghibliService;
        mMovieRepositoryRealm = movieRepositoryRealm;
        mContext = context;
    }

    public MovieStore create() {
        if(isConnected(mContext)) {
            return new NetworkMovieStore(mGhibliService);
        }
        return new DiskMovieStore(mMovieRepositoryRealm);
    }

    public Observable<MovieStore> onMovieStore() {
        return Observable.just(create());
    }

    public PublishSubject<MovieStore> onMovieStoreUpdated() {
        return merge;
    }

    public void updateMovieStore() {
        merge.onNext(create());
    }

    public void updateAllMovieStore(final List<Movie> movies) {
        DiskMovieStore diskMovieStore = new DiskMovieStore(mMovieRepositoryRealm);
        diskMovieStore.updateMovies(movies);
    }

    public boolean isConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
