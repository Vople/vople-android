package com.mobile.vople.vople.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mobile.vople.vople.ListOrCreateActivity;
import com.mobile.vople.vople.LoginActivity;
import com.mobile.vople.vople.R;
import com.mobile.vople.vople.TutorialActivity;
import com.mobile.vople.vople.VopleApplication;
import com.mobile.vople.vople.server.MySharedPreferences;
import com.mobile.vople.vople.server.VopleApi;
import com.mobile.vople.vople.server.VopleServiceApi;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TERM = 1000;

    private static String TAG = "SPLASHACTIVITY TAG";

    Bitmap bgBitmap;

    @Inject
    MySharedPreferences mySharedPreferences;

    @Inject
    VopleApi mApi;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("VER", 0);

        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            int version = pi.versionCode;
            int old_ver = pref.getInt("version", 0);

            if (old_ver < version) {
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt("version", version);
                edit.commit();
                Intent it = new Intent(this, TutorialActivity.class);
                startActivity(it);
                finish();
            } else {
                startSplash();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        if (bgBitmap != null)
            bgBitmap.recycle();
        if(!disposable.isDisposed())
            disposable.dispose();
        super.onDestroy();
    }


    private void startSplash() {
        new Handler().postDelayed(() -> {

            String is_auto_login = mySharedPreferences.get("IS_AUTO_LOGIN");

            String stored_id = "";
            String stored_pwd = "";

            if(is_auto_login.equals("Yes"))
            {
                stored_id = mySharedPreferences.get("STORED_ID");
                stored_pwd = mySharedPreferences.get("STORED_PWD");
            }
            else {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }

            if(stored_id.length() <= 0 || stored_pwd.length() <= 0)
            {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }

            disposable = mApi.getRetrofit().create(VopleServiceApi.login.class)
                    .repoContributors(stored_id, stored_pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        mySharedPreferences.put("Authorization", response.token);
                        startActivity(new Intent(getApplicationContext(), ListOrCreateActivity.class));
                        finish();
                        }, throwable -> {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();});
        }, SPLASH_TERM);
    }
}