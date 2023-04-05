package com.triton.johnson_tap_app.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FailureReportFetchDetailsByComIdResponse {

    @Expose
    @SerializedName("Code")
    private int Code;
    @Expose
    @SerializedName("Data")
    private List<Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private String Status;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public List<Data> getData() {
        return Data;
    }

    public void setData(List<Data> Data) {
        this.Data = Data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public static class Data {
        @Expose
        @SerializedName("malt_id")
        private String malt_id;
        @Expose
        @SerializedName("comp_device_name")
        private String comp_device_name;

        public String getMalt_id() {
            return malt_id;
        }

        public void setMalt_id(String malt_id) {
            this.malt_id = malt_id;
        }

        public String getComp_device_name() {
            return comp_device_name;
        }

        public void setComp_device_name(String comp_device_name) {
            this.comp_device_name = comp_device_name;
        }
    }
}
