package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        btn_back = findViewById(R.id.btn_back);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btn_back.getId()) {
            finish();
        }
    }
}
