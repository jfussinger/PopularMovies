package com.example.android.popularmovies.fragments;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activity.DetailActivity;
import com.example.android.popularmovies.activity.MainAdapter;
import com.example.android.popularmovies.activity.MovieResponse;
import com.example.android.popularmovies.activity.PopularMovies;
import com.example.android.popularmovies.apiservice.APIService;
import com.example.android.popularmovies.apiservice.Retrofit2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMovieFragment extends Fragment {

    public PopularMovieFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "popularmoviefragment";

    private GridView popularmoviesGridview;

    private MainAdapter Adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popularmovie, container, false);

        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment

        if (getString(R.string.apiKey).isEmpty()) {
            Toast.makeText(getActivity(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        Adapter = new MainAdapter(getActivity(), new ArrayList<PopularMovies>());
        popularmoviesGridview = (GridView) view.findViewById(R.id.gridview_fragmentPopularMovie);

        //https://github.com/codepath/android-networking-persistence-sample-moviedb/blob/master/app/src/main/java/com/shrikant/themoviedb/fragments/TopRatedMoviesFragment.java

        if (!isOnline()) {
            Toast.makeText(getContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {

            //https://stackoverflow.com/questions/39378586/how-to-create-multiple-retrofit-callbacks-in-same-fragment-android
            //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit

            APIService apiService = Retrofit2.retrofit.create(APIService.class);

            Call<MovieResponse> callPopularMovie = apiService.getPopularMovie(getString(R.string.apiKey));
            callPopularMovie.enqueue(new Callback<MovieResponse>() {

                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> PopularMovie = response.body().getResults();
                    popularmoviesGridview.setAdapter(Adapter);
                }

                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }

            });
        }

        //https://stackoverflow.com/questions/27180933/gridview-with-gridviewadapter-how-to-set-onclick-listener-in-gridview-and-not-gr

        popularmoviesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class);

                // Start the new activity
                startActivity(detailActivityIntent);
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    //https://github.com/codepath/android-networking-persistence-sample-moviedb/blob/master/app/src/main/java/com/shrikant/themoviedb/fragments/TopRatedMoviesFragment.java

    @Override
    public void onResume() {
        super.onResume();
    }

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }
}
