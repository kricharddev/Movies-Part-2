package com.example.android.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movies.Adapters.MovieAdapter;
import com.example.android.movies.AsyncTasks.MovieQuery;

import java.util.ArrayList;

import static com.example.android.movies.SearchURLs.POPULAR_URL;
import static com.example.android.movies.SearchURLs.SORT_METHOD;
import static com.example.android.movies.SearchURLs.TOP_RATED_URL;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    public static MovieAdapter mAdapter;
    public static GridView movieGridView;
    public static ArrayList<Movie> moviesList;
    public static ArrayList<String> posters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_fragment, new MovieFragment())
                    .commit();
                    new MovieQuery().execute(SORT_METHOD, null);
            Log.v(LOG_TAG, SORT_METHOD + " = SORT METHOD");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_setting:
                popular();
                return true;
            case R.id.top_rated_setting:
                rated();
                return true;
            case R.id.favorites_setting:
                Intent i = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void popular() {
        SearchURLs.SORT_METHOD = POPULAR_URL;
        setTitle(R.string.popular_movies);
        new MovieQuery().execute(SORT_METHOD, null);
    }

    public void rated() {
        SearchURLs.SORT_METHOD = TOP_RATED_URL;
        setTitle(R.string.top_rated_movies);
        new MovieQuery().execute(SORT_METHOD, null);
    }

    public static class MovieFragment extends Fragment {

        public MovieFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup details,
                                 Bundle onSavedInstanceState) {

            View posterView = inflater.inflate(R.layout.fragment_main, details, false);
            setHasOptionsMenu(true);
            movieGridView = (GridView) posterView.findViewById(R.id.poster_grid);
            int grid = getResources().getConfiguration().orientation;
            movieGridView.setNumColumns(grid == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            movieGridView.setAdapter(mAdapter);
            movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("movie_id", moviesList.get(position).getId());
                    intent.putExtra("movie_position", position);
                    intent.putExtra("movie_title", moviesList.get(position).getOriginal_title());
                    intent.putExtra("movie_rating", moviesList.get(position).getVote_average());
                    intent.putExtra("movie_overview", moviesList.get(position).getOverview());
                    intent.putExtra("movie_image", moviesList.get(position).getPoster_path());
                    intent.putExtra("movie_release_date", moviesList.get(position).getRelease_date());
                    startActivity(intent);
                }
            });
            return posterView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putParcelableArrayList("movies", MainActivity.moviesList);
            outState.putStringArrayList("images", MainActivity.posters);
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            if (savedInstanceState != null && savedInstanceState.containsKey("movies")) {
                moviesList = savedInstanceState.getParcelableArrayList("movies");
                posters = savedInstanceState.getStringArrayList("images");
            } else {
                moviesList = new ArrayList<>();
                posters = new ArrayList<>();
                mAdapter = new MovieAdapter(getActivity());
            }
            super.onCreate(savedInstanceState);
        }
    }
}