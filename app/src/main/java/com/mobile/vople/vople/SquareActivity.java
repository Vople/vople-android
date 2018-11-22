package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SquareActivity extends AppCompatActivity{

    private ListView lv_script_square;

    private Button btn_back;

    private SquareListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        btn_back = (Button) findViewById(R.id.btn_back);

        lv_script_square = (ListView)findViewById(R.id.lv_script_square);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new SquareListViewAdapter();

        lv_script_square.setAdapter(adapter);

        Retrofit retrofit = RetrofitInstance.getInstance(getApplicationContext());

        VopleServiceApi.getAllScripts service = retrofit.create(VopleServiceApi.getAllScripts.class);

        Call<List<RetrofitModel.Script>> repos = service.repoContributors();

        repos.enqueue(new Callback<List<RetrofitModel.Script>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.Script>> call, Response<List<RetrofitModel.Script>> response) {
                if(response.code() == 200)
                {
                    List<RetrofitModel.Script> list = response.body();

                    for(RetrofitModel.Script script : list)
                    {
                        adapter.addItem(script.title, script.id, script.member_restriction);
                    }

                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RetrofitModel.Script>> call, Throwable t) {

            }
        });


    }
}
