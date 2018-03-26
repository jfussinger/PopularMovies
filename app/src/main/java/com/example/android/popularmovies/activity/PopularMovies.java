package com.example.android.popularmovies.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//https://github.com/google/gson/blob/master/UserGuide.md

public class PopularMovies implements Serializable {

    /**
     * Poster_Path of the popular movie
     */
    @SerializedName("poster_path")
    @Expose
    private String Poster_Path;

    /**
     * Original_Title of the popular movie
     */
    @SerializedName("original_title")
    @Expose
    private String Original_Title;

    /**
     * Overview is a plot synopsis of the popular movie
     */
    @SerializedName("overview")
    @Expose
    private String Overview;

    /**
     * Vote_Average is the user rating of the popular movie
     */
    @SerializedName("vote_average")
    @Expose
    private Double Vote_Average;

    /**
     * Release_Date is the release date of the popular movie
     */
    @SerializedName("release_date")
    @Expose
    private String Release_Date;

    /**
     * popularity is the popularity of the popular movie
     */
    @SerializedName("popularity")
    @Expose
    private Double Popularity;

    /**
     * Returns the posterPath of the popular movie.
     */
    public String getPoster_Path() {
        return Poster_Path;
    }

    //Set the poster_path
    public void setPoster_Path (String poster_path) { this.Poster_Path = poster_path; }

    /**
     * Returns the originalTitle of the popular movie.
     */
    public String getOriginalTitle() {
        return Original_Title;
    }

    //Set the original title
    public void setOriginal_Title (String original_title) { this.Original_Title = original_title; }

    /**
     * Returns the overview of the popular movie.
     */
    public String getOverview() {
        return Overview;
    }

    //Set the overview
    public void setOverview (String overview) { this.Overview = overview; }

    /**
     * Returns the user rating of the popular movie.
     */
    public Double getVote_Average() {
        return Vote_Average;
    }

    //Set the vote_average
    public void setVote_Average (Double vote_average) { this.Vote_Average = vote_average; }

    /**
     * Returns the release date of the popular movie.
     */
    public String getRelease_Date() {
        return Release_Date;
    }

    //Set the release_date
    public void setRelease_Date (String release_date) { this.Release_Date = release_date; }

    /**
     * Returns the popularity of the popular movie.
     */
    public Double getPopularity() {
        return Popularity;
    }

    //Set the popularity
    public void setPopularity (Double popularity) { this.Popularity = popularity; }

}
