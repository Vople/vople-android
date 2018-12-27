package com.mobile.vople.vople.server;

/**
 * Created by parkjaemin on 22/12/2018.
 */

public class RoomBreifItem {
    private String title;
    private String script_title;
    private int roomType;
    private String kindOfScript;
    private int roomId;

    public RoomBreifItem(int roomId, String title, String script_title, int roomType, String kindOfScript) {
        this.roomId = roomId;
        this.title = title;
        this.script_title = script_title;
        this.roomType = roomType;
        this.kindOfScript = kindOfScript;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScript_title() {
        return script_title;
    }

    public void setScript_title(String script_title) {
        this.script_title = script_title;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getKindOfScript() {
        return kindOfScript;
    }

    public void setKindOfScript(String kindOfScript) {
        this.kindOfScript = kindOfScript;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
