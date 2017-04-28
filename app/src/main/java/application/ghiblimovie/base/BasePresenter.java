package application.ghiblimovie.base;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author anna
 */

public class BasePresenter<V extends BaseView> {

    private V mView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public void onAttach(@NonNull V view) {
        mView = view;
        mView.init();
    }

    public CompositeDisposable subscribe(Disposable observable) {
        mCompositeDisposable.add(observable);
        return mCompositeDisposable;
    }

    public void onDetach() {
        mView = null;
        mCompositeDisposable.clear();
    }
}
