package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout MainDrawer;
    private ActionBarDrawerToggle NavToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nav & CustomList
        MainDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        NavToggle = new ActionBarDrawerToggle(this, MainDrawer, R.string.open, R.string.close);

        MainDrawer.addDrawerListener(NavToggle);
        NavToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView MainlistView = (ListView) findViewById(R.id.main_list);
        ListView NavlistView = (ListView) findViewById(R.id.Nav_ListView);

        final MainAdapter mainAdapter = new MainAdapter();
        MainlistView.setAdapter(mainAdapter);

        final NavAdapter navAdapter = new NavAdapter();
        NavlistView.setAdapter(navAdapter);

        //pop up

        final Dialog EnterRoom = new Dialog(this);

        EnterRoom.setContentView(R.layout.enter_room_pop);

        MainlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ScriptName = mainAdapter.getScriptInformation(position); //나중에 main.get~ 으로 쓰고 변수 없애도 됨

                TextView ScriptContentByRole = (TextView) EnterRoom.findViewById(R.id.ScriptContent_ByRole);

                TextView TitleSC = (TextView) EnterRoom.findViewById(R.id.SC);
                LinearLayout ContentOfSC = (LinearLayout) EnterRoom.findViewById(R.id.ContentOfScript);
                Spinner Role = (Spinner) EnterRoom.findViewById(R.id.MyRole);
                if (mainAdapter.getRoomType(position) == 0){
                    TitleSC.setVisibility(View.INVISIBLE);
                    ContentOfSC.setVisibility(View.INVISIBLE);
                    Role.setVisibility(View.INVISIBLE);
                }
                if (mainAdapter.getRoomType(position) == 1){
                    TitleSC.setVisibility(View.VISIBLE);
                    ContentOfSC.setVisibility(View.VISIBLE);
                    Role.setVisibility(View.VISIBLE);
                }



                EnterRoom.show();
            }
        });

        //EX)
        mainAdapter.Title.add("I got eyes on that four, five, six mo...");
        mainAdapter.Like_List.add("1000");
        mainAdapter.RoomType_List.add(0);
        mainAdapter.KindOfScript.add("Na to the fla - Wu");

        mainAdapter.Title.add("Indigo we just go xuck the ops...");
        mainAdapter.Like_List.add("10000");
        mainAdapter.RoomType_List.add(1);
        mainAdapter.KindOfScript.add("Justhis, kidmilli, young b, no:el - Indigo");
    }

    //Nav
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NavToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}