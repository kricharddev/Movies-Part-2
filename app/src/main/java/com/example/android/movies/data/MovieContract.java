package com.example.android.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kyle on 5/15/17.
 */

public final class MovieContract {

    private MovieContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movie";

    public static final String PATH_REVIEWS = "reviews";

    public static final String PATH_TRAILERS = "trailers";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public final static String TABLE_NAME = "movie";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_MOVIE_TITLE ="original_title";

        public final static String COLUMN_MOVIE_ID = "movie_id";

        public final static String COLUMN_MOVIE_RATING = "vote_average";

        public final static String COLUMN_MOVIE_OVERVIEW = "overview";

        public final static String COLUMN_MOVIE_DATE = "releaseDate";

        public final static String COLUMN_MOVIE_IMAGE = "poster_path";

        public static Uri buildMovieUri() {
            return CONTENT_URI;
        }

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";
        public static final String REVIEW_AUTHOR = "author";
        public static final String REVIEW_CONTENT = "content";

        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TrailersEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailers";
        public static final String VIDEO_TITLE = "name";

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
