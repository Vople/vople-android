package com.mobile.vople.vople;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_username, edt_password1, edt_password2, edt_email, edt_bio, edt_nickname;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password1 = (EditText) findViewById(R.id.edt_password1);
        edt_password2 = (EditText) findViewById(R.id.edt_password2);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_bio = (EditText) findViewById(R.id.edt_bio);
        edt_nickname = (EditText) findViewById(R.id.edt_nickname);

        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);



    }
}
