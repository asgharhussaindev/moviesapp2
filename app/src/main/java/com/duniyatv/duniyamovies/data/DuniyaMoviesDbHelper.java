package com.duniyatv.duniyamovies.data;

/**
 * Created by ashgarhussain on 9/10/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DuniyaMoviesDbHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "duniyaMovieDb.db";

    // Database version
    private static final int VERSION = 1;

    DuniyaMoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Creates a new DB for storing data. Is only called the first time.
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Remove before submission
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.TABLE_NAME;

        final String CREATE_TABLE = "CREATE TABLE "  +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.TABLE_NAME + " (" +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry._ID                + " INTEGER PRIMARY KEY, " +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_ID    + " INTEGER NOT NULL," +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_SYNOPSIS    + " TEXT NOT NULL, " +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_RATING      + " STRING NOT NULL," +
                DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_RELEASE_DATE+ " STRING NOT NULL," +
                " UNIQUE (" + DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_ID +"));";

        sqLiteDatabase.execSQL(DROP_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}