/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = MovieProvider.AUTHORITY,
        database = MovieDatabase.class,
        packageName = "com.w0yne.nanodegree.popularmovies.data.generated")
public final class MovieProvider {

    public static final String AUTHORITY =
            "com.w0yne.nanodegree.popularmovies.data.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {

        String MOVIES = "movies";

        String TRAILERS = "trailers";

        String REVIEWS = "reviews";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.POPULARITY + " DESC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }

        public static final String[] DEFAULT_PROJECTION = {
                MovieColumns._ID,
                MovieColumns.ID,
                MovieColumns.OVERVIEW,
                MovieColumns.POSTER_PATH,
                MovieColumns.RELEASE_DATE,
                MovieColumns.RUNTIME,
                MovieColumns.TITLE,
                MovieColumns.POPULARITY,
                MovieColumns.VOTE_AVERAGE,
                MovieColumns.FAVORITE
        };

        private static final int IDX_BASE = 0;// index of _id
        public static final int IDX_ID = 1;
        public static final int IDX_OVERVIEW = 2;
        public static final int IDX_POSTER_PATH = 3;
        public static final int IDX_RELEASE_DATE = 4;
        public static final int IDX_RUNTIME = 5;
        public static final int IDX_TITLE = 6;
        public static final int IDX_POPULARITY = 7;
        public static final int IDX_VOTE_AVERAGE = 8;
        public static final int IDX_FAVORITE = 9;
    }

    @TableEndpoint(table = MovieDatabase.TRAILERS)
    public static class Trailers {

        @ContentUri(
                path = Path.TRAILERS,
                type = "vnd.android.cursor.dir/trailer")
        public static final Uri CONTENT_URI = buildUri(Path.TRAILERS);

        @InexactContentUri(
                name = "TRAILER_ID",
                path = Path.TRAILERS + "/#",
                type = "vnd.android.cursor.item/trailer",
                whereColumn = TrailerColumns.ID,
                pathSegment = 1)
        public static Uri withId(String id) {
            return buildUri(Path.TRAILERS, id);
        }

        public static final String[] DEFAULT_PROJECTION = {
                TrailerColumns._ID,
                TrailerColumns.ID,
                TrailerColumns.MOVIE_ID,
                TrailerColumns.KEY,
                TrailerColumns.NAME,
                TrailerColumns.SITE,
                TrailerColumns.SIZE,
                TrailerColumns.TYPE
        };

        private static final int IDX_BASE = 0;// index of _id
        public static final int IDX_ID = 1;
        public static final int IDX_MOVIE_ID = 2;
        public static final int IDX_KEY = 3;
        public static final int IDX_NAME = 4;
        public static final int IDX_SITE = 5;
        public static final int IDX_SIZE = 6;
        public static final int IDX_TYPE = 7;
    }

    @TableEndpoint(table = MovieDatabase.REVIEWS)
    public static class Reviews {

        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/review")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = ReviewColumns.ID,
                pathSegment = 1)
        public static Uri withId(String id) {
            return buildUri(Path.REVIEWS, id);
        }

        public static final String[] DEFAULT_PROJECTION = {
                ReviewColumns._ID,
                ReviewColumns.ID,
                ReviewColumns.MOVIE_ID,
                ReviewColumns.AUTHOR,
                ReviewColumns.CONTENT,
                ReviewColumns.URL
        };

        private static final int IDX_BASE = 0;// index of _id
        public static final int IDX_ID = 1;
        public static final int IDX_MOVIE_ID = 2;
        public static final int IDX_AUTHOR = 3;
        public static final int IDX_CONTENT = 4;
        public static final int IDX_URL = 5;
    }
}
