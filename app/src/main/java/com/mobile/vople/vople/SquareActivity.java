package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import com.mobile.vople.vople.server.MyUtils;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SquareActivity extends AppCompatActivity{

    @BindView(R.id.lv_script_square)
    ListView lv_script_square;
    @BindView(R.id.btn_back)
    Button btn_back;

    private SquareListViewAdapter adp_square;

    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        ButterKnife.bind(this);

        initialize();

    }

    private void initialize()
    {
        adp_square = new SquareListViewAdapter();

        lv_script_square.setAdapter(adp_square);

        VopleServiceApi.getAllScripts service_get_all_scripts = retrofit.create(VopleServiceApi.getAllScripts.class);

        service_get_all_scripts.repoContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for(RetrofitModel.Script script : response)
                        adp_square.addItem(script.title, script.id, script.member_restriction);
                    adp_square.notifyDataSetChanged();
                }, throwable -> {
                    MyUtils.makeNetworkErrorToast(this);
                });
    }

    @OnClick(R.id.btn_back)
    void onBackClick()
    {
        finish();
    }
}
