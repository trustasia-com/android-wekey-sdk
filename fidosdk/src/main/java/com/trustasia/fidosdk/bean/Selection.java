package com.trustasia.fidosdk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Selection implements Serializable {
    @SerializedName("requireResidentKey")
    private final boolean rk;

    public Selection(boolean rk) {
        this.rk = rk;
    }

    public boolean getRK() {
        return rk;
    }
}
