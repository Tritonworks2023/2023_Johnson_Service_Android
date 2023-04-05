package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FailureReportRequestListByMechCodeRequest {
    @Expose
    @SerializedName("mech_code")
    private String mech_code;

    public String getMech_code() {
        return mech_code;
    }

    public void setMech_code(String mech_code) {
        this.mech_code = mech_code;
    }
}
