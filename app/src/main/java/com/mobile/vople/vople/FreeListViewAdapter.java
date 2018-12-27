package com.mobile.vople.vople;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FreeListViewAdapter extends BaseAdapter {

    MediaPlayer mediaPlayer;

    private Context context;

    private List<FreeListViewItem> listViewItemList;

    public FreeListViewAdapter() {
        listViewItemList = new ArrayList<FreeListViewItem>();
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        this.context = context;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_free, parent, false);
        }

        ImageView profileImageView = (ImageView) convertView.findViewById(R.id.iv_profile) ;
        TextView nicknameTextView = (TextView) convertView.findViewById(R.id.tv_title) ;
        TextView nowtimeTextView = (TextView) convertView.findViewById(R.id.tv_nowtime) ;
        Button btn_play = (Button) convertView.findViewById(R.id.btn_play);

        btn_play.setTag(true);

        FreeListViewItem listViewItem = listViewItemList.get(pos);

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                boolean state = (boolean)btn_play.getTag();
                if(state)
                {
                    btn_play.setBackgroundResource(R.drawable.button_play_stop);
                    //녹음본 시작
                    try {
                        playAudio(listViewItemList.get(position).getsSoundUrl());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    btn_play.setBackgroundResource(R.drawable.event_recording_start);
                    if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }

                btn_play.setTag(!state);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = (boolean)btn_play.getTag();
                if(state)
                {
                    btn_play.setBackgroundResource(R.drawable.button_play_stop);
                    //녹음본 시작
                    try {
                        playAudio(listViewItemList.get(position).getsSoundUrl());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    btn_play.setBackgroundResource(R.drawable.event_recording_start);
                    if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }

                btn_play.setTag(!state);
            }
        };

        // 아이템 내 각 위젯에 데이터 반영
        profileImageView.setImageDrawable(listViewItem.getProfile());
        nicknameTextView.setText(listViewItem.getNickName());
        nowtimeTextView.setText(listViewItem.getNowTime());
        btn_play.setOnClickListener(listener);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable profile, String nickname, String nowtime, String sound_url) {
        FreeListViewItem item = new FreeListViewItem(profile, nickname, nowtime, sound_url);

        listViewItemList.add(item);
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

    public void clear(){
        listViewItemList.clear();
    }
}
