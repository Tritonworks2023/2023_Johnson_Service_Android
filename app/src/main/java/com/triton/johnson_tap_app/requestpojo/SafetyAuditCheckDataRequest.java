package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SafetyAuditCheckDataRequest {
    @Expose
    @SerializedName("submitted_by_num")
    private String submitted_by_num;
    @Expose
    @SerializedName("job_id")
    private String job_id;
    @Expose
    @SerializedName("conducted_on")
    private String conducted_on;
    /*@Expose
    @SerializedName("submitted_by_on")
    private String submitted_by_on;*/

    public String getSubmitted_by_num() {
        return submitted_by_num;
    }

    public void setSubmitted_by_num(String submitted_by_num) {
        this.submitted_by_num = submitted_by_num;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getConducted_on() {
        return conducted_on;
    }

    public void setConducted_on(String conducted_on) {
        this.conducted_on = conducted_on;
    }

    /*public String getSubmitted_by_on() {
        return submitted_by_on;
    }

    public void setSubmitted_by_on(String submitted_by_on) {
        this.submitted_by_on = submitted_by_on;
    }*/
}
