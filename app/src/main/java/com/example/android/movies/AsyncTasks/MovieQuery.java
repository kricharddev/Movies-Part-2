package com.example.android.movies.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movies.MainActivity;
import com.example.android.movies.Movie;
import com.example.android.movies.SearchURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kyle on 5/15/17.
 */

public class MovieQuery extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = "MovieQuery";

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // SORT_METHOD defaults to "popular", will update upon choosing menu option
        SearchURLs.SORT_METHOD = params[0];

        Uri buildUri = Uri.parse(SearchURLs.BASE_URL + SearchURLs.SORT_METHOD + "?api_key=" +
            SearchURLs.API_KEY);

        String response;

        Log.v(LOG_TAG, buildUri.toString());

        try {
            response = getJSON(buildUri);
            return response;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(String movieData) {
        if (movieData != null) {
            loadInfo(movieData);
        } else {
            return;
        }
    }

    public  static void loadInfo (String jsonString) {
        MainActivity.moviesList.clear();
        MainActivity.posters.clear();

        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");

                for (int i = 0; i <= moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    Movie movieItem = new Movie();
                    movieItem.setId(movie.getInt("id"));
                    movieItem.setOriginal_title(movie.getString("original_title"));
                    movieItem.setOverview(movie.getString("overview"));
                    movieItem.setRelease_date(movie.getString("release_date"));
                    movieItem.setVote_average(movie.getString("vote_average"));
                    movieItem.setPoster_path(movie.getString("poster_path"));
                    MainActivity.posters.add(SearchURLs.IMG_BASE_URL + SearchURLs.IMG_SIZE + movie.getString("poster_path"));
                    MainActivity.moviesList.add(movieItem);
                    MainActivity.mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getJSON(Uri buildUri)
    {
        InputStream inputStream;
        StringBuffer buffer;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;

        try {
            URL url = new URL(buildUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
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
            moviesJson = buffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }
        return moviesJson;
    }
}