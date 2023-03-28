package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FailureReportFetchDetailsByJobCodeRequest {
    @Expose
    @SerializedName("job_id")
    private String job_id;
    @Expose
    @SerializedName("FACTBARCODE")
    private String FACTBARCODE;

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getFACTBARCODE() {
        return FACTBARCODE;
    }

    public void setFACTBARCODE(String FACTBARCODE) {
        this.FACTBARCODE = FACTBARCODE;
    }
}
