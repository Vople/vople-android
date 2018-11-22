package com.mobile.vople.vople;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AskScriptActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_ask, btn_back;
    private EditText edt_title;
    private EditText edt_script;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askscript);

        btn_ask = findViewById(R.id.btn_ask);
        btn_back = findViewById(R.id.btn_back);
        btn_ask.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        edt_title = findViewById(R.id.tv_title);

        edt_script = findViewById(R.id.edt_script);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btn_ask.getId()){
            if(edt_title.getText().length() <= 0){
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            }

            else if(edt_script.getText().length() <= 0){
                Toast.makeText(this, "대본을 입력해주세요", Toast.LENGTH_SHORT).show();
            }

            else{

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"once29@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT,edt_title.getText().toString());
                email.putExtra(Intent.EXTRA_TEXT,edt_script.getText().toString());
                startActivity(email);
                finish();
            }
        }

        else if(v.getId() == btn_back.getId())
        {
            finish();
        }
    }
}
