package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestMechRequest {

    @Expose
    @SerializedName("rep_eng_code")
    private String rep_eng_code;

    public String getRep_eng_code() {
        return rep_eng_code;
    }

    public void setRep_eng_code(String rep_eng_code) {
        this.rep_eng_code = rep_eng_code;
    }
}
