package com.mobile.vople.vople;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavAdapter extends BaseAdapter {

    private int[] NAVIMAGES = {R.drawable.participate_record, R.drawable.follow, R.drawable.vople_present, R.drawable.information, R.drawable.download};
    private String[] NAVNAMES = {"대본신청", "대본광장", "보플이 쏜다", "공지사항", "보관함"};

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

        String title = NAVNAMES[position];

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals("보플이 쏜다"))
                {
                    Intent intent = new Intent(context, VopleAdminEventActivity.class);
                    context.startActivity(intent);
                }
                else if(title.equals("대본광장"))
                {
                    Intent intent = new Intent(context, SquareActivity.class);
                    context.startActivity(intent);
                }
                else if(title.equals("대본신청"))
                {
                    Intent intent = new Intent(context, AskScriptActivity.class);
                    context.startActivity(intent);
                }

                else if(title.equals("공지사항"))
                {
                    Intent intent = new Intent(context, NoticeActivity.class);
                    context.startActivity(intent);
                }

                else if(title.equals("보관함"))
                {
                    Intent intent = new Intent(context, DownloadActivity.class);
                    context.startActivity(intent);
                }
            }
        });


        return convertView;
    }
}
