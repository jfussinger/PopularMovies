package com.example.android.popularmovies.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activity.DetailActivity;
import com.example.android.popularmovies.model.Movie;

import java.util.List;

import javax.security.auth.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;

//https://github.com/mitchtabian/Recyclerview/blob/master/RecyclerView/app/src/main/java/codingwithmitch/com/recyclerview/RecyclerViewAdapter.java

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private static final String TAG = "mainadapter";

    public List<Movie> movies;
    public int rowLayout;
    private Context context;

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private MainAdapterCallback callback;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        if (movies != null) {
            this.movies = movies;
            notifyDataSetChanged();
        }
    }

    public MainAdapter(List<Movie> movieList, int rowLayout, Context context) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.movies = movieList;
        //this.callback = (MainAdapterCallback) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_list_item, parent, false);
        @NonNull MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        //https://stackoverflow.com/questions/40577659/android-recyclerview-gridlayoutmanager-onclick-not-working

        Movie Movies = movies.get(i);

        if (movies.get(i).getPosterPath()==null) {
            holder.posterPath.setVisibility(View.GONE);
        } else {

            // Poster_path URL
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
            final String file_size = "w185/";
            final String posterPathString = movies.get(i).getPosterPath();

            Glide.with(context)
                    .load(base_url + file_size + posterPathString)
                    .placeholder(R.drawable.placeholderimagemainactivity)
                    .into(holder.posterPath);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailActivity.class);

                //Movie Movies = movies.get(holder.getAdapterPosition());
                intent.putExtra("movies", Movies);
                //intent.putExtra("movieKey", movies.get(holder.getAdapterPosition()).getMovieKey());
                intent.putExtra("id", movies.get(holder.getAdapterPosition()).getId());
                intent.putExtra("moviePosterPath", movies.get(holder.getAdapterPosition()).getPosterPath());
                intent.putExtra("movieTitle", movies.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("movieReleaseDate", movies.get(holder.getAdapterPosition()).getReleaseDate());
                intent.putExtra("movieAverageRating", movies.get(holder.getAdapterPosition()).getVoteAverage());
                intent.putExtra("movieOverview", movies.get(holder.getAdapterPosition()).getOverview());
                intent.putExtra("moviePopularity", movies.get(holder.getAdapterPosition()).getPopularity());
                intent.putExtra("saveDate", movies.get(holder.getAdapterPosition()).getSaveDate());

                //https://stackoverflow.com/questions/23182853/calling-startactivity-from-outside-of-an-activity-context-requires-the-flag-ac

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

                Log.d(TAG, "MainAdapter");
                Log.d(TAG, "movies : " + movies);
                //Log.d(TAG, "movieKey:" + movies.get(i).getMovieKey());
                Log.d(TAG, "id:" + movies.get(i).getId());
                Log.d(TAG, "moviePosterPath : " + movies.get(i).getPosterPath());
                Log.d(TAG, "movieTitle : " + movies.get(i).getTitle());
                Log.d(TAG, "Release Date : " + movies.get(i).getReleaseDate());
                Log.d(TAG, "Average Rating : " + movies.get(i).getVoteAverage());
                Log.d(TAG, "Overview : " + movies.get(i).getOverview());
                Log.d(TAG, "Popularity : " + movies.get(i).getPopularity());
                Log.d(TAG, "SaveDate : " + movies.get(i).getSaveDate());

                Toast.makeText(holder.context, "Movie: " + movies.get(i).getTitle() + " " + "selected",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        } else {
            return 0;
        }
        //return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    //Pagination helpers

    public void add(Movie r) {
        movies.add(r);
        notifyItemInserted(movies.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void remove(Movie r) {
        int position = movies.indexOf(r);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movies.size() - 1);

        //if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent_layout) LinearLayout parentLayout;
        @BindView(R.id.grid_item_posterPath) ImageView posterPath;

        Context context;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();

        }
    }

}

