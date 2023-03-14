package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElevatorCheckDateRequest {
    @Expose
    @SerializedName("submitted_by_num")
    private String submitted_by_num;
    @Expose
    @SerializedName("survey_no")
    private String survey_no;
    @Expose
    @SerializedName("job_id")
    private String job_id;

    public ElevatorCheckDateRequest() {
    }

    public ElevatorCheckDateRequest(String submitted_by_num, String survey_no, String job_id) {
        this.submitted_by_num = submitted_by_num;
        this.survey_no = survey_no;
        this.job_id = job_id;
    }

    public String getSubmitted_by_num() {
        return submitted_by_num;
    }

    public void setSubmitted_by_num(String submitted_by_num) {
        this.submitted_by_num = submitted_by_num;
    }

    public String getSurvey_no() {
        return survey_no;
    }

    public void setSurvey_no(String survey_no) {
        this.survey_no = survey_no;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }
}
