package com.mobile.vople.vople;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    public final int PERMISSION = 1;

    public int permissionCheck_STORAGE;
    public int permissionCheck_RECORD;

    public static LoginActivity instance;

    private EditText edt_id, edt_password;
    private TextView tv_signup;
    private Button btn_login;

    private Retrofit retrofit;

    private Map<String, String> map = null;

    private SharedPreference sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RequestPermission();

        Initialize();

        instance = this;

    }


    private void Initialize() {
        edt_id = (EditText) findViewById(R.id.edt_password1);
        edt_password = (EditText) findViewById(R.id.edt_username);

        tv_signup = (TextView) findViewById(R.id.tv_signup);

        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);

        retrofit = MyRetrofit.getInstance().getRetrofit();

        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        sp = SharedPreference.getInstance();

        tv_signup.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void RequestPermission() {

        permissionCheck_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionCheck_RECORD = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permissionCheck_STORAGE != PackageManager.PERMISSION_GRANTED ||
                permissionCheck_RECORD != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), "앱 사용을 위해 권한 허가를 부탁드립니다.",Toast.LENGTH_SHORT).show();

            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO))
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, PERMISSION);
            }

            else
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, PERMISSION);
            }
        }
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == btn_login.getId()) {

            final ProgressDialog pd = ProgressDialog.show(LoginActivity.this, "로그인중", "로그인중 입니다.");

            VopleServiceApi.login service = retrofit.create(VopleServiceApi.login.class);

            String id_str = edt_id.getText().toString();
            String pw_str = edt_password.getText().toString();

            final Call<VopleServiceApi.Token> repos = service.repoContributors(id_str, pw_str);
            repos.enqueue(new Callback<VopleServiceApi.Token>() {
                @Override
                public void onResponse(Call<VopleServiceApi.Token> call, Response<VopleServiceApi.Token> response) {
                    if (response.code() == 200) {
                        sp.put("Authorization", response.body().token);
                        Intent intent = new Intent(getApplicationContext(), ListOrCreateActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();

                }

                @Override
                public void onFailure(Call<VopleServiceApi.Token> call, Throwable t) {
                    Log.d("TAG", t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
        else if(v.getId() == tv_signup.getId())
        {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

}