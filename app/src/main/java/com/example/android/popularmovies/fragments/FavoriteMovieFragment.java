package com.example.android.popularmovies.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MainAdapter;
import com.example.android.popularmovies.model.Favorite;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.roomdatabase.viewmodel.MoviesViewModel;

import java.util.ArrayList;
import java.util.List;

//https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/DetailFragment.java
//https://gist.github.com/AntonioDiaz/c48399619719fccfee982774f66a8650
//https://stackoverflow.com/questions/41267446/how-to-get-loadermanager-initloader-working-within-a-fragment
//https://stackoverflow.com/questions/44905046/how-to-store-data-fetch-from-retrofit-to-sqlite-database

public class FavoriteMovieFragment extends Fragment {

    //Params for saving data

    public static final String PARAM_LIST_STATE = "param-state";
    private static final String TAG = "savedfragment";

    private RecyclerView recyclerView;
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private MainAdapter mAdapter;
    private MoviesViewModel moviesViewModel;
    private boolean showSaved = false;
    private Parcelable listState;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    public static FavoriteMovieFragment newInstance() {
        return new FavoriteMovieFragment();
    }

    public void onLoadViewModel() {

        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        if (showSaved) {
            moviesViewModel.getAllSaved().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    if (movies != null) {
                        mAdapter.setMovies(movies);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(new MainAdapter(movies, R.layout.activity_main_list_item, getActivity()));

                        restoreRecyclerViewState();

                        Log.d(TAG, "FavoritesFragment - ViewModel");
                        Log.d(TAG, "Number of movies received: " + movies.size());
                        Log.d(TAG, "FavoriteMovies: " + movies);

                    } else {
                        //mAdapter.notifyDataSetChanged();
                        restoreRecyclerViewState();
                    }

                    if (movies.size() > 0) {
                        Log.d(TAG, "FavoritesFragment");
                        Log.d(TAG, "Number of movies received : " + movies.size());
                        //Log.d(TAG, "Key: " + movies.get(0).getMovieKey());
                        Log.d(TAG, "Id :" + movies.get(0).getId());
                        Log.d(TAG, "PosterPath :" + movies.get(0).getPosterPath());
                        Log.d(TAG, "Title :" + movies.get(0).getTitle());
                        Log.d(TAG, "Release Date :" + movies.get(0).getReleaseDate());
                        Log.d(TAG, "Average Rating :" + movies.get(0).getVoteAverage());
                        Log.d(TAG, "Overview :" + movies.get(0).getOverview());
                        Log.d(TAG, "SaveDate :" + movies.get(0).getSaveDate());
                    } else {
                        Toast.makeText(getContext(), "No favorites found", Toast.LENGTH_SHORT).show();
                        //mySwipeRefreshLayout.setRefreshing(false);
                    }

                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSaved = true;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favoritemovie, container, false);

        //https://stackoverflow.com/questions/28570842/setlayoutmanager-nullpointexception-in-recyclerview

        if(savedInstanceState != null) {
            Log.v(TAG, "RecyclerView not found!");
            //movies = savedInstanceState.getParcelableArrayList("movie");
        }

        mAdapter = new MainAdapter(movies, R.layout.activity_main_list_item, getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setAdapter(new MainAdapter(movies, R.layout.activity_main_list_item, getActivity()));

        onLoadViewModel();

        setRetainInstance(true);

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(PARAM_LIST_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(PARAM_LIST_STATE, listState);
        //savedInstanceState.putParcelableArrayList("movie", movies);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            //movies = savedInstanceState.getParcelableArrayList("movie");
            savedInstanceState.getParcelable(PARAM_LIST_STATE);
        }
    }

    private void restoreRecyclerViewState() {
        if (recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    //https://stackoverflow.com/questions/13472258/handling-actionbar-title-with-the-fragment-back-stack

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.nav_favoriteMovie);

    }

}
