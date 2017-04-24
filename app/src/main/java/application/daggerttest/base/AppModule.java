package application.daggerttest.base;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anna
 */

@Module
public class AppModule {

    private static final String HTTPS_API_GITHUB_COM = "https://api.github.com";
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
    AppPreferences provideAppPreferences(Context context) {
        return new AppPreferences(context);
    }

    @Provides
    Retrofit getRetrofitInstance() {
        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPS_API_GITHUB_COM)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
        return retrofit;
    }
}
