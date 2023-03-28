package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FailureReportFetchDetailsByJobCodeResponse implements Parcelable {

    public static final Creator<FailureReportFetchDetailsByJobCodeResponse> CREATOR = new Creator<FailureReportFetchDetailsByJobCodeResponse>() {
        @Override
        public FailureReportFetchDetailsByJobCodeResponse createFromParcel(Parcel in) {
            return new FailureReportFetchDetailsByJobCodeResponse(in);
        }

        @Override
        public FailureReportFetchDetailsByJobCodeResponse[] newArray(int size) {
            return new FailureReportFetchDetailsByJobCodeResponse[size];
        }
    };
    @Expose
    @SerializedName("Code")
    private int Code;
    @Expose
    @SerializedName("Dropdown")
    private Dropdown Dropdown;
    @Expose
    @SerializedName("Data")
    private ArrayList<FailureReportFetchDetailsByJobCodeResponse.Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private String Status;

    public FailureReportFetchDetailsByJobCodeResponse() {
    }

    protected FailureReportFetchDetailsByJobCodeResponse(Parcel in) {
        Code = in.readInt();
        Message = in.readString();
        Status = in.readString();
        Data = in.readParcelable(FailureReportFetchDetailsByJobCodeResponse.Data.class.getClassLoader());
        Dropdown = in.readParcelable(FailureReportFetchDetailsByJobCodeResponse.Dropdown.class.getClassLoader());
//        Data = in.createTypedArrayList(FailureReportFetchDetailsByJobCodeResponse.Data.CREATOR);
//        Data = in.readParcelable(FailureReportFetchDetailsByJobCodeResponse.Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(Code);
        parcel.writeString(Message);
        parcel.writeString(Status);
        parcel.writeTypedList(Data);
        parcel.writeParcelable(Dropdown, i);
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

    public Dropdown getDropdown() {
        return Dropdown;
    }

    public void setDropdown(Dropdown Dropdown) {
        this.Dropdown = Dropdown;
    }

    public ArrayList<FailureReportFetchDetailsByJobCodeResponse.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<FailureReportFetchDetailsByJobCodeResponse.Data> Data) {
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

    public static class Dropdown implements Parcelable {
        public static final Creator<FailureReportFetchDetailsByJobCodeResponse.Dropdown> CREATOR = new Creator<FailureReportFetchDetailsByJobCodeResponse.Dropdown>() {
            @Override
            public FailureReportFetchDetailsByJobCodeResponse.Dropdown createFromParcel(Parcel in) {
                return new Dropdown(in);
            }

            @Override
            public FailureReportFetchDetailsByJobCodeResponse.Dropdown[] newArray(int size) {
                return new Dropdown[size];
            }
        };
        @Expose
        @SerializedName("ld_inside_lift")
        private List<Ld_inside_lift> ld_inside_lift;
        @Expose
        @SerializedName("encoder_checked")
        private List<Encoder_checked> encoder_checked;
        @Expose
        @SerializedName("serv_type")
        private List<Serv_type> serv_type;
        @Expose
        @SerializedName("depart")
        private List<Depart> depart;
        @Expose
        @SerializedName("bat_warr_status")
        private List<Bat_warr_status> bat_warr_status;
        @Expose
        @SerializedName("bat_check_status")
        private List<Bat_check_status> bat_check_status;
        @Expose
        @SerializedName("vvvf_trip_type")
        private List<Vvvf_trip_type> vvvf_trip_type;
        @Expose
        @SerializedName("electric_supply")
        private List<Electric_supply> electric_supply;
        @Expose
        @SerializedName("vvvf_trip_while")
        private List<Vvvf_trip_while> vvvf_trip_while;
        @Expose
        @SerializedName("natu_failure")
        private List<Natu_failure> natu_failure;
        @Expose
        @SerializedName("cuur_status")
        private List<Cuur_status> cuur_status;
        @Expose
        @SerializedName("pys_condition")
        private List<Pys_condition> pys_condition;
        @Expose
        @SerializedName("matl_reture_type")
        private List<Matl_reture_type> matl_reture_type;

        protected Dropdown(Parcel in) {
            ld_inside_lift = in.createTypedArrayList(Ld_inside_lift.CREATOR);
            encoder_checked = in.createTypedArrayList(Encoder_checked.CREATOR);
            serv_type = in.createTypedArrayList(Serv_type.CREATOR);
            depart = in.createTypedArrayList(Depart.CREATOR);
            bat_warr_status = in.createTypedArrayList(Bat_warr_status.CREATOR);
            bat_check_status = in.createTypedArrayList(Bat_check_status.CREATOR);
            vvvf_trip_type = in.createTypedArrayList(Vvvf_trip_type.CREATOR);
            electric_supply = in.createTypedArrayList(Electric_supply.CREATOR);
            vvvf_trip_while = in.createTypedArrayList(Vvvf_trip_while.CREATOR);
            natu_failure = in.createTypedArrayList(Natu_failure.CREATOR);
            cuur_status = in.createTypedArrayList(Cuur_status.CREATOR);
            pys_condition = in.createTypedArrayList(Pys_condition.CREATOR);
            matl_reture_type = in.createTypedArrayList(Matl_reture_type.CREATOR);
        }

        public List<Ld_inside_lift> getLd_inside_lift() {
            return ld_inside_lift;
        }

        public void setLd_inside_lift(List<Ld_inside_lift> ld_inside_lift) {
            this.ld_inside_lift = ld_inside_lift;
        }

        public List<Encoder_checked> getEncoder_checked() {
            return encoder_checked;
        }

        public void setEncoder_checked(List<Encoder_checked> encoder_checked) {
            this.encoder_checked = encoder_checked;
        }

        public List<Serv_type> getServ_type() {
            return serv_type;
        }

        public void setServ_type(List<Serv_type> serv_type) {
            this.serv_type = serv_type;
        }

        public List<Depart> getDepart() {
            return depart;
        }

        public void setDepart(List<Depart> depart) {
            this.depart = depart;
        }

        public List<Bat_warr_status> getBat_warr_status() {
            return bat_warr_status;
        }

        public void setBat_warr_status(List<Bat_warr_status> bat_warr_status) {
            this.bat_warr_status = bat_warr_status;
        }

        public List<Bat_check_status> getBat_check_status() {
            return bat_check_status;
        }

        public void setBat_check_status(List<Bat_check_status> bat_check_status) {
            this.bat_check_status = bat_check_status;
        }

        public List<Vvvf_trip_type> getVvvf_trip_type() {
            return vvvf_trip_type;
        }

        public void setVvvf_trip_type(List<Vvvf_trip_type> vvvf_trip_type) {
            this.vvvf_trip_type = vvvf_trip_type;
        }

        public List<Electric_supply> getElectric_supply() {
            return electric_supply;
        }

        public void setElectric_supply(List<Electric_supply> electric_supply) {
            this.electric_supply = electric_supply;
        }

        public List<Vvvf_trip_while> getVvvf_trip_while() {
            return vvvf_trip_while;
        }

        public void setVvvf_trip_while(List<Vvvf_trip_while> vvvf_trip_while) {
            this.vvvf_trip_while = vvvf_trip_while;
        }

        public List<Natu_failure> getNatu_failure() {
            return natu_failure;
        }

        public void setNatu_failure(List<Natu_failure> natu_failure) {
            this.natu_failure = natu_failure;
        }

        public List<Cuur_status> getCuur_status() {
            return cuur_status;
        }

        public void setCuur_status(List<Cuur_status> cuur_status) {
            this.cuur_status = cuur_status;
        }

        public List<Pys_condition> getPys_condition() {
            return pys_condition;
        }

        public void setPys_condition(List<Pys_condition> pys_condition) {
            this.pys_condition = pys_condition;
        }

        public List<Matl_reture_type> getMatl_reture_type() {
            return matl_reture_type;
        }

        public void setMatl_reture_type(List<Matl_reture_type> matl_reture_type) {
            this.matl_reture_type = matl_reture_type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeTypedList(ld_inside_lift);
            parcel.writeTypedList(encoder_checked);
            parcel.writeTypedList(serv_type);
            parcel.writeTypedList(depart);
            parcel.writeTypedList(bat_warr_status);
            parcel.writeTypedList(bat_check_status);
            parcel.writeTypedList(vvvf_trip_type);
            parcel.writeTypedList(electric_supply);
            parcel.writeTypedList(vvvf_trip_while);
            parcel.writeTypedList(natu_failure);
            parcel.writeTypedList(cuur_status);
            parcel.writeTypedList(pys_condition);
            parcel.writeTypedList(matl_reture_type);
        }
    }

    public static class Ld_inside_lift implements Parcelable {
        public static final Creator<Ld_inside_lift> CREATOR = new Creator<Ld_inside_lift>() {
            @Override
            public Ld_inside_lift createFromParcel(Parcel in) {
                return new Ld_inside_lift(in);
            }

            @Override
            public Ld_inside_lift[] newArray(int size) {
                return new Ld_inside_lift[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Ld_inside_lift(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Encoder_checked implements Parcelable {
        public static final Creator<Encoder_checked> CREATOR = new Creator<Encoder_checked>() {
            @Override
            public Encoder_checked createFromParcel(Parcel in) {
                return new Encoder_checked(in);
            }

            @Override
            public Encoder_checked[] newArray(int size) {
                return new Encoder_checked[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Encoder_checked(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Serv_type implements Parcelable {
        public static final Creator<Serv_type> CREATOR = new Creator<Serv_type>() {
            @Override
            public Serv_type createFromParcel(Parcel in) {
                return new Serv_type(in);
            }

            @Override
            public Serv_type[] newArray(int size) {
                return new Serv_type[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Serv_type(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Depart implements Parcelable {
        public static final Creator<Depart> CREATOR = new Creator<Depart>() {
            @Override
            public Depart createFromParcel(Parcel in) {
                return new Depart(in);
            }

            @Override
            public Depart[] newArray(int size) {
                return new Depart[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Depart(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Bat_warr_status implements Parcelable {
        public static final Creator<Bat_warr_status> CREATOR = new Creator<Bat_warr_status>() {
            @Override
            public Bat_warr_status createFromParcel(Parcel in) {
                return new Bat_warr_status(in);
            }

            @Override
            public Bat_warr_status[] newArray(int size) {
                return new Bat_warr_status[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Bat_warr_status(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Bat_check_status implements Parcelable {
        public static final Creator<Bat_check_status> CREATOR = new Creator<Bat_check_status>() {
            @Override
            public Bat_check_status createFromParcel(Parcel in) {
                return new Bat_check_status(in);
            }

            @Override
            public Bat_check_status[] newArray(int size) {
                return new Bat_check_status[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Bat_check_status(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Vvvf_trip_type implements Parcelable {
        public static final Creator<Vvvf_trip_type> CREATOR = new Creator<Vvvf_trip_type>() {
            @Override
            public Vvvf_trip_type createFromParcel(Parcel in) {
                return new Vvvf_trip_type(in);
            }

            @Override
            public Vvvf_trip_type[] newArray(int size) {
                return new Vvvf_trip_type[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Vvvf_trip_type(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Electric_supply implements Parcelable {
        public static final Creator<Electric_supply> CREATOR = new Creator<Electric_supply>() {
            @Override
            public Electric_supply createFromParcel(Parcel in) {
                return new Electric_supply(in);
            }

            @Override
            public Electric_supply[] newArray(int size) {
                return new Electric_supply[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Electric_supply(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Vvvf_trip_while implements Parcelable {
        public static final Creator<Vvvf_trip_while> CREATOR = new Creator<Vvvf_trip_while>() {
            @Override
            public Vvvf_trip_while createFromParcel(Parcel in) {
                return new Vvvf_trip_while(in);
            }

            @Override
            public Vvvf_trip_while[] newArray(int size) {
                return new Vvvf_trip_while[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Vvvf_trip_while(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Natu_failure implements Parcelable {
        public static final Creator<Natu_failure> CREATOR = new Creator<Natu_failure>() {
            @Override
            public Natu_failure createFromParcel(Parcel in) {
                return new Natu_failure(in);
            }

            @Override
            public Natu_failure[] newArray(int size) {
                return new Natu_failure[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Natu_failure(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Cuur_status implements Parcelable {
        public static final Creator<Cuur_status> CREATOR = new Creator<Cuur_status>() {
            @Override
            public Cuur_status createFromParcel(Parcel in) {
                return new Cuur_status(in);
            }

            @Override
            public Cuur_status[] newArray(int size) {
                return new Cuur_status[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Cuur_status(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Pys_condition implements Parcelable {
        public static final Creator<Pys_condition> CREATOR = new Creator<Pys_condition>() {
            @Override
            public Pys_condition createFromParcel(Parcel in) {
                return new Pys_condition(in);
            }

            @Override
            public Pys_condition[] newArray(int size) {
                return new Pys_condition[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Pys_condition(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Matl_reture_type implements Parcelable {
        public static final Creator<Matl_reture_type> CREATOR = new Creator<Matl_reture_type>() {
            @Override
            public Matl_reture_type createFromParcel(Parcel in) {
                return new Matl_reture_type(in);
            }

            @Override
            public Matl_reture_type[] newArray(int size) {
                return new Matl_reture_type[size];
            }
        };
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

        protected Matl_reture_type(Parcel in) {
            value = in.readString();
            display_name = in.readString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(value);
            parcel.writeString(display_name);
        }
    }

    public static class Data implements Parcelable {

        public static final Creator<FailureReportFetchDetailsByJobCodeResponse.Data> CREATOR = new Creator<FailureReportFetchDetailsByJobCodeResponse.Data>() {
            @Override
            public FailureReportFetchDetailsByJobCodeResponse.Data createFromParcel(Parcel in) {
                return new FailureReportFetchDetailsByJobCodeResponse.Data(in);
            }

            @Override
            public FailureReportFetchDetailsByJobCodeResponse.Data[] newArray(int size) {
                return new FailureReportFetchDetailsByJobCodeResponse.Data[size];
            }
        };
        @Expose
        @SerializedName("serv_type")
        private String serv_type;
        @Expose
        @SerializedName("department")
        private String department;
        @Expose
        @SerializedName("malt_id")
        private String malt_id;
        @Expose
        @SerializedName("comp_device_name")
        private String comp_device_name;
        @Expose
        @SerializedName("comp_device")
        private String comp_device;
        @Expose
        @SerializedName("bar_code_job_no")
        private String bar_code_job_no;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("job_id")
        private String job_id;
        @Expose
        @SerializedName("customer_address")
        private String customer_address;
        @Expose
        @SerializedName("install_address")
        private String install_address;

        public Data() {
        }

        protected Data(Parcel in) {
            serv_type = in.readString();
            department = in.readString();
            malt_id = in.readString();
            comp_device_name = in.readString();
            comp_device = in.readString();
            bar_code_job_no = in.readString();
            status = in.readString();
            job_id = in.readString();
            customer_address = in.readString();
            install_address = in.readString();
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(serv_type);
            parcel.writeString(department);
            parcel.writeString(malt_id);
            parcel.writeString(comp_device_name);
            parcel.writeString(comp_device);
            parcel.writeString(bar_code_job_no);
            parcel.writeString(status);
            parcel.writeString(job_id);
            parcel.writeString(customer_address);
            parcel.writeString(install_address);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public String getServ_type() {
            return serv_type;
        }

        public void setServ_type(String serv_type) {
            this.serv_type = serv_type;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

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

        public String getComp_device() {
            return comp_device;
        }

        public void setComp_device(String comp_device) {
            this.comp_device = comp_device;
        }

        public String getBar_code_job_no() {
            return bar_code_job_no;
        }

        public void setBar_code_job_no(String bar_code_job_no) {
            this.bar_code_job_no = bar_code_job_no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }

        public String getInstall_address() {
            return install_address;
        }

        public void setInstall_address(String install_address) {
            this.install_address = install_address;
        }
    }
}
