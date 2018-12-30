package com.mobile.vople.vople.splash;

import com.mobile.vople.vople.di.ActivityScoped;
import com.mobile.vople.vople.server.MySharedPreferenceModule;

import dagger.Binds;
import dagger.Module;

@Module()
public abstract class SplashModule {

    @ActivityScoped
    @Binds
    abstract SplashContract.Presenter splashPresenter(SplashPresenter presenter);
}
