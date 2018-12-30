package com.mobile.vople.vople;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.MySettings;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleApi;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.mobile.vople.vople.server.MyUtils.mergeMediaFiles;

public class SituationActivity extends BaseEventActivity {

    @BindView(R.id.lv_event)
    ListView lv_event;
    @BindView(R.id.lv_event_role)
    ListView listView_role_play;
    @BindView(R.id.btn_back)
    Button btn_back;
    @BindView(R.id.btn_big_heart)
    Button btn_big_heart;
    @BindView(R.id.btn_gather)
    Button btn_gather;
    @BindView(R.id.tv_script)
    TextView tv_script;
    @BindView(R.id.tv_room_title)
    TextView tv_room_title;

    int i_big_heart;

    private List<RetrofitModel.Plot> plotList;

    @Inject
    VopleApi mVopleApi;

    private boolean is_first = true;

    private ListViewAdapter adp_event;
    private RolePlayListViewAdapter adp_role_play;


    public static int roomId;

    public static String roomTitle;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);

        initialize();

    }

    @SuppressLint({"CheckResult", "HandlerLeak"})
    private void initialize()
    {
        adp_event = new ListViewAdapter();
        adp_role_play = new RolePlayListViewAdapter();

        lv_event.setAdapter(adp_event);
        listView_role_play.setAdapter(adp_role_play);

        i_big_heart = 0;

        plotList = new ArrayList<>();

        occurDataChange();

        Intent intent = getIntent();

        roomId = intent.getIntExtra("RoomID", -1);
        roomTitle = intent.getStringExtra("RoomTitle");

        tv_room_title.setText(roomTitle);

        if(roomId == -1)
        {
            Toast.makeText(SituationActivity.this, "유효하지 않는 방입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        VopleServiceApi.get_plots service_get_plots = mVopleApi.getRetrofit().create(VopleServiceApi.get_plots.class);
        service_get_plots.repoContributors(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for(RetrofitModel.Plot plot : response)
                    {
                        adp_role_play.addItem(null, plot.content, plot.id);
                    }
                    adp_role_play.notifyDataSetChanged();
                }, throwable -> {
                    Log.e("TAG", throwable.getLocalizedMessage());
                });

        mergeHandler = new Handler() {
            public void handleMessage(Message msg) {
                Toast.makeText(SituationActivity.this, "Success to Merge your file", Toast.LENGTH_SHORT).show();
                try {
                    mergeHandler.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mergeHandler = null;
            }
        };
    }

    @SuppressLint("CheckResult")
    public void occurDataChange()
    {
        adp_event.clear();

        VopleServiceApi.boardDetail service_board_detail = mVopleApi.getRetrofit().create(VopleServiceApi.boardDetail.class);

        service_board_detail.repoContributors(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if(result.code() == 200)
                        settingBoardDetail(result.body());
                }, throwable -> {
                    Toast.makeText(SituationActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void settingBoardDetail(RetrofitModel.BoardDetail boardDetail)
    {
        if(boardDetail.script == null) return;

        boolean is_duplicated = false;

        int cast_color = 0;

        for(RetrofitModel.Cast cast : boardDetail.script.casts)
        {
            for(RetrofitModel.Plot plot : cast.plots_by_cast) {
                for (RetrofitModel.Plot _plot : plotList)
                    if ( _plot.commenting.board.id == plot.commenting.board.id &&
                            _plot.order == plot.order &&
                            _plot.id == plot.id) is_duplicated = true;
                if ( is_duplicated == false && plot.commenting != null && plot.commenting.board != null && plot.commenting.board.id == roomId)
                {
                    plot.color = cast_color;
                    plotList.add(plot);
                }
            }
            is_duplicated = false;
            cast_color ++;
        }

        Collections.sort(plotList, (o1, o2) -> {
            if(o1.order < o2.order) return -1;
            else if(o1.order > o2.order) return 1;
            return 0;
        });

        for(RetrofitModel.Plot plot : plotList)
        {
            if(plot.commenting.comment == null) continue;

            RetrofitModel.CommentBrief comment = plot.commenting.comment;
            String[] temp1 = comment.created_at.split("T");
            String[] temp2 = temp1[0].split("-");
            String[] temp3 = temp1[1].split(":");

            String nowtime = temp2[1] + "월 " + temp2[2] + "일 " + temp3[0] + ":" + temp3[1];

            adp_event.addItem(getResources().getDrawable(MySettings.ARR_CAST_COLOR[plot.color]), comment.owner.name, nowtime, comment.sound);
        }

        adp_event.notifyDataSetChanged();

        lv_event.setAdapter(adp_event);

        if(is_first) {

            // 배경에 넣어주기
            tv_script.setText(boardDetail.get_all_plots);
        }
    }

    private void combineAllComments()
    {
        List<String> soundPathList = new ArrayList<>();

        int count = 0;

        Collections.sort(plotList, (o1, o2) -> {
            if(o1.order < o2.order) return -1;
            else if(o1.order > o2.order) return 1;
            return 0;
        });

        RetrofitModel.CommentBrief[] arrComment = new RetrofitModel.CommentBrief[50];

        for(RetrofitModel.Plot plot : plotList)
            arrComment[plot.order-1] = plot.commenting.comment;

        for(RetrofitModel.CommentBrief comment : arrComment)
        {
            if(comment == null || comment.sound == null) continue;

            try {
                URLConnection conn = new URL(comment.sound).openConnection();

                InputStream is = conn.getInputStream();

                OutputStream outstream = new FileOutputStream(new File(
                        Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + "sound" + count + ".mp3"));
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);
                }
                outstream.close();

                soundPathList.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "sound" + count++ + ".mp3");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String merge_filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/merge";

        mergeMediaFiles(true, soundPathList, merge_filename + ".mp3");

    }


    @OnClick({R.id.btn_back, R.id.btn_big_heart, R.id.btn_gather})
    public void onClick(View v) {

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
        else if(v.getId() == btn_gather.getId())
        {
            new Thread() {
                public void run() {
                    combineAllComments();

                    Bundle bun = new Bundle();

                    Message msg = mergeHandler.obtainMessage();
                    msg.setData(bun);
                    mergeHandler.sendMessage(msg);

                }
            }.start();
        }
    }

}

