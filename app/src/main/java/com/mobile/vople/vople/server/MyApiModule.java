package com.mobile.vople.vople.server;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parkjaemin on 22/12/2018.
 */

@Module
public abstract class MyApiModule {

    @Provides
    @Singleton
    @NonNull
    static OkHttpClient.Builder provideOkHttp()
    {
        return new OkHttpClient.Builder();
    }

    @Provides
    @Named("context")
    static Context provideContext(Context context)
    {
        return context;
    }

    @Provides
    @Singleton
    @NonNull
    static Retrofit.Builder provideRetrofitBuilder()
    {
        return new Retrofit.Builder();
    }
}
