package application.daggerttest.base;

import java.util.List;

import application.daggerttest.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author anna
 */
public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/users/{username}")
    Observable<User> getUser(@Path("username") String username);

    @GET("/group/{id}/users")
    Observable<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("/users/new")
    Observable<User> createUser(@Body User user);
}