package com.mobile.vople.vople;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SquareListViewAdapter extends BaseAdapter {
    private ArrayList<SquareListViewItem> listViewItemList = new ArrayList<SquareListViewItem>();
    private Context context;

    public SquareListViewAdapter() {

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

    public void addItem(String title, int id, int member_restriction) {
        SquareListViewItem item = new SquareListViewItem(title,id, member_restriction);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_square, parent, false);
        }

        TextView tv_title = convertView.findViewById(R.id.tv_title);

        SquareListViewItem listViewItem = listViewItemList.get(pos);

        tv_title.setText(listViewItem.getsTitle());


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == tv_title.getId()) {
                    Intent intent = new Intent(context, ScriptInformationActivity.class);
                    intent.putExtra("script_id", listViewItem.getId());
                    context.startActivity(intent);
                }
            }
        };

        tv_title.setOnClickListener(listener);

        return convertView;
    }
}
