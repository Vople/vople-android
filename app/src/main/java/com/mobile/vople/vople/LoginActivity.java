package com.mobile.vople.vople;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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

import com.mobile.vople.vople.server.MySharedPreferences;
import com.mobile.vople.vople.server.VopleApi;
import com.mobile.vople.vople.server.VopleServiceApi;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public final int PERMISSION = 1;

    public int permissionCheck_STORAGE;
    public int permissionCheck_RECORD;

    public static LoginActivity instance;

    @BindView(R.id.edt_id)
    EditText edt_id;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.btn_login)
    Button btn_login;

    @Inject
    VopleApi mVopleApi;

    //@Inject
    MySharedPreferences sp;

    private Disposable disposable;

    private final String TAG = "LOGINACTIVITY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        RequestPermission();

        instance = this;


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
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}, PERMISSION);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }
    }

    @SuppressLint("CheckResult")
    @OnClick({R.id.btn_login, R.id.tv_signup})
    public void onClick(View v) {

        if (v.getId() == btn_login.getId()) {

            String login_id = edt_id.getText().toString();
            String login_pwd = edt_password.getText().toString();

            disposable = mVopleApi.getRetrofit().create(VopleServiceApi.login.class)
                    .repoContributors(login_id, login_pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        sp.put("Authorization", response.token);
                        sp.put("IS_AUTO_LOGIN", "Yes");
                        sp.put("STORED_ID", login_id);
                        sp.put("STORED_PWD", login_pwd);
                        Intent intent = new Intent(getApplicationContext(), ListOrCreateActivity.class);
                        startActivity(intent);
                        finish();
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(),
                                "아이디나 비밀번호가 잘못되었습니다",
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, throwable.getLocalizedMessage());
                    });
        }
        else if(v.getId() == tv_signup.getId())
        {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if(!disposable.isDisposed())
            disposable.dispose();
        super.onDestroy();
    }
}