package application.ghiblimovie.features.home;

import application.ghiblimovie.services.GhibliService;
import dagger.Module;
import dagger.Provides;

/**
 * @author anna
 */

@Module
public class HomeModule {

    @Provides
    @HomeScope
    GhibliService getGhibliService() {
        return new GhibliService();
    }
}
