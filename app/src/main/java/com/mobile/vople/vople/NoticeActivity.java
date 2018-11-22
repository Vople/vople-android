package com.mobile.vople.vople;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class NoticeActivity extends AppCompatActivity {

    private ListView lv_notice;

    private Button btn_back;

    private NoticeListViewAdapter noticeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        btn_back = (Button) findViewById(R.id.btn_back);

        lv_notice = (ListView)findViewById(R.id.lv_notice);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noticeAdapter = new NoticeListViewAdapter();
        lv_notice.setAdapter(noticeAdapter);

        noticeAdapter.addItem("[공지] 사용자 여러분의 더 나은 사용 환경을 위해 욕설을 자제해주시기 바랍니다.", "11월21일");
        noticeAdapter.addItem("[이벤트] 보플이 쏜다! 11월 30일 오후 5시 많은 참여 부탁드립니다!! " , "11월22일");
    }
}
