package application.daggerttest.base;

import android.os.Bundle;
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
}
