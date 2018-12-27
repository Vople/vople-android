package com.mobile.vople.vople;

import com.mobile.vople.vople.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerActivity;
import dagger.android.DaggerApplication;

/**
 * Created by parkjaemin on 22/12/2018.
 */

public class VopleApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector()
    {
        return DaggerAppComponent.builder().application(this).build();
    }


}
