/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = MovieDatabase.VERSION,
        packageName = "com.w0yne.nanodegree.popularmovies.data.generated")
public final class MovieDatabase {

    private MovieDatabase() {}

    public static final int VERSION = 1;

    @Table(MovieColumns.class)
    public static final String MOVIES = MovieProvider.Path.MOVIES;

    @Table(TrailerColumns.class)
    public static final String TRAILERS = MovieProvider.Path.TRAILERS;

    @Table(ReviewColumns.class)
    public static final String REVIEWS = MovieProvider.Path.REVIEWS;
}
