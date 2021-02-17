package com.sg.moviesindex.service.network;


import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofitTMDb = null;
    private static final String BASE_URL_TMDB = "https://api.themoviedb.org/3/";
    private static OkHttpClient okHttpClientTMDb;
    private static final int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static Cache cacheYTS;
    private static Cache cacheTMDb;


    public static TMDbService getTMDbService(Context context) {
        if (okHttpClientTMDb == null) {
            initOkHttpTMDb(context);
        }
        if (retrofitTMDb == null) {
            retrofitTMDb = new Retrofit.Builder().baseUrl(BASE_URL_TMDB)
                    .client(okHttpClientTMDb)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitTMDb.create(TMDbService.class);
    }

    private static void initOkHttpTMDb(Context context) {
        cacheTMDb = new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .cache(cacheTMDb);
        okHttpClientTMDb = httpClient.build();
    }

    public static void resetCache() throws IOException {
        if (cacheYTS != null) {
            cacheYTS.evictAll();
        }
        if (cacheTMDb != null) {
            cacheTMDb.evictAll();
        }
    }


}
