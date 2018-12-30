package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class FollowingActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private Button btn_back_following;
    private Button btn_follower;
    private Button btn_following;

    private FollowingListViewAdapter adapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        listView = (ListView) findViewById(R.id.lv_following);

        adapter = new FollowingListViewAdapter();

        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");
        adapter.addItem(null, "Kim");

        listView.setAdapter(adapter);

        btn_back_following = findViewById(R.id.btn_back);
        btn_follower = findViewById(R.id.btn_follower);
        btn_following = findViewById(R.id.btn_following);

        btn_back_following.setOnClickListener(this);
        btn_follower.setOnClickListener(this);
        btn_following.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == btn_back_following.getId())
        {
            finish();
        }
        else if (v.getId() == btn_following.getId())
        {
            //팔로잉만 보이게
            btn_following.setBackgroundResource(R.drawable.following_click);
            btn_follower.setBackgroundResource(R.drawable.following_unclick);
        }
        else if(v.getId() == btn_follower.getId())
        {
            //팔로워만 보이게
            btn_follower.setBackgroundResource(R.drawable.following_click);
            btn_following.setBackgroundResource(R.drawable.following_unclick);
        }
    }
}
