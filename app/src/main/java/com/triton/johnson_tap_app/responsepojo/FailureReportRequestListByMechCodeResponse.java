package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FailureReportRequestListByMechCodeResponse implements Parcelable {

    public static final Creator<FailureReportRequestListByMechCodeResponse> CREATOR = new Creator<FailureReportRequestListByMechCodeResponse>() {
        @Override
        public FailureReportRequestListByMechCodeResponse createFromParcel(Parcel in) {
            return new FailureReportRequestListByMechCodeResponse(in);
        }

        @Override
        public FailureReportRequestListByMechCodeResponse[] newArray(int size) {
            return new FailureReportRequestListByMechCodeResponse[size];
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

    protected FailureReportRequestListByMechCodeResponse(Parcel in) {
        Code = in.readInt();
        Data = in.readParcelable(FailureReportRequestListByMechCodeResponse.Data.class.getClassLoader());
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
        @SerializedName("submitted_by_on")
        private String submitted_by_on;
        @Expose
        @SerializedName("submitted_by_num")
        private String submitted_by_num;
        @Expose
        @SerializedName("submitted_by_name")
        private String submitted_by_name;
        @Expose
        @SerializedName("submitted_by_emp_code")
        private String submitted_by_emp_code;
        @Expose
        @SerializedName("__v")
        private int __v;
        @Expose
        @SerializedName("delete_status")
        private boolean delete_status;
        @Expose
        @SerializedName("app_status")
        private String app_status;
        @Expose
        @SerializedName("customer_address")
        private String customer_address;
        @Expose
        @SerializedName("ins_address")
        private String ins_address;
        @Expose
        @SerializedName("prvlss_no")
        private String prvlss_no;
        @Expose
        @SerializedName("curlss_no")
        private String curlss_no;
        @Expose
        @SerializedName("route_code")
        private String route_code;
        @Expose
        @SerializedName("reason_code")
        private String reason_code;
        @Expose
        @SerializedName("eng_name")
        private String eng_name;
        @Expose
        @SerializedName("eng_code")
        private String eng_code;
        @Expose
        @SerializedName("mech_name")
        private String mech_name;
        @Expose
        @SerializedName("mech_code")
        private String mech_code;
        @Expose
        @SerializedName("tech_comment")
        private String tech_comment;
        @Expose
        @SerializedName("curr_status")
        private String curr_status;
        @Expose
        @SerializedName("phys_cond")
        private String phys_cond;
        @Expose
        @SerializedName("inst_date")
        private String inst_date;
        @Expose
        @SerializedName("supply_vol")
        private String supply_vol;
        @Expose
        @SerializedName("observation")
        private String observation;
        @Expose
        @SerializedName("failure_date")
        private String failure_date;
        @Expose
        @SerializedName("serial_no")
        private String serial_no;
        @Expose
        @SerializedName("rating")
        private String rating;
        @Expose
        @SerializedName("model_make")
        private String model_make;
        @Expose
        @SerializedName("serv_type")
        private String serv_type;
        @Expose
        @SerializedName("depart_name")
        private String depart_name;
        @Expose
        @SerializedName("comp_device_no")
        private String comp_device_no;
        @Expose
        @SerializedName("comp_device_name")
        private String comp_device_name;
        @Expose
        @SerializedName("job_id")
        private String job_id = "";
        @Expose
        @SerializedName("br_code")
        private String br_code;
        @Expose
        @SerializedName("matl_id")
        private String matl_id;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("qr_bar_code")
        private String qr_bar_code;
        @Expose
        @SerializedName("matl_return_type")
        private String matl_return_type;
        @Expose
        @SerializedName("_id")
        private String _id;
        @Expose
        @SerializedName("file_image")
        private List<File_image> file_image;

        public Data() {
        }

        public Data(Parcel in) {
            submitted_by_on = in.readString();
            submitted_by_num = in.readString();
            submitted_by_name = in.readString();
            submitted_by_emp_code = in.readString();
            __v = in.readInt();
            delete_status = in.readByte() != 0;
            app_status = in.readString();
            customer_address = in.readString();
            ins_address = in.readString();
            prvlss_no = in.readString();
            curlss_no = in.readString();
            route_code = in.readString();
            reason_code = in.readString();
            eng_name = in.readString();
            eng_code = in.readString();
            mech_name = in.readString();
            mech_code = in.readString();
            tech_comment = in.readString();
            curr_status = in.readString();
            phys_cond = in.readString();
            inst_date = in.readString();
            supply_vol = in.readString();
            observation = in.readString();
            failure_date = in.readString();
            serial_no = in.readString();
            rating = in.readString();
            model_make = in.readString();
            serv_type = in.readString();
            depart_name = in.readString();
            comp_device_no = in.readString();
            comp_device_name = in.readString();
            job_id = in.readString();
            br_code = in.readString();
            matl_id = in.readString();
            status = in.readString();
            qr_bar_code = in.readString();
            matl_return_type = in.readString();
            _id = in.readString();
            file_image = in.createTypedArrayList(File_image.CREATOR);
        }

        public String getSubmitted_by_on() {
            return submitted_by_on;
        }

        public void setSubmitted_by_on(String submitted_by_on) {
            this.submitted_by_on = submitted_by_on;
        }

        public String getSubmitted_by_num() {
            return submitted_by_num;
        }

        public void setSubmitted_by_num(String submitted_by_num) {
            this.submitted_by_num = submitted_by_num;
        }

        public String getSubmitted_by_name() {
            return submitted_by_name;
        }

        public void setSubmitted_by_name(String submitted_by_name) {
            this.submitted_by_name = submitted_by_name;
        }

        public String getSubmitted_by_emp_code() {
            return submitted_by_emp_code;
        }

        public void setSubmitted_by_emp_code(String submitted_by_emp_code) {
            this.submitted_by_emp_code = submitted_by_emp_code;
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

        public String getApp_status() {
            return app_status;
        }

        public void setApp_status(String app_status) {
            this.app_status = app_status;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }

        public String getIns_address() {
            return ins_address;
        }

        public void setIns_address(String ins_address) {
            this.ins_address = ins_address;
        }

        public String getPrvlss_no() {
            return prvlss_no;
        }

        public void setPrvlss_no(String prvlss_no) {
            this.prvlss_no = prvlss_no;
        }

        public String getCurlss_no() {
            return curlss_no;
        }

        public void setCurlss_no(String curlss_no) {
            this.curlss_no = curlss_no;
        }

        public String getRoute_code() {
            return route_code;
        }

        public void setRoute_code(String route_code) {
            this.route_code = route_code;
        }

        public String getReason_code() {
            return reason_code;
        }

        public void setReason_code(String reason_code) {
            this.reason_code = reason_code;
        }

        public String getEng_name() {
            return eng_name;
        }

        public void setEng_name(String eng_name) {
            this.eng_name = eng_name;
        }

        public String getEng_code() {
            return eng_code;
        }

        public void setEng_code(String eng_code) {
            this.eng_code = eng_code;
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

        public String getTech_comment() {
            return tech_comment;
        }

        public void setTech_comment(String tech_comment) {
            this.tech_comment = tech_comment;
        }

        public String getCurr_status() {
            return curr_status;
        }

        public void setCurr_status(String curr_status) {
            this.curr_status = curr_status;
        }

        public String getPhys_cond() {
            return phys_cond;
        }

        public void setPhys_cond(String phys_cond) {
            this.phys_cond = phys_cond;
        }

        public String getInst_date() {
            return inst_date;
        }

        public void setInst_date(String inst_date) {
            this.inst_date = inst_date;
        }

        public String getSupply_vol() {
            return supply_vol;
        }

        public void setSupply_vol(String supply_vol) {
            this.supply_vol = supply_vol;
        }

        public String getObservation() {
            return observation;
        }

        public void setObservation(String observation) {
            this.observation = observation;
        }

        public String getFailure_date() {
            return failure_date;
        }

        public void setFailure_date(String failure_date) {
            this.failure_date = failure_date;
        }

        public String getSerial_no() {
            return serial_no;
        }

        public void setSerial_no(String serial_no) {
            this.serial_no = serial_no;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getModel_make() {
            return model_make;
        }

        public void setModel_make(String model_make) {
            this.model_make = model_make;
        }

        public String getServ_type() {
            return serv_type;
        }

        public void setServ_type(String serv_type) {
            this.serv_type = serv_type;
        }

        public String getDepart_name() {
            return depart_name;
        }

        public void setDepart_name(String depart_name) {
            this.depart_name = depart_name;
        }

        public String getComp_device_no() {
            return comp_device_no;
        }

        public void setComp_device_no(String comp_device_no) {
            this.comp_device_no = comp_device_no;
        }

        public String getComp_device_name() {
            return comp_device_name;
        }

        public void setComp_device_name(String comp_device_name) {
            this.comp_device_name = comp_device_name;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getBr_code() {
            return br_code;
        }

        public void setBr_code(String br_code) {
            this.br_code = br_code;
        }

        public String getMatl_id() {
            return matl_id;
        }

        public void setMatl_id(String matl_id) {
            this.matl_id = matl_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getQr_bar_code() {
            return qr_bar_code;
        }

        public void setQr_bar_code(String qr_bar_code) {
            this.qr_bar_code = qr_bar_code;
        }

        public String getMatl_return_type() {
            return matl_return_type;
        }

        public void setMatl_return_type(String matl_return_type) {
            this.matl_return_type = matl_return_type;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public List<File_image> getFile_image() {
            return file_image;
        }

        public void setFile_image(List<File_image> file_image) {
            this.file_image = file_image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(submitted_by_on);
            parcel.writeString(submitted_by_num);
            parcel.writeString(submitted_by_name);
            parcel.writeString(submitted_by_emp_code);
            parcel.writeInt(__v);
            parcel.writeByte((byte) (delete_status ? 1 : 0));
            parcel.writeString(app_status);
            parcel.writeString(customer_address);
            parcel.writeString(ins_address);
            parcel.writeString(prvlss_no);
            parcel.writeString(curlss_no);
            parcel.writeString(route_code);
            parcel.writeString(reason_code);
            parcel.writeString(eng_name);
            parcel.writeString(eng_code);
            parcel.writeString(mech_name);
            parcel.writeString(mech_code);
            parcel.writeString(tech_comment);
            parcel.writeString(curr_status);
            parcel.writeString(phys_cond);
            parcel.writeString(inst_date);
            parcel.writeString(supply_vol);
            parcel.writeString(observation);
            parcel.writeString(failure_date);
            parcel.writeString(serial_no);
            parcel.writeString(rating);
            parcel.writeString(model_make);
            parcel.writeString(serv_type);
            parcel.writeString(depart_name);
            parcel.writeString(comp_device_no);
            parcel.writeString(comp_device_name);
            parcel.writeString(job_id);
            parcel.writeString(br_code);
            parcel.writeString(matl_id);
            parcel.writeString(status);
            parcel.writeString(qr_bar_code);
            parcel.writeString(matl_return_type);
            parcel.writeString(_id);
            parcel.writeTypedList(file_image);
        }
    }

    public static class File_image implements Parcelable {
        public static final Creator<File_image> CREATOR = new Creator<File_image>() {
            @Override
            public File_image createFromParcel(Parcel in) {
                return new File_image(in);
            }

            @Override
            public File_image[] newArray(int size) {
                return new File_image[size];
            }
        };
        @Expose
        @SerializedName("image")
        private String image;

        protected File_image(Parcel in) {
            image = in.readString();
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(image);
        }
    }
}
