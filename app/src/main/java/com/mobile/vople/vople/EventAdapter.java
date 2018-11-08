package com.mobile.vople.vople;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter
{
    LayoutInflater inflater = null;
    private ArrayList<EventItemData> m_oData = null;
    private int nListCnt = 0;

    public EventAdapter(ArrayList<EventItemData> _oData)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_event, parent, false);
        }

        ImageView oImageProfile = (ImageView) convertView.findViewById(R.id.iv_profile);
        TextView oTextNickName = (TextView) convertView.findViewById(R.id.tv_nickname);
        TextView oTextRunningTime = (TextView) convertView.findViewById(R.id.tv_runningtime);
        TextView oTextNowTime = (TextView) convertView.findViewById(R.id.tv_nowtime);

        oImageProfile.setImageResource(m_oData.get(position).ivProfile);
        oTextNickName.setText(m_oData.get(position).strNickName);
        oTextRunningTime.setText(m_oData.get(position).strRunningTime);
        oTextNowTime.setText(m_oData.get(position).strNowTime);
        return convertView;
    }


    public void addItem(EventItemData data)
    {
        m_oData.add(data);
    }
}
