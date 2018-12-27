package com.mobile.vople.vople;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FreeActivity extends BaseEventActivity{

    @BindView(R.id.lv_event)
    ListView lv_event;
    @BindView(R.id.btn_back)
    Button btn_back;
    @BindView(R.id.btn_big_heart)
    Button btn_big_heart;
    @BindView(R.id.btn_record)
    Button btn_record;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.tv_room_title)
    TextView tv_room_title;

    private int i_big_heart;

    MediaRecorder recorder = null;

    private FreeListViewAdapter adp_free_list;
    private Retrofit retrofit;

    private int roomID;
    private String roomTitle;

    private String sound_path = "/recorder";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);

        ButterKnife.bind(this);
        initialize();
        displayAllItem();

    }


    private void initialize()
    {
        i_big_heart = 0;

        btn_record.setTag(true);

        Intent get_intent = getIntent();

        if(get_intent == null) finish();

        roomID = get_intent.getIntExtra("RoomID", -1);

        if(roomID == -1) finish();

        roomTitle = get_intent.getStringExtra("RoomTitle");

        tv_room_title.setText(roomTitle);

        adp_free_list = new FreeListViewAdapter();

        lv_event.setAdapter(adp_free_list);

        retrofit = RetrofitInstance.getInstance(getApplicationContext());
    }


    private void displayAllItem()
    {
        adp_free_list.clear();

        VopleServiceApi.boardDetail service_board_detail = retrofit.create(VopleServiceApi.boardDetail.class);

        service_board_detail.repoContributors(roomID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> { return response.code() == 200 ? response.body() : null;})
                .subscribe(response -> {
                    settingBoardDetail(response);
                }, throwable -> {
                    Toast.makeText(this, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show();
                });
    }

    @OnClick({R.id.btn_record, R.id.btn_send, R.id.btn_back})
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

        else if(v.getId() == btn_record.getId()){
            boolean state = (boolean) btn_record.getTag();

            if (state) {
                btn_record.setBackgroundResource(R.drawable.event_press_record_button);
                startRec();
            } else {
                btn_record.setBackgroundResource(R.drawable.event_unpress_record_button);
                stopRec();
            }

            btn_record.setTag(!state);
        }

        else if(v.getId() == btn_send.getId())
        {
            try {
                sendAudioFile();
            } catch(FileNotFoundException e){
                Toast.makeText(this, "녹음읋 해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show();
            }
            displayAllItem();
        }
    }

    public void startRec() {

        try {
            recorder = new MediaRecorder();

            File file = Environment.getExternalStorageDirectory();

            String path = file.getAbsolutePath() + sound_path + ".mp3";

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);


            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);


            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


            recorder.setOutputFile(path);


            recorder.prepare();

            recorder.start();


            Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRec() {

        recorder.stop();



        recorder.release();

        recorder = null;

        Toast.makeText(this, "녹음 종료", Toast.LENGTH_SHORT).show();

    }

    private void sendAudioFile() throws Exception
    {
        File file = new File(Environment.getExternalStorageDirectory() + sound_path + ".mp3");

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("audio/*"), file);

        //MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);

        VopleServiceApi.commentOnBoard service = retrofit.create(VopleServiceApi.commentOnBoard.class);

        Call<ResponseBody> call = service.upload(this.roomID, requestFile, -1);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "전송완료!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void settingBoardDetail(RetrofitModel.BoardDetail response)
    {
        if(response == null)
        {
            MyUtils.makeNetworkErrorToast(this);
            return;
        }

        for(RetrofitModel.CommentBrief comment : response.comments) {
            String[] temp1 = comment.created_at.split("T");
            String[] temp2 = temp1[0].split("-");
            String[] temp3 = temp1[1].split(":");

            String nowtime = temp2[1] + "월 " + temp2[2] + "일 " + temp3[0] + ":" + temp3[1];

            adp_free_list.addItem(null, comment.owner.name, nowtime, comment.sound);
        }
        adp_free_list.notifyDataSetChanged();
    }
}
