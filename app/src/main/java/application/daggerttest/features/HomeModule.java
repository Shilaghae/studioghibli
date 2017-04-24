package application.daggerttest.features;

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
