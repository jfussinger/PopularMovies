package com.example.android.popularmovies.apiservice;

//https://stackoverflow.com/questions/32490011/how-i-can-use-gson-in-retrofit-library
//https://stackoverflow.com/questions/19975046/retrofit-multiple-query-parameters-in-get-command
//https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
//https://github.com/square/retrofit
//https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

// JSON Pretty Print http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
// JSON Pretty Print http://api.themoviedb.org/3/movie/top_rated?api_key=[YOUR_API_KEY]

import com.example.android.popularmovies.activity.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovie(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovie(@Query("api_key") String apiKey);

}
