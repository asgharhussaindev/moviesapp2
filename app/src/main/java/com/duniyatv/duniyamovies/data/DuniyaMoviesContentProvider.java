package com.duniyatv.duniyamovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ashgarhussain on 9/10/17.
 */

public class DuniyaMoviesContentProvider extends ContentProvider{

    // Convenient way of calling URI Matching using the integers below
    public static final int FAVORTIES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    // A URI Matcher static variable
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DuniyaMoviesDbContract.AUTHORITY, DuniyaMoviesDbContract.PATH_FAVORTIES, FAVORTIES);
        uriMatcher.addURI(DuniyaMoviesDbContract.AUTHORITY, DuniyaMoviesDbContract.PATH_FAVORTIES + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }

    private DuniyaMoviesDbHelper mDuniyaMoviesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDuniyaMoviesDbHelper = new DuniyaMoviesDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase sqLiteDatabase = mDuniyaMoviesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case FAVORTIES:
                retCursor = sqLiteDatabase.query(
                        DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase sqLiteDatabase = mDuniyaMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORTIES:
                long id = sqLiteDatabase.insert(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.TABLE_NAME, null, contentValues);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = mDuniyaMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int tasksDeleted;

        switch(match){
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = sqLiteDatabase.delete(
                        DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.TABLE_NAME,
                        "_id=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}