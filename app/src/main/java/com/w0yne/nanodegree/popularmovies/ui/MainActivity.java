/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;

import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.model.Movie;

public class MainActivity extends BaseActivity implements MovieGridFragment.OnClickedMovieListener {

    private static final String SAVED_TWO_PANE = "SAVED_TWO_PANE";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mTwoPane = findViewById(R.id.detail_frame) != null;
        } else {
            mTwoPane = savedInstanceState.getBoolean(SAVED_TWO_PANE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putBoolean(SAVED_TWO_PANE, mTwoPane);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (!mTwoPane) {
            startActivity(new Intent(this, MovieDetailActivity.class)
                    .putExtra(MovieDetailFragment.KEY_MOVIE, movie));
        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.detail_frame, MovieDetailFragment.createInstance(movie))
                    .commit();
        }
    }
}
