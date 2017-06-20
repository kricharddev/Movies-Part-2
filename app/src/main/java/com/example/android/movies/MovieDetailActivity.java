package com.example.android.movies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.movies.data.MovieProvider.LOG_TAG;

/**
 * Created by kyle on 5/15/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;

    public static Intent intent;

    public static TextView title;

    public static TextView overview;

    public static TextView rating;

    public static TextView release_date;

    public static ImageView poster;

    public Button addFavoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_activity_fragment, new DetailsFragment())
                    .commit();
        }

        addFavoriteButton = (Button) findViewById(R.id.add_favorite_button);
        addFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMovie();
            }
        });
    }

    private void insertMovie() {

        movie = new Movie();
        MovieDetailActivity.intent = getIntent();
        int movie_id = intent.getIntExtra("movie_id",0);
        int movie_position = intent.getIntExtra("movie_position",0);
        movie = MainActivity.moviesList.get(movie_position);
        String movieIdString = Integer.toString(intent.getIntExtra("movie_id", 0));
        String titleString = movie.getOriginal_title();
        String overviewString = movie.getOverview();
        String dateString = movie.getRelease_date();
        String ratingString = movie.getVote_average();
        String imageString = SearchURLs.IMG_BASE_URL + SearchURLs.IMG_SIZE + movie.getPoster_path();

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieIdString);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, titleString);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overviewString);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, dateString);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, ratingString);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, imageString);

        Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.insert_movie_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.insert_movie_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static class DetailsFragment extends Fragment {

        private LayoutInflater mLayoutInflater;
        private View rootView;
        private Movie movie;

        public DetailsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup details,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_details, details, false);
            getValues(rootView);
            setValues(rootView);
            parseReviews();
            parseTrailers();
            return rootView;
        }

        public void getValues(View rootView) {
            movie = new Movie();
            MovieDetailActivity.intent = getActivity().getIntent();
            int movie_id = intent.getIntExtra("movie_id",0);
            int movie_position = intent.getIntExtra("movie_position",0);
            movie = MainActivity.moviesList.get(movie_position);
            title = (TextView)rootView.findViewById(R.id.title_tv);
            overview = (TextView)rootView.findViewById(R.id.overview_tv);
            rating = (TextView)rootView.findViewById(R.id.rating_tv);
            release_date = (TextView)rootView.findViewById(R.id.release_date_tv);
            poster = (ImageView)rootView.findViewById(R.id.image_iv);
        }

        public void setValues(View rootView) {
            title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());
            rating.setText(movie.getVote_average() + " out of 10");
            release_date.setText("Release Date: " + movie.getRelease_date());
            String movie_poster_url;
            movie_poster_url = (SearchURLs.IMG_BASE_URL + SearchURLs.IMG_SIZE + movie.getPoster_path());
            Picasso.with(rootView.getContext()).load(movie_poster_url).into(poster);
        }

        private void parseReviews(){

            if(movie.getReviews() == null)
                return;

            final String ARRAY_OF_REVIEW = "results";
            final String AUTHOR = "author";
            final String REVIEW_CONTENT = "content";

            try {
                JSONObject reviewsJson = new JSONObject(movie.getReviews());
                JSONArray reviewsArray = reviewsJson.getJSONArray(ARRAY_OF_REVIEW);
                int reviewsLength = reviewsArray.length();

                if (reviewsLength > 0){

                    LinearLayout innerScrollLayout = (LinearLayout)
                            rootView.findViewById(R.id.inner_linear_layout);

                    View reviewsListView = mLayoutInflater.inflate(R.layout.list_reviews,
                            innerScrollLayout, false);

                    innerScrollLayout.addView(reviewsListView);

                    LinearLayout reviewList = (LinearLayout)
                            reviewsListView.findViewById(R.id.list_reviews);

                    for (int i = 0; i < reviewsLength; ++i) {

                        View reviewItem = mLayoutInflater.inflate(R.layout.item_reviews,
                                reviewList, false);

                        JSONObject review = reviewsArray.getJSONObject(i);
                        String reviewAuthor = review.getString(AUTHOR);
                        String reviewText = review.getString(REVIEW_CONTENT);

                        TextView author = (TextView) reviewItem.findViewById(R.id.review_author);
                        TextView content = (TextView) reviewItem.findViewById(R.id.review_text);

                        author.setText(reviewAuthor);
                        content.setText(reviewText);

                        reviewList.addView(reviewItem);
                    }
                }
            }catch (JSONException e){
                Log.e(LOG_TAG, "ERROR PARSING JSON STRING");
            }
        }

        private void parseTrailers(){

            final String ARRAY_OF_TRAILERS = "results";
            final String TRAILER_ID = "key";
            final String TRAILER_TITLE = "name";

            try{

                JSONObject trailersJson = new JSONObject(movie.getTrailers());
                JSONArray trailersArray = trailersJson.getJSONArray(ARRAY_OF_TRAILERS);
                int trailersLength =  trailersArray.length();

                if(trailersLength > 0) {

                    LinearLayout innerScrollLayout = (LinearLayout)
                            rootView.findViewById(R.id.inner_linear_layout);

                    View trailersListView = mLayoutInflater.inflate(R.layout.list_trailers,
                            innerScrollLayout, false);

                    innerScrollLayout.addView(trailersListView);

                    LinearLayout trailerList = (LinearLayout)
                            trailersListView.findViewById(R.id.list_trailers);

                    for (int i = 0; i < trailersLength; ++i) {

                        View trailerItem = mLayoutInflater.inflate(R.layout.item_trailers,
                                trailerList, false);

                        JSONObject trailer = trailersArray.getJSONObject(i);
                        final String trailerId = trailer.getString(TRAILER_ID);
                        String trailerTitle = trailer.getString(TRAILER_TITLE);
                        TextView videoTitle = (TextView) trailerItem.findViewById(R.id.trailer_movie_title);

                        videoTitle.setText(trailerTitle);
                        trailerList.addView(trailerItem);

                        trailerItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent ytIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("vnd.youtube:" + trailerId));
                                ytIntent.putExtra("VIDEO_ID", trailerId);
                                try{
                                    startActivity(ytIntent);
                                }catch (ActivityNotFoundException ex){
                                    Log.i(LOG_TAG, "youtube app not installed");
                                }
                            }
                        });
                    }
                }
            }catch (JSONException e){
                Log.e(LOG_TAG, "ERROR PARSING JSON STRING");
            }
        }
    }
}