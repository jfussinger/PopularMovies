package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Movie app.
 */

public final class FavoriteContract {

    int _id;
    int movieid;
    String originaltitle = "";
    String posterpath = "";
    String releasedate = "";
    double voteaverage;
    String overview = "";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FavoriteContract() {}

    // constructor
    public FavoriteContract(int _id, int movieid, String originaltitle, String posterpath,
                            String releasedate, double voteaverage, String overview){
        this._id = _id;
        this.movieid = movieid;
        this.originaltitle = originaltitle;
        this.posterpath = posterpath;
        this.releasedate = releasedate;
        this.voteaverage = voteaverage;
        this.overview = overview;
    }

    //Getter methods

    public int getId () {
        return this._id = _id;
    }
    public int getMovieid () {
        return this.movieid = movieid;
    }
    public String getOriginalTitle() {
        return this.originaltitle = originaltitle;
    }
    public String getPosterPath() {
        return this.posterpath = posterpath;
    }
    public String getReleaseDate() {
        return this.releasedate = releasedate;
    }
    public double getVoteAverage() {
        return this.voteaverage = voteaverage;
    }
    public String getOverview() {
        return this.overview = overview;
    }

    //Setter methods

    public void setId (int _id) {
        this._id = _id;
    }
    public void setMovieid (int movieid) {
        this.movieid = movieid;
    }
    public void setOriginalTitle(String originaltitle){
        this.originaltitle = originaltitle;
    }
    public void setPosterPath(String posterpath){
        this.posterpath = posterpath;
    }
    public void setReleaseDate(String releasedate){ this.releasedate = releasedate; }
    public void setVoteAverage(double voteaverage){
        this.voteaverage = voteaverage;
    }
    public void setOverview(String overview){
        this.overview = overview;
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.popularmovies/popularmovies/ is a valid path for
     * looking at movie data. content://com.example.android.popularmovies/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_FAVORITE = "favorite";

    /**
     * Inner class that defines constant values for the favorites database table.
     * Each entry in the table represents single favorite.
     */
    public static final class FavoriteEntry implements BaseColumns {

        /**
         * The content URI to access the favorite data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITE);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of favorites.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for single favorite.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        /** Name of database table for inventory */
        public final static String TABLE_NAME = "favorite";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FAVORITE_MOVIEID = "movieid";
        public final static String COLUMN_FAVORITE_ORIGINALTITLE = "originaltitle";
        public final static String COLUMN_FAVORITE_POSTERPATH = "posterpath";
        public final static String COLUMN_FAVORITE_RELEASEDATE = "releasedate";
        public final static String COLUMN_FAVORITE_VOTEAVERAGE = "voteaverage";
        public final static String COLUMN_FAVORITE_OVERVIEW = "overview";
    }
}
