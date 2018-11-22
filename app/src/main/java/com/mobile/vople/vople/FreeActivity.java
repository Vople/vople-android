package com.mobile.vople.vople;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FreeActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;

    private int i_big_heart;
    private Button btn_back;
    private Button btn_big_heart;
    private Button btn_record;
    private Button btn_cancel;
    private Button btn_send;
    private Date date;

    MediaRecorder recorder = null;

    private String sound_path = "/recorder";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        i_big_heart = 0;
        btn_back = findViewById(R.id.btn_back);
        btn_big_heart = findViewById(R.id.btn_big_heart);
        btn_record = findViewById(R.id.btn_record);
        btn_send = findViewById(R.id.btn_send);
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
                //startRec(position);
                //long start = System.currentTimeMillis();
            } else {
                btn_record.setBackgroundResource(R.drawable.event_unpress_record_button);
                stopRec();
                //long end = System.currentTimeMillis();
            }

            btn_record.setTag(!state);
        }

    }

    public void startRec(int position) {

        try {
            recorder = new MediaRecorder();

            File file = Environment.getExternalStorageDirectory();

//갤럭시 S4기준으로 /storage/emulated/0/의 경로를 갖고 시작한다.

            String path = file.getAbsolutePath() + sound_path + String.valueOf(position) + ".mp3";

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

}
