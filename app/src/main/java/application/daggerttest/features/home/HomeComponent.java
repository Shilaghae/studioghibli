package application.daggerttest.features.home;

import dagger.Subcomponent;

/**
 * @author anna
 */
@HomeScope
@Subcomponent(modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}
