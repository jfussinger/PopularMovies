package com.example.android.popularmovies.apiservice;

//https://stackoverflow.com/questions/32490011/how-i-can-use-gson-in-retrofit-library
//https://stackoverflow.com/questions/19975046/retrofit-multiple-query-parameters-in-get-command
//https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
//https://github.com/square/retrofit

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2 {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static APIService apiService = retrofit.create(APIService.class);

}
