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

import java.util.ArrayList;

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

    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    //private ArrayList<Movie> moviesTopRated = new ArrayList<Movie>();

    private MainAdapter moviesTopRatedAdapter;

    public void onLoadMovies() {

        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment

        if (getString(R.string.apiKey).isEmpty()) {
            Toast.makeText(getActivity(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        //https://stackoverflow.com/questions/39378586/how-to-create-multiple-retrofit-callbacks-in-same-fragment-android
        //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit

        APIService apiService = Retrofit2.retrofit.create(APIService.class);

        Call<MovieResponse> callTopRatedMovie = apiService.getTopRatedMovie(getString(R.string.apiKey));
        callTopRatedMovie.enqueue(new Callback<MovieResponse>() {

            //https://stackoverflow.com/questions/35254843/gridview-setadapter-method-gives-nullpointerexception

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse topRatedMovieResponse = response.body();
                if (topRatedMovieResponse != null) {
                    movieList = (ArrayList<Movie>) topRatedMovieResponse.getResults();
                    //moviesTopRatedAdapter.clear();// no need for this line in the first call
                    moviesTopRatedAdapter.addAll(movieList);
                    moviesTopRatedAdapter.notifyDataSetChanged();
                    topratedmoviesGridview.setAdapter(new MainAdapter(getActivity(), movieList));
                }
                Log.d(TAG, "Number of movies received: " + movieList.size());
                Log.d(TAG, "PosterPath:" + movieList.get(0).getPosterPath());
                Log.d(TAG, "Title:" + movieList.get(0).getTitle());
                Log.d(TAG, "Release Date:" + movieList.get(0).getReleaseDate());
                Log.d(TAG, "Average Rating:" + movieList.get(0).getVoteAverage());
                Log.d(TAG, "Overview:" + movieList.get(0).getOverview());
                Log.d(TAG, "Popularity:" + movieList.get(0).getPopularity());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }

        });
    }

    //https://github.com/udacity/android-custom-arrayadapter/blob/parcelable/app/src/main/java/demo/example/com/customarrayadapter/MainActivityFragment.java

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            movieList = new ArrayList<Movie>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movie");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topratedmovie, container, false);

        onLoadMovies();

        moviesTopRatedAdapter = new MainAdapter(getActivity(), movieList);

        //https://stackoverflow.com/questions/21339086/gridview-and-navigation-drawer-not-working-together-in-android
        //https://stackoverflow.com/questions/22890314/gridview-is-not-shown-in-an-example-with-navigation-drawer

        topratedmoviesGridview = (GridView) view.findViewById(R.id.gridview_fragmentTopRatedMovie);
        topratedmoviesGridview.setAdapter(new MainAdapter(getActivity(), movieList));

        //https://stackoverflow.com/questions/27180933/gridview-with-gridviewadapter-how-to-set-onclick-listener-in-gridview-and-not-gr

        //TODO add Fragment intent to DetailActivity
        //https://discussions.udacity.com/t/how-do-i-use-intent-to-get-and-display-movie-details/27778/5

        topratedmoviesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie Movies = moviesTopRatedAdapter.getItem(position);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("movie", Movies);
                startActivity(intent);
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

}