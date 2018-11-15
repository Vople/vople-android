package com.mobile.vople.vople;

public class DownloadListViewItem {
    private String sNickName;
    private String sRecord;
    private String sRunningTime;

    public DownloadListViewItem(String sNickName, String sRecord, String sRunningTime) {
        this.sNickName = sNickName;
        this.sRecord = sRecord;
        this.sRunningTime = sRunningTime;
    }

    public void setNickName(String nickname){
        sNickName = nickname;
    }

    public void setRecord(String record) {
        sRecord = record;
    }

    public void setRunningTime(String RunningTime) {
        this.sRunningTime = RunningTime;
    }

    public String getNickName() {
        return this.sNickName;
    }

    public String getRecord() {
        return this.sRecord;
    }

    public String getRunningTime(){return this.sRunningTime; }
}