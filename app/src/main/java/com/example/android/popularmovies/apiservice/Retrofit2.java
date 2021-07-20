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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2 {

    private static final String TAG = "retrofit2";
    public static final String apiKey = BuildConfig.MOVIE_DB_API_KEY;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static APIService apiService = retrofit.create(APIService.class);

}

