package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListOrCreate extends AppCompatActivity {

    private DrawerLayout LOCDrawer;
    private ActionBarDrawerToggle NavToggle_LOC;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_or_create);

        //Nav
        Button NavButton = (Button) findViewById(R.id.NavLOC);
        LOCDrawer = (DrawerLayout) findViewById(R.id.LOC);

        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCDrawer.openDrawer(Gravity.START);
            }
        });

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
        Button CreateRoomSubmit = (Button) CreateRoomDialog.findViewById(R.id.Submit);
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
                LinearLayout NoShowOnMission = (LinearLayout) findViewById(R.id.SituationOnlyShow);
                NoShowOnMission.setVisibility(View.GONE);
            }
        });

        Situation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout NoShowOnMission = (LinearLayout) findViewById(R.id.SituationOnlyShow);
                NoShowOnMission.setVisibility(View.VISIBLE);
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
                EndDate.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                SelectDate.dismiss();
            }
        });

        //Server CreateRoom

        retrofit = MyRetrofit.getInstance().getRetrofit();
        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        CreateRoomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VopleServiceApi.create_a_board CreateRoom_Server = retrofit.create(VopleServiceApi.create_a_board.class);
                EditText TitleText = (EditText) CreateRoomDialog.findViewById(R.id.RoomName);
                final RadioGroup RG = (RadioGroup) CreateRoomDialog.findViewById(R.id.RoomType_RG);
                TextView PeopleNum = (TextView) CreateRoomDialog.findViewById(R.id.MaxPeople);
                Spinner Script_spinner = (Spinner) CreateRoomDialog.findViewById(R.id.PlayScript);
                TextView Fin_Date_Text = (TextView) CreateRoomDialog.findViewById(R.id.EndDateOfRoom);

                int Roomtype_Int = 0;
                Date Fin_date = new Date();
                String TitleName = TitleText.getText().toString();
                int MaxPeople = Integer.parseInt(PeopleNum.getText().toString());
                String PlayScriptName = Script_spinner.getSelectedItem().toString();
                try {
                    Fin_date = new SimpleDateFormat("yyyy-MM-dd").parse(Fin_Date_Text.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (RG.getCheckedRadioButtonId() == R.id.SituationRoom) {
                    Roomtype_Int = 1;
                } else if (RG.getCheckedRadioButtonId() == R.id.MissionRoom){
                    Roomtype_Int = 0;
                }

                //TitleName PlayScriptName Fin_date Roomtype_Int MaxPeople
                final Call<RetrofitModel.CreateBoardContributor> repos = CreateRoom_Server.repoContributors(TitleName, PlayScriptName, Fin_date, Roomtype_Int);
                repos.enqueue(new Callback<RetrofitModel.CreateBoardContributor>() {
                    @Override
                    public void onResponse(Call<RetrofitModel.CreateBoardContributor> call, Response<RetrofitModel.CreateBoardContributor> response) {
                        if (response.code() == 201) {
                            CreateRoomDialog.dismiss();
                            Intent RoomListIntent = new Intent(getApplicationContext(), EventActivity.class);
                            startActivity(RoomListIntent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitModel.CreateBoardContributor> call, Throwable t) {
                        Log.d("TAG", t.getLocalizedMessage());
                    }
                });
            }
        });

    }

}