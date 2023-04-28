package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FailureReportEditEngRequest {

    @Expose
    @SerializedName("file_image")
    private List<File_image> file_image;
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
    @SerializedName("bat_warranty_status")
    private String bat_warranty_status;
    @Expose
    @SerializedName("bat_check_status")
    private String bat_check_status;
    @Expose
    @SerializedName("electric_volt")
    private String electric_volt;
    @Expose
    @SerializedName("electric_supply")
    private String electric_supply;
    @Expose
    @SerializedName("load_inside_lift")
    private String load_inside_lift;
    @Expose
    @SerializedName("encoder_checked")
    private String encoder_checked;
    @Expose
    @SerializedName("vvvf_trip_type")
    private String vvvf_trip_type;
    @Expose
    @SerializedName("vvvf_trip_while")
    private String vvvf_trip_while;
    @Expose
    @SerializedName("vvf_item")
    private String vvf_item;
    @Expose
    @SerializedName("vvf_remarks")
    private String vvf_remarks;
    @Expose
    @SerializedName("nature_failure")
    private String nature_failure;
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
    @SerializedName("fr_no")
    private String fr_no;
    @Expose
    @SerializedName("comp_device_name")
    private String comp_device_name;
    @Expose
    @SerializedName("job_id")
    private String job_id;
    @Expose
    @SerializedName("br_code")
    private String br_code;
    @Expose
    @SerializedName("seq_no")
    private String seq_no;
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

    public List<File_image> getFile_image() {
        return file_image;
    }

    public void setFile_image(List<File_image> file_image) {
        this.file_image = file_image;
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

    public String getBat_warranty_status() {
        return bat_warranty_status;
    }

    public void setBat_warranty_status(String bat_warranty_status) {
        this.bat_warranty_status = bat_warranty_status;
    }

    public String getBat_check_status() {
        return bat_check_status;
    }

    public void setBat_check_status(String bat_check_status) {
        this.bat_check_status = bat_check_status;
    }

    public String getElectric_volt() {
        return electric_volt;
    }

    public void setElectric_volt(String electric_volt) {
        this.electric_volt = electric_volt;
    }

    public String getElectric_supply() {
        return electric_supply;
    }

    public void setElectric_supply(String electric_supply) {
        this.electric_supply = electric_supply;
    }

    public String getLoad_inside_lift() {
        return load_inside_lift;
    }

    public void setLoad_inside_lift(String load_inside_lift) {
        this.load_inside_lift = load_inside_lift;
    }

    public String getEncoder_checked() {
        return encoder_checked;
    }

    public void setEncoder_checked(String encoder_checked) {
        this.encoder_checked = encoder_checked;
    }

    public String getVvvf_trip_type() {
        return vvvf_trip_type;
    }

    public void setVvvf_trip_type(String vvvf_trip_type) {
        this.vvvf_trip_type = vvvf_trip_type;
    }

    public String getVvvf_trip_while() {
        return vvvf_trip_while;
    }

    public void setVvvf_trip_while(String vvvf_trip_while) {
        this.vvvf_trip_while = vvvf_trip_while;
    }

    public String getVvf_item() {
        return vvf_item;
    }

    public void setVvf_item(String vvf_item) {
        this.vvf_item = vvf_item;
    }

    public String getVvf_remarks() {
        return vvf_remarks;
    }

    public void setVvf_remarks(String vvf_remarks) {
        this.vvf_remarks = vvf_remarks;
    }

    public String getNature_failure() {
        return nature_failure;
    }

    public void setNature_failure(String nature_failure) {
        this.nature_failure = nature_failure;
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

    public String getFr_no() {
        return fr_no;
    }

    public void setFr_no(String fr_no) {
        this.fr_no = fr_no;
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

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
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

    public static class File_image {
        @Expose
        @SerializedName("image")
        private String image;

        public File_image(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
