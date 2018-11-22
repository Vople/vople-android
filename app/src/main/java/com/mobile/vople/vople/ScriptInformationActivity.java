package com.mobile.vople.vople;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScriptInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title, tv_script;
    private Button btn_back;

    private String allPlots = "";
    private String script_title;

    private List<RetrofitModel.Plot> plotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_information);

        plotList = new ArrayList<>();

        tv_title = findViewById(R.id.tv_title);
        //광장 리스트 누른 후 화면의 제목 tv_title.setText("");

        tv_script = findViewById(R.id.tv_script);
        //광장 리스트 누른 후 화면의 대본 tv_script.setText("");

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);


        Intent intent = getIntent();

        if(intent == null)
        {
            Toast.makeText(getApplicationContext(), "잘못된 접근입니다!", Toast.LENGTH_SHORT).show();
            finish();
        }

        int script_id = intent.getIntExtra("script_id", -1);

        if(script_id <= 0)
        {
            Toast.makeText(getApplicationContext(), "잘못된 접근입니다!", Toast.LENGTH_SHORT).show();
            finish();
        }

        Retrofit retrofit = RetrofitInstance.getInstance(getApplicationContext());

        VopleServiceApi.getScriptDetail service = retrofit.create(VopleServiceApi.getScriptDetail.class);

        Call<RetrofitModel.ScriptDetail> repos = service.repoContributors(script_id);

        repos.enqueue(new Callback<RetrofitModel.ScriptDetail>() {
            @Override
            public void onResponse(Call<RetrofitModel.ScriptDetail> call, Response<RetrofitModel.ScriptDetail> response) {
                if(response.code() == 200)
                {
                    script_title = response.body().title;


                    for(RetrofitModel.CastBreif cast : response.body().casts)
                    {
                        for(RetrofitModel.Plot plot : cast.plots_by_cast)
                            plotList.add(plot);
                    }

                    String[] arrPlot = new String[plotList.size()];

                    for(RetrofitModel.Plot plot : plotList)
                        arrPlot[plot.order-1] = plot.content;

                    for(String s : arrPlot)
                    {
                        if(s != null)
                            allPlots += (s + "\n");
                    }

                    // 배경에 넣어주기
                    tv_script.setText(allPlots);
                    tv_title.setText(script_title);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "네트워크를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }

            @Override
            public void onFailure(Call<RetrofitModel.ScriptDetail> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == btn_back.getId())
        {
            finish();
        }
    }

}
