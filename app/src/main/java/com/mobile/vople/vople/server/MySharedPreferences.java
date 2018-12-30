package com.mobile.vople.vople.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by parkjaemin on 08/11/2018.
 */

public class MySharedPreferences {

    SharedPreferences mSharedPreferences;

    @Inject
    public MySharedPreferences(SharedPreferences sharedPreferences){
        this.mSharedPreferences = sharedPreferences;
    }

    public void put(String key, String value){
        mSharedPreferences.edit().putString(key, value);
    }
    public String get(String key){
        return mSharedPreferences.getString(key, "");
    }

    public void remove(String key){
        mSharedPreferences.edit().remove(key).apply();
    }
}