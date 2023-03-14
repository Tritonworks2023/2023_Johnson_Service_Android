package com.triton.johnson_tap_app.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceTypeLocalListResponse {

    @Expose
    @SerializedName("s_name")
    private String s_name;
    @Expose
    @SerializedName("s_code")
    private String s_code;

    public ServiceTypeLocalListResponse(String s_code, String s_name) {
        this.s_code = s_code;
        this.s_name = s_name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_code() {
        return s_code;
    }

    public void setS_code(String s_code) {
        this.s_code = s_code;
    }
}
