package com.example.android.popularmovies.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;

import java.util.List;

public class DetailAdapter extends ArrayAdapter<PopularMovies> {

    private static class ViewHolder {

        ImageView backdropPath;
        TextView OriginalTitle;
        TextView ReleaseDate;
        TextView VoteAverage;
        TextView Overview;

    }

    public DetailAdapter(Context context, List<PopularMovies> popularmovies) {
        super(context, 0, popularmovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.

        ViewHolder viewHolder;
        PopularMovies PopularMovies = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.popularmovies_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.backdropPath = (ImageView) convertView.findViewById(R.id.backdropPath);
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

        final String backdropPath = PopularMovies.getPoster_Path();
        Glide.with(getContext()).load(backdropPath).placeholder(R.drawable.imagenotfound).into(viewHolder.backdropPath);

        viewHolder.OriginalTitle.setText(PopularMovies.getOriginalTitle());
        viewHolder.ReleaseDate.setText(PopularMovies.getRelease_Date());

        //https://stackoverflow.com/questions/16334820/how-to-convert-double-to-string-in-android
        //double Vote_Average = PopularMovies.getVote_Average();
        //String voteAverageString = String.valueOf(Vote_Average);

        //https://stackoverflow.com/questions/35495421/android-passing-a-double-to-a-textview
        //viewHolder.VoteAverage.setText(Double.toString(PopularMovies.getVote_Average()));

        //https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
        viewHolder.VoteAverage.setText(String.format("0.00" ,PopularMovies.getVote_Average()));
        viewHolder.Overview.setText(PopularMovies.getOverview());

        return convertView;
    }
}
