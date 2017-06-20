package com.example.android.movies.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movies.Movie;
import com.example.android.movies.SearchURLs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kyle on 6/3/17.
 */

public class MovieTrailersAndReviewsQuery extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = MovieTrailersAndReviewsQuery.class.getSimpleName();
    private final String MOVIE_BASE = "http://api.themoviedb.org/3/movie/";
    private Movie movie;
    private String searchParam;

    public MovieTrailersAndReviewsQuery(Movie movie, String searchType){
        this.movie = movie;
        this.searchParam = searchParam;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length != 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            Uri builtUri = Uri.parse(MOVIE_BASE + movie.getId() + "/" + searchParam ).buildUpon()
                    .appendQueryParameter("api_key", SearchURLs.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

    }

    @Override
    protected void onPostExecute(String result) {

        if(searchParam.equals("reviews")){
            movie.setReviews(result);
        }else {
            movie.setTrailers(result);
        }
    }
}