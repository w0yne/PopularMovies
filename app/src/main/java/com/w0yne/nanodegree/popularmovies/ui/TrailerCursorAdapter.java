/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.model.Trailer;

public class TrailerCursorAdapter extends CursorRecyclerViewAdapter<TrailerCursorAdapter.ViewHolder> {

    private final Context context;
    private OnClickItemListener onClickItemListener;

    public TrailerCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final Trailer trailer = Trailer.getTrailer(cursor);
        viewHolder.nameTxv.setText(trailer.name);
        viewHolder.nameTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null) {
                    onClickItemListener.onItemClicked(trailer);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    public interface OnClickItemListener {

        void onItemClicked(Trailer trailer);
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTxv = (TextView) itemView.findViewById(R.id.trailer_name);
        }
    }
}
