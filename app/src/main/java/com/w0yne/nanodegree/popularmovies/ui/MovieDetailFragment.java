/*
 * Created by Stev Wayne on 2016/10/20
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.net.NetworkEngine;
import com.w0yne.nanodegree.popularmovies.net.ResponseListener;

import butterknife.BindView;

public class MovieDetailFragment extends BaseFragment {

    public static final String MOVIE_ID = "MOVIE_ID";

    @BindView(R.id.movie_title)
    TextView mMovieTitleTxv;
    @BindView(R.id.movie_poster)
    ImageView mMoviePoster;
    @BindView(R.id.movie_release_year)
    TextView mMovieReleaseYearTxv;
    @BindView(R.id.movie_length)
    TextView mMovieLengthTxv;
    @BindView(R.id.movie_rating)
    TextView mMovieRatingTxv;
    @BindView(R.id.movie_overview)
    TextView mMovieOverviewTxv;

    public static MovieDetailFragment createInstance(int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int movieId = getArguments().getInt(MOVIE_ID);
        getMovieFromServer(movieId);
    }

    private void bindViews(Movie movie) {
        mMovieTitleTxv.setText(movie.title);
        mMovieReleaseYearTxv.setText(movie.releaseDate);
        mMovieLengthTxv.setText(getString(R.string.format_time_min, movie.runtime));
        mMovieRatingTxv.setText(getString(R.string.format_rating, String.valueOf(movie.voteAverage)));
        mMovieOverviewTxv.setText(movie.overview);
        Glide.with(getActivity())
                .load(NetworkEngine.API_IMAGE_URL + movie.posterPath)
                .fitCenter()
                .into(mMoviePoster);
    }

    private void getMovieFromServer(int movieId) {
        NetworkEngine.getMovie(movieId, new ResponseListener<Movie>() {
            @Override
            public void onSuccess(Movie data) {
                bindViews(data);
            }

            @Override
            public void onFailure(Object error) {
                // TODO: 10/20/16 handle error
            }
        });
    }
}
