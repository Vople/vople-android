package com.mobile.vople.vople;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mainDrawer;
    private ActionBarDrawerToggle NavToggle;

    private Retrofit retrofit;

    private Button navButton;

    private ListView mainListView, navlistView;

    private MainAdapter mainAdapter;
    private NavAdapter navAdapter;

    private Dialog enterRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEnterRoomDialog(position);
        }});

        VopleServiceApi.boards CallList = retrofit.create(VopleServiceApi.boards.class);

        final Call<List<RetrofitModel.BoardContributor>> repos = CallList.repoContributors();

        repos.enqueue(new Callback<List<RetrofitModel.BoardContributor>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.BoardContributor>> call, Response<List<RetrofitModel.BoardContributor>> response) {
                Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                        Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {

                    for (RetrofitModel.BoardContributor object : response.body()) {

                        if(object.script == null)
                            mainAdapter.addItem(object.id, object.title, null, object.mode, null);
                        else
                            mainAdapter.addItem(object.id, object.title, null, object.mode, object.script.title);
                    }

                    mainAdapter.notifyDataSetChanged();

                } else if(response.code() == 202){
                    for (RetrofitModel.BoardContributor object : response.body()) {

                        if(object.script == null)
                            mainAdapter.addItem(object.id, object.title, null, object.mode, null);
                        else
                            mainAdapter.addItem(object.id, object.title, null, object.mode, object.script.title);
                    }
                }
                Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<RetrofitModel.BoardContributor>> call, Throwable t) {
                Log.d("TAG", t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), "Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEnterRoomDialog(int position)
    {
        RoomBreifItem item = (RoomBreifItem) mainAdapter.getItem(position);

        TextView ScriptContentByRole = (TextView) enterRoom.findViewById(R.id.ScriptContent_ByRole);

        TextView tv_script = (TextView) enterRoom.findViewById(R.id.SC);
        LinearLayout ContentOfSC = (LinearLayout) enterRoom.findViewById(R.id.ContentOfScript);
        Spinner sp_role = (Spinner) enterRoom.findViewById(R.id.MyRole);
        Button btn_enter = (Button) enterRoom.findViewById(R.id.btn_enter);



        if (item.getRoomType() == 0){
            tv_script.setVisibility(View.INVISIBLE);
            ContentOfSC.setVisibility(View.INVISIBLE);
            sp_role.setVisibility(View.INVISIBLE);

            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VopleServiceApi.joinFreeRoom service = retrofit.create(VopleServiceApi.joinFreeRoom.class);

                    Call<ResponseBody> repos = service.repoContributors(item.getRoomID(), "sadlfjlxzcvnqwenx,mvnsdlkj");
                    repos.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200)
                            {
                                Intent intent = new Intent(MainActivity.this, FreeActivity.class);
                                intent.putExtra("RoomID", item.getRoomID());
                                intent.putExtra("RoomTitle", item.getTitle());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

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
                Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                        Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                    Toast.LENGTH_SHORT).show();
                            if(response.code() == 200)
                            {
                                // Free Mode
                                Intent intent = new Intent(MainActivity.this, FreeActivity.class);
                                intent.putExtra("RoomID", item.getRoomID());
                                intent.putExtra("RoomTitle", item.getTitle());
                                startActivity(intent);
                            }
                            else if(response.code() == 202)
                            {
                                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                                intent.putExtra("RoomID", item.getRoomID());
                                Gson gson = new Gson();
                                String cast = gson.toJson(response.body());
                                intent.putExtra("Cast", cast);
                                intent.putExtra("RoomTitle", item.getTitle());
                                startActivity(intent);
                            }
                            else if(response.code() == 204)
                            {
                                Toast.makeText(MainActivity.this, "This room is already full", Toast.LENGTH_SHORT).show();
                            }
                            enterRoom.dismiss();
                        }

                        @Override
                        public void onFailure(Call<RetrofitModel.Cast> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                                Toast.LENGTH_SHORT).show();

                        if(response.code() == 200)
                        {
                            Intent intent = new Intent(MainActivity.this, EventActivity.class);
                            intent.putExtra("RoomID", item.getRoomID());
                            intent.putExtra("RoomTitle", item.getTitle());
                            startActivity(intent);
                        }
                        else if(response.code() == 202)
                        {
                            // Free Mode
                            Intent intent = new Intent(MainActivity.this, FreeActivity.class);
                            intent.putExtra("RoomID", item.getRoomID());
                            intent.putExtra("RoomTitle", item.getTitle());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitModel.Casting> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });




        enterRoom.show();
    }

    private void initialize()
    {

        //Nav & CustomList
        navButton = (Button) findViewById(R.id.NavMain);
        mainDrawer = (DrawerLayout) findViewById(R.id.main_drawer);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDrawer.openDrawer(Gravity.START);
            }
        });

        mainListView = (ListView) findViewById(R.id.main_list);
        navlistView = (ListView) findViewById(R.id.Nav_ListView);

        Button btn_logout = findViewById(R.id.NavBody).findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.getInstance(MainActivity.this).remove("IS_AUTO_LOGIN");
                SharedPreference.getInstance(MainActivity.this).put("IS_AUTO_LOGIN", "No");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                if(ListOrCreateActivity.mInstance != null)
                    ListOrCreateActivity.mInstance.finish();
            }
        });

        mainAdapter = new MainAdapter();
        mainListView.setAdapter(mainAdapter);


        navAdapter = new NavAdapter();
        navlistView.setAdapter(navAdapter);

        //pop up

        enterRoom = new Dialog(this);

        enterRoom.setContentView(R.layout.enter_room_pop);


        retrofit = MyRetrofit.getInstance().getRetrofit();
        retrofit = RetrofitInstance.getInstance(getApplicationContext());
    }

}