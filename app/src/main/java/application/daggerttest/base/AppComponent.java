package application.daggerttest.base;

import javax.inject.Singleton;

import application.daggerttest.features.HomeComponent;
import application.daggerttest.features.HomeModule;
import dagger.Component;

/**
 * @author anna
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    HomeComponent getHomeComponent(HomeModule homeModule);
}
