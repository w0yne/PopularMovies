/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.w0yne.nanodegree.popularmovies.data.MovieProvider;
import com.w0yne.nanodegree.popularmovies.data.generated.values.MoviesValuesBuilder;

public class Movie implements Parcelable {

    public long id;
    public String overview;
    public String posterPath;
    public String releaseDate;
    public int runtime;
    public String title;
    public double popularity;
    public double voteAverage;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    public Movie() {}

    public static Movie getMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.id = cursor.getLong(MovieProvider.Movies.IDX_ID);
        movie.overview = cursor.getString(MovieProvider.Movies.IDX_OVERVIEW);
        movie.posterPath = cursor.getString(MovieProvider.Movies.IDX_POSTER_PATH);
        movie.releaseDate = cursor.getString(MovieProvider.Movies.IDX_RELEASE_DATE);
        movie.runtime = cursor.getInt(MovieProvider.Movies.IDX_RUNTIME);
        movie.title = cursor.getString(MovieProvider.Movies.IDX_TITLE);
        movie.popularity = cursor.getDouble(MovieProvider.Movies.IDX_POPULARITY);
        movie.voteAverage = cursor.getDouble(MovieProvider.Movies.IDX_VOTE_AVERAGE);
        movie.setFavorite(cursor.getInt(MovieProvider.Movies.IDX_FAVORITE) != 0);
        return movie;
    }

    public static ContentValues getContentValues(Movie movie) {
        return new MoviesValuesBuilder()
                .id(movie.id)
                .overview(movie.overview)
                .posterPath(movie.posterPath)
                .releaseDate(movie.releaseDate)
                .runtime(movie.runtime)
                .title(movie.title)
                .popularity(movie.popularity)
                .voteAverage(movie.voteAverage)
                .favorite(movie.isFavorite() ? 1 : 0)
                .values();
    }

    public interface HasMovieId {

        void setMovieId(long movieId);

        long getMovieId();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.runtime);
        dest.writeString(this.title);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.voteAverage);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.runtime = in.readInt();
        this.title = in.readString();
        this.popularity = in.readDouble();
        this.voteAverage = in.readDouble();
        this.isFavorite = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {return new Movie(source);}

        @Override
        public Movie[] newArray(int size) {return new Movie[size];}
    };
}
