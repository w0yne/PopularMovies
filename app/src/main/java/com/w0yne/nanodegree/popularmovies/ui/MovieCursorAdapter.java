/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.data.MovieColumns;
import com.w0yne.nanodegree.popularmovies.net.NetworkEngine;

public class MovieCursorAdapter extends CursorAdapter {

    private int columnWidth;

    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
        notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        if (columnWidth == 0) {
            return;
        }

        final String posterPath = cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH));

        final ViewHolder viewHolder;
        if (view.getTag() == null) {
            viewHolder = new ViewHolder();
            viewHolder.moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(context)
                .load(NetworkEngine.getApiImageUrl(context) + posterPath)
                .centerCrop()
                .dontAnimate()
                .override(columnWidth, columnWidth * 3 / 2)
                .into(viewHolder.moviePoster);
    }

    final class ViewHolder {

        ImageView moviePoster;
    }
}
