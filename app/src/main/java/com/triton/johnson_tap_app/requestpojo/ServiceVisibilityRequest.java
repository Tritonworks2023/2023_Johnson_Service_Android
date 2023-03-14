package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ServiceVisibilityRequest {

    @Expose
    @SerializedName("program_date")
    private String program_date;
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
    @SerializedName("cus_name")
    private String cus_name;
    @Expose
    @SerializedName("images_ary")
    private ArrayList<Images_ary> images_ary;
    @Expose
    @SerializedName("cat_type")
    private String cat_type;
    @Expose
    @SerializedName("building_name")
    private String building_name;
    @Expose
    @SerializedName("job_id")
    private String job_id;
    @Expose
    @SerializedName("brcode")
    private String brcode;

    public String getBrcode() {
        return brcode;
    }

    public void setBrcode(String brcode) {
        this.brcode = brcode;
    }

    public String getProgram_date() {
        return program_date;
    }

    public void setProgram_date(String program_date) {
        this.program_date = program_date;
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

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public ArrayList<Images_ary> getImages_ary() {
        return images_ary;
    }

    public void setImages_ary(ArrayList<Images_ary> images_ary) {
        this.images_ary = images_ary;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
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
    }

    public static class Images_ary {

        /*public Images_ary() {
        }*/

        @Expose
        @SerializedName("img_path")
        private String img_path;

        public Images_ary(String img_path) {
            this.img_path = img_path;
        }

        public String getImg_path() {
            return img_path;
        }

        public void setImg_path(String img_path) {
            this.img_path = img_path;
        }
    }
}
