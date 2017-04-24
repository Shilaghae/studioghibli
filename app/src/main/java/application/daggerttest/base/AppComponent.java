package application.daggerttest.base;

import application.daggerttest.features.HomeActivity;
import dagger.Component;

/**
 * @author anna
 */

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(HomeActivity activity);
}
