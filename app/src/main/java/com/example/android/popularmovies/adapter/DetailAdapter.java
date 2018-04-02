package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

import java.util.List;

public class DetailAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {

        ImageView posterPath;
        TextView OriginalTitle;
        TextView ReleaseDate;
        TextView VoteAverage;
        TextView Overview;

    }

    public DetailAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.

        ViewHolder viewHolder;
        Movie Movies = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.posterPath = (ImageView) convertView.findViewById(R.id.posterPath);
            viewHolder.OriginalTitle = (TextView) convertView.findViewById(R.id.original_title);
            viewHolder.ReleaseDate = (TextView) convertView.findViewById(R.id.release_date);
            viewHolder.VoteAverage = (TextView) convertView.findViewById(R.id.vote_average);
            viewHolder.Overview = (TextView) convertView.findViewById(R.id.overview);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // TODO Build poster_path URL
        // https://developers.themoviedb.org/3/getting-started/images
        //
        // 1. The base URL will look like: http://image.tmdb.org/t/p/
        // 2. Then you will need a ‘size’, which will be one of the following:
        // "w92", "w154", "w185", "w342", "w500", "w780", or "original".
        // For most phones we recommend using “w185”.
        // 3. And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”
        //
        // Combining these three parts gives us a final url of
        // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg

        //https://stackoverflow.com/questions/47368652/passing-poster-path-from-one-activity-to-another

        final String base_url = "http://image.tmdb.org/t/p/";
        final String file_size = "w185";
        final String file_path = Movies.getPosterPath();

        String uri = Uri.parse(base_url)
                .buildUpon()
                .appendQueryParameter(file_size, file_path)
                .build().toString();

        final String posterPath = uri;
        Glide.with(getContext()).load(posterPath).placeholder(R.drawable.imagenotfound).into(viewHolder.posterPath);

        viewHolder.OriginalTitle.setText(Movies.getOriginalTitle());
        viewHolder.ReleaseDate.setText(Movies.getReleaseDate());

        //https://stackoverflow.com/questions/16334820/how-to-convert-double-to-string-in-android
        //double Vote_Average = PopularMovies.getVote_Average();
        //String voteAverageString = String.valueOf(Vote_Average);

        //https://stackoverflow.com/questions/35495421/android-passing-a-double-to-a-textview
        //viewHolder.VoteAverage.setText(Double.toString(PopularMovies.getVote_Average()));

        //https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
        viewHolder.VoteAverage.setText(String.format("0.00" ,Movies.getVoteAverage()));
        viewHolder.Overview.setText(Movies.getOverview());

        return convertView;
    }
}
