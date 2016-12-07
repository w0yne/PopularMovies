/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.net;

import android.content.Context;

import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.model.ModelList;
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.model.Review;
import com.w0yne.nanodegree.popularmovies.model.Trailer;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkEngine {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_IMAGE_URL = "https://image.tmdb.org/t/p/w%d";

    private static MovieDBClient movieDBClient =
            ServiceGenerator.createService(MovieDBClient.class, API_BASE_URL);

    public static void getPopularMovies(ResponseListener<ModelList<Movie>> listener) {
        movieDBClient.popularMovies().enqueue(new DefaultCallBack<>(listener));
    }

    public static void getTopRatedMovies(ResponseListener<ModelList<Movie>> listener) {
        movieDBClient.topRatedMovies().enqueue(new DefaultCallBack<>(listener));
    }

    public static void getMovie(long movieId, ResponseListener<Movie> listener) {
        movieDBClient.movie(movieId).enqueue(new DefaultCallBack<>(listener));
    }

    public static void getTrailers(long movieId, ResponseListener<ModelList<Trailer>> listener) {
        movieDBClient.trailers(movieId).enqueue(new DefaultCallBack<>(listener));
    }

    public static void getReviews(long movieId, ResponseListener<ModelList<Review>> listener) {
        movieDBClient.reviews(movieId).enqueue(new DefaultCallBack<>(listener));
    }

    public static String getApiImageUrl(Context context) {
        return String.format(Locale.US,
                API_IMAGE_URL, (int) context.getResources().getDimension(R.dimen.movie_poster_size));
    }

    static class DefaultCallBack<T> implements Callback<T> {

        private ResponseListener<T> listener;

        DefaultCallBack(ResponseListener<T> listener) {
            this.listener = listener;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                if (listener != null) {
                    listener.onSuccess(response.body());
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (listener != null) {
                listener.onFailure(t);
            }
        }
    }
}
