package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateFailureReportRequest {

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
    /*@Expose
    @SerializedName("eng_signature")
    private String eng_signature;
    @Expose
    @SerializedName("eng_name")
    private String eng_name;*/
    @Expose
    @SerializedName("mech_signature")
    private String mech_signature;
    @Expose
    @SerializedName("mech_name")
    private String mech_name;
    @Expose
    @SerializedName("env_condition")
    private String env_condition;
    @Expose
    @SerializedName("comments")
    private String comments;
    @Expose
    @SerializedName("incom_suppy_voltage")
    private String incom_suppy_voltage;
    @Expose
    @SerializedName("failure_obs")
    private String failure_obs;
    @Expose
    @SerializedName("date_of_failure")
    private String date_of_failure;
    @Expose
    @SerializedName("device_ins_date")
    private String device_ins_date;
    @Expose
    @SerializedName("serial_no")
    private String serial_no;
    @Expose
    @SerializedName("rating")
    private String rating;
    @Expose
    @SerializedName("model_no")
    private String model_no;
    @Expose
    @SerializedName("device_name")
    private String device_name;
    @Expose
    @SerializedName("type_of_ser")
    private String type_of_ser;
    @Expose
    @SerializedName("department_name")
    private String department_name;
    @Expose
    @SerializedName("site_name")
    private String site_name;
    @Expose
    @SerializedName("job_id")
    private String job_id;
    @Expose
    @SerializedName("branch_name")
    private String branch_name;
    @Expose
    @SerializedName("report_date")
    private String report_date;

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

    /*public String getEng_signature() {
        return eng_signature;
    }

    public void setEng_signature(String eng_signature) {
        this.eng_signature = eng_signature;
    }

    public String getEng_name() {
        return eng_name;
    }

    public void setEng_name(String eng_name) {
        this.eng_name = eng_name;
    }*/

    public String getMech_signature() {
        return mech_signature;
    }

    public void setMech_signature(String mech_signature) {
        this.mech_signature = mech_signature;
    }

    public String getMech_name() {
        return mech_name;
    }

    public void setMech_name(String mech_name) {
        this.mech_name = mech_name;
    }

    public String getEnv_condition() {
        return env_condition;
    }

    public void setEnv_condition(String env_condition) {
        this.env_condition = env_condition;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIncom_suppy_voltage() {
        return incom_suppy_voltage;
    }

    public void setIncom_suppy_voltage(String incom_suppy_voltage) {
        this.incom_suppy_voltage = incom_suppy_voltage;
    }

    public String getFailure_obs() {
        return failure_obs;
    }

    public void setFailure_obs(String failure_obs) {
        this.failure_obs = failure_obs;
    }

    public String getDate_of_failure() {
        return date_of_failure;
    }

    public void setDate_of_failure(String date_of_failure) {
        this.date_of_failure = date_of_failure;
    }

    public String getDevice_ins_date() {
        return device_ins_date;
    }

    public void setDevice_ins_date(String device_ins_date) {
        this.device_ins_date = device_ins_date;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getModel_no() {
        return model_no;
    }

    public void setModel_no(String model_no) {
        this.model_no = model_no;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getType_of_ser() {
        return type_of_ser;
    }

    public void setType_of_ser(String type_of_ser) {
        this.type_of_ser = type_of_ser;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }
}
