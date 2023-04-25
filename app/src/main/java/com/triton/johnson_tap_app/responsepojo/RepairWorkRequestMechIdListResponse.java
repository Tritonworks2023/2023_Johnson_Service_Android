package com.triton.johnson_tap_app.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepairWorkRequestMechIdListResponse {
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
        @SerializedName("CONTYPE")
        private String CONTYPE;
        @Expose
        @SerializedName("OM_SEN_MOBILENO")
        private String OM_SEN_MOBILENO;
        @Expose
        @SerializedName("BR_CODE")
        private String BR_CODE;
        @Expose
        @SerializedName("EMPCODE")
        private String EMPCODE;
        @Expose
        @SerializedName("EMPNAME")
        private String EMPNAME;

        public String getCONTYPE() {
            return CONTYPE;
        }

        public void setCONTYPE(String CONTYPE) {
            this.CONTYPE = CONTYPE;
        }

        public String getOM_SEN_MOBILENO() {
            return OM_SEN_MOBILENO;
        }

        public void setOM_SEN_MOBILENO(String OM_SEN_MOBILENO) {
            this.OM_SEN_MOBILENO = OM_SEN_MOBILENO;
        }

        public String getBR_CODE() {
            return BR_CODE;
        }

        public void setBR_CODE(String BR_CODE) {
            this.BR_CODE = BR_CODE;
        }

        public String getEMPCODE() {
            return EMPCODE;
        }

        public void setEMPCODE(String EMPCODE) {
            this.EMPCODE = EMPCODE;
        }

        public String getEMPNAME() {
            return EMPNAME;
        }

        public void setEMPNAME(String EMPNAME) {
            this.EMPNAME = EMPNAME;
        }
    }
}
