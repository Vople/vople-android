package com.mobile.vople.vople;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class EventActivity extends Activity {
    private ListView m_oListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        int nDataCnt = 0;
        ArrayList<EventItemData> oData = new ArrayList<>();

        //for(int i = 0; i < size(); ++i)
        {
            //데이터 할당
        }

        m_oListView = (ListView)findViewById(R.id.lv_event);
        EventAdapter oAdapter = new EventAdapter(oData);

        m_oListView.setAdapter(oAdapter);
    }
}
