package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestFetchListEngIdRequest {
    @Expose
    @SerializedName("eng_code")
    private String eng_code;

    public String getEng_code() {
        return eng_code;
    }

    public void setEng_code(String eng_code) {
        this.eng_code = eng_code;
    }
}
