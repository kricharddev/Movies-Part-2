package com.example.android.movies.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.movies.R;
import com.example.android.movies.data.MovieContract;


/**
 * Created by kyle on 5/16/17.
 */

public class FavoritesCursorAdapter extends CursorAdapter {

    public FavoritesCursorAdapter(Context context, Cursor c) {
        super(context, c,  0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_favorites, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView titleTextView = (TextView) view.findViewById(R.id.favorites_title);
        Button unfavoriteButton = (Button) view.findViewById(R.id.unfavorite_button);
        TextView ratingTextView = (TextView) view.findViewById(R.id.favorites_rating);
        TextView dateTextView = (TextView) view.findViewById(R.id.favorites_release_date);

        int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        int ratingColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        int dateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE);

        String movieTitle = cursor.getString(titleColumnIndex);
        String movieRating = cursor.getString(ratingColumnIndex);
        String movieDate = cursor.getString(dateColumnIndex);

        titleTextView.setText(movieTitle);
        ratingTextView.setText(movieRating);
        dateTextView.setText(movieDate);

        unfavoriteButton.setTag(cursor.getLong(0));

        unfavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = ((Long) view.getTag());
                Uri currentMovieUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                Log.v("FavoritesCursorAdapter", currentMovieUri.toString());
                int rowsDeleted = context.getContentResolver().delete(currentMovieUri, null, null);
            }
        });
    }
}