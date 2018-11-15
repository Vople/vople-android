package com.mobile.vople.vople.item;

import java.util.ArrayList;

/**
 * Created by parkjaemin on 15/11/2018.
 */

public class RoomBreifItem {

    private int roomID;
    private String title;
    private String num;
    private int roomType;
    private String script_title;

    public RoomBreifItem(int roomID, String title, String num, int roomType, String script_title) {
        this.roomID = roomID;
        this.title = title;
        this.num = num;
        this.roomType = roomType;
        this.script_title = script_title;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getScript_title() {
        return script_title;
    }

    public void setScript_title(String script_title) {
        this.script_title = script_title;
    }
}
