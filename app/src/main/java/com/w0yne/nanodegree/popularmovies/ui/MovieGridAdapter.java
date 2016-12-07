/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.model.Movie;
import com.w0yne.nanodegree.popularmovies.net.NetworkEngine;

import java.util.List;

public class MovieGridAdapter extends ArrayAdapter<Movie> {

    public MovieGridAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        boolean isLandscape =
                getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        Pair<Integer, Integer> widthAndHeight = calculateWidthAndHeight(parent.getWidth(), isLandscape);
        Glide.with(getContext())
                .load(NetworkEngine.getApiImageUrl(getContext()) + movie.posterPath)
                .centerCrop()
                .override(widthAndHeight.first, widthAndHeight.second)
                .into(viewHolder.moviePoster);

        return convertView;
    }

    /**
     * Movie poster image always has a 2 / 3 ratio from TheMovieDB.
     */
    private Pair<Integer, Integer> calculateWidthAndHeight(int parentWidth, boolean isLandscape) {
        int width;
        int height;
        if (isLandscape) {
            width = parentWidth / 3;
        } else {
            width = parentWidth / 2;
        }
        height = width / 2 * 3;
        return Pair.create(width, height);
    }

    class ViewHolder {

        ImageView moviePoster;
    }
}
