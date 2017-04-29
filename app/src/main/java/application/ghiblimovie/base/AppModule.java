package application.ghiblimovie.base;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * @author anna
 */

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    AppPreferences provideAppPreferences(final Context context) {
        return new AppPreferences(context);
    }

    @Provides
    NetworkStatusChangeReceiver provideNetworkStatusChangeReceiver() {
        return new NetworkStatusChangeReceiver();
    }

    @Provides
    MovieRepositoryRealm provideMovieRepositoryRealm(final Context context) {
        return new MovieRepositoryRealm(context);
    }
}
