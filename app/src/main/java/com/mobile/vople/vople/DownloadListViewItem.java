package com.mobile.vople.vople;

public class DownloadListViewItem {
    private String sNickName;
    private String sRecord;

    public DownloadListViewItem(String sNickName, String sRecord) {
        this.sNickName = sNickName;
        this.sRecord = sRecord;
    }

    public void setNickName(String nickname){
        sNickName = nickname;
    }

    public void setRecord(String record) {
        sRecord = record;
    }

    public String getNickName() {
        return this.sNickName;
    }

    public String getRecord() {
        return this.sRecord;
    }
}