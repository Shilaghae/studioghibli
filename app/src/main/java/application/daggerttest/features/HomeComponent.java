package application.daggerttest.features;

import dagger.Subcomponent;

/**
 * @author anna
 */
@HomeScope
@Subcomponent(modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}
