/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.net;

import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieDBClient {

    @GET("movie/popular")
    Call<MovieList> popularMovies();

    @GET("movie/top_rated")
    Call<MovieList> topRatedMovies();

    @GET("movie/{id}")
    Call<Movie> movie(@Path("id") int id);
}
