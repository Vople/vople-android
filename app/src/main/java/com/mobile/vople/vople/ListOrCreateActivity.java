package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListOrCreateActivity extends AppCompatActivity {

    private DrawerLayout LOCDrawer;

    private Retrofit retrofit;

    private List<RetrofitModel.Script> scripts;

    public static ListOrCreateActivity mInstance;

    private boolean is_shutdown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_or_create);

        mInstance = this;

        retrofit = MyRetrofit.getInstance().getRetrofit();
        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        setNavigation();

        //pop up
        final Dialog CreateRoomDialog = new Dialog(this);

        CreateRoomDialog.setContentView(R.layout.createroom_pop);

        Button RoomListButton = (Button) findViewById(R.id.ViewRoomList);
        final Button CreateRoomButton = (Button) findViewById(R.id.CreateRoom);
        Button ClosePop = (Button) CreateRoomDialog.findViewById(R.id.Cancle);
        Button CreateRoomSubmit = (Button) CreateRoomDialog.findViewById(R.id.Submit);
        RadioButton Mission = (RadioButton) CreateRoomDialog.findViewById(R.id.MissionRoom);
        RadioButton Situation = (RadioButton) CreateRoomDialog.findViewById(R.id.SituationRoom);

        Button btn_logout = findViewById(R.id.NavBody).findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.getInstance(ListOrCreateActivity.this).remove("IS_AUTO_LOGIN");
                SharedPreference.getInstance(ListOrCreateActivity.this).put("IS_AUTO_LOGIN", "No");
                Intent intent = new Intent(ListOrCreateActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                is_shutdown = false;
            }
        });
        Mission.setChecked(true);

        scripts = new ArrayList<>();

        VopleServiceApi.listAllScripts service = retrofit.create(VopleServiceApi.listAllScripts.class);

        Call<List<RetrofitModel.Script>> repos = service.repoContributors();

        repos.enqueue(new Callback<List<RetrofitModel.Script>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.Script>> call, Response<List<RetrofitModel.Script>> response) {

                if(response.code() == 200)
                {
                    List<RetrofitModel.Script> result = response.body();

                    for(RetrofitModel.Script script : result)
                        scripts.add(script);
                }
            }

            @Override
            public void onFailure(Call<List<RetrofitModel.Script>> call, Throwable t) {
                Toast.makeText(ListOrCreateActivity.this, "Faield to connect!", Toast.LENGTH_SHORT).show();
            }
        });

        CreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner sp = (CreateRoomDialog).findViewById(R.id.PlayScript);

                ArrayList<String> list = new ArrayList<>();

                for(RetrofitModel.Script script : scripts)
                    list.add(script.title);

                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, list);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adp1);

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
                ConstraintLayout NoShowOnMission = (ConstraintLayout) CreateRoomDialog.findViewById(R.id.constraintLayout);
                if(NoShowOnMission.getVisibility() == View.VISIBLE)
                    NoShowOnMission.setVisibility(View.GONE);
            }
        });

        Situation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout NoShowOnMission = (ConstraintLayout) CreateRoomDialog.findViewById(R.id.constraintLayout);
                if(NoShowOnMission.getVisibility() != View.VISIBLE)
                    NoShowOnMission.setVisibility(View.VISIBLE);

            }
        });

        //Server CreateRoom

        CreateRoomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VopleServiceApi.create_board CreateRoom_Server = retrofit.create(VopleServiceApi.create_board.class);
                EditText TitleText = (EditText) CreateRoomDialog.findViewById(R.id.RoomName);
                final RadioGroup RG = (RadioGroup) CreateRoomDialog.findViewById(R.id.radioGroup);
                TextView PeopleNum = (TextView) CreateRoomDialog.findViewById(R.id.MaxPeople);
                Spinner Script_spinner = (Spinner) CreateRoomDialog.findViewById(R.id.PlayScript);

                int roomType = 0;
                String titleName = TitleText.getText().toString();

                int member_restriction = 1;

                if (RG.getCheckedRadioButtonId() == R.id.SituationRoom) {
                    roomType = 1;
                } else if (RG.getCheckedRadioButtonId() == R.id.MissionRoom){
                    roomType = 0;
                }

                String script_title = (String)Script_spinner.getSelectedItem();

                int script_id = -1;

                for(RetrofitModel.Script script : scripts)
                {
                    if(script_title.equals(script.title)) {
                        script_id = script.id;
                        member_restriction = script.member_restriction;
                        break;
                    }
                }

                VopleServiceApi.create_board service = retrofit.create(VopleServiceApi.create_board.class);

                if(roomType == 0) script_id = -1;

                //TitleName PlayScriptName Fin_date Roomtype_Int MaxPeople
                final Call<RetrofitModel.BoardContributor> repos = service.repoContributors(titleName, script_title, roomType, script_id);
                repos.enqueue(new Callback<RetrofitModel.BoardContributor>() {
                    @Override
                    public void onResponse(Call<RetrofitModel.BoardContributor> call, Response<RetrofitModel.BoardContributor> response) {
                        if (response.code() == 201) {
                            CreateRoomDialog.dismiss();
                            Intent roomListIntent = new Intent(getApplicationContext(), EventActivity.class);
                            roomListIntent.putExtra("RoomID", response.body().id);
                            startActivity(roomListIntent);
                            Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitModel.BoardContributor> call, Throwable t) {
                        Log.d("TAG", t.getLocalizedMessage());
                    }
                });
            }
        });

    }

    private void setNavigation()
    {
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

    }

    @Override
    protected void onDestroy() {
        if( is_shutdown && LoginActivity.instance != null) LoginActivity.instance.finish();
        super.onDestroy();
    }
}