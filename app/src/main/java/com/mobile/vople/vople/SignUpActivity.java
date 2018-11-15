package com.mobile.vople.vople;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_username, edt_password1, edt_password2, edt_email, edt_bio, edt_nickname;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female;
    private Button btn_submit;

    private Retrofit retrofit;

    private SharedPreference sp;

    private final static String TAG = "-----SignUpActivity----";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_facebook);

        Initialize();

    }

    private void Initialize()
    {
        edt_username    = (EditText) findViewById(R.id.edt_username);
        edt_password1   = (EditText) findViewById(R.id.edt_password1);
        edt_password2   = (EditText) findViewById(R.id.edt_password2);
        edt_email       = (EditText) findViewById(R.id.edt_email);
        edt_bio         = (EditText) findViewById(R.id.edt_bio);
        edt_nickname    = (EditText) findViewById(R.id.edt_nickname);

        rg_gender       = (RadioGroup) findViewById(R.id.rg_gender);
        rb_male         = (RadioButton) findViewById(R.id.rb_male);
        rb_female       = (RadioButton) findViewById(R.id.rb_female);

        btn_submit      = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);

        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        sp = SharedPreference.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btn_submit.getId())
        {

            VopleServiceApi.signup service = retrofit.create(VopleServiceApi.signup.class);

            String username     = edt_username.getText().toString();
            String password1    = edt_password1.getText().toString();
            String password2    = edt_password2.getText().toString();
            String email        = edt_email.getText().toString();
            String bio          = edt_bio.getText().toString();
            String nickname     = edt_nickname.getText().toString();

            String gender       = "";

            if(rb_male.isChecked()) gender = "male";
            else gender = "female";

            final Call<VopleServiceApi.Token> repos = service.repoContributors(username, password1, password2,
                    email, nickname, bio, gender);
            repos.enqueue(new Callback<VopleServiceApi.Token>() {
                @Override
                public void onResponse(Call<VopleServiceApi.Token> call, Response<VopleServiceApi.Token> response) {
                    if (response.code() == 201)
                    {
                        sp.put("Authorization", response.body().token);

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);

                        if(LoginActivity.instance != null) LoginActivity.instance.finish();

                        finish();
                    }
                    else if(response.code() == 400)
                    {
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Response.code = "
                                + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<VopleServiceApi.Token> call, Throwable t) {
                    Log.d("TAG", t.getLocalizedMessage());

                }
            });
        }
    }
}
