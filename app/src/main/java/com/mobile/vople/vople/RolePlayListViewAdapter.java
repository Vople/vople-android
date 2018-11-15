package com.mobile.vople.vople;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RolePlayListViewAdapter extends BaseAdapter {

    private Context context;

    MediaRecorder recorder = null;

    private ArrayList<RolePlayListViewItem> listViewItemList = new ArrayList<RolePlayListViewItem>();

    public RolePlayListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable profile, String runtime, String script) {
        RolePlayListViewItem item = new RolePlayListViewItem(profile, runtime, script);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        this.context = context;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_role_play, parent, false);
        }

        ImageView iv_role_play_profile = (ImageView) convertView.findViewById(R.id.iv_role_play_profile);
        TextView tv_role_play_script = (TextView) convertView.findViewById(R.id.tv_role_play_script);
        TextView tv_role_play_runtime = (TextView) convertView.findViewById(R.id.tv_role_play_runtime);
        Button btn_role_play_record = (Button) convertView.findViewById(R.id.btn_rold_play_record);

        btn_role_play_record.setTag(true);

        RolePlayListViewItem listViewItem = listViewItemList.get(pos);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btn_role_play_record.getId()) {
                    boolean state = (boolean) btn_role_play_record.getTag();

                    if (state) {
                        btn_role_play_record.setBackgroundResource(R.drawable.event_press_record_button);
                        startRec();
                        //long start = System.currentTimeMillis();
                    } else {
                        btn_role_play_record.setBackgroundResource(R.drawable.event_unpress_record_button);
                        stopRec();
                        //long end = System.currentTimeMillis();
                    }

                    btn_role_play_record.setTag(!state);
                }
            }
        };

        iv_role_play_profile.setImageDrawable(listViewItem.getProfile());
        tv_role_play_script.setText(listViewItem.getScript());
        tv_role_play_runtime.setText(listViewItem.getRunTime());
        btn_role_play_record.setOnClickListener(listener);

        return convertView;
    }

    public void startRec() {

        try {
            recorder = new MediaRecorder();

            File file = Environment.getExternalStorageDirectory();

//갤럭시 S4기준으로 /storage/emulated/0/의 경로를 갖고 시작한다.

            String path = file.getAbsolutePath() + "/" + "recoder3.mp3";

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

            Toast.makeText(context, "녹음 시작", Toast.LENGTH_SHORT).show();

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

        Toast.makeText(context, "녹음 종료", Toast.LENGTH_SHORT).show();

    }
}