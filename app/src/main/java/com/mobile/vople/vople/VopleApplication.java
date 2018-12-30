package com.mobile.vople.vople;

import com.mobile.vople.vople.di.AppComponent;
import com.mobile.vople.vople.di.DaggerAppComponent;
import com.mobile.vople.vople.server.MySharedPreferenceModule;
import com.mobile.vople.vople.server.VopleApiModule;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by parkjaemin on 22/12/2018.
 */

public class VopleApplication extends DaggerApplication {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .application(this)
                .mySharedPreferences(new MySharedPreferenceModule(getApplicationContext()))
                .vopleApiModule(new VopleApiModule(getApplicationContext()))
                .build();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector()
    {
        return DaggerAppComponent.builder().application(this).
                mySharedPreferences(
                        new MySharedPreferenceModule(getApplicationContext()))
                .build();
    }

    public AppComponent getAppComponent()
    {
        return mAppComponent;
    }

}
