package com.mobile.vople.vople;

import android.graphics.drawable.Drawable;

public class RolePlayListViewItem {
    private Drawable nProfile;
    private String sRunTime;
    private String sScript;
    private int plot_id;

    public RolePlayListViewItem(Drawable nProfile, String sScript, int plot_id) {
        this.nProfile = nProfile;
        this.sScript = sScript;
        this.plot_id = plot_id;
    }

    public int getPlot_id() {
        return plot_id;
    }

    public void setPlot_id(int plot_id) {
        this.plot_id = plot_id;
    }

    public Drawable getnProfile() {
        return nProfile;
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

    public String getScript() {
        return this.sScript;
    }
}
