package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewJobListServiceVisibilityResponse implements Parcelable {
    public static final Creator<NewJobListServiceVisibilityResponse> CREATOR = new Creator<NewJobListServiceVisibilityResponse>() {
        @Override
        public NewJobListServiceVisibilityResponse createFromParcel(Parcel in) {
            return new NewJobListServiceVisibilityResponse(in);
        }

        @Override
        public NewJobListServiceVisibilityResponse[] newArray(int size) {
            return new NewJobListServiceVisibilityResponse[size];
        }
    };
    @Expose
    @SerializedName("Code")
    private int Code;
    @Expose
    @SerializedName("Data")
    private ArrayList<NewJobListServiceVisibilityResponse.Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private String Status;

    public NewJobListServiceVisibilityResponse() {
    }

    protected NewJobListServiceVisibilityResponse(Parcel in) {
        Code = in.readInt();
        Data = in.readParcelable(NewJobListServiceVisibilityResponse.Data.class.getClassLoader());
        Message = in.readString();
        Status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Code);
        dest.writeString(Message);
        dest.writeString(Status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public ArrayList<NewJobListServiceVisibilityResponse.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<NewJobListServiceVisibilityResponse.Data> Data) {
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

    public static class Data implements Parcelable {

        public static final Creator<NewJobListServiceVisibilityResponse.Data> CREATOR = new Creator<NewJobListServiceVisibilityResponse.Data>() {
            @Override
            public NewJobListServiceVisibilityResponse.Data createFromParcel(Parcel in) {
                return new NewJobListServiceVisibilityResponse.Data(in);
            }

            @Override
            public NewJobListServiceVisibilityResponse.Data[] newArray(int size) {
                return new NewJobListServiceVisibilityResponse.Data[size];
            }
        };

        @Expose
        @SerializedName("OM_SED_SLNO")
        private int OM_SED_SLNO;
        @Expose
        @SerializedName("INST_ON")
        private String INST_ON;
        @Expose
        @SerializedName("PINCODE")
        private String PINCODE = "";
        @Expose
        @SerializedName("LANDMARK")
        private String LANDMARK = "";
        @Expose
        @SerializedName("INST_ADD3")
        private String INST_ADD3 = "";
        @Expose
        @SerializedName("INST_ADD")
        private String INST_ADD = "";
        @Expose
        @SerializedName("INST_ADD1")
        private String INST_ADD1 = "";
        @Expose
        @SerializedName("CUST_NAME")
        private String CUST_NAME;
        @Expose
        @SerializedName("JOBNO")
        private String JOBNO;
        @Expose
        @SerializedName("imie_no")
        private String imie_no;
        @Expose
        @SerializedName("insp_by")
        private String insp_by;

        public Data() {
        }

        protected Data(Parcel in) {
            OM_SED_SLNO = in.readInt();
            INST_ON = in.readString();
            PINCODE = in.readString();
            LANDMARK = in.readString();
            INST_ADD3 = in.readString();
            INST_ADD = in.readString();
            INST_ADD1 = in.readString();
            CUST_NAME = in.readString();
            JOBNO = in.readString();
            imie_no = in.readString();
            insp_by = in.readString();
        }

        public String getImie_no() {
            return imie_no;
        }

        public void setImie_no(String imie_no) {
            this.imie_no = imie_no;
        }

        public String getInsp_by() {
            return insp_by;
        }

        public void setInsp_by(String insp_by) {
            this.insp_by = insp_by;
        }

        public int getOM_SED_SLNO() {
            return OM_SED_SLNO;
        }

        public void setOM_SED_SLNO(int OM_SED_SLNO) {
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
            parcel.writeInt(OM_SED_SLNO);
            parcel.writeString(INST_ON);
            parcel.writeString(PINCODE);
            parcel.writeString(LANDMARK);
            parcel.writeString(INST_ADD3);
            parcel.writeString(INST_ADD);
            parcel.writeString(INST_ADD1);
            parcel.writeString(CUST_NAME);
            parcel.writeString(JOBNO);
            parcel.writeString(imie_no);
            parcel.writeString(insp_by);
        }
    }
}
