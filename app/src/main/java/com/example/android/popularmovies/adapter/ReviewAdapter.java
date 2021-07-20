package com.example.android.popularmovies.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> reviews;
    private int rowLayout;
    private Context context;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        LinearLayout reviewsLayout;
        TextView authorBase;
        TextView author;
        TextView content;

        public ReviewViewHolder(View v) {
            super(v);
            reviewsLayout = (LinearLayout) v.findViewById(R.id.reviews_layout);
            authorBase = (TextView) v.findViewById(R.id.authorBase);
            author = (TextView) v.findViewById(R.id.author);
            content = (TextView) v.findViewById(R.id.content);
        }
    }

    public ReviewAdapter(ArrayList<Review> reviews, int rowLayout, Context context) {
        this.reviews = reviews;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}