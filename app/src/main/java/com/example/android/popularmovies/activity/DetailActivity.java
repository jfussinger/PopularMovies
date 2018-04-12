package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.DetailAdapter;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private DetailAdapter detailAdapter;
    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    Movie Movies;
    ImageView posterPath;
    TextView OriginalTitle;
    TextView ReleaseDate;
    TextView VoteAverage;
    TextView Overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        posterPath = (ImageView) findViewById(R.id.posterPath);
        OriginalTitle = (TextView) findViewById(R.id.original_title);
        ReleaseDate = (TextView) findViewById(R.id.release_date);
        VoteAverage = (TextView) findViewById(R.id.vote_average);
        Overview = (TextView) findViewById(R.id.overview);

        detailAdapter = new DetailAdapter(this, movieList);

        //https://discussions.udacity.com/t/how-do-i-use-intent-to-get-and-display-movie-details/27778/11
        //TODO Use Parcelable, it's faster than Serializable
        //http://www.developerphil.com/parcelable-vs-serializable/
        //https://guides.codepath.com/android/using-parcelable

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {
            Movies = getIntent().getParcelableExtra("movie");
            //intent.getSerializableExtra("movie");
            //Movie movie = getIntent().getParcelableExtra("movie");

        }else{
            Toast.makeText(this, "Insert your API KEY first from The Movie Db", Toast.LENGTH_SHORT).show();
        }
    }
}

