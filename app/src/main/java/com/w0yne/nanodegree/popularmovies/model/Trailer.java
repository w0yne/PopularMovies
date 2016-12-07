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
import com.w0yne.nanodegree.popularmovies.data.generated.values.TrailersValuesBuilder;

public class Trailer implements Parcelable, Movie.HasMovieId {

    public String id;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    private long movieId;

    public static Trailer getTrailer(Cursor cursor) {
        Trailer trailer = new Trailer();
        trailer.id = cursor.getString(MovieProvider.Trailers.IDX_ID);
        trailer.key = cursor.getString(MovieProvider.Trailers.IDX_KEY);
        trailer.name = cursor.getString(MovieProvider.Trailers.IDX_NAME);
        trailer.site = cursor.getString(MovieProvider.Trailers.IDX_SITE);
        trailer.size = cursor.getInt(MovieProvider.Trailers.IDX_SIZE);
        trailer.type = cursor.getString(MovieProvider.Trailers.IDX_TYPE);
        trailer.movieId = cursor.getLong(MovieProvider.Trailers.IDX_MOVIE_ID);
        return trailer;
    }

    public static ContentValues getContentValues(Trailer trailer) {
        return new TrailersValuesBuilder()
                .id(trailer.id)
                .key(trailer.key)
                .name(trailer.name)
                .site(trailer.site)
                .size(trailer.size)
                .type(trailer.type)
                .movieId(trailer.getMovieId())
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

    public Trailer() {}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
        dest.writeLong(this.movieId);
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
        this.movieId = in.readLong();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {return new Trailer(source);}

        @Override
        public Trailer[] newArray(int size) {return new Trailer[size];}
    };
}
