package com.mobile.vople.vople;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
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
import com.mobile.vople.vople.main.MainActivity;
import com.mobile.vople.vople.main.MainAdapter;
import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.RoomBreifItem;
import com.mobile.vople.vople.server.SharedPreference;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.VopleServiceApi.getEventBoards;
import com.mobile.vople.vople.server.model.MyRetrofit;
import com.mobile.vople.vople.ui.EnterRoomDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
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

public class VopleAdminEventActivity extends AppCompatActivity
{
    @BindView(R.id.btn_back)
    Button btn_back;

    private VopleAdminAdapter adp_event;

    private Retrofit retrofit;

    @BindView(R.id.lv_event)
    ListView lv_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vople_admin_event);

        ButterKnife.bind(this);

        initialize();

        VopleServiceApi.getEventBoards service_get_all_event_boards = retrofit.create(getEventBoards.class);

        service_get_all_event_boards.repoContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (RetrofitModel.BoardContributor object : response) {
                        if(object.script == null) adp_event.addItem(object.id, object.title, null, object.mode, null);
                        else adp_event.addItem(object.id, object.title, null, object.mode, object.script.title);
                    }
                    adp_event.notifyDataSetChanged();
                }, throwable -> {
                    MyUtils.makeNetworkErrorToast(this);
                });
    }

    private void initialize()
    {
        adp_event = new VopleAdminAdapter();
        lv_event.setAdapter(adp_event);

        retrofit = RetrofitInstance.getInstance(getApplicationContext());

    }

    @OnItemClick(R.id.lv_event)
    void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        new EnterRoomDialog(this, (RoomBreifItem) adp_event.getItem(position)).show();
    }

}
