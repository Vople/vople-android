package com.mobile.vople.vople.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.vople.vople.FreeActivity;
import com.mobile.vople.vople.R;
import com.mobile.vople.vople.SituationActivity;
import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.RoomBreifItem;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by parkjaemin on 27/12/2018.
 */

public class EnterRoomDialog extends Dialog {

    private final int JOINABLE_ROOM = 1;
    private final int ALREADY_JOINED_ROOM = 2;
    private final int UNJOINABLE_ROOM = 3;

    @BindView(R.id.tv_script_content)
    TextView tv_script_content;
    @BindView(R.id.tv_script)
    TextView tv_script;
    @BindView(R.id.ly_content_of_script)
    LinearLayout ly_content_of_script;
    @BindView(R.id.sp_available_role)
    Spinner sp_available_role;
    @BindView(R.id.btn_enter)
    Button btn_enter;

    private Retrofit retrofit;

    private RoomBreifItem item;

    public EnterRoomDialog(@NonNull Context context, RoomBreifItem item) {
        super(context);
        setContentView(R.layout.enter_room_pop);

        this.item = item;

        ButterKnife.bind(this);

        initialize();
    }

    protected void initialize()
    {

        if(item.getRoomType() == 1)
            enterSituationRoom();
    }

    @OnClick({R.id.btn_enter})
    void onClick(View v)
    {
        if(v.getId() == R.id.btn_enter)
        {
            if(item.getRoomType() == 0)
            {
                enterFreeRoom();
            }
            else
            {
                String roll_name = "Default Roll Name";


                if (sp_available_role.getAdapter() != null && sp_available_role.getAdapter().getCount() > 0)
                    roll_name = sp_available_role.getSelectedItem().toString();
                else
                    MyUtils.makeFullRoomErrorToast(getContext());

                VopleServiceApi.joinRoom service_join_room = retrofit.create(VopleServiceApi.joinRoom.class);

                service_join_room.repoContributors(item.getRoomId(), roll_name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            Intent intent = null;
                            if (response.code() == 200) {
                                intent = new Intent(getContext(), SituationActivity.class);
                            } else if (response.code() == 202) {
                                intent = new Intent(getContext(), FreeActivity.class);
                            }
                            intent.putExtra("RoomID", item.getRoomId())
                                    .putExtra("RoomTitle", item.getTitle());
                            getContext().startActivity(intent);
                        }, throwable -> {
                            MyUtils.makeNetworkErrorToast(getContext());
                        });
            }
        }
    }

    private void enterFreeRoom()
    {
        VopleServiceApi.joinFreeRoom service_join_free_room = retrofit.create(VopleServiceApi.joinFreeRoom.class);

        service_join_free_room.repoContributors(item.getRoomId(), "Null")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {Intent intent = new Intent(getContext(), FreeActivity.class);
                    intent.putExtra("RoomID", item.getRoomId());
                    intent.putExtra("RoomTitle", item.getTitle());
                    getContext().startActivity(intent);
                }, throwable -> {
                    MyUtils.makeNetworkErrorToast(getContext());
                });

        ly_content_of_script.setVisibility(View.GONE);
    }

    private void enterSituationRoom()
    {
        if(item.getScript_title() != null)
            tv_script.setText(item.getScript_title());

        VopleServiceApi.quilifyJoinRoom service_quilify_join_room = retrofit.create(VopleServiceApi.quilifyJoinRoom.class);

        service_quilify_join_room.repoContributors(item.getRoomId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.code() == 200)
                        setContent(JOINABLE_ROOM, response.body());
                    else if(response.code() == 202)
                        setContent(ALREADY_JOINED_ROOM, response.body());
                }, throwable -> {
                    MyUtils.makeNetworkErrorToast(getContext());
                });
    }

    private void setContent(int type, RetrofitModel.Roll_Brief response)
    {
        switch(type)
        {
            case JOINABLE_ROOM:
                ArrayList<String> list = (ArrayList<String>) response.rolls;

                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, list);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_available_role.setAdapter(adp1);
                break;
            case ALREADY_JOINED_ROOM:
                VopleServiceApi.joinAlreadyRegistedRoom service_already_joined_room = retrofit.create(VopleServiceApi.joinAlreadyRegistedRoom.class);

                service_already_joined_room.repoContributors(item.getRoomId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if(result.code() == 200 ) enterAlreadyJoinedFreeRoom();
                            else if(result.code() == 202) enterAlreadyJoinedSituationRoom(result.body());
                            else if(result.code() == 204) MyUtils.makeFullRoomErrorToast(getContext());
                            this.dismiss();
                        }, throwable -> {
                            MyUtils.makeNetworkErrorToast(getContext());
                        });

                break;
            case UNJOINABLE_ROOM:
                Toast.makeText(getContext(), "방에 들어가실 수 없습니다!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void enterAlreadyJoinedFreeRoom()
    {
        Intent intent = new Intent(getContext(), FreeActivity.class);
        intent.putExtra("RoomID", item.getRoomId());
        intent.putExtra("RoomTitle", item.getTitle());
        getContext().startActivity(intent);
    }

    private void enterAlreadyJoinedSituationRoom(RetrofitModel.Cast response)
    {
        Intent intent = new Intent(getContext(), SituationActivity.class);
        intent.putExtra("RoomID", item.getRoomId());
        Gson gson = new Gson();
        String cast = gson.toJson(response);
        intent.putExtra("Cast", cast);
        intent.putExtra("RoomTitle", item.getTitle());
        getContext().startActivity(intent);
    }
}
