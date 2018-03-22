package com.example.android.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private DetailAdapter Adapter;
    ImageView backdropPath;
    TextView OriginalTitle;
    TextView ReleaseDate;
    TextView VoteAverage;
    TextView Overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movieposter_detail);

        backdropPath = (ImageView) findViewById(R.id.backdropPath);
        OriginalTitle = (TextView)findViewById(R.id.original_title);
        ReleaseDate = (TextView)findViewById(R.id.release_date);
        VoteAverage = (TextView)findViewById(R.id.vote_average);
        Overview = (TextView) findViewById(R.id.overview);

        Adapter = new DetailAdapter(this, new ArrayList<PopularMovies>());
    }
}