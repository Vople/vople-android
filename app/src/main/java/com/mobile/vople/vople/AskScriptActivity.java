package com.mobile.vople.vople;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AskScriptActivity extends AppCompatActivity{

    @BindView(R.id.btn_ask)
    Button btn_ask;
    @BindView(R.id.btn_back)
    Button btn_back;
    @BindView(R.id.edt_title)
    EditText edt_title;
    @BindView(R.id.edt_script)
    EditText edt_script;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askscript);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_ask, R.id.btn_back})
    public void onButtonClick(View v) {
        if(v.getId() == btn_ask.getId())
        {
            if(edt_title.getText().length() <= 0)
            {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            else if(edt_script.getText().length() <= 0)
            {
                Toast.makeText(this, "대본을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            else
            {
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
