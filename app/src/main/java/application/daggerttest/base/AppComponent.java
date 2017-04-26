package application.daggerttest.base;

import javax.inject.Singleton;

import application.daggerttest.features.home.HomeComponent;
import application.daggerttest.features.home.HomeModule;
import application.daggerttest.features.moviedetails.MovieDetailsComponent;
import application.daggerttest.features.moviedetails.MovieDetailsModule;
import dagger.Component;

/**
 * @author anna
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    HomeComponent getHomeComponent(HomeModule homeModule);

    MovieDetailsComponent getMovieDetailsComponent(MovieDetailsModule movieDetailsModule);
}
