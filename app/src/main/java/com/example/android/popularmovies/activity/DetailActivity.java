package com.example.android.popularmovies.activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.VideoAdapter;
import com.example.android.popularmovies.apiservice.APIService;
import com.example.android.popularmovies.apiservice.Retrofit2;
import com.example.android.popularmovies.data.FavoriteContract;
import com.example.android.popularmovies.data.FavoriteDbHelper;
import com.example.android.popularmovies.model.Movie;

import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.ReviewResponse;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.model.VideoResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.android.popularmovies.data.FavoriteContract.FavoriteEntry;

public class DetailActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String TAG = "detailactivity";
    Movie Movies;
    int movie_id;
    String key;
    String movieIdentification, movieThumbnail, movieTitle, dateOfRelease, userRating, movieSynopsis;
    TextView MovieId;
    ImageView posterPath;
    TextView OriginalTitle;
    TextView ReleaseDate;
    TextView VoteAverage;
    TextView Overview;
    Video Videos;
    Review Reviews;
    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    private ArrayList<Video> videoList = new ArrayList<Video>();
    private ArrayList<Review> reviewList = new ArrayList<Review>();
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private RecyclerView videoRecyclerView;
    private RecyclerView reviewRecyclerView;
    private Boolean hasFavorite = false;
    private ImageView FavoriteIcon;

    private static final String IMAGE_URI = "IMAGE_URI";
    private Uri imageUri;
    private ImageView ImageImageView;
    private TextView ImageTextView;
    public static final String CONTENT_URI = "URI";
    private Uri CurrentFavoriteUri;

    private FavoriteDbHelper favoriteDbHelper;

    private Intent shareIntent;

    String apiKey = BuildConfig.MOVIE_DB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate Lifecycle invoked");

        setContentView(R.layout.activity_detail);

        MovieId = (TextView) findViewById(R.id.movie_id);
        posterPath = (ImageView) findViewById(R.id.posterPath);
        ImageTextView = (TextView) findViewById(R.id.image_uri);
        OriginalTitle = (TextView) findViewById(R.id.original_title);
        ReleaseDate = (TextView) findViewById(R.id.release_date);
        VoteAverage = (TextView) findViewById(R.id.vote_average);
        Overview = (TextView) findViewById(R.id.overview);

        //https://discussions.udacity.com/t/how-do-i-use-intent-to-get-and-display-movie-details/27778/11
        //http://www.developerphil.com/parcelable-vs-serializable/
        //https://guides.codepath.com/android/using-parcelable

        final Intent intent = getIntent();

        if (intent.hasExtra("movie")) {
            Movies = getIntent().getParcelableExtra("movie");

            movieIdentification = Integer.toString(Movies.getId());
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
                    .placeholder(R.drawable.placeholderimagemainactivity)
                    .into(posterPath);

            MovieId.setText(movieIdentification);
            OriginalTitle.setText(movieTitle);
            ReleaseDate.setText(dateOfRelease);
            VoteAverage.setText(userRating);
            Overview.setText(movieSynopsis);

        } else {
            Toast.makeText(this, "Insert your API KEY first from The Movie Db", Toast.LENGTH_SHORT).show();
        }

        onLoadVideos();

        videoRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
        videoRecyclerView.setAdapter(videoAdapter);

        onLoadReviews();

        isFavorite();

        //https://stackoverflow.com/questions/4193167/change-source-image-for-image-view-when-pressed?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        //https://stackoverflow.com/questions/6083641/android-imageviews-onclicklistener-does-not-work?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        final ImageView favoriteClick = (ImageView) findViewById(R.id.favorite);

        favoriteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFavorite) {
                    deleteFavorite();
                    favoriteClick.setImageResource(R.drawable.ic_not_favorite);

                } else {
                    saveFavorite();
                    favoriteClick.setImageResource(R.drawable.ic_favorite);
                }
            }
        });

        if(isFavorite()){
            favoriteClick.setImageResource(R.drawable.ic_favorite);
        }else {
            favoriteClick.setImageResource(R.drawable.ic_not_favorite);
        }

        //https://github.com/udacity/ud851-Sunshine/blob/student/S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/DetailActivity.java
        //https://github.com/codepath/android_guides/wiki/Sharing-Content-with-Intents
        //https://developer.android.com/reference/android/support/v7/widget/ShareActionProvider
        //https://discussions.udacity.com/t/share-icon-provider-icon-not-displaying-in-action-bar/34777/9
        //https://developer.android.com/training/sharing/send

        final ImageView shareIntent = (ImageView) findViewById(R.id.action_share);

        shareIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String videoBaseUrl = "https://www.youtube.com/watch?v=";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Watch:" + Movies.getOriginalTitle() + videoBaseUrl + videoList.get(0).getKey());
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(Intent.createChooser(shareIntent, "Share using"));

                Log.d(TAG, "Share Movie: " + Movies.getOriginalTitle());
                Log.d(TAG, "Share Intent: " + videoBaseUrl + videoList.get(0).getKey());
            }
        });

    }

    //https://developer.android.com/guide/components/activities/activity-lifecycle

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Lifecycle invoked");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Lifecycle invoked");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Lifecycle invoked");
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        Log.d(TAG, "onStop Lifecycle invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy Lifecycle invoked");
    }

    public void onLoadVideos (){

        if (apiKey.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        videoRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        APIService apiService = Retrofit2.retrofit.create(APIService.class);

        Call<VideoResponse> callVideo = apiService.getVideos(Movies.getId(), apiKey);
        callVideo.enqueue(new Callback<VideoResponse>(){

            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                VideoResponse videoResponse = response.body();
                if (videoResponse != null) {
                    videoList = (ArrayList<Video>) videoResponse.getResults();
                    videoRecyclerView.setAdapter(new VideoAdapter(videoList, R.layout.activity_detail_videos_list_item, getApplicationContext()));

                }

                if (videoList.size()>0) {

                    Log.d(TAG, "Number of videos received: " + videoList.size());
                    Log.d(TAG, "Id:" + videoList.get(0).getId());
                    Log.d(TAG, "Language:" + videoList.get(0).getIso_639_1());
                    Log.d(TAG, "Country:" + videoList.get(0).getIso_3166_1());
                    Log.d(TAG, "Key:" + videoList.get(0).getKey());
                    Log.d(TAG, "Name:" + videoList.get(0).getName());
                    Log.d(TAG, "Site:" + videoList.get(0).getSite());
                    Log.d(TAG, "Size:" + videoList.get(0).getSize());
                    Log.d(TAG, "Type:" + videoList.get(0).getType());
                }
                else {
                    Toast.makeText(getApplicationContext(), "No videos found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }

        });

        videoRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                videoRecyclerView, new RecyclerTouchListener.ClickListener()
        {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "Key:" + videoList.get(position).getKey());
                key = videoList.get(position).getKey();
                Intent intent = new Intent(DetailActivity.this, YouTubeActivity.class);
                intent.putExtra("videoId",key);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public void onLoadReviews (){

        if (apiKey.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        APIService apiService = Retrofit2.retrofit.create(APIService.class);

        Call<ReviewResponse> callReview = apiService.getReviews(Movies.getId(), apiKey);
        callReview.enqueue(new Callback<ReviewResponse>(){

            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponse reviewResponse = response.body();
                if (reviewResponse != null) {
                    reviewList = (ArrayList<Review>) reviewResponse.getResults();
                    reviewRecyclerView.setAdapter(new ReviewAdapter(reviewList, R.layout.activity_detail_reviews_list_item, getApplicationContext()));
                }

                if (reviewList.size()>0) {
                    Log.d(TAG, "Number of reviews received: " + reviewList.size());
                    Log.d(TAG, "Author:" + reviewList.get(0).getAuthor());
                    Log.d(TAG, "Content:" + reviewList.get(0).getContent());
                    Log.d(TAG, "Id:" + reviewList.get(0).getId());
                    Log.d(TAG, "Url:" + reviewList.get(0).getUrl());
                }
                else {
                        Toast.makeText(getApplicationContext(), "No reviews found", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }

        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("movie", movieList);
        //outState.putParcelableArrayList("video", videoList);
        //outState.putParcelableArrayList("review", reviewList);
        //outState.putParcelable("movie", Movies);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        movieList = savedInstanceState.getParcelableArrayList("movie");
        //videoList = savedInstanceState.getParcelableArrayList("video");
        //reviewList = savedInstanceState.getParcelableArrayList("review");
        //Movies = (Movie) savedInstanceState.getParcelable("movie");

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all inventory attributes, define a projection that contains
        // all columns from the inventory table

        String[] projection = {
                FavoriteEntry._ID,
                FavoriteEntry.COLUMN_FAVORITE_MOVIEID,
                FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE,
                FavoriteEntry.COLUMN_FAVORITE_POSTERPATH,
                FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE,
                FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE,
                FavoriteEntry.COLUMN_FAVORITE_OVERVIEW };

        // This loader will execute the ContentProvider's query method on a background thread

        return new CursorLoader(this,   // Parent activity context
                CurrentFavoriteUri,         // Query the content URI for the current inventory
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        ArrayList<Movie> movieList = new ArrayList<Movie>();
        while (cursor.moveToNext()) {
            MovieId.setText(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID)));
            OriginalTitle.setText(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE)));
            posterPath.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH))));
            ReleaseDate.setText(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE)));
            VoteAverage.setText(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE)));
            Overview.setText(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW)));
        }

        cursor.close();

    }

    //https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists
    //https://stackoverflow.com/questions/15933592/check-if-content-resolver-has-value

    public boolean isFavorite() {

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = getApplicationContext().getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI, new String[]{FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE}, FavoriteEntry.COLUMN_FAVORITE_MOVIEID + " = " + movieIdentification, null, null);

        while (cursor.moveToNext()) {
            if (Movies.getOriginalTitle().equals(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE)))) {
                hasFavorite = true;
            }
        }return hasFavorite;
    }

    //https://stackoverflow.com/questions/44905046/how-to-store-data-fetch-from-retrofit-to-sqlite-database
    //https://discussions.udacity.com/t/favorite-and-un-favorite-implementation/651708

    //https://discussions.udacity.com/t/question-about-the-favorites-button/640499/9

    private void saveFavorite() {

        final Intent intent = getIntent();

        Movies = getIntent().getParcelableExtra("movie");

        String movieIdentification = Integer.toString(Movies.getId());
        String movieThumbnail = Movies.getPosterPath();
        String movieTitle = Movies.getOriginalTitle();
        String dateOfRelease = Movies.getReleaseDate();
        String userRating = Double.toString(Movies.getVoteAverage());
        String movieSynopsis = Movies.getOverview();

        ContentValues values = new ContentValues();

        values.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID, "" + movieIdentification);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE, "" + movieTitle);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH, "" + movieThumbnail); //
        values.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE, "" + dateOfRelease);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE, "" + userRating);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW, "" + movieSynopsis);

        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Added to Favorites - Movie Id:" + Movies.getId());
        Log.d(TAG, "Added to Favorites - Original Title:" + Movies.getOriginalTitle());
        Log.d(TAG, "Added to Favorites - Poster Path:" + Movies.getPosterPath());
        Log.d(TAG, "Added to Favorites - Release Date:" + Movies.getReleaseDate());
        Log.d(TAG, "Added to Favorites - Vote Average:" + Movies.getVoteAverage());
        Log.d(TAG, "Added to Favorites - Overview:" + Movies.getOverview());

        // Determine if this is a new or existing favorite by checking if mCurrentFavoriteUri is null or not
        if (CurrentFavoriteUri == null) {

            // This is a NEW favorite, so insert new favorite into the provider,
            // returning the content URI for the new favorite.
            Uri newUri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_favorite_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_favorite_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        hasFavorite = true;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        MovieId.setText("");
        OriginalTitle.setText("");
        posterPath.setImageBitmap(null);
        ReleaseDate.setText("");
        VoteAverage.setText("");
        Overview.setText("");

    }

    private void deleteFavorite() {

        final Intent intent = getIntent();

        Movies = getIntent().getParcelableExtra("movie");

        movieIdentification = Integer.toString(Movies.getId());
        movieThumbnail = Movies.getPosterPath();
        movieTitle = Movies.getOriginalTitle();
        dateOfRelease = Movies.getReleaseDate();
        userRating = Double.toString(Movies.getVoteAverage());
        movieSynopsis = Movies.getOverview();

        if (FavoriteContract.FavoriteEntry.CONTENT_URI != null) {

            int favoriteDeleted = getContentResolver().delete(FavoriteContract.FavoriteEntry.CONTENT_URI, (FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID + "=" + movieIdentification), null);

            // Show a toast message depending on whether or not the delete was successful.
            if (favoriteDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.detail_delete_favorite_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.detail_delete_favorite_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Removed from Favorites - Movie Id:" + Movies.getId());
        Log.d(TAG, "Removed from Favorites - Original Title:" + Movies.getOriginalTitle());
        Log.d(TAG, "Removed from Favorites - Poster Path:" + Movies.getPosterPath());
        Log.d(TAG, "Removed from Favorites - Release Date:" + Movies.getReleaseDate());
        Log.d(TAG, "Removed from Favorites - Vote Average:" + Movies.getVoteAverage());
        Log.d(TAG, "Removed from Favorites - Overview:" + Movies.getOverview());

        hasFavorite = false;
    }

}

