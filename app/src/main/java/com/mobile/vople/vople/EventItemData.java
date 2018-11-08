package com.mobile.vople.vople;

import android.widget.ImageView;

public class EventItemData {
    public int ivProfile;
    public String strNickName;
    public String strRunningTime;
    public String strNowTime;

    public EventItemData(int ivProfile, String strNickName, String strRunningTime, String strNowTime) {
        this.ivProfile = ivProfile;
        this.strNickName = strNickName;
        this.strRunningTime = strRunningTime;
        this.strNowTime = strNowTime;
    }

    public int getIvProfile() {
        return ivProfile;
    }

    public void setIvProfile(int ivProfile) {
        this.ivProfile = ivProfile;
    }

    public String getStrNickName() {
        return strNickName;
    }

    public void setStrNickName(String strNickName) {
        this.strNickName = strNickName;
    }

    public String getStrRunningTime() {
        return strRunningTime;
    }

    public void setStrRunningTime(String strRunningTime) {
        this.strRunningTime = strRunningTime;
    }

    public String getStrNowTime() {
        return strNowTime;
    }

    public void setStrNowTime(String strNowTime) {
        this.strNowTime = strNowTime;
    }
}
