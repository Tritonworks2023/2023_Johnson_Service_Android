package com.triton.johnson_tap_app.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FailureReportCompDeviceListResponse {
    @Expose
    @SerializedName("Code")
    private int Code;
    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
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

    public ArrayList<Data> getData() {
        return Data;
    }

    public void setData(ArrayList<Data> Data) {
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
        @SerializedName("ST_PMH_BARCODEID")
        private String ST_PMH_BARCODEID;
        @Expose
        @SerializedName("ST_PMH_OLDPART")
        private String ST_PMH_OLDPART;
        @Expose
        @SerializedName("ST_PMH_DRAWINGNO")
        private String ST_PMH_DRAWINGNO;
        @Expose
        @SerializedName("ST_PMH_COMPPARTNO")
        private String ST_PMH_COMPPARTNO;
        @Expose
        @SerializedName("ST_PMH_PARTNO")
        private String ST_PMH_PARTNO;
        @Expose
        @SerializedName("ST_PMH_PARTNAME")
        private String ST_PMH_PARTNAME;

        public String getST_PMH_BARCODEID() {
            return ST_PMH_BARCODEID;
        }

        public void setST_PMH_BARCODEID(String ST_PMH_BARCODEID) {
            this.ST_PMH_BARCODEID = ST_PMH_BARCODEID;
        }

        public String getST_PMH_OLDPART() {
            return ST_PMH_OLDPART;
        }

        public void setST_PMH_OLDPART(String ST_PMH_OLDPART) {
            this.ST_PMH_OLDPART = ST_PMH_OLDPART;
        }

        public String getST_PMH_DRAWINGNO() {
            return ST_PMH_DRAWINGNO;
        }

        public void setST_PMH_DRAWINGNO(String ST_PMH_DRAWINGNO) {
            this.ST_PMH_DRAWINGNO = ST_PMH_DRAWINGNO;
        }

        public String getST_PMH_COMPPARTNO() {
            return ST_PMH_COMPPARTNO;
        }

        public void setST_PMH_COMPPARTNO(String ST_PMH_COMPPARTNO) {
            this.ST_PMH_COMPPARTNO = ST_PMH_COMPPARTNO;
        }

        public String getST_PMH_PARTNO() {
            return ST_PMH_PARTNO;
        }

        public void setST_PMH_PARTNO(String ST_PMH_PARTNO) {
            this.ST_PMH_PARTNO = ST_PMH_PARTNO;
        }

        public String getST_PMH_PARTNAME() {
            return ST_PMH_PARTNAME;
        }

        public void setST_PMH_PARTNAME(String ST_PMH_PARTNAME) {
            this.ST_PMH_PARTNAME = ST_PMH_PARTNAME;
        }
    }
}
