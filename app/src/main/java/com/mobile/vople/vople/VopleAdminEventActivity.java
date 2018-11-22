package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.vople.vople.item.RoomBreifItem;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VopleAdminEventActivity extends AppCompatActivity {

    private DrawerLayout AdminEventDrawer;
    private ActionBarDrawerToggle NavToggle;

    private Button btn_back;

    private VopleAdminAdapter adapter;

    private Retrofit retrofit;

    private Dialog enterRoom;

    ListView lv_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vople_admin_event);

        AdminEventDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        NavToggle = new ActionBarDrawerToggle(this, AdminEventDrawer, R.string.open, R.string.close);
        AdminEventDrawer.addDrawerListener(NavToggle);
        NavToggle.syncState();

        ListView NavlistView = (ListView) findViewById(R.id.Nav_ListView);

        lv_event = (ListView) findViewById(R.id.lv_event);

        btn_back = (Button) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterRoom = new Dialog(this);

        enterRoom.setContentView(R.layout.enter_room_pop);


        retrofit = MyRetrofit.getInstance().getRetrofit();
        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        adapter = new VopleAdminAdapter();

        lv_event.setAdapter(adapter);

        final NavAdapter navAdapter = new NavAdapter();

        NavlistView.setAdapter(navAdapter);


        Retrofit retrofit = RetrofitInstance.getInstance(getApplicationContext());

        VopleServiceApi.getEventBoards service = retrofit.create(VopleServiceApi.getEventBoards.class);

        Call<List<RetrofitModel.BoardContributor>> repos = service.repoContributors();

        repos.enqueue(new Callback<List<RetrofitModel.BoardContributor>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.BoardContributor>> call, Response<List<RetrofitModel.BoardContributor>> response) {
                if(response.code() == 200)
                {
                    for(RetrofitModel.BoardContributor board : response.body())
                    {
                        if(board.script != null)
                            adapter.addItem(board.id, board.title, "3", board.mode, board.script.title);
                        else
                            adapter.addItem(board.id, board.title, "3", board.mode, null);
                    }

                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(getApplicationContext(), "Response code : " + response.code() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<RetrofitModel.BoardContributor>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NavToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showEnterRoomDialog(int position)
    {
        RoomBreifItem item = (RoomBreifItem) adapter.getItem(position);

        TextView ScriptContentByRole = (TextView) enterRoom.findViewById(R.id.ScriptContent_ByRole);

        TextView tv_script = (TextView) enterRoom.findViewById(R.id.SC);
        LinearLayout ContentOfSC = (LinearLayout) enterRoom.findViewById(R.id.ContentOfScript);
        Spinner sp_role = (Spinner) enterRoom.findViewById(R.id.MyRole);
        Button btn_enter = (Button) enterRoom.findViewById(R.id.btn_enter);

        if (item.getRoomType() == 0){
            tv_script.setVisibility(View.INVISIBLE);
            ContentOfSC.setVisibility(View.INVISIBLE);
            sp_role.setVisibility(View.INVISIBLE);

            enterRoom.show();

            return;
        }
        if (item.getRoomType() == 1){
            tv_script.setVisibility(View.VISIBLE);
            ContentOfSC.setVisibility(View.VISIBLE);
            sp_role.setVisibility(View.VISIBLE);
        }

        if(item.getScript_title() != null)
            tv_script.setText(item.getScript_title());

        VopleServiceApi.quilifyJoinRoom service = retrofit.create(VopleServiceApi.quilifyJoinRoom.class);

        final Call<RetrofitModel.Roll_Brief> repos = service.repoContributors(item.getRoomID());

        repos.enqueue(new Callback<RetrofitModel.Roll_Brief>() {
            @Override
            public void onResponse(Call<RetrofitModel.Roll_Brief> call, Response<RetrofitModel.Roll_Brief> response) {
                if(response.code() == 200)
                {
                    ArrayList<String> list = (ArrayList<String>) response.body().rolls;

                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, list);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_role.setAdapter(adp1);
                }
                else if(response.code() == 202)
                {
                    VopleServiceApi.joinAlreadyRegistedRoom innerService = retrofit.create(VopleServiceApi.joinAlreadyRegistedRoom.class);

                    final Call<RetrofitModel.Cast> innerRepos = innerService.repoContributors(item.getRoomID());

                    innerRepos.enqueue(new Callback<RetrofitModel.Cast>() {
                        @Override
                        public void onResponse(Call<RetrofitModel.Cast> call, Response<RetrofitModel.Cast> response) {
                            if(response.code() == 200)
                            {
                                Intent intent = new Intent(VopleAdminEventActivity.this, EventActivity.class);
                                intent.putExtra("RoomID", item.getRoomID());
                                startActivity(intent);
                            }
                            else if(response.code() == 202)
                            {
                                Intent intent = new Intent(VopleAdminEventActivity.this, EventActivity.class);
                                intent.putExtra("RoomID", item.getRoomID());
                                Gson gson = new Gson();
                                String cast = gson.toJson(response.body());
                                intent.putExtra("Cast", cast);
                                startActivity(intent);
                            }
                            else if(response.code() == 204)
                            {
                                Toast.makeText(VopleAdminEventActivity.this, "This room is already full", Toast.LENGTH_SHORT).show();
                            }
                            enterRoom.dismiss();
                        }

                        @Override
                        public void onFailure(Call<RetrofitModel.Cast> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RetrofitModel.Roll_Brief> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roll_name = "Default Roll Name";

                if(item.getRoomType() == 1){
                    if(sp_role.getAdapter() != null && sp_role.getAdapter().getCount() > 0)
                    {
                        roll_name = sp_role.getSelectedItem().toString();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "방이 이미 꽉 찼거나 입장할 수 없는 방입니다.", Toast.LENGTH_SHORT).show();
                        enterRoom.dismiss();
                    }
                }

                VopleServiceApi.joinRoom service = retrofit.create(VopleServiceApi.joinRoom.class);

                final Call<RetrofitModel.Casting> repos = service.repoContributors(item.getRoomID(), roll_name);

                repos.enqueue(new Callback<RetrofitModel.Casting>() {
                    @Override
                    public void onResponse(Call<RetrofitModel.Casting> call, Response<RetrofitModel.Casting> response) {
                        if(response.code() == 200)
                        {
                            Intent intent = new Intent(VopleAdminEventActivity.this, EventActivity.class);
                            intent.putExtra("RoomID", item.getRoomID());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitModel.Casting> call, Throwable t) {
                        Toast.makeText(VopleAdminEventActivity.this, "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        enterRoom.show();
    }

}
