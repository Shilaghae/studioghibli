package application.daggerttest.services;

import java.util.List;

import application.daggerttest.base.Movie;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * @author anna
 */

public class GhibliService {

    private static final String GHIBLI_API_URL = "https://ghibliapi.herokuapp.com";

    private Retrofit mRetrofit;

    public GhibliService() {
        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        mRetrofit = new Retrofit.Builder()
                .baseUrl(GHIBLI_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    public Observable<List<Movie>> getMovies() {
        GhibliApi ghibliApiService =
                mRetrofit.create(GhibliApi.class);
        return ghibliApiService.getMovies();
    }

    interface GhibliApi {

        @GET("/films")
        Observable<List<Movie>> getMovies();
    }
}
