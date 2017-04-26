package application.ghiblimovie.base;

import android.app.Application;

/**
 * @author anna
 */

public class AppApplication extends Application {

    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        createAppComponent();
    }

    public void createAppComponent() {
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
