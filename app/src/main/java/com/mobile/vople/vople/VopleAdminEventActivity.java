package com.mobile.vople.vople;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.RoomBreifItem;
import com.mobile.vople.vople.server.VopleApi;
import com.mobile.vople.vople.server.VopleServiceApi;
import com.mobile.vople.vople.server.VopleServiceApi.getEventBoards;
import com.mobile.vople.vople.ui.EnterRoomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class VopleAdminEventActivity extends AppCompatActivity
{
    @BindView(R.id.btn_back)
    Button btn_back;

    private VopleAdminAdapter adp_event;

    @Inject
    VopleApi mVopleApi;

    @BindView(R.id.lv_event)
    ListView lv_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vople_admin_event);

        ButterKnife.bind(this);

        initialize();

        VopleServiceApi.getEventBoards service_get_all_event_boards = mVopleApi.getRetrofit().create(getEventBoards.class);

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
    }

    @OnItemClick(R.id.lv_event)
    void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        new EnterRoomDialog(this, (RoomBreifItem) adp_event.getItem(position)).show();
    }

    @OnClick(R.id.btn_back)
    void onButtonClick(View v)
    {
        finish();
    }
}
