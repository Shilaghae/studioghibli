package application.ghiblimovie.base;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author anna
 */

public abstract class BaseActivity<V extends BaseView> extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        inject();
        getPresenter().onAttach(getView());
    }

    public abstract int getLayoutId();

    public abstract V getView();

    public abstract BasePresenter<V> getPresenter();

    public abstract void inject();

    public AppComponent getAppComponent() {
        return ((AppApplication) getApplication()).getAppComponent();
    }

    public boolean isAirplaneModeActive() {
        return Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }
}
