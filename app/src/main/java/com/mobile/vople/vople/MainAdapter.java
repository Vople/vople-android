package com.mobile.vople.vople;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.vople.vople.item.RoomBreifItem;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainAdapter extends BaseAdapter {

    private List<RoomBreifItem> roomList;

    public MainAdapter()
    {
        roomList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.main_list, null);

        TextView textView = (TextView) convertView.findViewById(R.id.title_text);
        TextView Script = (TextView) convertView.findViewById(R.id.ScriptKind);

        ImageView RoomType = (ImageView) convertView.findViewById(R.id.RoomKind);

        RoomBreifItem item = roomList.get(position);

        if(item.getTitle() != null)
            textView.setText(item.getTitle());
        if(item.getScript_title() != null)
            Script.setText(item.getScript_title());

        if (item.getRoomType() == 0){
            RoomType.setImageResource(R.drawable.mission);
        }
        if (item.getRoomType() == 1){
            RoomType.setImageResource(R.drawable.situation);
        }

        return convertView;
    }

    public void addItem(int roomID, String title, String num, int roomType, String kindOfScript)
    {
        roomList.add(new RoomBreifItem(roomID, title, num, roomType, kindOfScript));
    }
}
