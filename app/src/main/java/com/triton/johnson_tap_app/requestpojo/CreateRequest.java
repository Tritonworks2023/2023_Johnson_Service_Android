package com.triton.johnson_tap_app.requestpojo;

public class CreateRequest {


    /**
     * user_id : 12345
     * user_password : 12345
     * last_login_time : 20-10-2021 11:00 AM
     */

    private String user_mobile_no;
    private String user_name;
    private String att_date;
    private String att_start_time;
    private String att_status;
    private String att_start_lat;
    private String att_start_long;
    private String login_lat;
    private String login_long;
    private String login_address;

    public String getUser_mobile_no() {
        return user_mobile_no;
    }

    public void setUser_mobile_no(String user_mobile_no) {
        this.user_mobile_no = user_mobile_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAtt_date() {
        return att_date;
    }

    public void setAtt_date(String att_date) {
        this.att_date = att_date;
    }

    public String getAtt_start_time() {
        return att_start_time;
    }

    public void setAtt_start_time(String att_start_time) {
        this.att_start_time = att_start_time;
    }

    public String getAtt_status() {
        return att_status;
    }

    public void setAtt_status(String att_status) {
        this.att_status = att_status;
    }

    public String getAtt_start_lat() {
        return att_start_lat;
    }

    public void setAtt_start_lat(String att_start_lat) {
        this.att_start_lat = att_start_lat;
    }

    public String getAtt_start_long() {
        return att_start_long;
    }

    public void setAtt_start_long(String att_start_long) {
        this.att_start_long = att_start_long;
    }

    public String getLogin_lat() {
        return login_lat;
    }

    public void setLogin_lat(String login_lat) {
        this.login_lat = login_lat;
    }

    public String getLogin_long() {
        return login_long;
    }

    public void setLogin_long(String login_long) {
        this.login_long = login_long;
    }

    public String getLogin_address() {
        return login_address;
    }

    public void setLogin_address(String login_address) {
        this.login_address = login_address;
    }
}
