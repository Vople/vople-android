package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    private ListView listView;

    private ListViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        listView = (ListView) findViewById(R.id.lv_event);

        adapter = new ListViewAdapter();

        adapter.addItem(null, "Evan", "04:11", "2018-09-23 09:30");

        listView.setAdapter(adapter);
    }
}
