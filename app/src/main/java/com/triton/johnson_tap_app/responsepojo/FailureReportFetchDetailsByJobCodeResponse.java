package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private List<Data> Data;
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
        Data = in.readParcelable(FailureReportFetchDetailsByJobCodeResponse.Data.class.getClassLoader());
        Message = in.readString();
        Status = in.readString();
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

    public static class Dropdown {
        @Expose
        @SerializedName("reasoncode")
        private List<Reasoncode> reasoncode;
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

        public List<Reasoncode> getReasoncode() {
            return reasoncode;
        }

        public void setReasoncode(List<Reasoncode> reasoncode) {
            this.reasoncode = reasoncode;
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
    }

    public static class Reasoncode {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Ld_inside_lift {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Encoder_checked {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Serv_type {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Depart {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Bat_warr_status {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Bat_check_status {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Vvvf_trip_type {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Electric_supply {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Vvvf_trip_while {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Natu_failure {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Cuur_status {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Pys_condition {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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
    }

    public static class Matl_reture_type {
        @Expose
        @SerializedName("value")
        private String value;
        @Expose
        @SerializedName("display_name")
        private String display_name;

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

        @Override
        public int describeContents() {
            return 0;
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
    }
}
