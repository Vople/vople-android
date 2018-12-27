package com.mobile.vople.vople.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobile.vople.vople.SituationActivity;
import com.mobile.vople.vople.R;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by parkjaemin on 27/12/2018.
 */

public class CreateRoomDialog extends Dialog{

    @BindView(R.id.edt_roomName)
    EditText edt_roomName;
    @BindView(R.id.btn_cancel)
    Button btn_close;
    @BindView(R.id.btn_submit)
    Button btn_createRoom;
    @BindView(R.id.rb_mission)
    RadioButton rb_mission;
    @BindView(R.id.rb_situation)
    RadioButton rb_situation;
    @BindView(R.id.cy_mission)
    ConstraintLayout cy_mission;
    @BindView(R.id.sp_playScript)
    Spinner sp_playScript;
    @BindView(R.id.rg_roomType)
    RadioGroup rg_roomType;

    private List<RetrofitModel.Script> all_scripts;

    private Retrofit retrofit;

    public CreateRoomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_create_room);

        ButterKnife.bind(this);
        initialze();
        loadScripts();
    }

    private void initialze()
    {
        all_scripts = new ArrayList<>();

        retrofit = RetrofitInstance.getInstance(getContext());
    }

    private void loadScripts()
    {
        VopleServiceApi.listAllScripts service = retrofit.create(VopleServiceApi.listAllScripts.class);

        service.repoContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    for (RetrofitModel.Script script : result) {
                        all_scripts.add(script);
                    }
                }, throwable -> {
                    Toast.makeText(getContext(), "네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
                });


        List<String> tempList = new ArrayList<>();

        for(RetrofitModel.Script script : all_scripts) tempList.add(script.title);

        ArrayAdapter<String> adp1 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, tempList);

        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_playScript.setAdapter(adp1);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit, R.id.rb_situation, R.id.rb_mission})
    void onButtonClick(View v)
    {

        if(v.getId() == R.id.btn_submit)
        {

            int roomType = rg_roomType.getCheckedRadioButtonId() == R.id.rb_situation ? 1 : 0;
            String script_title = (String)sp_playScript.getSelectedItem();
            int script_id = -1;

            for(RetrofitModel.Script script : all_scripts)
            {
                if(script_title.equals(script.title)) {
                    script_id = script.id;
                    break;
                }
            }

            VopleServiceApi.create_board mkBoardService = retrofit.create(VopleServiceApi.create_board.class);

            mkBoardService.repoContributors(edt_roomName.getText().toString(),
                    (String) sp_playScript.getSelectedItem(),
                    roomType,
                    roomType == 0 ? -1 : script_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                      if(response.code() == 201) {
                          this.dismiss();
                          Intent roomListIntent = new Intent(getContext(), SituationActivity.class);
                          roomListIntent.putExtra("RoomID", response.body().id);
                          getContext().startActivity(roomListIntent);
                      }
                    }, throwable -> {
                            Log.d("TAG", throwable.getLocalizedMessage());
                            this.dismiss();
                    });
        }
        else if(v.getId() == R.id.btn_cancel)
        {
            this.dismiss();
        }
        else if(v.getId() == R.id.rb_mission)
        {
            if(cy_mission.getVisibility() == View.VISIBLE)
                cy_mission.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.rb_situation)
        {
            if(cy_mission.getVisibility() != View.VISIBLE)
                cy_mission.setVisibility(View.VISIBLE);
        }
    }
}
