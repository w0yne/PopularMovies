/*
 * Created by Stev Wayne on 2016/12/7
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.data;

import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

public interface ReviewColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = BaseColumns._ID;

    @DataType(DataType.Type.TEXT)
    @NotNull
    @Unique(onConflict = ConflictResolutionType.REPLACE)
    String ID = "id";

    @DataType(DataType.Type.INTEGER)
    String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT)
    String AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    String CONTENT = "content";

    @DataType(DataType.Type.TEXT)
    String URL = "url";
}
