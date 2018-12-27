package com.mobile.vople.vople.server;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parkjaemin on 08/11/2018.
 */

public class RetrofitInstance {
    private static Retrofit retrofit=null;
    private static final String API_URL = MySettings.BASE_URL;

    static public  Retrofit getInstance(Context context){
        if(retrofit==null){
            final SharedPreference preference = SharedPreference.getInstance(context);
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String token=preference.get("Authorization");
                    //Log.d("TAG", token);
                    Request.Builder  requestBuilder = chain.request().newBuilder();

                    if(!token.equals("")){
                        requestBuilder.addHeader("Authorization", "JWT "+token);
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            OkHttpClient client = builder.build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL) // 통신 url
                    .addConverterFactory(GsonConverterFactory.create()) // json통신 여부
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

        }
        return retrofit;
    }
}