package application.daggerttest.features.home;

import application.daggerttest.services.GhibliService;
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
