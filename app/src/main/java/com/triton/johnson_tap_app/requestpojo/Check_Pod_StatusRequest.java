package com.triton.johnson_tap_app.requestpojo;

public class Check_Pod_StatusRequest {

    private String user_mobile_no;
    private String job_id;
    private String comp_no;

    public String getUser_mobile_no() {
        return user_mobile_no;
    }

    public void setUser_mobile_no(String user_mobile_no) {
        this.user_mobile_no = user_mobile_no;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getComp_no() {
        return comp_no;
    }

    public void setComp_no(String comp_no) {
        this.comp_no = comp_no;
    }
}
