package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mobile.vople.vople.main.MainActivity;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.ui.CreateRoomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListOrCreateActivity extends AppCompatActivity {

    public static ListOrCreateActivity mInstance;

    private boolean is_shutdown = true;

    @BindView(R.id.btn_roomList)
    Button btn_roomList;
    @BindView(R.id.btn_createRoom)
    Button btn_createRoom;
    @BindView(R.id.btn_logout)
    Button btn_logout;
    @BindView(R.id.dy_list_or_create)
    DrawerLayout dy_list_or_create;
    @BindView(R.id.btn_nav)
    Button btn_nav;
    @BindView(R.id.lv_nav)
    ListView lv_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_or_create);

        ButterKnife.bind(this);

        mInstance = this;

        setNavigation();
    }

    private void setNavigation()
    {
        NavAdapter navAdapter = new NavAdapter();
        lv_nav.setAdapter(navAdapter);
    }

    @Override
    protected void onDestroy() {
        if( is_shutdown && LoginActivity.instance != null) LoginActivity.instance.finish();
        super.onDestroy();
    }

    @OnClick({R.id.btn_createRoom, R.id.btn_roomList, R.id.btn_logout, R.id.btn_nav})
    protected void onButtonClick(View v){
        if (v.getId() == R.id.btn_createRoom)
        {
            Dialog createRoomDialog = new CreateRoomDialog(this);
            createRoomDialog.show();
        }
        else if(v.getId() == R.id.btn_roomList)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_logout)
        {
            SharedPreference.getInstance(ListOrCreateActivity.this).remove("IS_AUTO_LOGIN");
            SharedPreference.getInstance(ListOrCreateActivity.this).put("IS_AUTO_LOGIN", "No");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            is_shutdown = false;
        }
        else if(v.getId() == R.id.btn_nav) {
            dy_list_or_create.openDrawer(Gravity.START);
        }
    }

}