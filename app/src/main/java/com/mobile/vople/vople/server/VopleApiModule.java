package com.mobile.vople.vople.server;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class VopleApiModule {

    private Context mContext;
//
//    @Inject
//    MySharedPreferences mySharedPreferences;

    public VopleApiModule(Context context)
    {
        this.mContext = context;
    }

    @Provides
    Retrofit provideRetrofit(){
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    //String token = mySharedPreferences.get("Authorization");
                    //Log.d("TAG", token);
                    Request.Builder  requestBuilder = chain.request().newBuilder();

                    String token = "asdfxzcvasdf";

                    //if(!token.equals("")){
                        requestBuilder.addHeader("Authorization", "JWT " + token);
                    //}

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            OkHttpClient client = builder
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();


            return new Retrofit.Builder()
                    .baseUrl(MySettings.BASE_URL) // 통신 url
                    .addConverterFactory(GsonConverterFactory.create()) // json통신 여부
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
    }
}
