package com.mobile.vople.vople.server.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by parkjaemin on 07/11/2018.
 */

public class User extends BaseResponse{

    @SerializedName("username")
    String username;

    @SerializedName("email")
    String email;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
