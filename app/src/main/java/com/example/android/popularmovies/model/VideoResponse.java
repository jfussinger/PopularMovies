package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Video> results;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public List<Video> getMovies() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    public void setMovies(List<Video> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.results);
    }

    public VideoResponse() {
    }

    protected VideoResponse(Parcel in) {
        this.id = in.readInt();
        this.results = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Parcelable.Creator<VideoResponse> CREATOR = new Parcelable.Creator<VideoResponse>() {
        @Override
        public VideoResponse createFromParcel(Parcel source) {
            return new VideoResponse(source);
        }

        @Override
        public VideoResponse[] newArray(int size) {
            return new VideoResponse[size];
        }
    };
}