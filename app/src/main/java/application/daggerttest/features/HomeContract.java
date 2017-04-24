package application.daggerttest.features;

import application.daggerttest.base.BaseView;
import io.reactivex.Observable;

/**
 * @author anna
 */

public class HomeContract {

    public interface HomeView extends BaseView {

        Observable<String> onInitPreferences();

        void setInitPreferences(String value);
    }

    public interface HomePresenter {

    }
}
