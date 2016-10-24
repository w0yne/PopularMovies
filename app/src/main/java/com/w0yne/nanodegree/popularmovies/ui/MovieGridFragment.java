/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.model.MovieList;
import com.w0yne.nanodegree.popularmovies.net.NetworkEngine;
import com.w0yne.nanodegree.popularmovies.net.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MovieGridFragment extends BaseFragment {

    @BindView(R.id.movie_grid)
    GridView mMovieGridView;

    private List<Movie> mMovies;
    private MovieGridAdapter mMoviesAdapter;

    enum SortType {
        POPULAR, TOP_RATED
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
            case R.id.most_popular:{
                if (!item.isChecked()) {
                    item.setChecked(true);
                    getMoviesFromServer(SortType.POPULAR);
                }
                return true;}
            case R.id.top_rated:{
                if (!item.isChecked()) {
                    item.setChecked(true);
                    getMoviesFromServer(SortType.TOP_RATED);
                }
                return true;}
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            mMovies = new ArrayList<>();
            mMoviesAdapter = new MovieGridAdapter(getActivity(), mMovies);
            mMovieGridView.setAdapter(mMoviesAdapter);
            mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie clickedMovie = mMoviesAdapter.getItem(position);
                    startActivity(new Intent(getActivity(), MovieDetailActivity.class)
                            .putExtra(MovieDetailFragment.MOVIE_ID, clickedMovie.id));
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            getMoviesFromServer(SortType.POPULAR);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMovieGridView.setNumColumns(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mMovieGridView.setNumColumns(2);
        }
    }

    private void getMoviesFromServer(SortType sortType) {
        ResponseListener<MovieList> movieListResponseListener = new ResponseListener<MovieList>() {
            @Override
            public void onSuccess(MovieList data) {
                mMoviesAdapter.clear();
                mMoviesAdapter.addAll(data.movies);
            }

            @Override
            public void onFailure(Object error) {
                // TODO: 10/20/16 handle error
            }
        };
        switch (sortType) {
            case POPULAR:
                NetworkEngine.getPopularMovies(movieListResponseListener);
                break;
            case TOP_RATED:
                NetworkEngine.getTopRatedMovies(movieListResponseListener);
                break;
        }
    }
}
