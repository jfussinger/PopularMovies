package com.example.android.popularmovies.activity;

//https://developer.android.com/guide/topics/ui/layout/gridview.html
////https://github.com/Mayur-007/iSearch
//https://www.androidhive.info/2016/01/android-working-with-recycler-view/

//https://stackoverflow.com/questions/47393110/how-to-add-onclicklistener-for-grid-view-which-opens-new-activity

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.apiservice.APIService;
import com.example.android.popularmovies.apiservice.Retrofit2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMoviesActivity extends AppCompatActivity {

    private static final String TAG = "popularmoviesactivity";

    private PopularMoviesAdapter Adapter;

    //https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popularmovies_activity);

        if (getString(R.string.apiKey).isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
            return;
        }

        Adapter = new PopularMoviesAdapter(this, new ArrayList<PopularMovies>());
        final GridView popularmoviesGridview = (GridView) findViewById(R.id.gridview);

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

        Call<MovieResponse> callTopRatedMovie = apiService.getTopRatedMovie(getString(R.string.apiKey));
        callTopRatedMovie.enqueue(new Callback<MovieResponse>() {

            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> TopRatedMovie = response.body().getResults();
                popularmoviesGridview.setAdapter(Adapter);
            }

            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        //https://stackoverflow.com/questions/27180933/gridview-with-gridviewadapter-how-to-set-onclick-listener-in-gridview-and-not-gr

        popularmoviesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailActivityIntent = new Intent(PopularMoviesActivity.this, DetailActivity.class);

                // Start the new activity
                startActivity(detailActivityIntent);
            }
        });
    }

    private void sortPopularMovie() {

        //TODO add code here to sort PopularMovie

    }

    private void sortTopRatedMovie() {

        //TODO add code here to sort TopRatedMovie

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "sortPopularMovie" menu option
            case R.id.action_sort_popularMovie:
                sortPopularMovie();
                return true;
            // Respond to a click on the "sortTopRatedMovie" menu option
            case R.id.action_sort_topRatedMovie:
                sortTopRatedMovie();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


