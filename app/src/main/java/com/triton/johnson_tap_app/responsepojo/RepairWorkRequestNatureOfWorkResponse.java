package com.triton.johnson_tap_app.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RepairWorkRequestNatureOfWorkResponse {
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
        @SerializedName("R_ID")
        private String R_ID;
        @Expose
        @SerializedName("R_NAME")
        private String R_NAME;
        @Expose
        @SerializedName("C_ID")
        private String C_ID;
        @Expose
        @SerializedName("C_NAME")
        private String C_NAME;
        @Expose
        @SerializedName("P_ID")
        private String P_ID;
        @Expose
        @SerializedName("P_NAME")
        private String P_NAME;
        @Expose
        @SerializedName("ST_PEX_REVNO")
        private String ST_PEX_REVNO;
        @Expose
        @SerializedName("ST_PEX_CODE")
        private String ST_PEX_CODE;
        @Expose
        @SerializedName("ST_PEX_STRUCTID")
        private String ST_PEX_STRUCTID;

        public String getR_ID() {
            return R_ID;
        }

        public void setR_ID(String R_ID) {
            this.R_ID = R_ID;
        }

        public String getR_NAME() {
            return R_NAME;
        }

        public void setR_NAME(String R_NAME) {
            this.R_NAME = R_NAME;
        }

        public String getC_ID() {
            return C_ID;
        }

        public void setC_ID(String C_ID) {
            this.C_ID = C_ID;
        }

        public String getC_NAME() {
            return C_NAME;
        }

        public void setC_NAME(String C_NAME) {
            this.C_NAME = C_NAME;
        }

        public String getP_ID() {
            return P_ID;
        }

        public void setP_ID(String P_ID) {
            this.P_ID = P_ID;
        }

        public String getP_NAME() {
            return P_NAME;
        }

        public void setP_NAME(String P_NAME) {
            this.P_NAME = P_NAME;
        }

        public String getST_PEX_REVNO() {
            return ST_PEX_REVNO;
        }

        public void setST_PEX_REVNO(String ST_PEX_REVNO) {
            this.ST_PEX_REVNO = ST_PEX_REVNO;
        }

        public String getST_PEX_CODE() {
            return ST_PEX_CODE;
        }

        public void setST_PEX_CODE(String ST_PEX_CODE) {
            this.ST_PEX_CODE = ST_PEX_CODE;
        }

        public String getST_PEX_STRUCTID() {
            return ST_PEX_STRUCTID;
        }

        public void setST_PEX_STRUCTID(String ST_PEX_STRUCTID) {
            this.ST_PEX_STRUCTID = ST_PEX_STRUCTID;
        }
    }
}
