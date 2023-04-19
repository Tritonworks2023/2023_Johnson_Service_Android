package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepairWorkRequestFetchListEngIdResponse implements Parcelable {
    public static final Creator<RepairWorkRequestFetchListEngIdResponse> CREATOR = new Creator<RepairWorkRequestFetchListEngIdResponse>() {
        @Override
        public RepairWorkRequestFetchListEngIdResponse createFromParcel(Parcel in) {
            return new RepairWorkRequestFetchListEngIdResponse(in);
        }

        @Override
        public RepairWorkRequestFetchListEngIdResponse[] newArray(int size) {
            return new RepairWorkRequestFetchListEngIdResponse[size];
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

    protected RepairWorkRequestFetchListEngIdResponse(Parcel in) {
        Code = in.readInt();
        Data = in.readParcelable(RepairWorkRequestFetchListEngIdResponse.Data.class.getClassLoader());
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
        @SerializedName("__v")
        private int __v;
        @Expose
        @SerializedName("delete_status")
        private boolean delete_status;
        @Expose
        @SerializedName("submitted_by_on")
        private String submitted_by_on;
        @Expose
        @SerializedName("submitted_by_name")
        private String submitted_by_name;
        @Expose
        @SerializedName("submitted_by_num")
        private String submitted_by_num;
        @Expose
        @SerializedName("submitted_by_emp_code")
        private String submitted_by_emp_code;
        @Expose
        @SerializedName("tech_code")
        private String tech_code;
        @Expose
        @SerializedName("tech_name")
        private String tech_name;
        @Expose
        @SerializedName("remarks")
        private String remarks;
        @Expose
        @SerializedName("mat_available_sts")
        private String mat_available_sts;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("route")
        private String route;
        @Expose
        @SerializedName("site_name")
        private String site_name;
        @Expose
        @SerializedName("job_id")
        private String job_id;
        @Expose
        @SerializedName("request_on")
        private String request_on;
        @Expose
        @SerializedName("_id")
        private String _id;

        public Data() {
        }

        protected Data(Parcel in) {
            __v = in.readInt();
            delete_status = in.readByte() != 0;
            submitted_by_on = in.readString();
            submitted_by_name = in.readString();
            submitted_by_num = in.readString();
            submitted_by_emp_code = in.readString();
            tech_code = in.readString();
            tech_name = in.readString();
            remarks = in.readString();
            mat_available_sts = in.readString();
            status = in.readString();
            route = in.readString();
            site_name = in.readString();
            job_id = in.readString();
            request_on = in.readString();
            _id = in.readString();
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public boolean getDelete_status() {
            return delete_status;
        }

        public void setDelete_status(boolean delete_status) {
            this.delete_status = delete_status;
        }

        public String getSubmitted_by_on() {
            return submitted_by_on;
        }

        public void setSubmitted_by_on(String submitted_by_on) {
            this.submitted_by_on = submitted_by_on;
        }

        public String getSubmitted_by_name() {
            return submitted_by_name;
        }

        public void setSubmitted_by_name(String submitted_by_name) {
            this.submitted_by_name = submitted_by_name;
        }

        public String getSubmitted_by_num() {
            return submitted_by_num;
        }

        public void setSubmitted_by_num(String submitted_by_num) {
            this.submitted_by_num = submitted_by_num;
        }

        public String getSubmitted_by_emp_code() {
            return submitted_by_emp_code;
        }

        public void setSubmitted_by_emp_code(String submitted_by_emp_code) {
            this.submitted_by_emp_code = submitted_by_emp_code;
        }

        public String getTech_code() {
            return tech_code;
        }

        public void setTech_code(String tech_code) {
            this.tech_code = tech_code;
        }

        public String getTech_name() {
            return tech_name;
        }

        public void setTech_name(String tech_name) {
            this.tech_name = tech_name;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getMat_available_sts() {
            return mat_available_sts;
        }

        public void setMat_available_sts(String mat_available_sts) {
            this.mat_available_sts = mat_available_sts;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getSite_name() {
            return site_name;
        }

        public void setSite_name(String site_name) {
            this.site_name = site_name;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getRequest_on() {
            return request_on;
        }

        public void setRequest_on(String request_on) {
            this.request_on = request_on;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeInt(__v);
            parcel.writeByte((byte) (delete_status ? 1 : 0));
            parcel.writeString(submitted_by_on);
            parcel.writeString(submitted_by_name);
            parcel.writeString(submitted_by_num);
            parcel.writeString(submitted_by_emp_code);
            parcel.writeString(tech_code);
            parcel.writeString(tech_name);
            parcel.writeString(remarks);
            parcel.writeString(mat_available_sts);
            parcel.writeString(status);
            parcel.writeString(route);
            parcel.writeString(site_name);
            parcel.writeString(job_id);
            parcel.writeString(request_on);
            parcel.writeString(_id);
        }
    }
}
