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

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {
            Movies = getIntent().getParcelableExtra("movie");
            //Movie Movies = getIntent().getSerializableExtra("movie");

        }else{
            Toast.makeText(this, "Insert your API KEY first from The Movie Db", Toast.LENGTH_SHORT).show();
        }
    }
}