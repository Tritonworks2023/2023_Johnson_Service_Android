package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestMechIdListRequest {
    @Expose
    @SerializedName("CONTYPE")
    private String CONTYPE;
    @Expose
    @SerializedName("BR_CODE")
    private String BR_CODE;

    public String getCONTYPE() {
        return CONTYPE;
    }

    public void setCONTYPE(String CONTYPE) {
        this.CONTYPE = CONTYPE;
    }

    public String getBR_CODE() {
        return BR_CODE;
    }

    public void setBR_CODE(String BR_CODE) {
        this.BR_CODE = BR_CODE;
    }
}
