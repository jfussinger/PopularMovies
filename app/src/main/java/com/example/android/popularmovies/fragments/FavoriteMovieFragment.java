package com.example.android.popularmovies.fragments;

import android.net.Uri;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.FavoriteAdapter;
import com.example.android.popularmovies.data.FavoriteContract;
import com.example.android.popularmovies.data.FavoriteDbHelper;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

//https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/DetailFragment.java
//https://gist.github.com/AntonioDiaz/c48399619719fccfee982774f66a8650
//https://stackoverflow.com/questions/41267446/how-to-get-loadermanager-initloader-working-within-a-fragment
//https://stackoverflow.com/questions/44905046/how-to-store-data-fetch-from-retrofit-to-sqlite-database

public class FavoriteMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public FavoriteMovieFragment() {
    }

    private static final String TAG = "favoritemoviefragment";

    private GridView favoritemoviesGridview;

    private ArrayList<Movie> movieList = new ArrayList<Movie>();

    private Movie favoriteMovies;

    private FavoriteAdapter favoriteAdapter;

    public static final String CONTENT_URI = "URI";

    private Uri mUri;

    private static final int FAVORITE_LOADER = 0;

    private FavoriteDbHelper favoriteDbHelper;

    int movie_id;

    //https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/DetailFragment.java

    public static final String[] FAVORITE_DETAIL_PROJECTION = {
            FavoriteContract.FavoriteEntry._ID,
            FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID,
            FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE,
            FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH,
            FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE,
            FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE,
            FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW
    };

    /*
     * The columns of data that we are interested in displaying within our DetailActivity's
     * FavoriteMovie display.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList("movie");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie", movieList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(FavoriteMovieFragment.CONTENT_URI);
        }

        //https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/DetailFragment.java

        final View view = inflater.inflate(R.layout.fragment_favoritemovie, container, false);
        favoritemoviesGridview = (GridView) view.findViewById(R.id.gridview_fragmentFavoriteMovie);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = view.findViewById(R.id.empty_view);
        favoritemoviesGridview.setEmptyView(emptyView);

        // Find the ListView which will be populated with the favorite data

        favoriteAdapter = new FavoriteAdapter(getContext(), null, movieList);
        favoritemoviesGridview.setAdapter(favoriteAdapter);

        //https://stackoverflow.com/questions/8619794/maintain-scroll-position-of-gridview-through-screen-rotation

        // Kick off the loader
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);

        // Inflate the layout for this fragment
        return view;

    }

    //https://stackoverflow.com/questions/23154037/accessing-sqlite-database-from-fragment
    //https://github.com/udacity/ud851-Sunshine/blob/student/S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/DetailActivity.java

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                FavoriteContract.FavoriteEntry.CONTENT_URI,   // Provider content URI to query
                FAVORITE_DETAIL_PROJECTION,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        favoriteAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        favoriteAdapter.swapCursor(null);

    }

}

