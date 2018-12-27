package com.mobile.vople.vople;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScriptInformationActivity extends AppCompatActivity{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_script)
    TextView tv_script;
    @BindView(R.id.btn_back)
    Button btn_back;

    private String allPlots = "";
    private String script_title;

    private Retrofit retrofit;

    private List<RetrofitModel.Plot> plotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_information);

        ButterKnife.bind(this);

        initialize();
    }

    private void initialize()
    {
        plotList = new ArrayList<>();

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

        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        VopleServiceApi.getScriptDetail service_script_detail = retrofit.create(VopleServiceApi.getScriptDetail.class);

        service_script_detail.repoContributors(script_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {setContent(response);},
                        throwable -> {MyUtils.makeNetworkErrorToast(this);
                });
    }

    @OnClick(R.id.btn_back)
    public void onBackButtonClick(View v) {
        finish();
    }

    private void setContent(RetrofitModel.ScriptDetail response)
    {
        script_title = response.title;

        for(RetrofitModel.CastBreif cast : response.casts)
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

}
