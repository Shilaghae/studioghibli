package application.daggerttest.features.moviedetails;

import application.daggerttest.base.BaseView;

/**
 * @author anna
 */

public class MovieDetailsContract {

    public interface MovieDetailsView extends BaseView {
        void initTitle(String title);
    }

    public interface MovieDetailsPresenter {

    }
}
