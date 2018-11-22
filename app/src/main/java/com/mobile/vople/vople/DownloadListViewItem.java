package com.mobile.vople.vople;

public class DownloadListViewItem {
    private String sTitle;
    private String sRecord;

    public DownloadListViewItem(String sTitle, String sRecord) {
        this.sTitle = sTitle;
        this.sRecord = sRecord;
    }

    public void setTitle(String title){
        sTitle = title;
    }

    public void setRecord(String record) {
        sRecord = record;
    }

    public String getTitle() {
        return this.sTitle;
    }

    public String getRecord() {
        return this.sRecord;
    }
}