package application.daggerttest.features;

import application.daggerttest.User;
import application.daggerttest.base.MyApiEndpointInterface;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * @author anna
 */

@Module
public class HomeModule {

    @Provides
    @HomeScope
    Observable<User> getUser(final Retrofit retrofit) {
        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);
        return apiService.getUser("shilaghae");
    }
}
