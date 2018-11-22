package com.mobile.vople.vople;

public class SquareListViewItem {
    private String sTitle;
    private int id;
    private int member_restriction;

    public SquareListViewItem(String sTitle, int id, int member_restriction) {
        this.sTitle = sTitle;
        this.id = id;
        this.member_restriction = member_restriction;
    }

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_restriction() {
        return member_restriction;
    }

    public void setMember_restriction(int member_restriction) {
        this.member_restriction = member_restriction;
    }

    public void setTitle(String title){
        sTitle = title;
    }

    public String getTitle(){
        return this.sTitle;
    }
}
