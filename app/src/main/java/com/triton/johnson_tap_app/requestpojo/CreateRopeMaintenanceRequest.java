package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateRopeMaintenanceRequest {
    @Expose
    @SerializedName("job_id")
    private String job_id;
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
    @SerializedName("remarks")
    private String remarks;
    @Expose
    @SerializedName("activity_date")
    private String activity_date;
    @Expose
    @SerializedName("tech_code")
    private String tech_code;
    @Expose
    @SerializedName("tech_name")
    private String tech_name;
    @Expose
    @SerializedName("activity_code_list")
    private ArrayList<String> activity_code_list;
    @Expose
    @SerializedName("activity_code")
    private String activity_code;
    @Expose
    @SerializedName("osg_rope_dia")
    private String osg_rope_dia;
    @Expose
    @SerializedName("main_rope_dia")
    private String main_rope_dia;
    @Expose
    @SerializedName("machine_type")
    private String machine_type;
    @Expose
    @SerializedName("building_name")
    private String building_name;

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
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

    public ArrayList<String> getActivity_code_list() {
        return activity_code_list;
    }

    public void setActivity_code_list(ArrayList<String> activity_code_list) {
        this.activity_code_list = activity_code_list;
    }

    public String getActivity_code() {
        return activity_code;
    }

    public void setActivity_code(String activity_code) {
        this.activity_code = activity_code;
    }

    public String getOsg_rope_dia() {
        return osg_rope_dia;
    }

    public void setOsg_rope_dia(String osg_rope_dia) {
        this.osg_rope_dia = osg_rope_dia;
    }

    public String getMain_rope_dia() {
        return main_rope_dia;
    }

    public void setMain_rope_dia(String main_rope_dia) {
        this.main_rope_dia = main_rope_dia;
    }

    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    /*@Expose
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
    @SerializedName("remarks")
    private String remarks;
    @Expose
    @SerializedName("activity_date")
    private String activity_date;
    @Expose
    @SerializedName("tech_code")
    private String tech_code;
    @Expose
    @SerializedName("tech_name")
    private String tech_name;
    @Expose
    @SerializedName("activity_code")
    private String activity_code;
    @Expose
    @SerializedName("osg_rope_dia")
    private String osg_rope_dia;
    @Expose
    @SerializedName("main_rope_dia")
    private String main_rope_dia;
    @Expose
    @SerializedName("machine_type")
    private String machine_type;
    @Expose
    @SerializedName("building_name")
    private String building_name;
    @Expose
    @SerializedName("job_id")
    private String job_id;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
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

    public String getActivity_code() {
        return activity_code;
    }

    public void setActivity_code(String activity_code) {
        this.activity_code = activity_code;
    }

    public String getOsg_rope_dia() {
        return osg_rope_dia;
    }

    public void setOsg_rope_dia(String osg_rope_dia) {
        this.osg_rope_dia = osg_rope_dia;
    }

    public String getMain_rope_dia() {
        return main_rope_dia;
    }

    public void setMain_rope_dia(String main_rope_dia) {
        this.main_rope_dia = main_rope_dia;
    }

    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }*/
}
