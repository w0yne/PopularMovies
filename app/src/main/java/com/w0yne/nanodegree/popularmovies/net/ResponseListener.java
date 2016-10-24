/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.net;

public interface ResponseListener<T> {

    void onSuccess(T data);

    void onFailure(Object error);
}
