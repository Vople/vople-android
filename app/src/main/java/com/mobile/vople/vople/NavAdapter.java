package com.mobile.vople.vople;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.jar.Attributes;

public class NavAdapter extends BaseAdapter {

    private int[] NAVIMAGES = {R.drawable.participate_record, R.drawable.follow, R.drawable.ranking, R.drawable.vople_present, R.drawable.information, R.drawable.download, R.drawable.setup};
    private String[] NAVNAMES = {"참여기록", "팔로우/팔로잉", "보플 랭킹", "보플이 쏜다", "공지사항/이벤트", "보관함", "설정"};

    @Override
    public int getCount() {
        return NAVNAMES.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.image_layout, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.Nav_ItemImage);
        TextView textView = (TextView) convertView.findViewById(R.id.Nav_ItemName);

        imageView.setImageResource(NAVIMAGES[position]);
        textView.setText(NAVNAMES[position]);

        return convertView;
    }
}
