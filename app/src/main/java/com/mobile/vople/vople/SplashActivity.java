package com.mobile.vople.vople;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity{

    private static int SPLASH_TERM = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_splash);

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(getApplicationContext(), EventActivity.class);
                            startActivity(mainIntent);
                            finish();
            }
        }, SPLASH_TERM);
    }
}