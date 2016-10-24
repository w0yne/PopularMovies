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

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_grid_item, parent, false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
        boolean isLandscape = getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        Pair<Integer, Integer> widthAndHeight = calculateWidthAndHeight(parent.getWidth(), isLandscape);
        Glide.with(getContext())
                .load(NetworkEngine.API_IMAGE_URL + movie.posterPath)
                .centerCrop()
                .override(widthAndHeight.first, widthAndHeight.second)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
//                                "onException(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
//                                "onResourceReady(%s, %s, %s, %s, %s)", resource, model, target, isFromMemoryCache, isFirstResource));
//                        return false;
//                    }
//                })
                .into(moviePoster);

        return convertView;
    }

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
}
