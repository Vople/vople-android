package com.mobile.vople.vople.di;

import android.app.Application;
import android.content.Context;

import com.mobile.vople.vople.VopleApplication;
import com.mobile.vople.vople.server.MyApiModule;
import com.mobile.vople.vople.server.MySharedPreferenceModule;
import com.mobile.vople.vople.server.MySharedPreferences;
import com.mobile.vople.vople.server.VopleApiModule;
import com.mobile.vople.vople.splash.SplashActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {
        MySharedPreferenceModule.class,
        VopleApiModule.class,
        ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<VopleApplication>
{
    void inject(SplashActivity splashActivity);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        Builder mySharedPreferences(MySharedPreferenceModule module);

        Builder vopleApiModule(VopleApiModule module);

        AppComponent build();
    }
}
