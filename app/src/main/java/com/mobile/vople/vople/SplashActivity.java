package com.mobile.vople.vople;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TERM = 1000;

    private static String TAG = "SPLASH ACTIVITY LOG";

    Bitmap bgBitmap;

    private SharedPreference sp;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        retrofit = RetrofitInstance.getInstance(SplashActivity.this);

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
        super.onDestroy();
        if (bgBitmap != null)
            bgBitmap.recycle();
    }


    private void startSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sp = SharedPreference.getInstance(SplashActivity.this);

                String is_auto_login = sp.get("IS_AUTO_LOGIN");

                String stored_id = "";
                String stored_pwd = "";

                if(is_auto_login.equals("Yes"))
                {
                    stored_id = sp.get("STORED_ID");
                    stored_pwd = sp.get("STORED_PWD");
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

                VopleServiceApi.login service = retrofit.create(VopleServiceApi.login.class);

                service.repoContributors(stored_id, stored_pwd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            sp.put("Authorization", response.token);
                            startActivity(new Intent(getApplicationContext(), ListOrCreateActivity.class));
                            finish();
                            }, throwable -> {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();});
            }
        }, SPLASH_TERM);
    }
}