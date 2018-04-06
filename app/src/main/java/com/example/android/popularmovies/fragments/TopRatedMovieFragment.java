package com.example.android.popularmovies.fragments;

import android.content.Intent;
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
import com.example.android.popularmovies.adapter.MainAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.apiservice.APIService;
import com.example.android.popularmovies.apiservice.Retrofit2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

public class TopRatedMovieFragment extends Fragment {

    public TopRatedMovieFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "topratedmoviefragment";

    private GridView topratedmoviesGridview;

    private ArrayList<Movie> moviesTopRated = new ArrayList<Movie>();

    private MainAdapter moviesTopRatedAdapter;

    public void onLoadMovies() {

        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment

        if (getString(R.string.apiKey).isEmpty()) {
            Toast.makeText(getActivity(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        //https://github.com/codepath/android-networking-persistence-sample-moviedb/blob/master/app/src/main/java/com/shrikant/themoviedb/fragments/TopRatedMoviesFragment.java

        if (!isOnline()) {
            Toast.makeText(getContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {

            //https://stackoverflow.com/questions/39378586/how-to-create-multiple-retrofit-callbacks-in-same-fragment-android
            //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit

            APIService apiService = Retrofit2.retrofit.create(APIService.class);

            Call<MovieResponse> callTopRatedMovie = apiService.getTopRatedMovie(getString(R.string.apiKey));
            callTopRatedMovie.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    MovieResponse topRatedMovieResponse = response.body();
                    if (topRatedMovieResponse != null) {
                        moviesTopRated = (ArrayList<Movie>) topRatedMovieResponse.getResults();
                        //moviesTopRatedAdapter.clear();// no need for this line in the first call
                        moviesTopRatedAdapter.addAll(moviesTopRated);
                        moviesTopRatedAdapter.notifyDataSetChanged();
                        topratedmoviesGridview.setAdapter(new MainAdapter(getActivity(), moviesTopRated));
                    }
                    Log.d(TAG, "Number of movies received: " + moviesTopRated.size());
                    Log.d(TAG, "PosterPath:" + moviesTopRated.get(0).getPosterPath());
                    Log.d(TAG, "Title:" + moviesTopRated.get(0).getTitle());
                    Log.d(TAG, "Release Date:" + moviesTopRated.get(0).getReleaseDate());
                    Log.d(TAG, "Average Rating:" + moviesTopRated.get(0).getVoteAverage());
                    Log.d(TAG, "Overview:" + moviesTopRated.get(0).getOverview());
                    Log.d(TAG, "Popularity:" + moviesTopRated.get(0).getPopularity());
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }

            });
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topratedmovie, container, false);

        onLoadMovies();

        moviesTopRatedAdapter = new MainAdapter(getActivity(), moviesTopRated);

        //https://stackoverflow.com/questions/21339086/gridview-and-navigation-drawer-not-working-together-in-android
        //https://stackoverflow.com/questions/22890314/gridview-is-not-shown-in-an-example-with-navigation-drawer

        topratedmoviesGridview = (GridView) view.findViewById(R.id.gridview_fragmentTopRatedMovie);
        topratedmoviesGridview.setAdapter(new MainAdapter(getActivity(), moviesTopRated));

        //https://stackoverflow.com/questions/27180933/gridview-with-gridviewadapter-how-to-set-onclick-listener-in-gridview-and-not-gr

        topratedmoviesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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