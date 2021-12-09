package com.trustasia.fidosdk.http;


import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RequestManager {
    private OkHttpClient client;


    public static RequestManager getInstance() {
        return Holder.INSTANCE;
    }

    private final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(s -> Log.d("FIDO_SDK", s));

    public void init() {
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }


    public OkHttpClient getClient() {
        return client;
    }

    private static class Holder {
        private static final RequestManager INSTANCE = new RequestManager();
    }

    private RequestManager() {
    }
}
