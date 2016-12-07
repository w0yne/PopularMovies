/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ModelList<T> {

    public int id;
    public int page;
    @SerializedName("results")
    public List<T> items;
    public int totalResults;
    public int totalPages;

    public static Type getTypeForClass(Class<?> tClass) {
        if (Movie.class.equals(tClass)) {
            return new TypeToken<ModelList<Movie>>() {}.getType();
        } else if (Trailer.class.equals(tClass)) {
            return new TypeToken<ModelList<Trailer>>() {}.getType();
        } else if (Review.class.equals(tClass)) {
            return new TypeToken<ModelList<Review>>() {}.getType();
        } else {
            throw new IllegalArgumentException("Not supported Class Type: "
                    + tClass.toString()
                    + ". Did you register it in ModelList?");
        }
    }
}
