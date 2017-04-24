package application.daggerttest.features;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import application.daggerttest.User;
import application.daggerttest.base.AppPreferences;
import application.daggerttest.base.BasePresenter;
import application.daggerttest.base.MyApiEndpointInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * @author anna
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    private final AppPreferences mAppPreferences;

    private final Retrofit mRetrofit;

    @Inject
    HomePresenterImpl(AppPreferences appPreferences, Retrofit retrofit) {
        mAppPreferences = appPreferences;
        mRetrofit = retrofit;
    }

    @Override
    public void onAttach(@NonNull final HomeContract.HomeView view) {
        super.onAttach(view);
//        subscribe(view.onInitPreferences()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<String>() {
//                    @Override
//                    public void onNext(@io.reactivex.annotations.NonNull final String value) {
//                        view.setInitPreferences(value);
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.annotations.NonNull final Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                }));
        MyApiEndpointInterface apiService =
                mRetrofit.create(MyApiEndpointInterface.class);
        Observable<User> shilaghae = apiService.getUser("shilaghae");

        subscribe(shilaghae.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(user -> {view.setInitPreferences(user.getName());}));

    }

    String getAppPreferenceInitSetup() {
        return mAppPreferences.getInitSetup() + " hello";
    }
}
