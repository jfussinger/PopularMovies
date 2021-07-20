package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath;

    @Ignore
    @SerializedName("adult")
    public boolean adult;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    public String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    public String releaseDate;

    @Ignore
    @SerializedName("genre_ids")
    public List<Integer> genreIds = new ArrayList<Integer>();

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    public String originalTitle;

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    public String originalLanguage;

    @ColumnInfo(name= "title")
    @SerializedName("title")
    public String title;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    public String backdropPath;

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    public Double popularity;

    @Ignore
    @SerializedName("vote_count")
    public Integer voteCount;

    @Ignore
    @SerializedName("video")
    public Boolean video;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    public Double voteAverage;

    @ColumnInfo(name = "save_date")
    @Expose(serialize = false, deserialize = false)
    private Timestamp saveDate = new Timestamp(System.currentTimeMillis());

    @ColumnInfo(name = "keyword")
    @Expose(serialize = false, deserialize = false)
    private String keyword;

    public Movie(Integer id, String posterPath, boolean adult, String overview, String releaseDate, List<Integer> genreIds,
                 String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                 Integer voteCount, boolean video, Double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    public Movie(){

    }

    //public int getMovieKey() {
        //return movieKey;
    //}

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosterPath() {
        return  posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Timestamp getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Timestamp saveDate) {
        this.saveDate = saveDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Movie{" +
                ", id=" + id + '\'' +
                ", posterPath=" + posterPath + '\'' +
                ", adult=" + adult + '\'' +
                ", overview=" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", originalTitle=" + originalTitle +
                ", originalLanguage=" + originalLanguage + '\'' +
                ", title=" + title + '\'' +
                ", backdropPath=" + backdropPath + '\'' +
                ", popularity=" + popularity + '\'' +
                ", voteCount=" + voteCount + '\'' +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", saveDate=" + saveDate +
                ", keyword=" + keyword +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.posterPath);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeList(this.genreIds);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.popularity);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
        dest.writeSerializable(this.saveDate);
        dest.writeString(this.keyword);
    }

    public Movie(Parcel in) {
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.backdropPath = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.saveDate = (Timestamp) in.readSerializable();
        this.keyword = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}