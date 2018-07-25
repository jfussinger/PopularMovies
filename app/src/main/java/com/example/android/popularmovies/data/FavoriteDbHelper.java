package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.model.Movie;

import com.example.android.popularmovies.data.FavoriteContract.FavoriteEntry;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavoriteDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "favorite";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Name of database table for favorite
     */
    public final static String TABLE_NAME = "favorite";

    /**
     * Constructs a new instance of {@link FavoriteDbHelper}.
     *
     * @param context of the app
     */
    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    Movie Movies;

    //https://stackoverflow.com/questions/22449540/how-to-use-double-in-android-via-sqlite

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the favorite table
        String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " ("
                + FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID + " INTEGER NOT NULL DEFAULT 0, "
                + FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE + " REAL, "
                + FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Adding new favorite
    public void addFavorite(FavoriteContract FavoriteContract) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteEntry.COLUMN_FAVORITE_MOVIEID, FavoriteContract.getId());
        values.put(FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE, FavoriteContract.getOriginalTitle());
        values.put(FavoriteEntry.COLUMN_FAVORITE_POSTERPATH, FavoriteContract.getPosterPath()); //
        values.put(FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE, FavoriteContract.getReleaseDate());
        values.put(FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE, FavoriteContract.getVoteAverage());
        values.put(FavoriteEntry.COLUMN_FAVORITE_OVERVIEW, FavoriteContract.getOverview());

        // Inserting Row
        db.insert(FavoriteEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection

    }

    // Getting single favorite
    public Cursor getFavorite(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW,
        };
        String selection = FavoriteContract.FavoriteEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(null)};

        Cursor cursor = db.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    // Getting All favorites
    public List<FavoriteContract> getAllFavorites() {
        List<FavoriteContract> favoriteList = new ArrayList<FavoriteContract>();

        String[] projection = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW,
        };

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavoriteContract favoriteContract = new FavoriteContract();
                favoriteContract.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID))));
                favoriteContract.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ORIGINALTITLE)));
                favoriteContract.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTERPATH)));
                favoriteContract.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_RELEASEDATE)));
                favoriteContract.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_VOTEAVERAGE))));
                favoriteContract.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW)));
                // Adding favorite to list
                favoriteList.add(favoriteContract);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return inventory list
        return favoriteList;
    }

    // Deleting single favorite
    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_MOVIEID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
