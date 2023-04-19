package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkMechBrCodeRequest {
    @Expose
    @SerializedName("brcode")
    private String brcode;

    public String getBrcode() {
        return brcode;
    }

    public void setBrcode(String brcode) {
        this.brcode = brcode;
    }
}
