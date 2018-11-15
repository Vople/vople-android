package com.mobile.vople.vople;
//취소키 만든다면 RolePlayListViewAdapter로~~
import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ListView listView_role_play;
    private boolean bool_owner;
    private int i_big_heart;
    private int i_record_button;
    private int n_board_like;
    private Button btn_back;
    private Button btn_big_heart;
    private Button btn_record;
    private Button btn_cancel;
    private Button btn_send;
    private Button btn_gather;
    private long now;
    private Date date;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM월 dd일 hh:mm");

    private Retrofit retrofit;


    private ListViewAdapter adapter;
    private RolePlayListViewAdapter adapter_role_play;

    private int roomId;

    private RetrofitModel.Cast cast;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initialize();

        Intent intent = getIntent();

        if(intent == null){
            Toast.makeText(this, "유효하지 않은 방 아이디입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        roomId = intent.getIntExtra("RoomID", -1);

        if(roomId == -1){
            Toast.makeText(this, "유효하지 않은 방 아이디입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        String str_cast = intent.getStringExtra("Cast");

        Gson gson = new Gson();

        cast = gson.fromJson(str_cast, RetrofitModel.Cast.class);

        if(cast == null) {
            Toast.makeText(this, "유효하지 않는 배역입니다", Toast.LENGTH_SHORT).show();
            finish();
        }

        for(RetrofitModel.Plot plot : cast.plots_by_cast)
        {
            adapter_role_play.addItem(null, "04:11", plot.content);
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
                        adapter.addItem(null, comment.owner.name, "04:11", getTime(comment.created_at), comment.sound);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetrofitModel.BoardDetail> call, Throwable t) {

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
                n_board_like = 0;
            }
            else if (i_big_heart % 2 != 0) {
                btn_big_heart.setBackgroundResource(R.drawable.event_press_heart);
                n_board_like = 1;
            }
        }

        else if (v.getId() == btn_send.getId()) {
            //녹음본 보내기
            btn_send.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            if (bool_owner == true) {
                btn_gather.setVisibility(View.VISIBLE);
            }
        }

        //else if (v.getId() == btn_cancel.getId()) {
            //취소
        //}
    }

    private void initialize()
    {

        listView = (ListView) findViewById(R.id.lv_event);
        listView_role_play = (ListView) findViewById(R.id.lv_event_role);

        adapter = new ListViewAdapter();
        adapter_role_play = new RolePlayListViewAdapter();

        listView.setAdapter(adapter);
        listView_role_play.setAdapter(adapter_role_play);

        n_board_like = 0;
        i_big_heart = 0;
        bool_owner = false;
        btn_back = findViewById(R.id.btn_back_following);
        btn_big_heart = findViewById(R.id.btn_big_heart);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);
        btn_gather = findViewById(R.id.btn_gather);

        btn_back.setOnClickListener(this);
        btn_big_heart.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_gather.setOnClickListener(this);

        retrofit = RetrofitInstance.getInstance(this);
    }
}

