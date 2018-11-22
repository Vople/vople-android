package com.mobile.vople.vople;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeListViewAdapter extends BaseAdapter {
    private ArrayList<NoticeListViewItem> listViewItemList = new ArrayList<NoticeListViewItem>();
    private Context context;
    ListView listView;

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

    public void addItem(String title, String nowtime) {
        NoticeListViewItem item = new NoticeListViewItem(title, nowtime);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_notice, parent, false);
        }

        TextView tv_title = convertView.findViewById(R.id.tv_title);

        NoticeListViewItem listViewItem = listViewItemList.get(pos);

        tv_title.setText(listViewItem.getTitle());

        return convertView;
    }


}
