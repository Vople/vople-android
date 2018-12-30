package com.mobile.vople.vople;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FollowingListViewAdapter extends BaseAdapter{

    private ArrayList<FollowingListViewItem> listViewItemList = new ArrayList<FollowingListViewItem>() ;

    public FollowingListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_following, parent, false);
        }

        ImageView profileImageView = (ImageView) convertView.findViewById(R.id.iv_following_profile) ;
        TextView nicknameTextView = (TextView) convertView.findViewById(R.id.tv_title) ;
        Button btn_follower = (Button) convertView.findViewById(R.id.btn_follower);
        Button btn_following = (Button) convertView.findViewById(R.id.btn_following);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        FollowingListViewItem listViewItem = listViewItemList.get(pos);

        // 아이템 내 각 위젯에 데이터 반영
        profileImageView.setImageDrawable(listViewItem.getProfile());
        nicknameTextView.setText(listViewItem.getNickName());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(Drawable profile, String nickname) {
        FollowingListViewItem item = new FollowingListViewItem(profile, nickname);

        listViewItemList.add(item);
    }
}
