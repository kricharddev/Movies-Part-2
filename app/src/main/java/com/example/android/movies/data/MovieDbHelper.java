package com.example.android.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movies.data.MovieContract.*;

/**
 * Created by kyle on 5/15/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT ," +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT , " +
                MovieEntry.COLUMN_MOVIE_DATE + " TEXT , " +
                MovieEntry.COLUMN_MOVIE_IMAGE + " TEXT , " +
                MovieEntry.COLUMN_MOVIE_RATING + " REAL " +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " INTEGER PRIMARY KEY," +
                ReviewsEntry.REVIEW_AUTHOR + " TEXT NOT NULL," +
                ReviewsEntry.REVIEW_CONTENT + " TEXT NOT NULL," +
                " FOREIGN KEY (" + ReviewsEntry._ID  + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +
                " );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +
                TrailersEntry._ID + " INTEGER PRIMARY KEY," +
                TrailersEntry.VIDEO_TITLE + " TEXT NOT NULL," +
                " FOREIGN KEY (" + TrailersEntry._ID  + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Place for changes to DB, increase DB version by 1 before running app.

    }
}