/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.w0yne.nanodegree.popularmovies.data.MovieProvider;
import com.w0yne.nanodegree.popularmovies.data.generated.values.ReviewsValuesBuilder;

public class Review implements Parcelable, Movie.HasMovieId {

    public String id;
    public String author;
    public String content;
    public String url;

    private long movieId;

    public static Review getReview(Cursor cursor) {
        Review review = new Review();
        review.id = cursor.getString(MovieProvider.Reviews.IDX_ID);
        review.author = cursor.getString(MovieProvider.Reviews.IDX_AUTHOR);
        review.content = cursor.getString(MovieProvider.Reviews.IDX_CONTENT);
        review.url = cursor.getString(MovieProvider.Reviews.IDX_URL);
        review.movieId = cursor.getLong(MovieProvider.Reviews.IDX_MOVIE_ID);
        return review;
    }

    public static ContentValues getContentValues(Review review) {
        return new ReviewsValuesBuilder()
                .id(review.id)
                .author(review.author)
                .content(review.content)
                .url(review.url)
                .movieId(review.getMovieId())
                .values();
    }

    @Override
    public long getMovieId() {
        return movieId;
    }

    @Override
    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public Review() {}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeLong(this.movieId);
    }

    protected Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.movieId = in.readLong();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {return new Review(source);}

        @Override
        public Review[] newArray(int size) {return new Review[size];}
    };
}
