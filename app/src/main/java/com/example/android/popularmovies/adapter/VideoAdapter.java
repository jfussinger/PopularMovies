package com.example.android.popularmovies.adapter;

//https://stackoverflow.com/questions/47753405/get-selected-video-in-youtube-intent-or-youtube-player?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
//https://www.codeproject.com/Articles/1214971/Youtube-channel-integration-in-Android-using

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Video;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private ArrayList<Video> videos;
    private int rowLayout;
    private Context context;

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout videosLayout;
        ImageView imageyoutubevideo;
        TextView name;
        TextView site;
        TextView type;
        TextView iso_3166_1;
        TextView iso_639_1;
        TextView key;

        public VideoViewHolder(View v) {
            super(v);
            videosLayout = (LinearLayout) v.findViewById(R.id.videos_layout);
            imageyoutubevideo = (ImageView) v.findViewById(R.id.imageyoutubevideo);
            name = (TextView) v.findViewById(R.id.name);
            site = (TextView) v.findViewById(R.id.site);
            type = (TextView) v.findViewById(R.id.type);
            iso_3166_1 = (TextView) v.findViewById(R.id.iso_3166_1);
            iso_639_1 = (TextView) v.findViewById(R.id.iso_639_1);
            key = (TextView) v.findViewById(R.id.key);
        }
    }

    public VideoAdapter(ArrayList<Video> videos, int rowLayout, Context context) {
        this.videos = videos;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {
        holder.name.setText(videos.get(position).getName());
        holder.site.setText(videos.get(position).getSite());
        holder.type.setText(videos.get(position).getType());
        holder.iso_3166_1.setText(videos.get(position).getIso_3166_1());
        holder.iso_639_1.setText(videos.get(position).getIso_639_1());
        holder.key.setText(videos.get(position).getKey());

        final String base_url = "http://img.youtube.com/vi/";
        final String key = videos.get(position).getKey();
        final String jpg = "/0.jpg";

        //https://stackoverflow.com/questions/45938741/android-glide-in-the-recyclerview

        Glide.with(context)
                .load(base_url + key + jpg)
                .placeholder(R.drawable.placeholderimageyoutubevideo)
                .into(holder.imageyoutubevideo);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

}


