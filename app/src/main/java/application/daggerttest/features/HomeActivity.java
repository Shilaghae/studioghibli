package application.daggerttest.features;

import android.widget.TextView;

import javax.inject.Inject;

import application.daggerttest.R;
import application.daggerttest.base.AppApplication;
import application.daggerttest.base.BaseActivity;
import application.daggerttest.base.BasePresenter;
import butterknife.BindView;
import io.reactivex.Observable;

public class HomeActivity extends BaseActivity implements HomeContract.HomeView {

    @Inject
    HomePresenterImpl mHomePresenter;

    @BindView(R.id.init_message)
    TextView setup;

    public void init() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public HomeContract.HomeView getView() {
        return this;
    }

    @Override
    public BasePresenter getPresenter() {
        return mHomePresenter;
    }

    @Override
    public void inject() {
        ((AppApplication) getApplication()).getAppComponent().getHomeComponent(new HomeModule()).inject(this);

//        DaggerHomeComponent.builder().homeModule(new HomeModule()).build().inject(this);
    }

    @Override
    public Observable<String> onInitPreferences() {
        return Observable.just("xzfklndkfnskjdfkjsdh");
    }

    @Override
    public void setInitPreferences(final String value) {
        setup.setText(value);
    }
}
