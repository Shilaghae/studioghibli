package application.ghiblimovie.base;

import javax.inject.Singleton;

import application.ghiblimovie.features.home.HomeComponent;
import application.ghiblimovie.features.home.HomeModule;
import application.ghiblimovie.features.moviedetails.MovieDetailsComponent;
import application.ghiblimovie.features.moviedetails.MovieDetailsModule;
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
