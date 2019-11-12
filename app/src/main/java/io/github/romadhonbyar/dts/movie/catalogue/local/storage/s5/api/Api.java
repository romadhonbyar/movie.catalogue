package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.api;


import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.model.detail.MoviesDetailModel;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.model.main.MoviesModel;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.model.detail.TVShowsDetailModel;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.model.main.TVShowsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @Headers("Content-Type: application/json")
    @GET("discover/movie")
    Call<MoviesModel> getMovieList(@Query("api_key") String api_key,
                                   @Query("language") String language);

    @Headers("Content-Type: application/json")
    @GET("discover/tv")
    Call<TVShowsModel> getTVList(@Query("api_key") String api_key,
                                 @Query("language") String language);

    @Headers("Content-Type: application/json")
    @GET("movie/{id}")
    Call<MoviesDetailModel> getMovieDetailList(@Path("id") String id,
                                               @Query("api_key") String api_key,
                                               @Query("language") String language);

    @Headers("Content-Type: application/json")
    @GET("tv/{id}")
    Call<TVShowsDetailModel> getTVDetailList(@Path("id") String id,
                                             @Query("api_key") String api_key,
                                             @Query("language") String language);

    @Headers("Content-Type: application/json")
    @GET("search/movie")
    Call<MoviesModel> getMovieSearch(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("query") String query);
    @Headers("Content-Type: application/json")
    @GET("search/tv")
    Call<TVShowsModel> getTVSearch(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("query") String query);
}
