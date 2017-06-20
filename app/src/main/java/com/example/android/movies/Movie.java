package com.example.android.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kyle on 5/15/17.
 */

public class Movie implements Parcelable {

    // Movie ID
    int id;

    // Movie Title
    String original_title;

    // Movie Plot
    String overview;

    // Movie Release Date
    String release_date;

    // Movie Poster IMAGE URL
    String poster_path;

    // Movie's rating
    String vote_average;

    private String movieReviews = "";

    private String movieTrailers = "";

    public Movie() {
    }
    private Movie(Parcel parcel){
        id = parcel.readInt();
        original_title = parcel.readString();
        poster_path = parcel.readString();
        overview = parcel.readString();
        vote_average = parcel.readString();
        release_date = parcel.readString();
        movieReviews = parcel.readString();
        movieTrailers = parcel.readString();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }
    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }
    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getReviews() {
        return movieReviews;
    }
    public void setReviews(String reviews) {
        movieReviews = reviews;
    }

    public String getTrailers() {
        return movieTrailers;
    }
    public void setTrailers(String trailers) {
        movieTrailers = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(original_title);
        out.writeString(poster_path);
        out.writeString(overview);
        out.writeString(vote_average);
        out.writeString(release_date);
        out.writeString(movieReviews);
        out.writeString(movieTrailers);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}

