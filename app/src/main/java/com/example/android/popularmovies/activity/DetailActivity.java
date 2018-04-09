package com.example.android.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.DetailAdapter;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private DetailAdapter Adapter;
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

        Adapter = new DetailAdapter(this, new ArrayList<Movie>());

        Movie Movies = getIntent().getParcelableExtra("movie");
        //Movie movie = getIntent().getSerializableExtra("movie");

    }
}