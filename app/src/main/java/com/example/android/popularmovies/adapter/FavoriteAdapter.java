package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activity.DetailActivity;
import com.example.android.popularmovies.data.FavoriteContract;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

//https://stackoverflow.com/questions/44905046/how-to-store-data-fetch-from-retrofit-to-sqlite-database

public class FavoriteAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link FavoriteAdapter}.
     *
     * @param context The context
     * @param c The cursor from which to get the data.
     */
    public FavoriteAdapter(Context context, Cursor c, List<Movie> movieList) {

        super(context, c, 0 /* flags */);

    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param viewGroup  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.activity_favorite_list_item, viewGroup, false);

    }

    /**
     * This method binds the favorite data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */



    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        // Find individual views that we want to modify in the list item layout

        final TextView originaltitleTextView = (TextView) view.findViewById(R.id.favorite_movie_original_title);
        ImageView posterPathImageView = (ImageView) view.findViewById(R.id.favorite_movie_posterPath);

        final int _id = cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry._ID));

        final String originalTitle = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE));
        final String posterPath = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH));

        //https://stackoverflow.com/questions/47779756/resourcesnotfoundexception-string-resource-id-0x1

        originaltitleTextView.setText(originalTitle);

        final String base_url = "http://image.tmdb.org/t/p/";
        final String file_size = "w185/";

        Glide.with(context)
                .load(base_url + file_size + posterPath)
                .placeholder(R.drawable.placeholderimagemainactivity)
                .into(posterPathImageView);

        final Uri uri = Uri.parse(FavoriteContract.FavoriteEntry.CONTENT_URI + "/" + _id);

        final int position = cursor.getPosition();

        //https://stackoverflow.com/questions/37509486/android-recyclerview-onclicklistener-not-working-with-cursor

        posterPathImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);

                ArrayList<Movie> movieList = new ArrayList<Movie>();

                Movie Movies = new Movie();
                Movies.setId(cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID)));
                Movies.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE)));
                Movies.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH)));
                Movies.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE)));
                Movies.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE)));
                Movies.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW)));

                movieList.add(Movies);

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", Movies);
                context.startActivity(intent);
                Toast.makeText(context,String.valueOf(originalTitle), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

