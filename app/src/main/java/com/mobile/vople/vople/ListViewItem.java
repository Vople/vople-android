package com.mobile.vople.vople;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable nProfile;
    private String sNickName;
    private String sNowTime;
    private String sSoundUrl;

    public ListViewItem(Drawable nProfile, String sNickName, String sNowTime, String sSoundUrl) {
        this.nProfile = nProfile;
        this.sNickName = sNickName;
        this.sNowTime = sNowTime;
        this.sSoundUrl = sSoundUrl;
    }


    public Drawable getnProfile() {
        return nProfile;
    }

    public void setnProfile(Drawable nProfile) {
        this.nProfile = nProfile;
    }

    public String getsNickName() {
        return sNickName;
    }

    public void setsNickName(String sNickName) {
        this.sNickName = sNickName;
    }

    public String getsNowTime() {
        return sNowTime;
    }

    public void setsNowTime(String sNowTime) {
        this.sNowTime = sNowTime;
    }

    public String getsSoundUrl() {
        return sSoundUrl;
    }

    public void setsSoundUrl(String sSoundUrl) {
        this.sSoundUrl = sSoundUrl;
    }

    public void setProfile(Drawable profile) {
        nProfile = profile;
    }

    public void setNickName(String nickname) {
        sNickName = nickname;
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

    public String getNowTime() {
        return this.sNowTime;
    }
}
