package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestCreateTechRequest {
    @Expose
    @SerializedName("submitted_by_on")
    private String submitted_by_on;
    @Expose
    @SerializedName("submitted_by_name")
    private String submitted_by_name;
    @Expose
    @SerializedName("submitted_by_num")
    private String submitted_by_num;
    @Expose
    @SerializedName("submitted_by_emp_code")
    private String submitted_by_emp_code;
    @Expose
    @SerializedName("tech_code")
    private String tech_code;
    @Expose
    @SerializedName("tech_name")
    private String tech_name;
    @Expose
    @SerializedName("remarks")
    private String remarks;
    @Expose
    @SerializedName("mat_available_sts")
    private String mat_available_sts;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("route")
    private String route;
    @Expose
    @SerializedName("br_code")
    private String br_code;
    @Expose
    @SerializedName("request_on")
    private String request_on;
    @Expose
    @SerializedName("job_id")
    private String job_id;
    @Expose
    @SerializedName("site_name")
    private String site_name;

    public String getSubmitted_by_on() {
        return submitted_by_on;
    }

    public void setSubmitted_by_on(String submitted_by_on) {
        this.submitted_by_on = submitted_by_on;
    }

    public String getSubmitted_by_name() {
        return submitted_by_name;
    }

    public void setSubmitted_by_name(String submitted_by_name) {
        this.submitted_by_name = submitted_by_name;
    }

    public String getSubmitted_by_num() {
        return submitted_by_num;
    }

    public void setSubmitted_by_num(String submitted_by_num) {
        this.submitted_by_num = submitted_by_num;
    }

    public String getSubmitted_by_emp_code() {
        return submitted_by_emp_code;
    }

    public void setSubmitted_by_emp_code(String submitted_by_emp_code) {
        this.submitted_by_emp_code = submitted_by_emp_code;
    }

    public String getTech_code() {
        return tech_code;
    }

    public void setTech_code(String tech_code) {
        this.tech_code = tech_code;
    }

    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMat_available_sts() {
        return mat_available_sts;
    }

    public void setMat_available_sts(String mat_available_sts) {
        this.mat_available_sts = mat_available_sts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBr_code() {
        return br_code;
    }

    public void setBr_code(String br_code) {
        this.br_code = br_code;
    }

    public String getRequest_on() {
        return request_on;
    }

    public void setRequest_on(String request_on) {
        this.request_on = request_on;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }
}
