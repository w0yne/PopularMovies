/*
 * Created by Stev Wayne on 2016/10/19
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String API_KEY = "{your_api_key™}";

    private static OkHttpClient sharedOkHttpClient;

    private static GsonBuilder gsonBuilder;

    private static Retrofit.Builder retrofitBuilder;

    static {
        sharedOkHttpClient =
                new OkHttpClient.Builder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request oldRequest = chain.request();
                                HttpUrl newUrl = oldRequest.url().newBuilder()
                                        .addQueryParameter("api_key", API_KEY)
                                        .build();
                                Request newRequest = oldRequest.newBuilder()
                                        .url(newUrl)
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        })
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.HEADERS))
                        .build();

        gsonBuilder =
                new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        retrofitBuilder =
                new Retrofit.Builder();
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit retrofit = retrofitBuilder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(sharedOkHttpClient)
                .build();
        return retrofit.create(serviceClass);
    }

    public static OkHttpClient getSharedOkHttpClient() {
        return sharedOkHttpClient;
    }
}
