/*
 * Created by Stev Wayne on 2016/10/20
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.data.MovieProvider;
import com.w0yne.nanodegree.popularmovies.data.ReviewColumns;
import com.w0yne.nanodegree.popularmovies.data.TrailerColumns;
import com.w0yne.nanodegree.popularmovies.model.ModelList;
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.model.Review;
import com.w0yne.nanodegree.popularmovies.model.Trailer;
import com.w0yne.nanodegree.popularmovies.net.NetworkEngine;
import com.w0yne.nanodegree.popularmovies.net.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MovieDetailFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        TrailerCursorAdapter.OnClickItemListener {

    public static final String KEY_MOVIE = "KEY_MOVIE";

    private static final String SAVED_MOVIE = "SAVED_MOVIE";

    private static final int LOADER_ID_MOVIE = 0;
    private static final int LOADER_ID_TRAILERS = 1;
    private static final int LOADER_ID_REVIEWS = 2;

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
    @BindView(R.id.movie_favorite)
    ImageButton mFavoriteBtn;
    @BindView(R.id.movie_overview)
    TextView mMovieOverviewTxv;
    @BindView(R.id.movie_trailers)
    RecyclerView mMovieTrailersList;
    @BindView(R.id.movie_reviews)
    RecyclerView mMovieReviewsList;

    private Movie mMovie;
    private TrailerCursorAdapter mTrailerCursorAdapter;
    private ReviewCursorAdapter mReviewCursorAdapter;

    public static MovieDetailFragment createInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            mMovie = getArguments().getParcelable(KEY_MOVIE);
        } else {
            mMovie = savedInstanceState.getParcelable(SAVED_MOVIE);
        }

        if (mMovie != null) {
            mTrailerCursorAdapter = new TrailerCursorAdapter(getActivity(), null);
            mTrailerCursorAdapter.setOnClickItemListener(this);
            mMovieTrailersList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mMovieTrailersList.setAdapter(mTrailerCursorAdapter);
            mReviewCursorAdapter = new ReviewCursorAdapter(getActivity(), null);
            mMovieReviewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mMovieReviewsList.setAdapter(mReviewCursorAdapter);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putParcelable(SAVED_MOVIE, mMovie);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mMovie != null) {
            getLoaderManager().initLoader(LOADER_ID_MOVIE, null, this);
            getLoaderManager().initLoader(LOADER_ID_TRAILERS, null, this);
            getLoaderManager().initLoader(LOADER_ID_REVIEWS, null, this);
            getMovie(mMovie);
            getTrailers(mMovie);
            getReviews(mMovie);
        }
    }

    private void bindViews(final Movie movie) {
        mMovieTitleTxv.setText(movie.title);
        mMovieReleaseYearTxv.setText(movie.releaseDate);
        mMovieLengthTxv.setText(getString(R.string.format_time_min, movie.runtime));
        mMovieRatingTxv.setText(getString(R.string.format_rating, String.valueOf(movie.voteAverage)));
        mFavoriteBtn.setImageResource(movie.isFavorite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
        mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movie.isFavorite()) {
                    movie.setFavorite(false);
                } else {
                    movie.setFavorite(true);
                }
                getActivity().getContentResolver()
                        .update(MovieProvider.Movies.withId(movie.id),
                                Movie.getContentValues(movie), null, null);
            }
        });
        mMovieOverviewTxv.setText(movie.overview);
        Glide.with(getActivity())
                .load(NetworkEngine.getApiImageUrl(getActivity()) + movie.posterPath)
                .fitCenter()
                .into(mMoviePoster);
    }

    /**
     * Request Movie info from server again since /movie/popular API endpoint does not return
     * "runtime" attribute.
     */
    private void getMovie(final Movie movie) {
        NetworkEngine.getMovie(movie.id, new ResponseListener<Movie>() {
            @Override
            public void onSuccess(Movie data) {
                if (isAdded()) {
                    data.setFavorite(movie.isFavorite());
                    getActivity().getContentResolver()
                            .update(MovieProvider.Movies.withId(data.id),
                                    Movie.getContentValues(data), null, null);
                }
            }

            @Override
            public void onFailure(Object error) {

            }
        });
    }

    private void getTrailers(Movie movie) {
        NetworkEngine.getTrailers(movie.id, new ResponseListener<ModelList<Trailer>>() {
            @Override
            public void onSuccess(ModelList<Trailer> data) {
                if (isAdded()) {
                    List<ContentValues> contentValues = new ArrayList<>();
                    for (Trailer trailer : data.items) {
                        trailer.setMovieId(data.id);
                        contentValues.add(Trailer.getContentValues(trailer));
                    }
                    getActivity().getContentResolver()
                            .bulkInsert(MovieProvider.Trailers.CONTENT_URI, contentValues.toArray(new ContentValues[contentValues.size()]));
                }
            }

            @Override
            public void onFailure(Object error) {

            }
        });
    }

    private void getReviews(Movie movie) {
        NetworkEngine.getReviews(movie.id, new ResponseListener<ModelList<Review>>() {
            @Override
            public void onSuccess(ModelList<Review> data) {
                if (isAdded()) {
                    List<ContentValues> contentValues = new ArrayList<>();
                    for (Review review : data.items) {
                        review.setMovieId(data.id);
                        contentValues.add(Review.getContentValues(review));
                    }
                    getActivity().getContentResolver()
                            .bulkInsert(MovieProvider.Reviews.CONTENT_URI, contentValues.toArray(new ContentValues[contentValues.size()]));
                }
            }


            @Override
            public void onFailure(Object error) {

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_MOVIE:
                return new CursorLoader(getActivity(),
                        MovieProvider.Movies.withId(mMovie.id),
                        MovieProvider.Movies.DEFAULT_PROJECTION,
                        null, null, null);
            case LOADER_ID_TRAILERS:
                return new CursorLoader(getActivity(),
                        MovieProvider.Trailers.CONTENT_URI,
                        MovieProvider.Trailers.DEFAULT_PROJECTION,
                        TrailerColumns.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(mMovie.id)}, null);
            case LOADER_ID_REVIEWS:
                return new CursorLoader(getActivity(),
                        MovieProvider.Reviews.CONTENT_URI,
                        MovieProvider.Reviews.DEFAULT_PROJECTION,
                        ReviewColumns.MOVIE_ID + "= ?",
                        new String[]{String.valueOf(mMovie.id)}, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_MOVIE:
                if (data.moveToFirst()) {
                    mMovie = Movie.getMovie(data);
                    bindViews(mMovie);
                }
                break;
            case LOADER_ID_TRAILERS:
                mTrailerCursorAdapter.swapCursor(data);
                break;
            case LOADER_ID_REVIEWS:
                mReviewCursorAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_ID_MOVIE:
                break;
            case LOADER_ID_TRAILERS:
                mTrailerCursorAdapter.swapCursor(null);
                break;
            case LOADER_ID_REVIEWS:
                mReviewCursorAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onItemClicked(Trailer trailer) {
        if ("YouTube".equals(trailer.site)) {
            // https://www.youtube.com/watch?v=IzplmS-VeBc
            Uri uri = Uri.parse("https://www.youtube.com/watch").buildUpon()
                    .appendQueryParameter("v", trailer.key)
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(uri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
