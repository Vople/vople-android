package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.model.MyRetrofit;

import org.w3c.dom.Text;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout MainDrawer;
    private ActionBarDrawerToggle NavToggle;

    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nav & CustomList
        Button NavButton = (Button) findViewById(R.id.NavMain);
        MainDrawer = (DrawerLayout) findViewById(R.id.main_drawer);

        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainDrawer.openDrawer(Gravity.START);
            }
        });

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

        //Server RoomList

        retrofit = MyRetrofit.getInstance().getRetrofit();
        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        VopleServiceApi.boards CallList = retrofit.create(VopleServiceApi.boards.class);

        final Call<List<RetrofitModel.BoardContributor>> repos = CallList.repoContributors();

        repos.enqueue(new Callback<List<RetrofitModel.BoardContributor>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.BoardContributor>> call, Response<List<RetrofitModel.BoardContributor>> response) {
                if (response.code() == 200) {

                    for (Object object : response.body()) {
                        RetrofitModel.BoardContributor element = (RetrofitModel.BoardContributor) object;
                        mainAdapter.Title.add(element.title);
                        //mainAdapter.Num_List.add(String.format("%d", element.board_likes));
                        mainAdapter.RoomType_List.add(element.mode);
                        //mainAdapter.KindOfScript.add(element.script);
                        mainAdapter.RoomID.add(element.id);
                    }

                    Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Response.code = " + String.valueOf(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RetrofitModel.BoardContributor>> call, Throwable t) {
                Log.d("TAG", t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), "Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}