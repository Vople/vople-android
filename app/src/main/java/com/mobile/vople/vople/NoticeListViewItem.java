package com.mobile.vople.vople;

public class NoticeListViewItem {
    private String sTitle;
    private String sNowTime;

    public NoticeListViewItem(String title, String nowtime){
        this.sTitle = title;
        this.sNowTime = nowtime;
    }

    public void setTitle(String title){
        sTitle = title;
    }

    public void setNowTime(String nowtime) {
        sNowTime = nowtime;
    }

    public String getTitle() {
        return this.sTitle;
    }

    public String getNowTime() {
        return this.sNowTime;
    }
}
