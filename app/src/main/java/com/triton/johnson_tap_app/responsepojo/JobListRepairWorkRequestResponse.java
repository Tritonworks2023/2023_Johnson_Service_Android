package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobListRepairWorkRequestResponse implements Parcelable {

    public static final Creator<JobListRepairWorkRequestResponse> CREATOR = new Creator<JobListRepairWorkRequestResponse>() {
        @Override
        public JobListRepairWorkRequestResponse createFromParcel(Parcel in) {
            return new JobListRepairWorkRequestResponse(in);
        }

        @Override
        public JobListRepairWorkRequestResponse[] newArray(int size) {
            return new JobListRepairWorkRequestResponse[size];
        }
    };
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

    protected JobListRepairWorkRequestResponse(Parcel in) {
        Code = in.readInt();
        Data = in.readParcelable(JobListRepairWorkRequestResponse.Data.class.getClassLoader());
        Message = in.readString();
        Status = in.readString();
    }

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
        @SerializedName("ZPROUTECD")
        private String ZPROUTECD;
        @Expose
        @SerializedName("EPROUTECD")
        private String EPROUTECD;
        @Expose
        @SerializedName("zone_name")
        private String zone_name;
        @Expose
        @SerializedName("zone_code")
        private String zone_code;
        @Expose
        @SerializedName("mech_name")
        private String mech_name;
        @Expose
        @SerializedName("mech_code")
        private String mech_code;
        @Expose
        @SerializedName("tech_name")
        private String tech_name;
        @Expose
        @SerializedName("tech_code")
        private String tech_code;
        @Expose
        @SerializedName("insp_by")
        private String insp_by;
        @Expose
        @SerializedName("imie_no")
        private String imie_no;
        @Expose
        @SerializedName("SROUTE")
        private String SROUTE;
        @Expose
        @SerializedName("OM_SED_SLNO")
        private String OM_SED_SLNO;
        @Expose
        @SerializedName("INST_ON")
        private String INST_ON;
        @Expose
        @SerializedName("PINCODE")
        private String PINCODE;
        @Expose
        @SerializedName("LANDMARK")
        private String LANDMARK;
        @Expose
        @SerializedName("INST_ADD3")
        private String INST_ADD3;
        @Expose
        @SerializedName("INST_ADD")
        private String INST_ADD;
        @Expose
        @SerializedName("INST_ADD1")
        private String INST_ADD1;
        @Expose
        @SerializedName("CUST_NAME")
        private String CUST_NAME;
        @Expose
        @SerializedName("BRCODE")
        private String BRCODE;
        @Expose
        @SerializedName("JOBNO")
        private String JOBNO;

        public Data() {
        }

        protected Data(Parcel in) {
            ZPROUTECD = in.readString();
            EPROUTECD = in.readString();
            zone_name = in.readString();
            zone_code = in.readString();
            mech_name = in.readString();
            mech_code = in.readString();
            tech_name = in.readString();
            tech_code = in.readString();
            insp_by = in.readString();
            imie_no = in.readString();
            SROUTE = in.readString();
            OM_SED_SLNO = in.readString();
            INST_ON = in.readString();
            PINCODE = in.readString();
            LANDMARK = in.readString();
            INST_ADD3 = in.readString();
            INST_ADD = in.readString();
            INST_ADD1 = in.readString();
            CUST_NAME = in.readString();
            BRCODE = in.readString();
            JOBNO = in.readString();
        }

        public String getZPROUTECD() {
            return ZPROUTECD;
        }

        public void setZPROUTECD(String ZPROUTECD) {
            this.ZPROUTECD = ZPROUTECD;
        }

        public String getEPROUTECD() {
            return EPROUTECD;
        }

        public void setEPROUTECD(String EPROUTECD) {
            this.EPROUTECD = EPROUTECD;
        }

        public String getZone_name() {
            return zone_name;
        }

        public void setZone_name(String zone_name) {
            this.zone_name = zone_name;
        }

        public String getZone_code() {
            return zone_code;
        }

        public void setZone_code(String zone_code) {
            this.zone_code = zone_code;
        }

        public String getMech_name() {
            return mech_name;
        }

        public void setMech_name(String mech_name) {
            this.mech_name = mech_name;
        }

        public String getMech_code() {
            return mech_code;
        }

        public void setMech_code(String mech_code) {
            this.mech_code = mech_code;
        }

        public String getTech_name() {
            return tech_name;
        }

        public void setTech_name(String tech_name) {
            this.tech_name = tech_name;
        }

        public String getTech_code() {
            return tech_code;
        }

        public void setTech_code(String tech_code) {
            this.tech_code = tech_code;
        }

        public String getInsp_by() {
            return insp_by;
        }

        public void setInsp_by(String insp_by) {
            this.insp_by = insp_by;
        }

        public String getImie_no() {
            return imie_no;
        }

        public void setImie_no(String imie_no) {
            this.imie_no = imie_no;
        }

        public String getSROUTE() {
            return SROUTE;
        }

        public void setSROUTE(String SROUTE) {
            this.SROUTE = SROUTE;
        }

        public String getOM_SED_SLNO() {
            return OM_SED_SLNO;
        }

        public void setOM_SED_SLNO(String OM_SED_SLNO) {
            this.OM_SED_SLNO = OM_SED_SLNO;
        }

        public String getINST_ON() {
            return INST_ON;
        }

        public void setINST_ON(String INST_ON) {
            this.INST_ON = INST_ON;
        }

        public String getPINCODE() {
            return PINCODE;
        }

        public void setPINCODE(String PINCODE) {
            this.PINCODE = PINCODE;
        }

        public String getLANDMARK() {
            return LANDMARK;
        }

        public void setLANDMARK(String LANDMARK) {
            this.LANDMARK = LANDMARK;
        }

        public String getINST_ADD3() {
            return INST_ADD3;
        }

        public void setINST_ADD3(String INST_ADD3) {
            this.INST_ADD3 = INST_ADD3;
        }

        public String getINST_ADD() {
            return INST_ADD;
        }

        public void setINST_ADD(String INST_ADD) {
            this.INST_ADD = INST_ADD;
        }

        public String getINST_ADD1() {
            return INST_ADD1;
        }

        public void setINST_ADD1(String INST_ADD1) {
            this.INST_ADD1 = INST_ADD1;
        }

        public String getCUST_NAME() {
            return CUST_NAME;
        }

        public void setCUST_NAME(String CUST_NAME) {
            this.CUST_NAME = CUST_NAME;
        }

        public String getBRCODE() {
            return BRCODE;
        }

        public void setBRCODE(String BRCODE) {
            this.BRCODE = BRCODE;
        }

        public String getJOBNO() {
            return JOBNO;
        }

        public void setJOBNO(String JOBNO) {
            this.JOBNO = JOBNO;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(ZPROUTECD);
            parcel.writeString(EPROUTECD);
            parcel.writeString(zone_name);
            parcel.writeString(zone_code);
            parcel.writeString(mech_name);
            parcel.writeString(mech_code);
            parcel.writeString(tech_name);
            parcel.writeString(tech_code);
            parcel.writeString(insp_by);
            parcel.writeString(imie_no);
            parcel.writeString(SROUTE);
            parcel.writeString(OM_SED_SLNO);
            parcel.writeString(INST_ON);
            parcel.writeString(PINCODE);
            parcel.writeString(LANDMARK);
            parcel.writeString(INST_ADD3);
            parcel.writeString(INST_ADD);
            parcel.writeString(INST_ADD1);
            parcel.writeString(CUST_NAME);
            parcel.writeString(BRCODE);
            parcel.writeString(JOBNO);
        }
    }
}
