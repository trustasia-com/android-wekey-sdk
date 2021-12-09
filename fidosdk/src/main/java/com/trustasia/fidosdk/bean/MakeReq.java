package com.trustasia.fidosdk.bean;

import com.google.gson.annotations.SerializedName;

public class MakeReq {
    @SerializedName("attestation")
    private final String attestation;
    @SerializedName("authenticatorSelection")
    private final Selection authenticatorSelection;
    @SerializedName("displayName")
    private final String displayName;
    @SerializedName("username")
    private final String username;

    public MakeReq(String attestation, Selection authenticatorSelection, String displayName, String username) {
        this.attestation = attestation;
        this.authenticatorSelection = authenticatorSelection;
        this.displayName = displayName;
        this.username = username;
    }



    public String getAttestation() {
        return attestation;
    }

    public Selection getAuthenticatorSelection() {
        return authenticatorSelection;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }
}
