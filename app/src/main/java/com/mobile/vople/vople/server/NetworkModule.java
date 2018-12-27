package com.mobile.vople.vople.server;

import android.content.Context;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parkjaemin on 22/12/2018.
 */

@Module
public class NetworkModule {

    Context context;

    public NetworkModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {

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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MySettings.BASE_URL) // 통신 url
                .addConverterFactory(GsonConverterFactory.create()) // json통신 여부
                .client(client)
                .build();

        return retrofit;
    }
}
