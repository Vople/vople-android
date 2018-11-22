package com.mobile.vople.vople;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DownloadListViewAdapter extends BaseAdapter {
    private ArrayList<DownloadListViewItem> listViewItemList = new ArrayList<DownloadListViewItem>();
    private Context context;

    public DownloadListViewAdapter(){
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

    public void addItem(String title, String record) {
        DownloadListViewItem item = new DownloadListViewItem(title, record);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_download, parent, false);
        }

        TextView tv_title= (TextView) convertView.findViewById(R.id.tv_title);
        Button btn_play = (Button) convertView.findViewById(R.id.btn_play);

        DownloadListViewItem listViewItem = listViewItemList.get(pos);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btn_play.getId()) {
                    boolean state = (boolean) btn_play.getTag();

                    if (state) {
                        btn_play.setBackgroundResource(R.drawable.button_play_stop);
                        //합쳐진 녹음 파일 시작.
                    }
                    else {
                        btn_play.setBackgroundResource(R.drawable.event_recording_start);
                        //합쳐진 녹음 파일 정지.
                    }

                    btn_play.setTag(!state);
                }
            }
        };

        tv_title.setText(listViewItem.getTitle());
        btn_play.setOnClickListener(listener);

        return convertView;
    }
}
