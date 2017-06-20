package com.example.android.movies;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.Adapters.FavoritesCursorAdapter;
import com.example.android.movies.data.MovieContract.MovieEntry;


/**
 * Created by kyle on 5/16/17.
 */

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentMovieUri;

    private FavoritesCursorAdapter mCursorAdapter;

    public int MOVIE_LOADER = 0;

    private static final String TAG = "FavoritesActivity";

    private TextView mTitle;

    private TextView mRating;

    private TextView mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ListView listView = (ListView) findViewById(R.id.favorites_list);

        mCursorAdapter = new FavoritesCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);

        mTitle = (TextView) findViewById(R.id.favorites_title);
        mRating = (TextView) findViewById(R.id.favorites_rating);
        mDate = (TextView) findViewById(R.id.favorites_release_date);

        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    public void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteMovie();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteMovie() {
        if (mCurrentMovieUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentMovieUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MovieEntry._ID,
                MovieEntry.COLUMN_MOVIE_TITLE,
                MovieEntry.COLUMN_MOVIE_RATING,
                MovieEntry.COLUMN_MOVIE_DATE };

        return new CursorLoader(this,
                MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    private void deleteAllFavorites() {
        int rowsDeleted = getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
        Log.v("FavoritesActivity", rowsDeleted + " rows deleted from movies db");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}