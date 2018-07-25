package com.example.android.popularmovies.apiservice;

//https://stackoverflow.com/questions/32490011/how-i-can-use-gson-in-retrofit-library
//https://stackoverflow.com/questions/19975046/retrofit-multiple-query-parameters-in-get-command
//https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
//https://github.com/square/retrofit
//https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

//JSON Formatter & Validator http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
//JSON Formatter & Validator http://api.themoviedb.org/3/movie/top_rated?api_key=[YOUR_API_KEY]
//JSON Formatter & Validator http://api.themoviedb.org/3/movie/{id}/videos?api_key=[YOUR_API_KEY]
//JSON Formatter & Validator http://api.themoviedb.org/3/movie/{id}/reviews?api_key=[YOUR_API_KEY]

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.model.ReviewResponse;
import com.example.android.popularmovies.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    String apiKey = BuildConfig.MOVIE_DB_API_KEY;

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovie(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovie(@Query("api_key") String apiKey);

    //https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

}

//Videos example API
//John Wick
//http://api.themoviedb.org/3/movie/245891/videos?api_key=[YOUR_API_KEY]

//Reviews example API
//John Wick
//http://api.themoviedb.org/3/movie/245891/reviews?api_key=[YOUR_API_KEY]