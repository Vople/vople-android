package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScriptInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title, tv_script;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_information);

        tv_title = findViewById(R.id.tv_title);
        //광장 리스트 누른 후 화면의 제목 tv_title.setText("");

        tv_script = findViewById(R.id.tv_script);
        //광장 리스트 누른 후 화면의 대본 tv_script.setText("");

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == btn_back.getId())
        {
            finish();
        }
    }

}
