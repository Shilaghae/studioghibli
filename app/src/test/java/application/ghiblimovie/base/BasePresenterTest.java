package application.ghiblimovie.base;

import android.support.annotation.CallSuper;

import org.junit.Before;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Base class for all the presenter test class should extend from
 *
 * @author anna
 */
public abstract class BasePresenterTest<P extends BasePresenter<V>, V extends BaseView> {

    protected P mPresenter;
    protected V mView;

    @CallSuper
    @Before
    public void before() {
        initMocks(this);
        mPresenter = createPresenter();
        mView = createView();
    }

    protected abstract P createPresenter();

    protected abstract V createView();

    protected void presenterOnAttachView() {
        mPresenter.onAttach(mView);
    }

    protected void presenterOnDetachView() {
        mPresenter.onDetach();
    }
}
