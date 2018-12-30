package com.mobile.vople.vople.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mobile.vople.vople.ListOrCreateActivity;
import com.mobile.vople.vople.LoginActivity;
import com.mobile.vople.vople.NavAdapter;
import com.mobile.vople.vople.R;
import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.RoomBreifItem;
import com.mobile.vople.vople.server.MySharedPreferences;
import com.mobile.vople.vople.server.VopleApi;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.ui.EnterRoomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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

    @Inject
    VopleApi mVopleApi;

    private MainAdapter adp_main;
    private NavAdapter adp_nav;

    //@Inject
    MySharedPreferences mySharedPreferences;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initialize();

        VopleServiceApi.boards service_get_all_boards = mVopleApi.getRetrofit().create(VopleServiceApi.boards.class);

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
            mySharedPreferences.remove("IS_AUTO_LOGIN");
            mySharedPreferences.put("IS_AUTO_LOGIN", "No");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            if(ListOrCreateActivity.mInstance != null)
                ListOrCreateActivity.mInstance.finish();

        }
    }

}