package com.example.android.popularmovies.apiservice;

//https://stackoverflow.com/questions/32490011/how-i-can-use-gson-in-retrofit-library
//https://stackoverflow.com/questions/19975046/retrofit-multiple-query-parameters-in-get-command
//https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
//https://github.com/square/retrofit

import com.example.android.popularmovies.activity.PopularMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie?language=en")
    public void popularMovie(@Path("movie") String typeMovie,
                             @Query("api_key") String keyApi,
                             Call<PopularMovies> response);

    @GET("movie/top-rated?language=en")
    public void topRatedMovie(@Path("movie") String typeMovie,
                             @Query("api_key") String keyApi,
                             Call<PopularMovies> response);

}
