package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestApprovalRequestListRpMechRequest {
    @Expose
    @SerializedName("repair_work_mech_id")
    private String repair_work_mech_id;

    public String getRepair_work_mech_id() {
        return repair_work_mech_id;
    }

    public void setRepair_work_mech_id(String repair_work_mech_id) {
        this.repair_work_mech_id = repair_work_mech_id;
    }
}
