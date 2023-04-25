package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestApprovalRequestListRpEngRequest {
    @Expose
    @SerializedName("repair_work_eng_id")
    private String repair_work_eng_id;

    public String getRepair_work_eng_id() {
        return repair_work_eng_id;
    }

    public void setRepair_work_eng_id(String repair_work_eng_id) {
        this.repair_work_eng_id = repair_work_eng_id;
    }
}
