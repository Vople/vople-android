package com.mobile.vople.vople;

import android.graphics.drawable.Drawable;

public class RolePlayListViewItem {
    private Drawable nProfile;
    private String sRunTime;
    private String sScript;


    public RolePlayListViewItem(Drawable nProfile, String sRunTime, String sScript) {
        this.nProfile = nProfile;
        this.sRunTime = sRunTime;
        this.sScript = sScript;
    }

    public Drawable getnProfile() {
        return nProfile;
    }

    public String getsRunTime() {
        return sRunTime;
    }

    public void setsRunTime(String sRunTime) {
        this.sRunTime = sRunTime;
    }

    public String getsScript() {
        return sScript;
    }

    public void setsScript(String sScript) {
        this.sScript = sScript;
    }

    public void setnProfile(Drawable profile){
        nProfile = profile;
    }

    public void setNickName(String runtime) {
        sRunTime = runtime;
    }

    public void setScript(String script) {
        sScript = script;
    }

    public Drawable getProfile() {
        return this.nProfile;
    }

    public String getRunTime() {
        return this.sRunTime;
    }

    public String getScript() {
        return this.sScript;
    }
}
