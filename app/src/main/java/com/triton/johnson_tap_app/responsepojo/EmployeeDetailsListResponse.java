package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeDetailsListResponse implements Parcelable {
    public static final Creator<EmployeeDetailsListResponse> CREATOR = new Creator<EmployeeDetailsListResponse>() {
        @Override
        public EmployeeDetailsListResponse createFromParcel(Parcel in) {
            return new EmployeeDetailsListResponse(in);
        }

        @Override
        public EmployeeDetailsListResponse[] newArray(int size) {
            return new EmployeeDetailsListResponse[size];
        }
    };
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

    protected EmployeeDetailsListResponse(Parcel in) {
        Code = in.readInt();
        Data = in.readParcelable(EmployeeDetailsListResponse.Data.class.getClassLoader());
        Message = in.readString();
        Status = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(Code);
        parcel.writeTypedList(Data);
        parcel.writeString(Message);
        parcel.writeString(Status);
    }

    public static class Data implements Parcelable {
        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
        @Expose
        @SerializedName("OM_SM_EMPNAME")
        private String OM_SM_EMPNAME;
        @Expose
        @SerializedName("OM_SM_EMPID")
        private String OM_SM_EMPID;
        @Expose
        @SerializedName("OM_SEN_SQNO")
        private int OM_SEN_SQNO;
        @Expose
        @SerializedName("OM_SEN_BRCODE")
        private String OM_SEN_BRCODE;
        @Expose
        @SerializedName("OM_SM_IMEI")
        private String OM_SM_IMEI;

        public Data() {
        }

        protected Data(Parcel in) {
            OM_SM_EMPNAME = in.readString();
            OM_SM_EMPID = in.readString();
            OM_SEN_SQNO = in.readInt();
            OM_SEN_BRCODE = in.readString();
            OM_SM_IMEI = in.readString();
        }

        public String getOM_SM_EMPNAME() {
            return OM_SM_EMPNAME;
        }

        public void setOM_SM_EMPNAME(String OM_SM_EMPNAME) {
            this.OM_SM_EMPNAME = OM_SM_EMPNAME;
        }

        public String getOM_SM_EMPID() {
            return OM_SM_EMPID;
        }

        public void setOM_SM_EMPID(String OM_SM_EMPID) {
            this.OM_SM_EMPID = OM_SM_EMPID;
        }

        public int getOM_SEN_SQNO() {
            return OM_SEN_SQNO;
        }

        public void setOM_SEN_SQNO(int OM_SEN_SQNO) {
            this.OM_SEN_SQNO = OM_SEN_SQNO;
        }

        public String getOM_SEN_BRCODE() {
            return OM_SEN_BRCODE;
        }

        public void setOM_SEN_BRCODE(String OM_SEN_BRCODE) {
            this.OM_SEN_BRCODE = OM_SEN_BRCODE;
        }

        public String getOM_SM_IMEI() {
            return OM_SM_IMEI;
        }

        public void setOM_SM_IMEI(String OM_SM_IMEI) {
            this.OM_SM_IMEI = OM_SM_IMEI;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(OM_SM_EMPNAME);
            parcel.writeString(OM_SM_EMPID);
            parcel.writeInt(OM_SEN_SQNO);
            parcel.writeString(OM_SEN_BRCODE);
            parcel.writeString(OM_SM_IMEI);
        }
    }
}
