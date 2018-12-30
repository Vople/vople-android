package com.mobile.vople.vople.server;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobile.vople.vople.di.MyApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MySharedPreferenceModule {

    private Context context;

    public MySharedPreferenceModule(Context context)
    {
        this.context = context;
    }

    @Provides
    SharedPreferences provideSharedPreferences()
    {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
}
