package com.mobile.vople.vople.main;

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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.vople.vople.SituationActivity;
import com.mobile.vople.vople.FreeActivity;
import com.mobile.vople.vople.ListOrCreateActivity;
import com.mobile.vople.vople.LoginActivity;
import com.mobile.vople.vople.NavAdapter;
import com.mobile.vople.vople.R;
import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.RoomBreifItem;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.ui.EnterRoomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.dy_main)
    DrawerLayout dy_main;
    @BindView(R.id.btn_nav)
    Button btn_nav;
    @BindView(R.id.btn_logout)
    Button btn_logout;
    @BindView(R.id.lv_main)
    ListView lv_main;
    @BindView(R.id.lv_nav)
    ListView lv_nav;

    private Retrofit retrofit;

    private MainAdapter adp_main;
    private NavAdapter adp_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initialize();

        VopleServiceApi.boards service_get_all_boards = retrofit.create(VopleServiceApi.boards.class);

        service_get_all_boards.repoContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (RetrofitModel.BoardContributor object : response) {
                        if(object.script == null) adp_main.addItem(object.id, object.title, null, object.mode, null);
                        else adp_main.addItem(object.id, object.title, null, object.mode, object.script.title);
                    }
                    adp_main.notifyDataSetChanged();
                }, throwable -> {
                    MyUtils.makeNetworkErrorToast(this);
                });
    }

    private void initialize()
    {
        adp_main = new MainAdapter();
        lv_main.setAdapter(adp_main);

        adp_nav = new NavAdapter();
        lv_nav.setAdapter(adp_nav);

        retrofit = RetrofitInstance.getInstance(getApplicationContext());

        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new EnterRoomDialog(MainActivity.this, (RoomBreifItem) adp_main.getItem(position)).show();
            }
        });

    }

    @OnClick({R.id.btn_nav, R.id.btn_logout})
    void onClick(View v)
    {
        if(v.getId() == R.id.btn_nav)
        {
            dy_main.openDrawer(Gravity.START);
        }
        else if(v.getId() == R.id.btn_logout)
        {
            SharedPreference.getInstance(MainActivity.this).remove("IS_AUTO_LOGIN");
            SharedPreference.getInstance(MainActivity.this).put("IS_AUTO_LOGIN", "No");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            if(ListOrCreateActivity.mInstance != null)
                ListOrCreateActivity.mInstance.finish();

        }
    }

}