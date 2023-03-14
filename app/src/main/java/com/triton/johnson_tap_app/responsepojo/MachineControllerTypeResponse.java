package com.triton.johnson_tap_app.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MachineControllerTypeResponse {
    @Expose
    @SerializedName("Code")
    private int Code;
    @Expose
    @SerializedName("Data")
    private Data Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private String Status;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data Data) {
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

    public static class Data {
        @Expose
        @SerializedName("controller_type")
        private ArrayList<Controller_type> controller_type;
        @Expose
        @SerializedName("machine_type")
        private ArrayList<Machine_type> machine_type;

        public ArrayList<Controller_type> getController_type() {
            return controller_type;
        }

        public void setController_type(ArrayList<Controller_type> controller_type) {
            this.controller_type = controller_type;
        }

        public ArrayList<Machine_type> getMachine_type() {
            return machine_type;
        }

        public void setMachine_type(ArrayList<Machine_type> machine_type) {
            this.machine_type = machine_type;
        }
    }

    public static class Controller_type {
        @Expose
        @SerializedName("type_name")
        private String type_name;

        public Controller_type(String type_name) {
            this.type_name = type_name;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

    public static class Machine_type {
        @Expose
        @SerializedName("type_name")
        private String type_name;

        public Machine_type(String type_name) {
            this.type_name = type_name;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
