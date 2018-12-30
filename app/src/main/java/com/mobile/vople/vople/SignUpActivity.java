package com.mobile.vople.vople;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobile.vople.vople.server.MySharedPreferences;
import com.mobile.vople.vople.server.VopleServiceApi;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.edt_id)
    EditText edt_id;
    @BindView(R.id.edt_password)
    EditText edt_password1;
    @BindView(R.id.edt_password2)
    EditText edt_password2;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_bio)
    EditText edt_bio;
    @BindView(R.id.edt_nickname)
    EditText edt_nickname;
    @BindView(R.id.rb_male)
    RadioButton rb_male;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @Inject
    Retrofit retrofit;

    private MySharedPreferences sp;

    private final static String TAG = "-----SignUpActivity----";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        initialize();

    }

    private void initialize()
    {

    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        if(v.getId() == btn_submit.getId())
        {
            VopleServiceApi.signup service_signup = retrofit.create(VopleServiceApi.signup.class);

            String id     = edt_id.getText().toString();
            String password1    = edt_password1.getText().toString();
            String password2    = edt_password2.getText().toString();
            String email        = edt_email.getText().toString();
            String bio          = edt_bio.getText().toString();
            String nickname     = edt_nickname.getText().toString();

            String gender       = "";

            if(rb_male.isChecked()) gender = "male";
            else gender = "female";

            service_signup.repoContributors(id, password1, password2, email, bio, nickname, gender)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        sp.put("Authorization", response.token);
                        sp.put("IS_AUTO_LOGIN", "Yes");
                        sp.put("STORED_ID", id);
                        sp.put("STORED_PWD", password1);

                        Intent intent = new Intent(SignUpActivity.this, ListOrCreateActivity.class);
                        startActivity(intent);

                        if(LoginActivity.instance != null) LoginActivity.instance.finish();

                        finish();
                    }, throwable -> {
                        Toast.makeText(SignUpActivity.this, "아이디와 패스워드를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    });

        }
    }
}
