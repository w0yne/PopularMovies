/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.w0yne.nanodegree.popularmovies.R;
import com.w0yne.nanodegree.popularmovies.model.Review;

public class ReviewCursorAdapter extends CursorRecyclerViewAdapter<ReviewCursorAdapter.ViewHolder> {

    private final Context context;

    public ReviewCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Review review = Review.getReview(cursor);
        viewHolder.author.setText(review.author);
        viewHolder.content.setText(review.content);
        String reviewUrl = review.url;
        viewHolder.url.setText(Html.fromHtml("<a href=\"" + reviewUrl + "\">" + reviewUrl + "</a>"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView content;
        TextView url;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
            url = (TextView) itemView.findViewById(R.id.review_url);
            url.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
