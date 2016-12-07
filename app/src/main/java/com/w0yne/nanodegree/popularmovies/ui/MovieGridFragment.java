/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.data.MovieColumns;
import com.w0yne.nanodegree.popularmovies.data.MovieProvider;
import com.w0yne.nanodegree.popularmovies.model.ModelList;
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.net.NetworkEngine;
import com.w0yne.nanodegree.popularmovies.net.ResponseListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;

public class MovieGridFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;

    private static final String SAVED_SORT_ORDER = "SAVED_SORT_ORDER";
    private static final String SAVED_DISPLAY_TYPE = "SAVED_DISPLAY_TYPE";
    private static final String SAVED_POSITION = "SAVED_POSITION";

    @BindView(R.id.movie_grid)
    GridView mMovieGridView;

    private MovieCursorAdapter mMoviesAdapter;
    private String mSortOrder;
    private int mCurrentDisplayType;
    private OnClickedMovieListener mOnClickedMovieListener;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            SORT_TYPE_POPULAR,
            SORT_TYPE_TOP_RATED,
            FILTER_FAVORITE
    })
    @interface DisplayType {}

    public static final int SORT_TYPE_POPULAR = 0;
    public static final int SORT_TYPE_TOP_RATED = 1;
    public static final int FILTER_FAVORITE = 2;

    public interface OnClickedMovieListener {

        void onMovieClicked(Movie movie);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnClickedMovieListener) {
            this.mOnClickedMovieListener = (OnClickedMovieListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular: {
                if (!item.isChecked()) {
                    item.setChecked(true);
                    getMoviesFromServer(SORT_TYPE_POPULAR);
                }
                return true;
            }
            case R.id.top_rated: {
                if (!item.isChecked()) {
                    item.setChecked(true);
                    getMoviesFromServer(SORT_TYPE_TOP_RATED);
                }
                return true;
            }
            case R.id.favorite: {
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mCurrentDisplayType = FILTER_FAVORITE;
                    getLoaderManager().restartLoader(LOADER_ID, null, this);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMoviesAdapter = new MovieCursorAdapter(getActivity(), null);
        mMovieGridView.setAdapter(mMoviesAdapter);
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnClickedMovieListener != null) {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    Movie clickedMovie = Movie.getMovie(cursor);
                    mOnClickedMovieListener.onMovieClicked(clickedMovie);
                }
            }
        });
        view.post(new Runnable() {
            @Override
            public void run() {
                int columnWidth = Math.min(view.getWidth(), view.getHeight()) / 2;
                mMovieGridView.setColumnWidth(columnWidth);
                mMoviesAdapter.setColumnWidth(columnWidth);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            mCurrentDisplayType = SORT_TYPE_POPULAR;
            getMoviesFromServer(SORT_TYPE_POPULAR);
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            mSortOrder = savedInstanceState.getString(SAVED_SORT_ORDER);
            mCurrentDisplayType = savedInstanceState.getInt(SAVED_DISPLAY_TYPE);
            mMovieGridView.smoothScrollToPosition(savedInstanceState.getInt(SAVED_POSITION));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(SAVED_SORT_ORDER, mSortOrder);
            outState.putInt(SAVED_DISPLAY_TYPE, mCurrentDisplayType);
            outState.putInt(SAVED_POSITION, mMovieGridView.getLastVisiblePosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = null;
        String selection = null;
        if (mCurrentDisplayType == FILTER_FAVORITE) {
            selection = MovieColumns.FAVORITE + " = 1";
        } else {
            sortOrder = mSortOrder;
        }
        return new CursorLoader(getActivity(),
                MovieProvider.Movies.CONTENT_URI,
                MovieProvider.Movies.DEFAULT_PROJECTION,
                selection, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    private void getMoviesFromServer(@DisplayType int sortType) {
        mCurrentDisplayType = sortType;
        ResponseListener<ModelList<Movie>> movieListResponseListener = new ResponseListener<ModelList<Movie>>() {
            @Override
            public void onSuccess(ModelList<Movie> data) {
                if (isAdded()) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    for (Movie movie : data.items) {
                        Cursor cursor = contentResolver.query(MovieProvider.Movies.withId(movie.id), new String[]{MovieColumns.ID, MovieColumns.FAVORITE}, null, null, null);
                        if (cursor != null) {
                            try {
                                if (cursor.moveToFirst()) {
                                    movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieColumns.FAVORITE)) != 0);
                                    contentResolver.update(MovieProvider.Movies.withId(movie.id), Movie.getContentValues(movie)
                                            , null, null);
                                } else {
                                    contentResolver.insert(MovieProvider.Movies.CONTENT_URI, Movie.getContentValues(movie));
                                }
                            } finally {
                                cursor.close();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object error) {
                // TODO: 10/20/16 handle error
            }
        };
        switch (sortType) {
            case SORT_TYPE_POPULAR:
                mSortOrder = MovieColumns.POPULARITY + " desc";
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                NetworkEngine.getPopularMovies(movieListResponseListener);
                break;
            case SORT_TYPE_TOP_RATED:
                mSortOrder = MovieColumns.VOTE_AVERAGE + " desc";
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                NetworkEngine.getTopRatedMovies(movieListResponseListener);
                break;
            default:
                break;
        }
    }
}
