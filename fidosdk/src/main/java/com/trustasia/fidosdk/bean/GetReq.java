package com.trustasia.fidosdk.bean;

import com.google.gson.annotations.SerializedName;

public class GetReq {
    @SerializedName("displayName")
    private final String displayName;
    @SerializedName("username")
    private final String username;

    public GetReq(String displayName, String username) {
        this.displayName = displayName;
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }
}
