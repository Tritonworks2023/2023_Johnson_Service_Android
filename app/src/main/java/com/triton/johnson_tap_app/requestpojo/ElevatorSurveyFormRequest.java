package com.triton.johnson_tap_app.requestpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElevatorSurveyFormRequest {

    @Expose
    @SerializedName("submitted_by_on")
    private String submitted_by_on;
    @Expose
    @SerializedName("submitted_by_emp_code")
    private String submitted_by_emp_code;
    @Expose
    @SerializedName("submitted_by_name")
    private String submitted_by_name;
    @Expose
    @SerializedName("submitted_by_num")
    private String submitted_by_num;
    @Expose
    @SerializedName("cus_survey_by_signature")
    private String cus_survey_by_signature;
    @Expose
    @SerializedName("cus_survey_conducted")
    private String cus_survey_conducted;
    @Expose
    @SerializedName("cus_contract")
    private String cus_contract;
    @Expose
    @SerializedName("cus_desg")
    private String cus_desg;
    @Expose
    @SerializedName("cus_name")
    private String cus_name;
    @Expose
    @SerializedName("recommendation")
    private String recommendation;
    @Expose
    @SerializedName("major_concern")
    private String major_concern;
    @Expose
    @SerializedName("elevator_safe_op")
    private String elevator_safe_op;
    @Expose
    @SerializedName("type_of_entrance")
    private String type_of_entrance;
    @Expose
    @SerializedName("door_Vf")
    private String door_Vf;
    @Expose
    @SerializedName("hall_button")
    private String hall_button;
    @Expose
    @SerializedName("door_closer")
    private String door_closer;
    @Expose
    @SerializedName("eme_alarm")
    private String eme_alarm;
    @Expose
    @SerializedName("counter_weight")
    private String counter_weight;
    @Expose
    @SerializedName("magnet_vanes")
    private String magnet_vanes;
    @Expose
    @SerializedName("limit_switch")
    private String limit_switch;
    @Expose
    @SerializedName("door_lock")
    private String door_lock;
    @Expose
    @SerializedName("travelling_cable")
    private String travelling_cable;
    @Expose
    @SerializedName("pti_condition")
    private String pti_condition;
    @Expose
    @SerializedName("gov_tension_pulley")
    private String gov_tension_pulley;
    @Expose
    @SerializedName("cwt_guard")
    private String cwt_guard;
    @Expose
    @SerializedName("buffer")
    private String buffer;
    @Expose
    @SerializedName("pit_switch_positioning")
    private String pit_switch_positioning;
    @Expose
    @SerializedName("pit_ladder")
    private String pit_ladder;
    /*@Expose
    @SerializedName("sub_roller")
    private String sub_roller;*/
    @Expose
    @SerializedName("car_gate_switch")
    private String car_gate_switch;
    @Expose
    @SerializedName("additional_car_stop_switch")
    private String additional_car_stop_switch;
    @Expose
    @SerializedName("mechanical_safe")
    private String mechanical_safe;
    @Expose
    @SerializedName("retiring_cam")
    private String retiring_cam;
    @Expose
    @SerializedName("car_inspec_box")
    private String car_inspec_box;
    @Expose
    @SerializedName("car_top_sheave")
    private String car_top_sheave;
    @Expose
    @SerializedName("car_top_barricade")
    private String car_top_barricade;
    @Expose
    @SerializedName("car_op_panel")
    private String car_op_panel;
    @Expose
    @SerializedName("buttons")
    private String buttons;
    @Expose
    @SerializedName("eme_light")
    private String eme_light;
    @Expose
    @SerializedName("fan")
    private String fan;
    @Expose
    @SerializedName("car_type")
    private String car_type;
    @Expose
    @SerializedName("ard")
    private String ard;
    @Expose
    @SerializedName("ard_battery_box")
    private String ard_battery_box;
    @Expose
    @SerializedName("governor_rope")
    private String governor_rope;
    @Expose
    @SerializedName("osg_gaurd")
    private String osg_gaurd;
    @Expose
    @SerializedName("controller_inspec")
    private String controller_inspec;
    @Expose
    @SerializedName("relays")
    private String relays;
    @Expose
    @SerializedName("controller_drive")
    private String controller_drive;
    @Expose
    @SerializedName("motor")
    private String motor;
    @Expose
    @SerializedName("rope_hole_cutout")
    private String rope_hole_cutout;
    @Expose
    @SerializedName("rescue_switch")
    private String rescue_switch;
    @Expose
    @SerializedName("ropes")
    private String ropes;
    @Expose
    @SerializedName("sheave")
    private String sheave;
    @Expose
    @SerializedName("gear")
    private String gear;
    @Expose
    @SerializedName("machine")
    private String machine;
    @Expose
    @SerializedName("survey_no")
    private String survey_no;
    @Expose
    @SerializedName("installed_on")
    private String installed_on;
    @Expose
    @SerializedName("controller_type")
    private String controller_type;
    @Expose
    @SerializedName("cus_address")
    private String cus_address;
    @Expose
    @SerializedName("machine_type")
    private String machine_type;
    @Expose
    @SerializedName("building_name")
    private String building_name;
    @Expose
    @SerializedName("job_id")
    private String job_id;

    public String getRope_hole_cutout() {
        return rope_hole_cutout;
    }

    public void setRope_hole_cutout(String rope_hole_cutout) {
        this.rope_hole_cutout = rope_hole_cutout;
    }

    public String getRescue_switch() {
        return rescue_switch;
    }

    public void setRescue_switch(String rescue_switch) {
        this.rescue_switch = rescue_switch;
    }

    public String getCwt_guard() {
        return cwt_guard;
    }

    public void setCwt_guard(String cwt_guard) {
        this.cwt_guard = cwt_guard;
    }

    public String getPit_switch_positioning() {
        return pit_switch_positioning;
    }

    public void setPit_switch_positioning(String pit_switch_positioning) {
        this.pit_switch_positioning = pit_switch_positioning;
    }

    public String getPit_ladder() {
        return pit_ladder;
    }

    public void setPit_ladder(String pit_ladder) {
        this.pit_ladder = pit_ladder;
    }

    public String getAdditional_car_stop_switch() {
        return additional_car_stop_switch;
    }

    public void setAdditional_car_stop_switch(String additional_car_stop_switch) {
        this.additional_car_stop_switch = additional_car_stop_switch;
    }

    public String getCar_top_barricade() {
        return car_top_barricade;
    }

    public void setCar_top_barricade(String car_top_barricade) {
        this.car_top_barricade = car_top_barricade;
    }

    public String getArd_battery_box() {
        return ard_battery_box;
    }

    public void setArd_battery_box(String ard_battery_box) {
        this.ard_battery_box = ard_battery_box;
    }

    public String getOsg_gaurd() {
        return osg_gaurd;
    }

    public void setOsg_gaurd(String osg_gaurd) {
        this.osg_gaurd = osg_gaurd;
    }

    public String getSubmitted_by_on() {
        return submitted_by_on;
    }

    public void setSubmitted_by_on(String submitted_by_on) {
        this.submitted_by_on = submitted_by_on;
    }

    public String getSubmitted_by_emp_code() {
        return submitted_by_emp_code;
    }

    public void setSubmitted_by_emp_code(String submitted_by_emp_code) {
        this.submitted_by_emp_code = submitted_by_emp_code;
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

    public String getCus_survey_by_signature() {
        return cus_survey_by_signature;
    }

    public void setCus_survey_by_signature(String cus_survey_by_signature) {
        this.cus_survey_by_signature = cus_survey_by_signature;
    }

    public String getCus_survey_conducted() {
        return cus_survey_conducted;
    }

    public void setCus_survey_conducted(String cus_survey_conducted) {
        this.cus_survey_conducted = cus_survey_conducted;
    }

    public String getCus_contract() {
        return cus_contract;
    }

    public void setCus_contract(String cus_contract) {
        this.cus_contract = cus_contract;
    }

    public String getCus_desg() {
        return cus_desg;
    }

    public void setCus_desg(String cus_desg) {
        this.cus_desg = cus_desg;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getMajor_concern() {
        return major_concern;
    }

    public void setMajor_concern(String major_concern) {
        this.major_concern = major_concern;
    }

    public String getElevator_safe_op() {
        return elevator_safe_op;
    }

    public void setElevator_safe_op(String elevator_safe_op) {
        this.elevator_safe_op = elevator_safe_op;
    }

    public String getType_of_entrance() {
        return type_of_entrance;
    }

    public void setType_of_entrance(String type_of_entrance) {
        this.type_of_entrance = type_of_entrance;
    }

    public String getDoor_Vf() {
        return door_Vf;
    }

    public void setDoor_Vf(String door_Vf) {
        this.door_Vf = door_Vf;
    }

    public String getHall_button() {
        return hall_button;
    }

    public void setHall_button(String hall_button) {
        this.hall_button = hall_button;
    }

    public String getDoor_closer() {
        return door_closer;
    }

    public void setDoor_closer(String door_closer) {
        this.door_closer = door_closer;
    }

    public String getEme_alarm() {
        return eme_alarm;
    }

    public void setEme_alarm(String eme_alarm) {
        this.eme_alarm = eme_alarm;
    }

    public String getCounter_weight() {
        return counter_weight;
    }

    public void setCounter_weight(String counter_weight) {
        this.counter_weight = counter_weight;
    }

    public String getMagnet_vanes() {
        return magnet_vanes;
    }

    public void setMagnet_vanes(String magnet_vanes) {
        this.magnet_vanes = magnet_vanes;
    }

    public String getLimit_switch() {
        return limit_switch;
    }

    public void setLimit_switch(String limit_switch) {
        this.limit_switch = limit_switch;
    }

    public String getDoor_lock() {
        return door_lock;
    }

    public void setDoor_lock(String door_lock) {
        this.door_lock = door_lock;
    }

    public String getTravelling_cable() {
        return travelling_cable;
    }

    public void setTravelling_cable(String travelling_cable) {
        this.travelling_cable = travelling_cable;
    }

    public String getPti_condition() {
        return pti_condition;
    }

    public void setPti_condition(String pti_condition) {
        this.pti_condition = pti_condition;
    }

    public String getGov_tension_pulley() {
        return gov_tension_pulley;
    }

    public void setGov_tension_pulley(String gov_tension_pulley) {
        this.gov_tension_pulley = gov_tension_pulley;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    /*public String getSub_roller() {
        return sub_roller;
    }

    public void setSub_roller(String sub_roller) {
        this.sub_roller = sub_roller;
    }*/

    public String getCar_gate_switch() {
        return car_gate_switch;
    }

    public void setCar_gate_switch(String car_gate_switch) {
        this.car_gate_switch = car_gate_switch;
    }

    public String getMechanical_safe() {
        return mechanical_safe;
    }

    public void setMechanical_safe(String mechanical_safe) {
        this.mechanical_safe = mechanical_safe;
    }

    public String getRetiring_cam() {
        return retiring_cam;
    }

    public void setRetiring_cam(String retiring_cam) {
        this.retiring_cam = retiring_cam;
    }

    public String getCar_inspec_box() {
        return car_inspec_box;
    }

    public void setCar_inspec_box(String car_inspec_box) {
        this.car_inspec_box = car_inspec_box;
    }

    public String getCar_top_sheave() {
        return car_top_sheave;
    }

    public void setCar_top_sheave(String car_top_sheave) {
        this.car_top_sheave = car_top_sheave;
    }

    public String getCar_op_panel() {
        return car_op_panel;
    }

    public void setCar_op_panel(String car_op_panel) {
        this.car_op_panel = car_op_panel;
    }

    public String getButtons() {
        return buttons;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }

    public String getEme_light() {
        return eme_light;
    }

    public void setEme_light(String eme_light) {
        this.eme_light = eme_light;
    }

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getArd() {
        return ard;
    }

    public void setArd(String ard) {
        this.ard = ard;
    }

    public String getGovernor_rope() {
        return governor_rope;
    }

    public void setGovernor_rope(String governor_rope) {
        this.governor_rope = governor_rope;
    }

    public String getController_inspec() {
        return controller_inspec;
    }

    public void setController_inspec(String controller_inspec) {
        this.controller_inspec = controller_inspec;
    }

    public String getRelays() {
        return relays;
    }

    public void setRelays(String relays) {
        this.relays = relays;
    }

    public String getController_drive() {
        return controller_drive;
    }

    public void setController_drive(String controller_drive) {
        this.controller_drive = controller_drive;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getRopes() {
        return ropes;
    }

    public void setRopes(String ropes) {
        this.ropes = ropes;
    }

    public String getSheave() {
        return sheave;
    }

    public void setSheave(String sheave) {
        this.sheave = sheave;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getSurvey_no() {
        return survey_no;
    }

    public void setSurvey_no(String survey_no) {
        this.survey_no = survey_no;
    }

    public String getInstalled_on() {
        return installed_on;
    }

    public void setInstalled_on(String installed_on) {
        this.installed_on = installed_on;
    }

    public String getController_type() {
        return controller_type;
    }

    public void setController_type(String controller_type) {
        this.controller_type = controller_type;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }
}
