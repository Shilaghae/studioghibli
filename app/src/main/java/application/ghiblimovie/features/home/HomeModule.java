package application.ghiblimovie.features.home;

import android.content.Context;

import application.ghiblimovie.base.AppPreferences;
import application.ghiblimovie.repositories.MovieRepositoryRealm;
import application.ghiblimovie.repositories.MovieRepositoryFactory;
import application.ghiblimovie.services.GhibliService;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author anna
 */

@Module
public class HomeModule {

    @Provides
    @HomeScope
    GhibliService getGhibliService() {
        return new GhibliService();
    }

    @Provides
    @HomeScope
    MovieRepositoryFactory provideMovieStore(final Context context, final GhibliService ghibliService, final MovieRepositoryRealm movieRepositoryRealm) {
        return new MovieRepositoryFactory(ghibliService, movieRepositoryRealm, context);
    }

    @Provides
    @HomeScope
    HomePresenterImpl provideHomePresenter(final AppPreferences appPreferences, final MovieRepositoryFactory movieRepositoryFactory) {
        return new HomePresenterImpl(appPreferences, movieRepositoryFactory, Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
