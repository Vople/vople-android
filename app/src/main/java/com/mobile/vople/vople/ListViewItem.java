package com.mobile.vople.vople;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable nProfile;
    private String sNickName;
    private String sRunningTime;
    private String sNowTime;

    public ListViewItem(Drawable nProfile, String sNickName, String sRunningTime, String sNowTime) {
        this.nProfile = nProfile;
        this.sNickName = sNickName;
        this.sRunningTime = sRunningTime;
        this.sNowTime = sNowTime;
    }

    public void setProfile(Drawable profile) {
        nProfile = profile;
    }

    public void setNickName(String nickname) {
        sNickName = nickname;
    }

    public void setRunningTime(String runningtime) {
        sRunningTime = runningtime;
    }

    public void setNowTime(String nowtime) {
        sNowTime = nowtime;
    }

    public Drawable getProfile() {
        return this.nProfile;
    }

    public String getNickName() {
        return this.sNickName;
    }

    public String getRunningTime() {
        return this.sRunningTime;
    }

    public String getNowTime() {
        return this.sNowTime;
    }
}
