package com.triton.johnson_tap_app.responsepojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RepairWorkRequestApprovalRequestListRpEngResponse implements Parcelable {
    public static final Creator<RepairWorkRequestApprovalRequestListRpEngResponse> CREATOR = new Creator<RepairWorkRequestApprovalRequestListRpEngResponse>() {
        @Override
        public RepairWorkRequestApprovalRequestListRpEngResponse createFromParcel(Parcel in) {
            return new RepairWorkRequestApprovalRequestListRpEngResponse(in);
        }

        @Override
        public RepairWorkRequestApprovalRequestListRpEngResponse[] newArray(int size) {
            return new RepairWorkRequestApprovalRequestListRpEngResponse[size];
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

    protected RepairWorkRequestApprovalRequestListRpEngResponse(Parcel in) {
        Code = in.readInt();
        Data = in.readParcelable(RepairWorkRequestApprovalRequestListRpEngResponse.Data.class.getClassLoader());
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
        @SerializedName("repair_work_mech_no")
        private String repair_work_mech_no;
        @Expose
        @SerializedName("repair_work_mech_name")
        private String repair_work_mech_name;
        @Expose
        @SerializedName("repair_work_mech_id")
        private String repair_work_mech_id;
        @Expose
        @SerializedName("pref_id")
        private String pref_id;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("completed_date")
        private String completed_date;
        @Expose
        @SerializedName("barricate_main_req")
        private String barricate_main_req;
        @Expose
        @SerializedName("dshackle_req")
        private String dshackle_req;
        @Expose
        @SerializedName("rebelling_clamp")
        private String rebelling_clamp;
        @Expose
        @SerializedName("ms_pipe_req")
        private String ms_pipe_req;
        @Expose
        @SerializedName("ladder_req")
        private String ladder_req;
        @Expose
        @SerializedName("webbing_belt")
        private String webbing_belt;
        @Expose
        @SerializedName("chain_block_capacity")
        private String chain_block_capacity;
        @Expose
        @SerializedName("chain_block_status")
        private String chain_block_status;
        @Expose
        @SerializedName("hand_gloves")
        private String hand_gloves;
        @Expose
        @SerializedName("safety_shoes")
        private String safety_shoes;
        @Expose
        @SerializedName("hard_hat")
        private String hard_hat;
        @Expose
        @SerializedName("full_body_harness")
        private String full_body_harness;
        @Expose
        @SerializedName("first_aid_kit")
        private String first_aid_kit;
        @Expose
        @SerializedName("repair_toolkit")
        private String repair_toolkit;
        @Expose
        @SerializedName("material_available_site")
        private String material_available_site;
        @Expose
        @SerializedName("mr_no")
        private String mr_no;
        @Expose
        @SerializedName("man_power")
        private String man_power;
        @Expose
        @SerializedName("tech_trained")
        private String tech_trained;
        @Expose
        @SerializedName("work_expected_date")
        private String work_expected_date;
        @Expose
        @SerializedName("work_start_date")
        private String work_start_date;
        @Expose
        @SerializedName("other_process")
        private String other_process;
        @Expose
        @SerializedName("nature_of_work_process_name")
        private String nature_of_work_process_name;
        @Expose
        @SerializedName("nature_of_work_process_id")
        private String nature_of_work_process_id;
        @Expose
        @SerializedName("nature_of_work_name")
        private String nature_of_work_name;
        @Expose
        @SerializedName("nature_of_work_id")
        private String nature_of_work_id;
        @Expose
        @SerializedName("install_address")
        private String install_address;
        @Expose
        @SerializedName("branch_office_no")
        private String branch_office_no;
        @Expose
        @SerializedName("assistant_name")
        private String assistant_name;
        @Expose
        @SerializedName("zonal_eng_name")
        private String zonal_eng_name;
        @Expose
        @SerializedName("zonal_eng_id")
        private String zonal_eng_id;
        @Expose
        @SerializedName("mech_name")
        private String mech_name;
        @Expose
        @SerializedName("mech_id")
        private String mech_id;
        @Expose
        @SerializedName("service_type")
        private String service_type;
        @Expose
        @SerializedName("execution_by")
        private String execution_by;
        @Expose
        @SerializedName("customer_name")
        private String customer_name;
        @Expose
        @SerializedName("job_no")
        private String job_no;
        @Expose
        @SerializedName("route_code")
        private String route_code;
        @Expose
        @SerializedName("rb_no")
        private String rb_no;
        @Expose
        @SerializedName("br_code")
        private String br_code;
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
            repair_work_mech_no = in.readString();
            repair_work_mech_name = in.readString();
            repair_work_mech_id = in.readString();
            pref_id = in.readString();
            status = in.readString();
            completed_date = in.readString();
            barricate_main_req = in.readString();
            dshackle_req = in.readString();
            rebelling_clamp = in.readString();
            ms_pipe_req = in.readString();
            ladder_req = in.readString();
            webbing_belt = in.readString();
            chain_block_capacity = in.readString();
            chain_block_status = in.readString();
            hand_gloves = in.readString();
            safety_shoes = in.readString();
            hard_hat = in.readString();
            full_body_harness = in.readString();
            first_aid_kit = in.readString();
            repair_toolkit = in.readString();
            material_available_site = in.readString();
            mr_no = in.readString();
            man_power = in.readString();
            tech_trained = in.readString();
            work_expected_date = in.readString();
            work_start_date = in.readString();
            other_process = in.readString();
            nature_of_work_process_name = in.readString();
            nature_of_work_process_id = in.readString();
            nature_of_work_name = in.readString();
            nature_of_work_id = in.readString();
            install_address = in.readString();
            branch_office_no = in.readString();
            assistant_name = in.readString();
            zonal_eng_name = in.readString();
            zonal_eng_id = in.readString();
            mech_name = in.readString();
            mech_id = in.readString();
            service_type = in.readString();
            execution_by = in.readString();
            customer_name = in.readString();
            job_no = in.readString();
            route_code = in.readString();
            rb_no = in.readString();
            br_code = in.readString();
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

        public String getRepair_work_mech_no() {
            return repair_work_mech_no;
        }

        public void setRepair_work_mech_no(String repair_work_mech_no) {
            this.repair_work_mech_no = repair_work_mech_no;
        }

        public String getRepair_work_mech_name() {
            return repair_work_mech_name;
        }

        public void setRepair_work_mech_name(String repair_work_mech_name) {
            this.repair_work_mech_name = repair_work_mech_name;
        }

        public String getRepair_work_mech_id() {
            return repair_work_mech_id;
        }

        public void setRepair_work_mech_id(String repair_work_mech_id) {
            this.repair_work_mech_id = repair_work_mech_id;
        }

        public String getPref_id() {
            return pref_id;
        }

        public void setPref_id(String pref_id) {
            this.pref_id = pref_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCompleted_date() {
            return completed_date;
        }

        public void setCompleted_date(String completed_date) {
            this.completed_date = completed_date;
        }

        public String getBarricate_main_req() {
            return barricate_main_req;
        }

        public void setBarricate_main_req(String barricate_main_req) {
            this.barricate_main_req = barricate_main_req;
        }

        public String getDshackle_req() {
            return dshackle_req;
        }

        public void setDshackle_req(String dshackle_req) {
            this.dshackle_req = dshackle_req;
        }

        public String getRebelling_clamp() {
            return rebelling_clamp;
        }

        public void setRebelling_clamp(String rebelling_clamp) {
            this.rebelling_clamp = rebelling_clamp;
        }

        public String getMs_pipe_req() {
            return ms_pipe_req;
        }

        public void setMs_pipe_req(String ms_pipe_req) {
            this.ms_pipe_req = ms_pipe_req;
        }

        public String getLadder_req() {
            return ladder_req;
        }

        public void setLadder_req(String ladder_req) {
            this.ladder_req = ladder_req;
        }

        public String getWebbing_belt() {
            return webbing_belt;
        }

        public void setWebbing_belt(String webbing_belt) {
            this.webbing_belt = webbing_belt;
        }

        public String getChain_block_capacity() {
            return chain_block_capacity;
        }

        public void setChain_block_capacity(String chain_block_capacity) {
            this.chain_block_capacity = chain_block_capacity;
        }

        public String getChain_block_status() {
            return chain_block_status;
        }

        public void setChain_block_status(String chain_block_status) {
            this.chain_block_status = chain_block_status;
        }

        public String getHand_gloves() {
            return hand_gloves;
        }

        public void setHand_gloves(String hand_gloves) {
            this.hand_gloves = hand_gloves;
        }

        public String getSafety_shoes() {
            return safety_shoes;
        }

        public void setSafety_shoes(String safety_shoes) {
            this.safety_shoes = safety_shoes;
        }

        public String getHard_hat() {
            return hard_hat;
        }

        public void setHard_hat(String hard_hat) {
            this.hard_hat = hard_hat;
        }

        public String getFull_body_harness() {
            return full_body_harness;
        }

        public void setFull_body_harness(String full_body_harness) {
            this.full_body_harness = full_body_harness;
        }

        public String getFirst_aid_kit() {
            return first_aid_kit;
        }

        public void setFirst_aid_kit(String first_aid_kit) {
            this.first_aid_kit = first_aid_kit;
        }

        public String getRepair_toolkit() {
            return repair_toolkit;
        }

        public void setRepair_toolkit(String repair_toolkit) {
            this.repair_toolkit = repair_toolkit;
        }

        public String getMaterial_available_site() {
            return material_available_site;
        }

        public void setMaterial_available_site(String material_available_site) {
            this.material_available_site = material_available_site;
        }

        public String getMr_no() {
            return mr_no;
        }

        public void setMr_no(String mr_no) {
            this.mr_no = mr_no;
        }

        public String getMan_power() {
            return man_power;
        }

        public void setMan_power(String man_power) {
            this.man_power = man_power;
        }

        public String getTech_trained() {
            return tech_trained;
        }

        public void setTech_trained(String tech_trained) {
            this.tech_trained = tech_trained;
        }

        public String getWork_expected_date() {
            return work_expected_date;
        }

        public void setWork_expected_date(String work_expected_date) {
            this.work_expected_date = work_expected_date;
        }

        public String getWork_start_date() {
            return work_start_date;
        }

        public void setWork_start_date(String work_start_date) {
            this.work_start_date = work_start_date;
        }

        public String getOther_process() {
            return other_process;
        }

        public void setOther_process(String other_process) {
            this.other_process = other_process;
        }

        public String getNature_of_work_process_name() {
            return nature_of_work_process_name;
        }

        public void setNature_of_work_process_name(String nature_of_work_process_name) {
            this.nature_of_work_process_name = nature_of_work_process_name;
        }

        public String getNature_of_work_process_id() {
            return nature_of_work_process_id;
        }

        public void setNature_of_work_process_id(String nature_of_work_process_id) {
            this.nature_of_work_process_id = nature_of_work_process_id;
        }

        public String getNature_of_work_name() {
            return nature_of_work_name;
        }

        public void setNature_of_work_name(String nature_of_work_name) {
            this.nature_of_work_name = nature_of_work_name;
        }

        public String getNature_of_work_id() {
            return nature_of_work_id;
        }

        public void setNature_of_work_id(String nature_of_work_id) {
            this.nature_of_work_id = nature_of_work_id;
        }

        public String getInstall_address() {
            return install_address;
        }

        public void setInstall_address(String install_address) {
            this.install_address = install_address;
        }

        public String getBranch_office_no() {
            return branch_office_no;
        }

        public void setBranch_office_no(String branch_office_no) {
            this.branch_office_no = branch_office_no;
        }

        public String getAssistant_name() {
            return assistant_name;
        }

        public void setAssistant_name(String assistant_name) {
            this.assistant_name = assistant_name;
        }

        public String getZonal_eng_name() {
            return zonal_eng_name;
        }

        public void setZonal_eng_name(String zonal_eng_name) {
            this.zonal_eng_name = zonal_eng_name;
        }

        public String getZonal_eng_id() {
            return zonal_eng_id;
        }

        public void setZonal_eng_id(String zonal_eng_id) {
            this.zonal_eng_id = zonal_eng_id;
        }

        public String getMech_name() {
            return mech_name;
        }

        public void setMech_name(String mech_name) {
            this.mech_name = mech_name;
        }

        public String getMech_id() {
            return mech_id;
        }

        public void setMech_id(String mech_id) {
            this.mech_id = mech_id;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        public String getExecution_by() {
            return execution_by;
        }

        public void setExecution_by(String execution_by) {
            this.execution_by = execution_by;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getJob_no() {
            return job_no;
        }

        public void setJob_no(String job_no) {
            this.job_no = job_no;
        }

        public String getRoute_code() {
            return route_code;
        }

        public void setRoute_code(String route_code) {
            this.route_code = route_code;
        }

        public String getRb_no() {
            return rb_no;
        }

        public void setRb_no(String rb_no) {
            this.rb_no = rb_no;
        }

        public String getBr_code() {
            return br_code;
        }

        public void setBr_code(String br_code) {
            this.br_code = br_code;
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
            parcel.writeString(repair_work_mech_no);
            parcel.writeString(repair_work_mech_name);
            parcel.writeString(repair_work_mech_id);
            parcel.writeString(pref_id);
            parcel.writeString(status);
            parcel.writeString(completed_date);
            parcel.writeString(barricate_main_req);
            parcel.writeString(dshackle_req);
            parcel.writeString(rebelling_clamp);
            parcel.writeString(ms_pipe_req);
            parcel.writeString(ladder_req);
            parcel.writeString(webbing_belt);
            parcel.writeString(chain_block_capacity);
            parcel.writeString(chain_block_status);
            parcel.writeString(hand_gloves);
            parcel.writeString(safety_shoes);
            parcel.writeString(hard_hat);
            parcel.writeString(full_body_harness);
            parcel.writeString(first_aid_kit);
            parcel.writeString(repair_toolkit);
            parcel.writeString(material_available_site);
            parcel.writeString(mr_no);
            parcel.writeString(man_power);
            parcel.writeString(tech_trained);
            parcel.writeString(work_expected_date);
            parcel.writeString(work_start_date);
            parcel.writeString(other_process);
            parcel.writeString(nature_of_work_process_name);
            parcel.writeString(nature_of_work_process_id);
            parcel.writeString(nature_of_work_name);
            parcel.writeString(nature_of_work_id);
            parcel.writeString(install_address);
            parcel.writeString(branch_office_no);
            parcel.writeString(assistant_name);
            parcel.writeString(zonal_eng_name);
            parcel.writeString(zonal_eng_id);
            parcel.writeString(mech_name);
            parcel.writeString(mech_id);
            parcel.writeString(service_type);
            parcel.writeString(execution_by);
            parcel.writeString(customer_name);
            parcel.writeString(job_no);
            parcel.writeString(route_code);
            parcel.writeString(rb_no);
            parcel.writeString(br_code);
            parcel.writeString(request_on);
            parcel.writeString(_id);
        }
    }
}
