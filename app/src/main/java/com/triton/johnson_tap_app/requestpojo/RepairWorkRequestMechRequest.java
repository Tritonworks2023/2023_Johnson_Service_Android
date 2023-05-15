package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestMechRequest {

    @Expose
    @SerializedName("rep_eng_code")
    private String rep_eng_code;

    @Expose
    @SerializedName("br_code")
    private String br_code;

    public String getRep_eng_code() {
        return rep_eng_code;
    }

    public void setRep_eng_code(String rep_eng_code) {
        this.rep_eng_code = rep_eng_code;
    }

    public String getBr_code() {
        return br_code;
    }

    public void setBr_code(String br_code) {
        this.br_code = br_code;
    }
}
