package com.mobile.vople.vople;

import android.graphics.drawable.Drawable;

public class FollowingListViewItem {
    private Drawable nProfile;
    private String sNickName;

    public FollowingListViewItem(Drawable nProfile, String sNickName) {
        this.nProfile = nProfile;
        this.sNickName = sNickName;
    }

    public void setProfile(Drawable profile) {
        nProfile = profile;
    }

    public void setNickName(String nickname) {
        sNickName = nickname;
    }

    public Drawable getProfile() {
        return this.nProfile;
    }

    public String getNickName() {
        return this.sNickName;
    }

}



