package com.mobile.vople.vople.server.model;

import com.mobile.vople.vople.server.Settings;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by parkjaemin on 08/11/2018.
 */

public class MyRetrofit {

    private static MyRetrofit instance;

    public static MyRetrofit getInstance()
    {
        if(instance == null)
            return instance = new MyRetrofit();
        else
            return instance;
    }

    public Retrofit getRetrofit()
    {
        Retrofit r = new Retrofit.Builder()
                .baseUrl(Settings.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return r;
    }


}
