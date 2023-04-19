package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestEditEngRequest {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("repair_work_eng_name")
    private String repair_work_eng_name;
    @Expose
    @SerializedName("repair_work_eng_phone")
    private String repair_work_eng_phone;
    @Expose
    @SerializedName("repair_work_eng_id")
    private String repair_work_eng_id;
    @Expose
    @SerializedName("_id")
    private String _id;
    @Expose
    @SerializedName("remarks")
    private String remarks;
    @Expose
    @SerializedName("repair_work_eng_date")
    private String repair_work_eng_date;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepair_work_eng_name() {
        return repair_work_eng_name;
    }

    public void setRepair_work_eng_name(String repair_work_eng_name) {
        this.repair_work_eng_name = repair_work_eng_name;
    }

    public String getRepair_work_eng_phone() {
        return repair_work_eng_phone;
    }

    public void setRepair_work_eng_phone(String repair_work_eng_phone) {
        this.repair_work_eng_phone = repair_work_eng_phone;
    }

    public String getRepair_work_eng_id() {
        return repair_work_eng_id;
    }

    public void setRepair_work_eng_id(String repair_work_eng_id) {
        this.repair_work_eng_id = repair_work_eng_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRepair_work_eng_date() {
        return repair_work_eng_date;
    }

    public void setRepair_work_eng_date(String repair_work_eng_date) {
        this.repair_work_eng_date = repair_work_eng_date;
    }
}
