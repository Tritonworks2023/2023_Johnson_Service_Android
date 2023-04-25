package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestJobHazardByPrefIdRequest {
    @Expose
    @SerializedName("pref_id")
    private String pref_id;

    public String getPref_id() {
        return pref_id;
    }

    public void setPref_id(String pref_id) {
        this.pref_id = pref_id;
    }
}
