package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {

    Movie Movies;
    int movie_id;
    String movieThumbnail, movieTitle, dateOfRelease, userRating, movieSynopsis;
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

        //https://discussions.udacity.com/t/how-do-i-use-intent-to-get-and-display-movie-details/27778/11
        //TODO Use Parcelable, it's faster than Serializable
        //http://www.developerphil.com/parcelable-vs-serializable/
        //https://guides.codepath.com/android/using-parcelable

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {
            Movies = getIntent().getParcelableExtra("movie");

            movieThumbnail = Movies.getPosterPath();
            movieTitle = Movies.getOriginalTitle();
            dateOfRelease = Movies.getReleaseDate();
            userRating = Double.toString(Movies.getVoteAverage());
            movieSynopsis = Movies.getOverview();

            movie_id = Movies.getId();

            final String base_url = "http://image.tmdb.org/t/p/";
            final String file_size = "w500/";
            final String posterPathString = Movies.getPosterPath();

            Glide.with(this)
                    .load(base_url + file_size + posterPathString)
                    .placeholder(R.drawable.placeholderimagedetailactivity)
                    .into(posterPath);

            OriginalTitle.setText(movieTitle);
            ReleaseDate.setText(dateOfRelease);
            VoteAverage.setText(userRating);
            Overview.setText(movieSynopsis);

        }else{
            Toast.makeText(this, "Insert your API KEY first from The Movie Db", Toast.LENGTH_SHORT).show();
        }
    }
}

