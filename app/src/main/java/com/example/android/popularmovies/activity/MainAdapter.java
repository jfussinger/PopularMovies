package com.example.android.popularmovies.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;

import java.util.List;

public class MainAdapter extends ArrayAdapter<PopularMovies> {

    private static class ViewHolder {

        ImageView backdropPath;
    }

    public MainAdapter(Context context, List<PopularMovies> popularmovies) {
        super(context, 0, popularmovies);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.

        MainAdapter.ViewHolder viewHolder;
        PopularMovies PopularMovies = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_list_item, parent, false);
            viewHolder = new MainAdapter.ViewHolder();
            viewHolder.backdropPath = (ImageView) convertView.findViewById(R.id.backdropPath);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (MainAdapter.ViewHolder) convertView.getTag();
        }

        final String backdropPath = PopularMovies.getPoster_Path();
        Glide.with(getContext()).load(backdropPath).placeholder(R.drawable.imagenotfound).into(viewHolder.backdropPath);

        return convertView;
    }
}
