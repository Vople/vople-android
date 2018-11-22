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

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class FreeActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;

    private FreeListViewAdapter adapter;
    private int i_big_heart;
    private Button btn_back;
    private Button btn_big_heart;
    private Button btn_record;
    private Button btn_cancel;
    private Button btn_send;
    private Date date;
    private MediaPlayer mediaPlayer;

    MediaRecorder recorder = null;

    private Retrofit retrofit;

    private int roomID;
    private String roomTitle;

    private String sound_path = "/recorder";

    private TextView tv_room_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);

        initialize();

        Intent get_intent = getIntent();

        if(get_intent == null) finish();

        roomID = get_intent.getIntExtra("RoomID", -1);

        if(roomID == -1) finish();

        roomTitle = get_intent.getStringExtra("RoomTitle");

        tv_room_title.setText(roomTitle);

        adapter = new FreeListViewAdapter();

        listView.setAdapter(adapter);

        VopleServiceApi.boardDetail service = retrofit.create(VopleServiceApi.boardDetail.class);

        Call<RetrofitModel.BoardDetail> repos = service.repoContributors(roomID);

        repos.enqueue(new Callback<RetrofitModel.BoardDetail>() {
            @Override
            public void onResponse(Call<RetrofitModel.BoardDetail> call, Response<RetrofitModel.BoardDetail> response) {
                if(response.code() == 200)
                {
                    for(RetrofitModel.CommentBrief comment : response.body().comments) {
                        String[] temp1 = comment.created_at.split("T");
                        String[] temp2 = temp1[0].split("-");
                        String[] temp3 = temp1[1].split(":");

                        String nowtime = temp2[1] + "월 " + temp2[2] + "일 " + temp3[0] + ":" + temp3[1];

                        adapter.addItem(null, comment.owner.name, nowtime, comment.sound);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetrofitModel.BoardDetail> call, Throwable t) {

            }
        });

    }

    @Override
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
                //long start = System.currentTimeMillis();
            } else {
                btn_record.setBackgroundResource(R.drawable.event_unpress_record_button);
                stopRec();
                //long end = System.currentTimeMillis();
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
        }
    }

    public void startRec() {

        try {
            recorder = new MediaRecorder();

            File file = Environment.getExternalStorageDirectory();

//갤럭시 S4기준으로 /storage/emulated/0/의 경로를 갖고 시작한다.

            String path = file.getAbsolutePath() + sound_path + "record.mp3";

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

//첫번째로 어떤 것으로 녹음할것인가를 설정한다. 마이크로 녹음을 할것이기에 MIC로 설정한다.

            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

//이것은 파일타입을 설정한다. 녹음파일의경우 3gp로해야 용량도 작고 효율적인 녹음기를 개발할 수있다.

            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

//이것은 코덱을 설정하는 것이라고 생각하면된다.

            recorder.setOutputFile(path);

//저장될 파일을 저장한뒤

            recorder.prepare();

            recorder.start();

//시작하면된다.

            Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show();

        } catch (IllegalStateException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        } catch (IOException e) {

// TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    public void stopRec() {

        recorder.stop();

//멈추는 것이다.

        recorder.release();

        recorder = null;

        Toast.makeText(this, "녹음 종료", Toast.LENGTH_SHORT).show();

    }

    private void initialize()
    {
        i_big_heart = 0;
        btn_back = findViewById(R.id.btn_back);
        btn_big_heart = findViewById(R.id.btn_big_heart);
        btn_record = findViewById(R.id.btn_record);
        btn_send = findViewById(R.id.btn_send);
        tv_room_title = findViewById(R.id.tv_room_title);

        btn_record = findViewById(R.id.btn_record);
        btn_send = findViewById(R.id.btn_send);

        listView = findViewById(R.id.lv_event);

        btn_record.setTag(true);

        btn_record.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        retrofit = RetrofitInstance.getInstance(getApplicationContext());
    }


    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void sendAudioFile() throws Exception
    {
        File file = new File(Environment.getExternalStorageDirectory() + sound_path + "record.mp3");

        Retrofit retrofit = RetrofitInstance.getInstance(getApplicationContext());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("audio/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);

        VopleServiceApi.commentOnBoard service = retrofit.create(VopleServiceApi.commentOnBoard.class);

        Call<ResponseBody> call = service.upload(this.roomID, requestFile, -1);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Code : " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
