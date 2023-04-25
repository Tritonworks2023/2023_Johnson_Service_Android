package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepairWorkRequestFinalCompletionEditRequest {
    @Expose
    @SerializedName("date_signature_eng")
    private String date_signature_eng;
    @Expose
    @SerializedName("signature_eng")
    private String signature_eng;
    @Expose
    @SerializedName("completed_date")
    private String completed_date;
    @Expose
    @SerializedName("repair_pref_id")
    private String repair_pref_id;
    @Expose
    @SerializedName("job_haz_id")
    private String job_haz_id;

    public String getDate_signature_eng() {
        return date_signature_eng;
    }

    public void setDate_signature_eng(String date_signature_eng) {
        this.date_signature_eng = date_signature_eng;
    }

    public String getSignature_eng() {
        return signature_eng;
    }

    public void setSignature_eng(String signature_eng) {
        this.signature_eng = signature_eng;
    }

    public String getCompleted_date() {
        return completed_date;
    }

    public void setCompleted_date(String completed_date) {
        this.completed_date = completed_date;
    }

    public String getRepair_pref_id() {
        return repair_pref_id;
    }

    public void setRepair_pref_id(String repair_pref_id) {
        this.repair_pref_id = repair_pref_id;
    }

    public String getJob_haz_id() {
        return job_haz_id;
    }

    public void setJob_haz_id(String job_haz_id) {
        this.job_haz_id = job_haz_id;
    }
}
