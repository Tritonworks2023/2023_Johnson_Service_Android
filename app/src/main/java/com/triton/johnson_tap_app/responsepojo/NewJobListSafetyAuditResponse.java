package com.triton.johnson_tap_app.responsepojo;

import java.util.List;

public class NewJobListSafetyAuditResponse {

    private String Status;
    private String Message;
    private List<DataBean> Data;
    private int Code;

    public NewJobListSafetyAuditResponse(String status, String message, List<DataBean> data, int code) {
        Status = status;
        Message = message;
        Data = data;
        Code = code;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public static class DataBean {
        private String job_no;
        private String building_name;
        private String machine_type;
        private String address;
        private String controller_type;
        private String installed_on;
        private String surveyed_on;
        private String date;

        public DataBean() {
        }

        public DataBean(String job_no, String building_name, String machine_type, String address, String controller_type, String installed_on, String surveyed_on, String date) {
            this.job_no = job_no;
            this.building_name = building_name;
            this.machine_type = machine_type;
            this.address = address;
            this.controller_type = controller_type;
            this.installed_on = installed_on;
            this.surveyed_on = surveyed_on;
            this.date = date;
        }

        public String getJob_no() {
            return job_no;
        }

        public void setJob_no(String job_no) {
            this.job_no = job_no;
        }

        public String getBuilding_name() {
            return building_name;
        }

        public void setBuilding_name(String building_name) {
            this.building_name = building_name;
        }

        public String getMachine_type() {
            return machine_type;
        }

        public void setMachine_type(String machine_type) {
            this.machine_type = machine_type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getController_type() {
            return controller_type;
        }

        public void setController_type(String controller_type) {
            this.controller_type = controller_type;
        }

        public String getInstalled_on() {
            return installed_on;
        }

        public void setInstalled_on(String installed_on) {
            this.installed_on = installed_on;
        }

        public String getSurveyed_on() {
            return surveyed_on;
        }

        public void setSurveyed_on(String surveyed_on) {
            this.surveyed_on = surveyed_on;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
