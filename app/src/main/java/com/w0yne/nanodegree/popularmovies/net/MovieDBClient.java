/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.net;

import com.w0yne.nanodegree.popularmovies.model.ModelList;
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.model.Review;
import com.w0yne.nanodegree.popularmovies.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieDBClient {

    @GET("movie/popular")
    Call<ModelList<Movie>> popularMovies();

    @GET("movie/top_rated")
    Call<ModelList<Movie>> topRatedMovies();

    @GET("movie/{id}")
    Call<Movie> movie(@Path("id") long id);

    @GET("movie/{id}/videos")
    Call<ModelList<Trailer>> trailers(@Path("id") long movieId);

    @GET("movie/{id}/reviews")
    Call<ModelList<Review>> reviews(@Path("id") long movieId);
}
