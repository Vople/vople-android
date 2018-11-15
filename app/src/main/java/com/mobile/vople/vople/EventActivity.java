package com.mobile.vople.vople;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ListView listView_role_play;
    private boolean bool_owner;
    private int i_big_heart;
    private Button btn_back;
    private Button btn_big_heart;
    private Button btn_record;
    private Button btn_cancel;
    private Button btn_send;
    private Button btn_gather;
    private Date date;

    private String allPlots = "";

    private List<RetrofitModel.Plot> plotList;

    SimpleDateFormat mFormat = new SimpleDateFormat("MM월 dd일 hh:mm");

    private Retrofit retrofit;


    private ListViewAdapter adapter;
    private RolePlayListViewAdapter adapter_role_play;

    private TextView tv_script;

    private int roomId;

    private RetrofitModel.Cast cast;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initialize();

        Intent intent = getIntent();

        roomId = intent.getIntExtra("RoomID", -1);

        if(roomId == -1)
        {
            Toast.makeText(EventActivity.this, "유효하지 않는 방입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        VopleServiceApi.boardDetail service = retrofit.create(VopleServiceApi.boardDetail.class);

        final Call<RetrofitModel.BoardDetail> repos = service.repoContributors(roomId);

        repos.enqueue(new Callback<RetrofitModel.BoardDetail>() {
            @Override
            public void onResponse(Call<RetrofitModel.BoardDetail> call, Response<RetrofitModel.BoardDetail> response) {
                if(response.code() == 200)
                {
                    List<RetrofitModel.CommentBrief> list = response.body().comments;

                    for( RetrofitModel.CommentBrief comment : list)
                    {
                        adapter.addItem(null, comment.owner.name, "04:11", comment.created_at, comment.sound);
                    }

                    adapter.notifyDataSetChanged();

                    for(RetrofitModel.Cast cast : response.body().script.casts)
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
                }
            }

            @Override
            public void onFailure(Call<RetrofitModel.BoardDetail> call, Throwable t) {
                Toast.makeText(EventActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        VopleServiceApi.get_plots getPlotService = retrofit.create(VopleServiceApi.get_plots.class);
        Call<List<RetrofitModel.Plot>> getPlotRepos = getPlotService.repoContributors(roomId);

        getPlotRepos.enqueue(new Callback<List<RetrofitModel.Plot>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.Plot>> call, Response<List<RetrofitModel.Plot>> response) {
                if(response.code() == 200)
                {
                    List<RetrofitModel.Plot> result = response.body();

                    for(RetrofitModel.Plot plot : result)
                    {
                        adapter_role_play.addItem(null, "04:11", plot.content);
                    }
                    adapter_role_play.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<RetrofitModel.Plot>> call, Throwable t) {

            }
        });

    }

    private String getTime(String now) {
        date = new Date(now);
        return mFormat.format(date);
    }

    @Override
    public void onClick(View v) {

        if(bool_owner == true)
        {
            btn_gather.setVisibility(View.VISIBLE);
        }

        if (v.getId() == btn_back.getId()) {
            finish();
        }

        else if (v.getId() == btn_big_heart.getId()) {
            i_big_heart++;
            if (i_big_heart % 2 == 0) {
                btn_big_heart.setBackgroundResource(R.drawable.event_unpress_heart);

            }
            else if (i_big_heart % 2 != 0) {
                btn_big_heart.setBackgroundResource(R.drawable.event_press_heart);

            }
        }

        else if (v.getId() == btn_send.getId()) {
            //녹음본 보내기
            sendAudioFile();
        }

    }

    private void sendAudioFile()
    {
        File file = new File(Environment.getExternalStorageDirectory() + "/recoder3.mp3");

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);

        VopleServiceApi.commentOnBoard service = retrofit.create(VopleServiceApi.commentOnBoard.class);

        Call<ResponseBody> call = service.upload(roomId, requestFile, 15);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Code : " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize()
    {

        listView = (ListView) findViewById(R.id.lv_event);
        listView_role_play = (ListView) findViewById(R.id.lv_event_role);

        adapter = new ListViewAdapter();
        adapter_role_play = new RolePlayListViewAdapter();

        listView.setAdapter(adapter);
        listView_role_play.setAdapter(adapter_role_play);


        i_big_heart = 0;
        bool_owner = false;
        btn_back = findViewById(R.id.btn_back);
        btn_big_heart = findViewById(R.id.btn_big_heart);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);
        btn_gather = findViewById(R.id.btn_gather);
        tv_script = findViewById(R.id.tv_script);

        btn_back.setOnClickListener(this);
        btn_big_heart.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_gather.setOnClickListener(this);



        retrofit = RetrofitInstance.getInstance(this);

        plotList = new ArrayList<RetrofitModel.Plot>();
    }
}

