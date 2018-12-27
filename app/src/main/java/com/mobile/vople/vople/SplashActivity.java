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
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TERM = 3000;

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

    /*private void RequestPermission() {
>>>>>>> 1b2fc21459f786f7e7d1a606f999d198d2982b41
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }*/



    private void startSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

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
                            Log.d("TAG", throwable.getLocalizedMessage());
                });

                finish();
            }
        }, SPLASH_TERM);
    }
}