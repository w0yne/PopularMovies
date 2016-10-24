/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.media;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.w0yne.nanodegree.popularmovies.net.ServiceGenerator;

import java.io.InputStream;

import okhttp3.OkHttpClient;

public class CustomGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient okHttpClient = ServiceGenerator.getSharedOkHttpClient();
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(okHttpClient);
        glide.register(GlideUrl.class, InputStream.class, factory);
    }
}
