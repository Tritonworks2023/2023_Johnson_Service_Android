package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestFetchListTechIdRequest {

    @Expose
    @SerializedName("tech_code")
    private String tech_code;

    public String getTech_code() {
        return tech_code;
    }

    public void setTech_code(String tech_code) {
        this.tech_code = tech_code;
    }
}
