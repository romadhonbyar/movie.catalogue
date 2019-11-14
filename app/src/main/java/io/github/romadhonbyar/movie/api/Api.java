package io.github.romadhonbyar.movie.api;

import io.github.romadhonbyar.movie.ui.alarm.model.MovieReleaseModel;
import io.github.romadhonbyar.movie.ui.movies.model.detail.MoviesDetailModel;
import io.github.romadhonbyar.movie.ui.movies.model.main.MoviesModel;
import io.github.romadhonbyar.movie.ui.tv_shows.model.detail.TVShowsDetailModel;
import io.github.romadhonbyar.movie.ui.tv_shows.model.main.TVShowsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String SHARED_PREF_NAME = "my_shared_data";
    String SHARED_SETTING_DAILY = "sub_DAILY";
    String SHARED_SETTING_RELEASE = "sub_RELEASE";

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

    @Headers("Content-Type: application/json")
    @GET("discover/movie")
    Call<MovieReleaseModel> getMovieRelease(@Query("api_key") String apiKey,
                                            @Query("primary_release_date.gte") String date_gte,
                                            @Query("primary_release_date.lte") String date_lte);
}
