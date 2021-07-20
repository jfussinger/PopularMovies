package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.VideoAdapter;
import com.example.android.popularmovies.apiservice.APIService;
import com.example.android.popularmovies.apiservice.Retrofit2;
import com.example.android.popularmovies.model.Movie;

import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.ReviewResponse;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.model.VideoResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.android.popularmovies.roomdatabase.MoviesRepository;

public class  DetailActivity extends AppCompatActivity {

    private ImageView favoriteClick;

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String TAG = "detailactivity";

    Movie Movies;
    private int id;
    String key;
    String movieIdentification, movieThumbnail, movieTitle, dateOfRelease, movieSynopsis;
    Double moviePopularity;
    Double userRating;
    TextView MovieId;
    ImageView posterPath;
    TextView OriginalTitle;
    TextView ReleaseDate;
    TextView VoteAverage;
    TextView Overview;
    Video Videos;
    Review Reviews;
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private ArrayList<Video> videoList = new ArrayList<Video>();
    private ArrayList<Review> reviewList = new ArrayList<Review>();
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private RecyclerView videoRecyclerView;
    private RecyclerView reviewRecyclerView;

    private ImageView FavoriteIcon;

    private ImageView ImageImageView;
    private TextView ImageTextView;

    private Intent shareIntent;

    String apiKey = BuildConfig.MOVIE_DB_API_KEY;

    private LinearLayoutManager linearLayoutManager;

    private Parcelable savedVideoRecyclerLayoutState;
    private Parcelable savedReviewRecyclerLayoutState;
    private static final String BUNDLE_VIDEO_RECYCLER_VIEW = "bundleVideoRecyclerView";
    private static final String BUNDLE_REVIEW_RECYCLER_VIEW = "bundleReviewRecyclerView";

    private ScrollView mScrollView;

    private MoviesRepository moviesRepository;

    private Boolean hasFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moviesRepository = MoviesRepository.getInstance(this);

        Log.d(TAG, "onCreate Lifecycle invoked");

        setContentView(R.layout.activity_detail);

        mScrollView = (ScrollView) findViewById(R.id.scrollview_detail);

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            Movies = getIntent().getParcelableExtra("movies");
            id = bundle.getInt("id");
            movieThumbnail = bundle.getString("moviePosterPath");
            movieTitle = bundle.getString("movieTitle");
            dateOfRelease = bundle.getString("movieReleaseDate");
            userRating = bundle.getDouble("movieAverageRating");
            moviePopularity = bundle.getDouble("moviePopularity");
            movieSynopsis = bundle.getString("movieOverview");

            //movieIdentification = Integer.toString(Movies.getId());
            //movieThumbnail = Movies.getPosterPath();
            //movieTitle = Movies.getOriginalTitle();
            //dateOfRelease = Movies.getReleaseDate();
            //userRating = Double.toString(Movies.getVoteAverage());
            //movieSynopsis = Movies.getOverview();


            Log.d(TAG, "DetailActivity");
            Log.d(TAG, "Id: " + id);
            Log.d(TAG, "Thumbnail: " + movieThumbnail);
            Log.d(TAG, "Title: " + movieTitle);
            Log.d(TAG, "Release Date: " + dateOfRelease);
            Log.d(TAG, "User Rating: " + userRating);
            Log.d(TAG, "Popularity: " + moviePopularity);
            Log.d(TAG, "Overview: " + movieSynopsis);

            final String base_url = "http://image.tmdb.org/t/p/";
            final String file_size = "w500/";
            final String posterPathString = Movies.getPosterPath();

            if(movieThumbnail==null){
                posterPath.setVisibility(View.GONE);
            }else {
                Glide.with(this)
                        .load(base_url + file_size + posterPathString)
                        .placeholder(R.drawable.placeholderimagemainactivity)
                        .into(posterPath);
            }
            if(movieIdentification==null){
                MovieId.setVisibility(View.GONE);
            }else {
                MovieId.setText(movieIdentification);
            }
            if(movieTitle==null){
                OriginalTitle.setVisibility(View.GONE);
            }else {
                OriginalTitle.setText(movieTitle);
            }
            if(dateOfRelease==null){
                ReleaseDate.setVisibility(View.GONE);
            }else {
                ReleaseDate.setText(dateOfRelease);
            }
            if(userRating==null){
                VoteAverage.setVisibility(View.GONE);
            }else {
                VoteAverage.setText(Double.toString(userRating));
            }
            if(movieSynopsis==null){
                Overview.setVisibility(View.GONE);
            }else {
                Overview.setText(movieSynopsis);
            }
        } else {
            Toast.makeText(this, "Insert your API KEY first from The Movie Db", Toast.LENGTH_SHORT).show();
        }

        onLoadVideos();

        onLoadReviews();

        //https://github.com/amardeshbd/android-recycler-view-wrap-content/blob/master/app/src/main/java/info/hossainkhan/recyclerviewdemo/RecyclerViewNestedScrollviewFixDemoFragment.java

        videoRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
        videoRecyclerView.setAdapter(videoAdapter);
        videoRecyclerView.setNestedScrollingEnabled(false);

        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        //mScrollView.setSmoothScrollingEnabled(true);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movie");
            savedVideoRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_VIDEO_RECYCLER_VIEW);
            savedReviewRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_REVIEW_RECYCLER_VIEW);
        }

        //https://stackoverflow.com/questions/4193167/change-source-image-for-image-view-when-pressed?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        //https://stackoverflow.com/questions/6083641/android-imageviews-onclicklistener-does-not-work?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        favoriteClick = (ImageView) findViewById(R.id.favorite);

        favoriteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFavorite) {
                    moviesRepository.deleteFavorite(id);
                    favoriteClick.setImageResource(R.drawable.ic_not_favorite);
                    hasFavorite = false;
                    Toast.makeText(getApplicationContext(), "Removed from Favorites" + " " + movieTitle, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Removed from Favorites - Id: " + id);
                    Log.d(TAG, "Removed from Favorites - Original Title: " + movieTitle);
                    Log.d(TAG, "Removed from Favorites - Poster Path: " + movieThumbnail);
                    Log.d(TAG, "Removed from Favorites - Release Date: " + dateOfRelease);
                    Log.d(TAG, "Removed from Favorites - Popularity: " + moviePopularity);
                    Log.d(TAG, "Removed from Favorites - Vote Average: " + userRating);
                    Log.d(TAG, "Removed from Favorites - Overview: " + movieSynopsis);

                } else {
                    moviesRepository.saveFavorite(id, movieThumbnail, movieTitle, dateOfRelease, userRating, movieSynopsis);
                    favoriteClick.setImageResource(R.drawable.ic_favorite);
                    hasFavorite = true;
                    Toast.makeText(getApplicationContext(), "Added to Favorites"+ " " + movieTitle, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Added to Favorites - Id: " + id);
                    Log.d(TAG, "Added to Favorites - Original Title: " + movieTitle);
                    Log.d(TAG, "Added to Favorites - Poster Path: " + movieThumbnail);
                    Log.d(TAG, "Added to Favorites - Release Date: " + dateOfRelease);
                    Log.d(TAG, "Added from Favorites - Popularity: " + moviePopularity);
                    Log.d(TAG, "Added to Favorites - Vote Average: " + userRating);
                    Log.d(TAG, "Added to Favorites - Overview: " + movieSynopsis);
                }
            }
        });

        if(isSaved()){
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

    public boolean isSaved() {
        if (movieTitle != null) {
            moviesRepository.isSaved(id).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean favoriteBoolean) {
                    if (favoriteBoolean != null) {
                        hasFavorite = favoriteBoolean;
                        if (hasFavorite) {
                            favoriteClick.setImageResource(R.drawable.ic_favorite);
                            hasFavorite = true;
                        }
                    }
                }
            });
        }

        return hasFavorite;
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
                    Log.d(TAG, "Id: " + videoList.get(0).getId());
                    Log.d(TAG, "Language: " + videoList.get(0).getIso_639_1());
                    Log.d(TAG, "Country: " + videoList.get(0).getIso_3166_1());
                    Log.d(TAG, "Key: " + videoList.get(0).getKey());
                    Log.d(TAG, "Name: " + videoList.get(0).getName());
                    Log.d(TAG, "Site: " + videoList.get(0).getSite());
                    Log.d(TAG, "Size: " + videoList.get(0).getSize());
                    Log.d(TAG, "Type: " + videoList.get(0).getType());
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
                    Log.d(TAG, "Author: " + reviewList.get(0).getAuthor());
                    Log.d(TAG, "Content: " + reviewList.get(0).getContent());
                    Log.d(TAG, "Id: " + reviewList.get(0).getId());
                    Log.d(TAG, "Url: " + reviewList.get(0).getUrl());
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

    //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("movie", movies);

        //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

        //https://stackoverflow.com/questions/36568168/how-to-save-scroll-position-of-recyclerview-in-android

        outState.putParcelable(BUNDLE_VIDEO_RECYCLER_VIEW, videoRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_REVIEW_RECYCLER_VIEW, reviewRecyclerView.getLayoutManager().onSaveInstanceState());

        //https://stackoverflow.com/questions/28658579/refreshing-data-in-recyclerview-and-keeping-its-scroll-position

        //https://stackoverflow.com/questions/29208086/save-the-position-of-scrollview-when-the-orientation-changes

        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movie");

            //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

            savedVideoRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_VIDEO_RECYCLER_VIEW);
            savedReviewRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_REVIEW_RECYCLER_VIEW);

            final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if(position != null)
                mScrollView.postDelayed(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);}
                }, 100);
        }
    }

}

