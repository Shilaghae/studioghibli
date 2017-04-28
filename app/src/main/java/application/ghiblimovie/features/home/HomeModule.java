package application.ghiblimovie.features.home;

import application.ghiblimovie.base.AppPreferences;
import application.ghiblimovie.base.MovieRepositoryRealm;
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
    HomePresenterImpl provideHomePresenter(final GhibliService ghibliService, final AppPreferences appPreferences, MovieRepositoryRealm movieRepositoryRealm) {
        return new HomePresenterImpl(ghibliService, appPreferences, movieRepositoryRealm, Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
