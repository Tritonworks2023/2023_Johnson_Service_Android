package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FailureReportFetchDetailsByComIdRequest {

    @Expose
    @SerializedName("comp_device_no")
    private String comp_device_no;

    public String getComp_device_no() {
        return comp_device_no;
    }

    public void setComp_device_no(String comp_device_no) {
        this.comp_device_no = comp_device_no;
    }
}
