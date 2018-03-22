package com.example.android.popularmovies.activity;

//https://developer.android.com/guide/topics/ui/layout/gridview.html
////https://github.com/Mayur-007/iSearch
//https://www.androidhive.info/2016/01/android-working-with-recycler-view/

//https://stackoverflow.com/questions/47393110/how-to-add-onclicklistener-for-grid-view-which-opens-new-activity

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.R;

import java.util.ArrayList;

public class PopularMoviesActivity extends AppCompatActivity {

    private PopularMoviesAdapter Adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popularmovies_activity);

        Adapter = new PopularMoviesAdapter(this, new ArrayList<PopularMovies>());

        GridView popularmoviesGridview = (GridView) findViewById(R.id.gridview);
        popularmoviesGridview.setAdapter(Adapter);

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
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_sort_popularMovie:
                sortPopularMovie();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_sort_topRatedMovie:
                sortTopRatedMovie();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


