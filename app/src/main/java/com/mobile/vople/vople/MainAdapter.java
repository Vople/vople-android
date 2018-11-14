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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainAdapter extends BaseAdapter {

    public ArrayList<Integer> RoomID = new ArrayList<Integer>();
    public ArrayList<String> Title = new ArrayList<String >();
    public ArrayList<String> Num_List = new ArrayList<String>();
    public ArrayList<Integer> RoomType_List = new ArrayList<Integer>();
    public ArrayList<String> KindOfScript = new ArrayList<String>();

    public String getScriptInformation(int position) {
        return KindOfScript.get(position);
    }

    public  int getRoomType(int position) {
        return RoomType_List.get(position);
    }

    @Override
    public int getCount() {
        return Title.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
        TextView Like = (TextView) convertView.findViewById(R.id.NumCount);
        TextView Script = (TextView) convertView.findViewById(R.id.ScriptKind);

        ImageView RoomType = (ImageView) convertView.findViewById(R.id.RoomKind);

        textView.setText(Title.get(position));
        Like.setText(Num_List.get(position));
        Script.setText(KindOfScript.get(position));

        if (RoomType_List.get(position) == 0){
            RoomType.setImageResource(R.drawable.mission);
        }
        if (RoomType_List.get(position) == 1){
            RoomType.setImageResource(R.drawable.situation);
        }

        return convertView;
    }
}
