package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class ListOrCreate extends AppCompatActivity {

    private DrawerLayout LOCDrawer;
    private ActionBarDrawerToggle NavToggle_LOC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_or_create);

        //Nav
        LOCDrawer = (DrawerLayout) findViewById(R.id.LOC);
        NavToggle_LOC = new ActionBarDrawerToggle(this, LOCDrawer, R.string.open, R.string.close);

        LOCDrawer.addDrawerListener(NavToggle_LOC);
        NavToggle_LOC.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView NavlistView_LOC = (ListView) findViewById(R.id.Nav_ListView);

        NavAdapter navAdapter = new NavAdapter();
        NavlistView_LOC.setAdapter(navAdapter);

        //pop up
        final Dialog CreateRoomDialog = new Dialog(this);
        final Dialog SelectDate = new Dialog(this);

        CreateRoomDialog.setContentView(R.layout.createroom_pop);
        SelectDate.setContentView(R.layout.select_date);

        Button RoomListButton = (Button) findViewById(R.id.ViewRoomList);
        final Button CreateRoomButton = (Button) findViewById(R.id.CreateRoom);
        Button ClosePop = (Button) CreateRoomDialog.findViewById(R.id.Cancle);
        Button ChooseDate = (Button) CreateRoomDialog.findViewById(R.id.Fin_Date);
        RadioButton Mission = (RadioButton) CreateRoomDialog.findViewById(R.id.MissionRoom);
        RadioButton Situation = (RadioButton) CreateRoomDialog.findViewById(R.id.SituationRoom);

        //Date Min, Max Setting
        DatePicker DatePick = (DatePicker) SelectDate.findViewById(R.id.datepicking);
        int year = DatePick.getYear();
        int month = DatePick.getMonth();
        int day = DatePick.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        DatePick.setMinDate(calendar.getTimeInMillis());
        calendar.set(year, month, day + 7);
        DatePick.setMaxDate(calendar.getTimeInMillis());

        final TextView EndDate = (TextView) CreateRoomDialog.findViewById(R.id.EndDateOfRoom);

        CreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRoomDialog.show();
            }
        });

        RoomListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RoomListIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(RoomListIntent);
            }
        });

        ClosePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRoomDialog.dismiss();
            }
        });

        Mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Script_Text = (TextView) CreateRoomDialog.findViewById(R.id.ScriptText);
                Spinner Script_Spinner = (Spinner) CreateRoomDialog.findViewById(R.id.PlayScript);
                Script_Text.setVisibility(View.GONE);
                Script_Spinner.setVisibility(View.GONE);
            }
        });

        Situation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Script_Text = (TextView) CreateRoomDialog.findViewById(R.id.ScriptText);
                Spinner Script_Spinner = (Spinner) CreateRoomDialog.findViewById(R.id.PlayScript);
                Script_Text.setVisibility(View.VISIBLE);
                Script_Spinner.setVisibility(View.VISIBLE);
            }
        });

        ChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate.show();
            }
        });

        DatePick.init(DatePick.getYear(), DatePick.getMonth(), DatePick.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EndDate.setText(String.format("%d년%d월%d일", year, monthOfYear + 1, dayOfMonth));
                SelectDate.dismiss();
            }
        });
    }


    //Nav
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NavToggle_LOC.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
