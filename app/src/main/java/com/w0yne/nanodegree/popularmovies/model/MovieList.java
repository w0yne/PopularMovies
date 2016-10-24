/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {

    public int page;
    @SerializedName("results")
    public List<Movie> movies;
    public int totalResults;
    public int totalPages;
}
