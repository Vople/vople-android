package com.mobile.vople.vople;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
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

        instance = this;

        Initialize();
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
                        Intent intent = new Intent(getApplicationContext(), ListOrCreate.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();

                    //Intent intent = new Intent()
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
