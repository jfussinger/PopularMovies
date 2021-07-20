package com.example.android.popularmovies.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activity.PaginationScrollListener;
import com.example.android.popularmovies.adapter.MainAdapter;
import com.example.android.popularmovies.apiservice.APIService;
import com.example.android.popularmovies.apiservice.Retrofit2;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

public class PopularMovieFragment extends Fragment {

    public PopularMovieFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "popularmoviefragment";

    private ArrayList<Movie> movies = new ArrayList<Movie>();

    private MainAdapter mAdapter;

    String apiKey = BuildConfig.MOVIE_DB_API_KEY;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private static final int TOTAL_PAGES = 1000;
    private int currentPage = PAGE_START;

    private APIService apiService;

    GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;

    private int position;

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Parcelable savedMoviesRecyclerLayoutState;
    private static final String BUNDLE_MOVIES_RECYCLER_VIEW = "bundleMoviesRecyclerView";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movie");
            savedMoviesRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_MOVIES_RECYCLER_VIEW);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedMoviesRecyclerLayoutState);
            currentPage = savedInstanceState.getInt("currentPage");
            Log.d(TAG, "OnCreate onSaveInstanceState: Loading page: " + currentPage);
        }

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_popularmovie, container, false);

        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        mAdapter = new MainAdapter(movies, R.layout.activity_main_list_item, getActivity());

        //https://stackoverflow.com/questions/21339086/gridview-and-navigation-drawer-not-working-together-in-android
        //https://stackoverflow.com/questions/22890314/gridview-is-not-shown-in-an-example-with-navigation-drawer

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MainAdapter(movies, R.layout.activity_main_list_item, getActivity()));

        gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                onLoadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        apiService = Retrofit2.retrofit.create(APIService.class);

        if(savedInstanceState != null) {
            Log.v(TAG, "RecyclerView not found!");
            movies = savedInstanceState.getParcelableArrayList("movie");
            savedMoviesRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_MOVIES_RECYCLER_VIEW);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedMoviesRecyclerLayoutState);
            currentPage = savedInstanceState.getInt("currentPage");
            Log.d(TAG, "onCreateView onSaveInstanceState: Loading page: " + currentPage);
        } else {
            onLoadFirstPage();
        }

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLoadingSwipeRefresh();
                //mySwipeRefreshLayout.setRefreshing(false);
            }
        });

        setRetainInstance(true);

        // Inflate the layout for this fragment
        return view;

    }

    private void onLoadingSwipeRefresh() {

        mAdapter.getMovies().clear();
        mAdapter.notifyDataSetChanged();
        onLoadFirstPage();
        //isLoading = true;
        mySwipeRefreshLayout.setRefreshing(false);
    }

    public void onLoadFirstPage() {

        currentPage = PAGE_START;

        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment

        if (apiKey.isEmpty()) {
            Toast.makeText(getActivity(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        //https://stackoverflow.com/questions/39378586/how-to-create-multiple-retrofit-callbacks-in-same-fragment-android
        //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit

        //APIService apiService = Retrofit2.retrofit.create(APIService.class);

        //Call<MovieResponse> callPopularMovie = apiService.getPopularMovie(apiKey, currentPage);
        callPopularMovie().enqueue(new Callback<MovieResponse>() {

            //https://stackoverflow.com/questions/35254843/gridview-setadapter-method-gives-nullpointerexception

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse popularMovieResponse = response.body();
                if (popularMovieResponse != null) {
                    movies = (ArrayList<Movie>) popularMovieResponse.getResults();
                    mAdapter.addAll(movies);
                    mAdapter.setMovies(movies);
                    //mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(new MainAdapter(movies, R.layout.activity_main_list_item, getActivity()));

                    if (currentPage <= TOTAL_PAGES) mAdapter.addLoadingFooter();
                    else isLastPage = true;

                    Log.d(TAG, "PopularMoviesFragment");
                    Log.d(TAG, "Number of movies received: " + movies.size());
                    Log.d(TAG, "PopularMovies: " + movies);
                }
                if (movies.size()>0) {
                    Log.d(TAG, "PopularMoviesFragment - First Page");
                    Log.d(TAG, "Loading page: " + currentPage);
                    Log.d(TAG, "Number of movies received: " + movies.size());
                    Log.d(TAG, "Id: " + movies.get(0).getId());
                    Log.d(TAG, "PosterPath: " + movies.get(0).getPosterPath());
                    Log.d(TAG, "Title: " + movies.get(0).getTitle());
                    Log.d(TAG, "Release Date: " + movies.get(0).getReleaseDate());
                    Log.d(TAG, "Average Rating: " + movies.get(0).getVoteAverage());
                    Log.d(TAG, "Overview: " + movies.get(0).getOverview());
                    Log.d(TAG, "Popularity: " + movies.get(0).getPopularity());
                    Log.d(TAG, "SaveDate: " + movies.get(0).getSaveDate());
                }
                else {
                    Toast.makeText(getActivity(), "No movies found", Toast.LENGTH_SHORT).show();
                    //mySwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }

        });
    }

    public void onLoadNextPage() {

        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment

        if (apiKey.isEmpty()) {
            Toast.makeText(getActivity(), "Insert your API KEY first from The Movie Db", Toast.LENGTH_LONG).show();
        }

        //https://stackoverflow.com/questions/39378586/how-to-create-multiple-retrofit-callbacks-in-same-fragment-android
        //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit

        //APIService apiService = Retrofit2.retrofit.create(APIService.class);

        //Call<MovieResponse> callPopularMovie = apiService.getPopularMovie(apiKey, currentPage);
        callPopularMovie().enqueue(new Callback<MovieResponse>() {

            //https://stackoverflow.com/questions/35254843/gridview-setadapter-method-gives-nullpointerexception

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse popularMovieResponse = response.body();
                if (popularMovieResponse != null) {
                    mAdapter.removeLoadingFooter();
                    isLoading = false;

                    movies = (ArrayList<Movie>) popularMovieResponse.getResults();
                    mAdapter.addAll(movies);
                    mAdapter.setMovies(movies);
                    //mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(new MainAdapter(movies, R.layout.activity_main_list_item, getActivity()));

                    if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
                    else isLastPage = true;

                    Log.d(TAG, "PopularMoviesFragment");
                    Log.d(TAG, "Number of movies received: " + movies.size());
                    Log.d(TAG, "PopularMovies: " + movies);
                }
                if (movies.size()>0) {
                    Log.d(TAG, "PopularMoviesFragment - Next Page");
                    Log.d(TAG, "Loading page: " + currentPage);
                    Log.d(TAG, "Number of movies received: " + movies.size());
                    Log.d(TAG, "Id: " + movies.get(0).getId());
                    Log.d(TAG, "PosterPath: " + movies.get(0).getPosterPath());
                    Log.d(TAG, "Title: " + movies.get(0).getTitle());
                    Log.d(TAG, "Release Date: " + movies.get(0).getReleaseDate());
                    Log.d(TAG, "Average Rating: " + movies.get(0).getVoteAverage());
                    Log.d(TAG, "Overview: " + movies.get(0).getOverview());
                    Log.d(TAG, "Popularity: " + movies.get(0).getPopularity());
                    Log.d(TAG, "SaveDate: " + movies.get(0).getSaveDate());
                }
                else {
                    Toast.makeText(getActivity(), "No movies found", Toast.LENGTH_SHORT).show();
                    //mySwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }

        });
    }

    private Call<MovieResponse> callPopularMovie() {
        return apiService.getPopularMovie(
                apiKey,
                currentPage
        );
    }

    //@Override
    public void retryPageLoad() {
        onLoadNextPage();
    }

    //https://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview#5688490
    //https://stackoverflow.com/questions/8619794/maintain-scroll-position-of-gridview-through-screen-rotation

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie", movies);
        outState.putInt("position", position);
        outState.putParcelable(BUNDLE_MOVIES_RECYCLER_VIEW, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt("currentPage", currentPage);
        Log.d(TAG, "onSaveInstanceState: Loading page: " + currentPage);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movie");
            position = savedInstanceState.getInt("position");
            savedMoviesRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_MOVIES_RECYCLER_VIEW);
            currentPage = savedInstanceState.getInt("currentPage");
            Log.d(TAG, "onViewStateRestored: Loading page: " + currentPage);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.nav_popularMovie);
    }

}