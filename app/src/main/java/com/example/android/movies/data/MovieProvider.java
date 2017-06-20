package com.example.android.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.movies.data.MovieContract.MovieEntry;

/**
 * Created by kyle on 5/15/17.
 */

public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final int MOVIE = 100;

    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIE);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
    }

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return insertMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMovie(Uri uri, ContentValues values) {
        String movieTitle = values.getAsString(MovieEntry.COLUMN_MOVIE_TITLE);
        if (movieTitle == null) {
            throw new IllegalArgumentException("Movie Title Required");
        }

        Integer movieId = values.getAsInteger(MovieEntry.COLUMN_MOVIE_ID);
        if (movieId != null && movieId < 0) {
            throw new IllegalArgumentException("Proper Movie ID required");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(MovieEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return updateMovie(uri, contentValues, selection, selectionArgs);
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMovie(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateMovie(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(MovieEntry.COLUMN_MOVIE_TITLE)) {
            String movieTitle = values.getAsString(MovieEntry.COLUMN_MOVIE_TITLE);
            if (movieTitle == null) {
                throw new IllegalArgumentException("Movie Title Required");
            }
        }

        Integer movieId = values.getAsInteger(MovieEntry.COLUMN_MOVIE_ID);
        if (movieId != null && movieId < 0) {
            throw new IllegalArgumentException("Proper Movie ID required");
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                selection = MovieEntry.COLUMN_MOVIE_TITLE + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
