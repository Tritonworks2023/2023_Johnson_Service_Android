package com.triton.johnson_tap_app.Service_Activity.elevatorSurveyModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.ElevatorCheckDateRequest;
import com.triton.johnson_tap_app.requestpojo.ElevatorSurveyFormRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.MachineControllerTypeResponse;
import com.triton.johnson_tap_app.responsepojo.NewJobListElevatorSurveyResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElevatorSurveyFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String formattedDate = "";
    private MultipartBody.Part signaturePart;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String TAG = ElevatorSurveyFormActivity.class.getSimpleName(), strDateType = "", se_user_mobile_no,
            se_user_name, se_user_id, service_title, networkStatus = "";
    private ImageView img_back;
    private Spinner spinner_machine_type, spinner_controller_type, spinner_break_Arm_liner, spinner_gear, spinner_sheave,
            spinner_ropes, spinner_motor, spinner_rope_hole_cutout, spinner_rescue_switch, spinner_pcb_drive, spinner_replays_contractor, spinner_ctrl_inspec_sw,
            spinner_governor_rope, spinner_osg_gaurd, spinner_ard, spinner_ard_battery_box, spinner_car_type, spinner_fan, spinner_emergency_light, spinner_buttons,
            spinner_car_operating_panel, spinner_car_top_sheave, spinner_car_top_barricade, spinner_car_inspection_box, spinner_retiring_cam,
            spinner_mechanical_safety, spinner_car_gate_switch, spinner_additional_car_stop_switch, /*spinner_sub_roller,*/
            spinner_buffer, spinner_pit_switch_positioning, spinner_pit_ladder, spinner_gov_tension_pulley,
            spinner_cwt_guard, spinner_pit_condition, spinner_travelling_cable, spinner_door_lock, spinner_limit_switch, spinner_magnet_vanes,
            spinner_counter_weight, spinner_emergency_alarm, spinner_door_closer, spinner_hall_button, spinner_door_vf,
            spinner_type_entrance, spinner_elevator_safe_operate;
    private EditText edt_maj_con_area_if, edt_recom_is, edt_cust_name, edt_desig, edt_contact_no;
    private TextView txt_survey_on, txt_survey_conducted_by;
    private SignaturePad signaturePad;
    private Button clear_button, save_button, btn_submit;
    private Bitmap signatureBitmap;
    private NewJobListElevatorSurveyResponse.Data newJobListElevatorSurveyDataResponse = new NewJobListElevatorSurveyResponse.Data();
    private String[] okNotRepArray, okNotRedArray, okNotRepNaArray, okNotRepTypArray, yesNoArray, yesNoNaArray, yesNoRepArray, typeEntranceArray, carTypeArray;
    private ElevatorSurveyFormRequest elevatorSurveyFormRequest = new ElevatorSurveyFormRequest();
    private ElevatorCheckDateRequest elevatorCheckDateRequest = new ElevatorCheckDateRequest();
    private ProgressDialog progressDialog;
    private String uploadimagepath;
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private Dialog dialog;
    private ArrayList<String> machineTypeResponseList = new ArrayList<>();
    private ArrayList<String> controllerTypeResponseList = new ArrayList<>();
    private boolean checkDate = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_elevator_survey_form);

        context = this;

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey("newJobListElevatorSurveyDataResponse")) {
                newJobListElevatorSurveyDataResponse = extra.getParcelable("newJobListElevatorSurveyDataResponse");
            }
        }
        Log.i(TAG, "onCreate: newJobListElevatorSurveyDataResponse -> " + new Gson().toJson(newJobListElevatorSurveyDataResponse));

        img_back = findViewById(R.id.img_back);

        txt_survey_on = findViewById(R.id.txt_survey_on);
        txt_survey_conducted_by = findViewById(R.id.txt_survey_conducted_by);

        spinner_machine_type = findViewById(R.id.spinner_machine_type);
        spinner_controller_type = findViewById(R.id.spinner_controller_type);
        spinner_break_Arm_liner = findViewById(R.id.spinner_break_Arm_liner);
        spinner_gear = findViewById(R.id.spinner_gear);
        spinner_sheave = findViewById(R.id.spinner_sheave);
        spinner_ropes = findViewById(R.id.spinner_ropes);
        spinner_motor = findViewById(R.id.spinner_motor);
        spinner_rope_hole_cutout = findViewById(R.id.spinner_rope_hole_cutout);
        spinner_rescue_switch = findViewById(R.id.spinner_rescue_switch);
        spinner_pcb_drive = findViewById(R.id.spinner_pcb_drive);
        spinner_replays_contractor = findViewById(R.id.spinner_replays_contractor);
        spinner_ctrl_inspec_sw = findViewById(R.id.spinner_ctrl_inspec_sw);
        spinner_governor_rope = findViewById(R.id.spinner_governor_rope);
        spinner_osg_gaurd = findViewById(R.id.spinner_osg_gaurd);
        spinner_ard = findViewById(R.id.spinner_ard);
        spinner_ard_battery_box = findViewById(R.id.spinner_ard_battery_box);
        spinner_car_type = findViewById(R.id.spinner_car_type);
        spinner_fan = findViewById(R.id.spinner_fan);
        spinner_emergency_light = findViewById(R.id.spinner_emergency_light);
        spinner_buttons = findViewById(R.id.spinner_buttons);
        spinner_car_operating_panel = findViewById(R.id.spinner_car_operating_panel);
        spinner_car_top_sheave = findViewById(R.id.spinner_car_top_sheave);
        spinner_car_top_barricade = findViewById(R.id.spinner_car_top_barricade);
        spinner_car_inspection_box = findViewById(R.id.spinner_car_inspection_box);
        spinner_retiring_cam = findViewById(R.id.spinner_retiring_cam);
        spinner_mechanical_safety = findViewById(R.id.spinner_mechanical_safety);
        spinner_car_gate_switch = findViewById(R.id.spinner_car_gate_switch);
        spinner_additional_car_stop_switch = findViewById(R.id.spinner_additional_car_stop_switch);
        /*spinner_sub_roller = findViewById(R.id.spinner_sub_roller);*/
        spinner_buffer = findViewById(R.id.spinner_buffer);
        spinner_pit_switch_positioning = findViewById(R.id.spinner_pit_switch_positioning);
        spinner_pit_ladder = findViewById(R.id.spinner_pit_ladder);
        spinner_gov_tension_pulley = findViewById(R.id.spinner_gov_tension_pulley);
        spinner_cwt_guard = findViewById(R.id.spinner_cwt_guard);
        spinner_pit_condition = findViewById(R.id.spinner_pit_condition);
        spinner_travelling_cable = findViewById(R.id.spinner_travelling_cable);
        spinner_door_lock = findViewById(R.id.spinner_door_lock);
        spinner_limit_switch = findViewById(R.id.spinner_limit_switch);
        spinner_magnet_vanes = findViewById(R.id.spinner_magnet_vanes);
        spinner_counter_weight = findViewById(R.id.spinner_counter_weight);
        spinner_emergency_alarm = findViewById(R.id.spinner_emergency_alarm);
        spinner_door_closer = findViewById(R.id.spinner_door_closer);
        spinner_hall_button = findViewById(R.id.spinner_hall_button);
        spinner_door_vf = findViewById(R.id.spinner_door_vf);
        spinner_type_entrance = findViewById(R.id.spinner_type_entrance);
        spinner_elevator_safe_operate = findViewById(R.id.spinner_elevator_safe_operate);

        edt_maj_con_area_if = findViewById(R.id.edt_maj_con_area_if);
        edt_recom_is = findViewById(R.id.edt_recom_is);
        edt_cust_name = findViewById(R.id.edt_cust_name);
        edt_desig = findViewById(R.id.edt_desig);
        edt_contact_no = findViewById(R.id.edt_contact_no);

        signaturePad = findViewById(R.id.signaturePad);

        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);
        btn_submit = findViewById(R.id.btn_submit);

        okNotRepArray = getResources().getStringArray(R.array.ok_not_rep_array);
        okNotRedArray = getResources().getStringArray(R.array.ok_not_red_array);
        okNotRepNaArray = getResources().getStringArray(R.array.ok_not_rep_na_array);
        okNotRepTypArray = getResources().getStringArray(R.array.ok_not_rep_typ_array);
        yesNoArray = getResources().getStringArray(R.array.yes_no_array);
        yesNoNaArray = getResources().getStringArray(R.array.yes_no_na_array);
        yesNoRepArray = getResources().getStringArray(R.array.yes_no_rep_array);
        typeEntranceArray = getResources().getStringArray(R.array.type_of_entrance_array);
        carTypeArray = getResources().getStringArray(R.array.car_type_array);

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "onCreate: service_title -> " + service_title);
        Log.i(TAG, "onCreate: se_user_mobile_no -> " + se_user_mobile_no);

        elevatorCheckDateRequest.setJob_id(newJobListElevatorSurveyDataResponse.getJOBNO());
        elevatorCheckDateRequest.setSubmitted_by_num(se_user_mobile_no);

        setDate(newJobListElevatorSurveyDataResponse.getINST_ON());
        elevatorSurveyFormRequest.setJob_id(newJobListElevatorSurveyDataResponse.getJOBNO());
        elevatorSurveyFormRequest.setBuilding_name(newJobListElevatorSurveyDataResponse.getCUST_NAME());
        elevatorSurveyFormRequest.setCus_address(String.format("%s, %s, %s, %s, %s", nullPointer(newJobListElevatorSurveyDataResponse.getINST_ADD()), nullPointer(newJobListElevatorSurveyDataResponse.getINST_ADD1()), nullPointer(newJobListElevatorSurveyDataResponse.getINST_ADD3()), nullPointer(newJobListElevatorSurveyDataResponse.getLANDMARK()), nullPointer(newJobListElevatorSurveyDataResponse.getPINCODE())));
        elevatorSurveyFormRequest.setSubmitted_by_num(se_user_mobile_no);
        elevatorSurveyFormRequest.setSubmitted_by_name(se_user_name);
        elevatorSurveyFormRequest.setSubmitted_by_emp_code(se_user_id);

        txt_survey_conducted_by.setText(elevatorSurveyFormRequest.getSubmitted_by_name());

        machineTypeResponseList.add("SELECT");
        controllerTypeResponseList.add("SELECT");

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            getMachineControllerTypeDropDown();
        }

        ArrayAdapter<String> machineTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, machineTypeResponseList);
        machineTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> controllerTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, controllerTypeResponseList);
        controllerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> okNotRepAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, okNotRepArray);
        okNotRepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> okNotRedAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, okNotRedArray);
        okNotRedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> okNotRepNaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, okNotRepNaArray);
        okNotRepNaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> okNotRepTypAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, okNotRepTypArray);
        okNotRepTypAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> typeEntranceAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, typeEntranceArray);
        typeEntranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> carTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, carTypeArray);
        carTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoArray);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> yesNoNaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoNaArray);
        yesNoNaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> yesNoRepAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoRepArray);
        yesNoRepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_machine_type.setAdapter(machineTypeAdapter);
        spinner_controller_type.setAdapter(controllerTypeAdapter);
        spinner_break_Arm_liner.setAdapter(okNotRepAdapter);
        spinner_gear.setAdapter(okNotRepNaAdapter);
        spinner_sheave.setAdapter(okNotRepAdapter);
        spinner_ropes.setAdapter(okNotRepAdapter);
        spinner_motor.setAdapter(okNotRepAdapter);
        spinner_rope_hole_cutout.setAdapter(okNotRedAdapter);
        spinner_rescue_switch.setAdapter(okNotRepAdapter);
        spinner_pcb_drive.setAdapter(okNotRepAdapter);
        spinner_replays_contractor.setAdapter(okNotRepAdapter);
        spinner_ctrl_inspec_sw.setAdapter(okNotRepAdapter);
        spinner_governor_rope.setAdapter(okNotRepAdapter);
        spinner_osg_gaurd.setAdapter(yesNoAdapter);
        spinner_ard.setAdapter(okNotRepAdapter);
        spinner_ard_battery_box.setAdapter(yesNoNaAdapter);
        spinner_fan.setAdapter(okNotRepAdapter);
        spinner_emergency_light.setAdapter(okNotRepAdapter);
        spinner_buttons.setAdapter(okNotRepAdapter);
        spinner_car_operating_panel.setAdapter(okNotRepAdapter);
        spinner_car_top_sheave.setAdapter(okNotRepNaAdapter);
        spinner_car_top_barricade.setAdapter(yesNoAdapter);
        spinner_car_inspection_box.setAdapter(okNotRepAdapter);
        spinner_retiring_cam.setAdapter(okNotRepNaAdapter);
        spinner_mechanical_safety.setAdapter(okNotRepAdapter);
        spinner_car_gate_switch.setAdapter(okNotRepAdapter);
        spinner_additional_car_stop_switch.setAdapter(okNotRepAdapter);
        /*spinner_sub_roller.setAdapter(okNotRepAdapter);*/
        spinner_buffer.setAdapter(okNotRepAdapter);
        spinner_pit_switch_positioning.setAdapter(okNotRepAdapter);
        spinner_pit_ladder.setAdapter(yesNoRepAdapter);
        spinner_gov_tension_pulley.setAdapter(okNotRepAdapter);
        spinner_cwt_guard.setAdapter(yesNoRepAdapter);
        spinner_pit_condition.setAdapter(okNotRepAdapter);
        spinner_travelling_cable.setAdapter(okNotRepAdapter);
        spinner_door_lock.setAdapter(okNotRepTypAdapter);
        spinner_limit_switch.setAdapter(okNotRepAdapter);
        spinner_magnet_vanes.setAdapter(okNotRepAdapter);
        spinner_counter_weight.setAdapter(okNotRepAdapter);
        spinner_emergency_alarm.setAdapter(okNotRepAdapter);
        spinner_door_closer.setAdapter(okNotRepAdapter);
        spinner_hall_button.setAdapter(okNotRepAdapter);
        spinner_door_vf.setAdapter(okNotRepAdapter);
        spinner_car_type.setAdapter(carTypeAdapter);
        spinner_type_entrance.setAdapter(typeEntranceAdapter);
        spinner_elevator_safe_operate.setAdapter(yesNoAdapter);

        spinner_machine_type.setOnItemSelectedListener(this);
        spinner_controller_type.setOnItemSelectedListener(this);
        spinner_break_Arm_liner.setOnItemSelectedListener(this);
        spinner_gear.setOnItemSelectedListener(this);
        spinner_sheave.setOnItemSelectedListener(this);
        spinner_ropes.setOnItemSelectedListener(this);
        spinner_motor.setOnItemSelectedListener(this);
        spinner_rope_hole_cutout.setOnItemSelectedListener(this);
        spinner_rescue_switch.setOnItemSelectedListener(this);
        spinner_pcb_drive.setOnItemSelectedListener(this);
        spinner_replays_contractor.setOnItemSelectedListener(this);
        spinner_ctrl_inspec_sw.setOnItemSelectedListener(this);
        spinner_governor_rope.setOnItemSelectedListener(this);
        spinner_osg_gaurd.setOnItemSelectedListener(this);
        spinner_ard.setOnItemSelectedListener(this);
        spinner_ard_battery_box.setOnItemSelectedListener(this);
        spinner_fan.setOnItemSelectedListener(this);
        spinner_emergency_light.setOnItemSelectedListener(this);
        spinner_buttons.setOnItemSelectedListener(this);
        spinner_car_operating_panel.setOnItemSelectedListener(this);
        spinner_car_top_sheave.setOnItemSelectedListener(this);
        spinner_car_top_barricade.setOnItemSelectedListener(this);
        spinner_car_inspection_box.setOnItemSelectedListener(this);
        spinner_retiring_cam.setOnItemSelectedListener(this);
        spinner_mechanical_safety.setOnItemSelectedListener(this);
        spinner_car_gate_switch.setOnItemSelectedListener(this);
        spinner_additional_car_stop_switch.setOnItemSelectedListener(this);
        /*spinner_sub_roller.setOnItemSelectedListener(this);*/
        spinner_buffer.setOnItemSelectedListener(this);
        spinner_pit_switch_positioning.setOnItemSelectedListener(this);
        spinner_pit_ladder.setOnItemSelectedListener(this);
        spinner_gov_tension_pulley.setOnItemSelectedListener(this);
        spinner_cwt_guard.setOnItemSelectedListener(this);
        spinner_pit_condition.setOnItemSelectedListener(this);
        spinner_travelling_cable.setOnItemSelectedListener(this);
        spinner_door_lock.setOnItemSelectedListener(this);
        spinner_limit_switch.setOnItemSelectedListener(this);
        spinner_magnet_vanes.setOnItemSelectedListener(this);
        spinner_counter_weight.setOnItemSelectedListener(this);
        spinner_emergency_alarm.setOnItemSelectedListener(this);
        spinner_door_closer.setOnItemSelectedListener(this);
        spinner_hall_button.setOnItemSelectedListener(this);
        spinner_door_vf.setOnItemSelectedListener(this);
        spinner_car_type.setOnItemSelectedListener(this);
        spinner_type_entrance.setOnItemSelectedListener(this);
        spinner_elevator_safe_operate.setOnItemSelectedListener(this);

        txt_survey_on.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        save_button.setOnClickListener(this);
        clear_button.setOnClickListener(this);
        img_back.setOnClickListener(this);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            public void onSigned() {
                save_button.setEnabled(true);
                clear_button.setEnabled(true);
            }

            public void onClear() {
                save_button.setEnabled(false);
                clear_button.setEnabled(false);
            }
        });

        getTodayDate();
    }

    private void setDate(String inputDateTime) {

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "dd-MMM-yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDateTime);
            str = outputFormat.format(date);
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/

            elevatorSurveyFormRequest.setInstalled_on(formattedDate);
            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void getTodayDate() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        strDateType = "txt_both";
        setDate(day, month, year);
    }

    private void callDatePicker() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        setDate(dayOfMonth, monthOfYear, year1), year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

        txt_survey_on.setText(dateTime);

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/
            if (strDateType.equalsIgnoreCase("txt_both")) {
                elevatorSurveyFormRequest.setSubmitted_by_on(formattedDate);
                elevatorSurveyFormRequest.setSurvey_no(formattedDate);
                elevatorCheckDateRequest.setSurvey_no(formattedDate);
                getElevatorCheckDate();
            } else if (strDateType.equalsIgnoreCase("txt_survey_on")) {
                elevatorSurveyFormRequest.setSurvey_no(formattedDate);
                elevatorCheckDateRequest.setSurvey_no(formattedDate);
                getElevatorCheckDate();
            }

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void validateElevatorSurveyFormRequest() {
        Log.i(TAG, "validateElevatorSurveyFormRequest: elevatorSurveyFormRequest (1) -> " + new Gson().toJson(elevatorSurveyFormRequest));

        elevatorSurveyFormRequest.setCus_contract(nullPointer(edt_contact_no.getText().toString().trim()));
        elevatorSurveyFormRequest.setCus_desg(nullPointer(edt_desig.getText().toString().trim()));
        elevatorSurveyFormRequest.setCus_name(nullPointer(edt_cust_name.getText().toString().trim()));
        elevatorSurveyFormRequest.setRecommendation(nullPointer(edt_recom_is.getText().toString().trim()));
        elevatorSurveyFormRequest.setMajor_concern(nullPointer(edt_maj_con_area_if.getText().toString().trim()));

        if (!checkDate) {
            ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getMachine_type())) {
            ErrorMsgDialog("Please Select Machine Type");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getController_type())) {
            ErrorMsgDialog("Please Select Controller Type");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getMachine())) {
            ErrorMsgDialog("Please Select Break / Arm / Liner");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getGear())) {
            ErrorMsgDialog("Please Select Gear");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getSheave())) {
            ErrorMsgDialog("Please Select Sheave");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getRopes())) {
            ErrorMsgDialog("Please Select Rope");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getMotor())) {
            ErrorMsgDialog("Please Select Motor");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getRope_hole_cutout())) {
            ErrorMsgDialog("Please Select Rope Hole Cutout");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getRescue_switch())) {
            ErrorMsgDialog("Please Select Rescue Switch");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getController_drive())) {
            ErrorMsgDialog("Please Select PCB / Drive");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getRelays())) {
            ErrorMsgDialog("Please Select Relays / Contactor");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getController_inspec())) {
            ErrorMsgDialog("Please Select Controller Inspection S/W");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getGovernor_rope())) {
            ErrorMsgDialog("Please Select Governor Rope");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getOsg_gaurd())) {
            ErrorMsgDialog("Please Select OSG Guard");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getArd())) {
            ErrorMsgDialog("Please Select ARD");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getArd_battery_box())) {
            ErrorMsgDialog("Please Select ARD Battery Box");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCar_type())) {
            ErrorMsgDialog("Please Select Car Type");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getFan())) {
            ErrorMsgDialog("Please Select Fan");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getEme_light())) {
            ErrorMsgDialog("Please Select Emergency Light");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getButtons())) {
            ErrorMsgDialog("Please Select Buttons");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCar_op_panel())) {
            ErrorMsgDialog("Please Select Car Operating Panel");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCar_top_sheave())) {
            ErrorMsgDialog("Please Select Car Top Sheave");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCar_top_barricade())) {
            ErrorMsgDialog("Please Select Car Top Barricade");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCar_inspec_box())) {
            ErrorMsgDialog("Please Select Car Inspection Box");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getRetiring_cam())) {
            ErrorMsgDialog("Please Select Retiring Cam");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getMechanical_safe())) {
            ErrorMsgDialog("Please Select Mechanical Safety");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCar_gate_switch())) {
            ErrorMsgDialog("Please Select Car Gate Switch");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getAdditional_car_stop_switch())) {
            ErrorMsgDialog("Please Select Additional Car Stop Switch");
        } /*else if (!nullPointerValidator(elevatorSurveyFormRequest.getSub_roller())) {
            ErrorMsgDialog("Please Select Sub Roller");
        }*/ else if (!nullPointerValidator(elevatorSurveyFormRequest.getPit_switch_positioning())) {
            ErrorMsgDialog("Please Select Pit Switch Positioning");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getPit_ladder())) {
            ErrorMsgDialog("Please Select Pit Ladder");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getBuffer())) {
            ErrorMsgDialog("Please Select Buffer");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getGov_tension_pulley())) {
            ErrorMsgDialog("Please Select Gov Tension Pulley");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCwt_guard())) {
            ErrorMsgDialog("Please Select CWT Guard");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getPti_condition())) {
            ErrorMsgDialog("Please Select Pit Condition");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getTravelling_cable())) {
            ErrorMsgDialog("Please Select Travelling Cable");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getDoor_lock())) {
            ErrorMsgDialog("Please Select Door Lock");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getLimit_switch())) {
            ErrorMsgDialog("Please Select Limit Switch");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getMagnet_vanes())) {
            ErrorMsgDialog("Please Select Magnet Vanes");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCounter_weight())) {
            ErrorMsgDialog("Please Select Counter Weight");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getEme_alarm())) {
            ErrorMsgDialog("Please Select Emergency Alarm");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getDoor_closer())) {
            ErrorMsgDialog("Please Select Door Closer");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getHall_button())) {
            ErrorMsgDialog("Please Select Hall Button");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getDoor_Vf())) {
            ErrorMsgDialog("Please Select Door VF");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getType_of_entrance())) {
            ErrorMsgDialog("Please Select Type Of Entrance");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getElevator_safe_op())) {
            ErrorMsgDialog("Please Select Elevator Safe To Operate");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCus_name())) {
            ErrorMsgDialog("Please Select Customer Name");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCus_desg())) {
            ErrorMsgDialog("Please Select Customer Designation");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCus_contract())) {
            ErrorMsgDialog("Please Select Contact No");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCus_name())) {
            ErrorMsgDialog("Please Select Survey Conducted By");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getSurvey_no())) {
            ErrorMsgDialog("Please Select Survey Conducted On");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getCus_survey_by_signature())) {
            ErrorMsgDialog("Please Select Survey Conductor Signature");
        } else if (!nullPointerValidator(elevatorSurveyFormRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Select Survey Submitted On");
        } else {
            Log.i(TAG, "validateElevatorSurveyFormRequest: elevatorSurveyFormRequest (2) -> " + new Gson().toJson(elevatorSurveyFormRequest));
            getCreateElevatorSurvey();
        }
    }

    public void NoInternetDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.dialog_nointernet, null);
        Button btn_Retry = mView.findViewById(R.id.btn_retry);

        mBuilder.setView(mView);
        final Dialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        btn_Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void ErrorMsgDialog(String strMsg) {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        txt_Message.setText(strMsg);

        mBuilder.setView(mView);
        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void uploadDigitalSignatureImageRequest(File file) {

        progressDialog = new ProgressDialog(ElevatorSurveyFormActivity.this);
        progressDialog.setMessage("Please Wait Image Upload ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);
        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(signaturePart);

        Log.i(TAG, "uploadDigitalSignatureImageRequest: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                Log.i(TAG, "uploadDigitalSignatureImageRequest: onResponse: FileUploadResponse -> " + new Gson().toJson(response.body()));
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (200 == response.body().getCode()) {
                        uploadimagepath = response.body().getData();
                        elevatorSurveyFormRequest.setCus_survey_by_signature(uploadimagepath);
                        if (uploadimagepath != null) {
                            signaturePad.setEnabled(false);
                            save_button.setEnabled(false);
                            clear_button.setEnabled(false);
                            Toast.makeText(context, "Signature Saved", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ErrorMsgDialog("Please Try Again Later");
                    }
                } else {
                    ErrorMsgDialog("Something Went Wrong.. Try Again Later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "uploadDigitalSignatureImageRequest: onFailure: error -> " + t.getMessage());
                ErrorMsgDialog("Something Went Wrong.. Try Again Later");
                progressDialog.dismiss();
            }
        });
    }

    private void getMachineControllerTypeDropDown() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<MachineControllerTypeResponse> call = apiInterface.getMachineControllerTypeDropDown(getContentType());
        Log.i(TAG, "getMachineControllerTypeDropDown: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<MachineControllerTypeResponse>() {
            @Override
            public void onResponse(@NonNull Call<MachineControllerTypeResponse> call, @NonNull Response<MachineControllerTypeResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getMachineControllerTypeDropDown: onResponse: MachineControllerTypeResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    MachineControllerTypeResponse machineControllerTypeResponse = new MachineControllerTypeResponse();

                    machineControllerTypeResponse = response.body();
                    if (response.body().getCode() == 200) {

                        if (machineControllerTypeResponse.getData() != null && machineControllerTypeResponse.getData().getMachine_type() != null && !machineControllerTypeResponse.getData().getMachine_type().isEmpty()) {
                            for (MachineControllerTypeResponse.Machine_type machineType : machineControllerTypeResponse.getData().getMachine_type()) {
                                machineTypeResponseList.add(machineType.getType_name());
                            }
                        }

                        if (machineControllerTypeResponse.getData() != null && machineControllerTypeResponse.getData().getController_type() != null && !machineControllerTypeResponse.getData().getController_type().isEmpty()) {
                            for (MachineControllerTypeResponse.Controller_type controllerType : machineControllerTypeResponse.getData().getController_type()) {
                                controllerTypeResponseList.add(controllerType.getType_name());
                            }
                        }

                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MachineControllerTypeResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getMachineControllerTypeDropDown: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void getElevatorCheckDate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Log.i(TAG, "getElevatorCheckDate: elevatorCheckDateRequest -> " + new Gson().toJson(elevatorCheckDateRequest));
        Call<SuccessResponse> call = apiInterface.getElevatorCheckDate(getContentType(), elevatorCheckDateRequest);
        Log.i(TAG, "getElevatorCheckDate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getElevatorCheckDate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (response.body().getCode() == 200) {
                        checkDate = false;
                        ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
                    } else {
                        checkDate = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getElevatorCheckDate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void getCreateElevatorSurvey() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getCreateElevatorSurvey(getContentType(), elevatorSurveyFormRequest);
        Log.i(TAG, "getCreateElevatorSurvey: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getCreateElevatorSurvey: onResponse: NewJobListElevatorSurveyResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (response.body().getCode() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getCreateElevatorSurvey: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "onItemSelected: view -> " + view.getId() + " i -> " + i + " l -> " + l);
        String item = adapterView.getItemAtPosition(i).toString();

        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_machine_type:
                    elevatorSurveyFormRequest.setMachine_type("");
                    break;
                case R.id.spinner_controller_type:
                    elevatorSurveyFormRequest.setController_type("");
                    break;
                case R.id.spinner_break_Arm_liner:
                    elevatorSurveyFormRequest.setMachine("");
                    break;
                case R.id.spinner_gear:
                    elevatorSurveyFormRequest.setGear("");
                    break;
                case R.id.spinner_sheave:
                    elevatorSurveyFormRequest.setSheave("");
                    break;
                case R.id.spinner_ropes:
                    elevatorSurveyFormRequest.setRopes("");
                    break;
                case R.id.spinner_motor:
                    elevatorSurveyFormRequest.setMotor("");
                    break;
                case R.id.spinner_rope_hole_cutout:
                    elevatorSurveyFormRequest.setRope_hole_cutout("");
                    break;
                case R.id.spinner_rescue_switch:
                    elevatorSurveyFormRequest.setRescue_switch("");
                    break;
                case R.id.spinner_pcb_drive:
                    elevatorSurveyFormRequest.setController_drive("");
                    break;
                case R.id.spinner_replays_contractor:
                    elevatorSurveyFormRequest.setRelays("");
                    break;
                case R.id.spinner_ctrl_inspec_sw:
                    elevatorSurveyFormRequest.setController_inspec("");
                    break;
                case R.id.spinner_governor_rope:
                    elevatorSurveyFormRequest.setGovernor_rope("");
                    break;
                case R.id.spinner_osg_gaurd:
                    elevatorSurveyFormRequest.setOsg_gaurd("");
                    break;
                case R.id.spinner_ard:
                    elevatorSurveyFormRequest.setArd("");
                    break;
                case R.id.spinner_ard_battery_box:
                    elevatorSurveyFormRequest.setArd_battery_box("");
                    break;
                case R.id.spinner_fan:
                    elevatorSurveyFormRequest.setFan("");
                    break;
                case R.id.spinner_emergency_light:
                    elevatorSurveyFormRequest.setEme_light("");
                    break;
                case R.id.spinner_buttons:
                    elevatorSurveyFormRequest.setButtons("");
                    break;
                case R.id.spinner_car_operating_panel:
                    elevatorSurveyFormRequest.setCar_op_panel("");
                    break;
                case R.id.spinner_car_top_sheave:
                    elevatorSurveyFormRequest.setCar_top_sheave("");
                    break;
                case R.id.spinner_car_top_barricade:
                    elevatorSurveyFormRequest.setCar_top_barricade("");
                    break;
                case R.id.spinner_car_inspection_box:
                    elevatorSurveyFormRequest.setCar_inspec_box("");
                    break;
                case R.id.spinner_retiring_cam:
                    elevatorSurveyFormRequest.setRetiring_cam("");
                    break;
                case R.id.spinner_mechanical_safety:
                    elevatorSurveyFormRequest.setMechanical_safe("");
                    break;
                case R.id.spinner_car_gate_switch:
                    elevatorSurveyFormRequest.setCar_gate_switch("");
                    break;
                case R.id.spinner_additional_car_stop_switch:
                    elevatorSurveyFormRequest.setAdditional_car_stop_switch("");
                    break;
                /*case R.id.spinner_sub_roller:
                    elevatorSurveyFormRequest.setSub_roller("");
                    break;*/
                case R.id.spinner_buffer:
                    elevatorSurveyFormRequest.setBuffer("");
                    break;
                case R.id.spinner_pit_switch_positioning:
                    elevatorSurveyFormRequest.setPit_switch_positioning("");
                    break;
                case R.id.spinner_pit_ladder:
                    elevatorSurveyFormRequest.setPit_ladder("");
                    break;
                case R.id.spinner_gov_tension_pulley:
                    elevatorSurveyFormRequest.setGov_tension_pulley("");
                    break;
                case R.id.spinner_cwt_guard:
                    elevatorSurveyFormRequest.setCwt_guard("");
                    break;
                case R.id.spinner_pit_condition:
                    elevatorSurveyFormRequest.setPti_condition("");
                    break;
                case R.id.spinner_travelling_cable:
                    elevatorSurveyFormRequest.setTravelling_cable("");
                    break;
                case R.id.spinner_door_lock:
                    elevatorSurveyFormRequest.setDoor_lock("");
                    break;
                case R.id.spinner_limit_switch:
                    elevatorSurveyFormRequest.setLimit_switch("");
                    break;
                case R.id.spinner_magnet_vanes:
                    elevatorSurveyFormRequest.setMagnet_vanes("");
                    break;
                case R.id.spinner_counter_weight:
                    elevatorSurveyFormRequest.setCounter_weight("");
                    break;
                case R.id.spinner_emergency_alarm:
                    elevatorSurveyFormRequest.setEme_alarm("");
                    break;
                case R.id.spinner_door_closer:
                    elevatorSurveyFormRequest.setDoor_closer("");
                    break;
                case R.id.spinner_hall_button:
                    elevatorSurveyFormRequest.setHall_button("");
                    break;
                case R.id.spinner_door_vf:
                    elevatorSurveyFormRequest.setDoor_Vf("");
                    break;
                case R.id.spinner_car_type:
                    elevatorSurveyFormRequest.setCar_type("");
                    break;
                case R.id.spinner_type_entrance:
                    elevatorSurveyFormRequest.setType_of_entrance("");
                    break;
                case R.id.spinner_elevator_safe_operate:
                    elevatorSurveyFormRequest.setElevator_safe_op("");
                    break;
                default:
                    break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_machine_type:
                    elevatorSurveyFormRequest.setMachine_type(item);
                    break;
                case R.id.spinner_controller_type:
                    elevatorSurveyFormRequest.setController_type(item);
                    break;
                case R.id.spinner_break_Arm_liner:
                    elevatorSurveyFormRequest.setMachine(item);
                    break;
                case R.id.spinner_gear:
                    elevatorSurveyFormRequest.setGear(item);
                    break;
                case R.id.spinner_sheave:
                    elevatorSurveyFormRequest.setSheave(item);
                    break;
                case R.id.spinner_ropes:
                    elevatorSurveyFormRequest.setRopes(item);
                    break;
                case R.id.spinner_motor:
                    elevatorSurveyFormRequest.setMotor(item);
                    break;
                case R.id.spinner_rope_hole_cutout:
                    elevatorSurveyFormRequest.setRope_hole_cutout(item);
                    break;
                case R.id.spinner_rescue_switch:
                    elevatorSurveyFormRequest.setRescue_switch(item);
                    break;
                case R.id.spinner_pcb_drive:
                    elevatorSurveyFormRequest.setController_drive(item);
                    break;
                case R.id.spinner_replays_contractor:
                    elevatorSurveyFormRequest.setRelays(item);
                    break;
                case R.id.spinner_ctrl_inspec_sw:
                    elevatorSurveyFormRequest.setController_inspec(item);
                    break;
                case R.id.spinner_governor_rope:
                    elevatorSurveyFormRequest.setGovernor_rope(item);
                    break;
                case R.id.spinner_osg_gaurd:
                    elevatorSurveyFormRequest.setOsg_gaurd(item);
                    break;
                case R.id.spinner_ard:
                    elevatorSurveyFormRequest.setArd(item);
                    break;
                case R.id.spinner_ard_battery_box:
                    elevatorSurveyFormRequest.setArd_battery_box(item);
                    break;
                case R.id.spinner_fan:
                    elevatorSurveyFormRequest.setFan(item);
                    break;
                case R.id.spinner_emergency_light:
                    elevatorSurveyFormRequest.setEme_light(item);
                    break;
                case R.id.spinner_buttons:
                    elevatorSurveyFormRequest.setButtons(item);
                    break;
                case R.id.spinner_car_operating_panel:
                    elevatorSurveyFormRequest.setCar_op_panel(item);
                    break;
                case R.id.spinner_car_top_sheave:
                    elevatorSurveyFormRequest.setCar_top_sheave(item);
                    break;
                case R.id.spinner_car_top_barricade:
                    elevatorSurveyFormRequest.setCar_top_barricade(item);
                    break;
                case R.id.spinner_car_inspection_box:
                    elevatorSurveyFormRequest.setCar_inspec_box(item);
                    break;
                case R.id.spinner_retiring_cam:
                    elevatorSurveyFormRequest.setRetiring_cam(item);
                    break;
                case R.id.spinner_mechanical_safety:
                    elevatorSurveyFormRequest.setMechanical_safe(item);
                    break;
                case R.id.spinner_car_gate_switch:
                    elevatorSurveyFormRequest.setCar_gate_switch(item);
                    break;
                case R.id.spinner_additional_car_stop_switch:
                    elevatorSurveyFormRequest.setAdditional_car_stop_switch(item);
                    break;
                /*case R.id.spinner_sub_roller:
                    elevatorSurveyFormRequest.setSub_roller(item);
                    break;*/
                case R.id.spinner_buffer:
                    elevatorSurveyFormRequest.setBuffer(item);
                    break;
                case R.id.spinner_pit_switch_positioning:
                    elevatorSurveyFormRequest.setPit_switch_positioning(item);
                    break;
                case R.id.spinner_pit_ladder:
                    elevatorSurveyFormRequest.setPit_ladder(item);
                    break;
                case R.id.spinner_gov_tension_pulley:
                    elevatorSurveyFormRequest.setGov_tension_pulley(item);
                    break;
                case R.id.spinner_cwt_guard:
                    elevatorSurveyFormRequest.setCwt_guard(item);
                    break;
                case R.id.spinner_pit_condition:
                    elevatorSurveyFormRequest.setPti_condition(item);
                    break;
                case R.id.spinner_travelling_cable:
                    elevatorSurveyFormRequest.setTravelling_cable(item);
                    break;
                case R.id.spinner_door_lock:
                    elevatorSurveyFormRequest.setDoor_lock(item);
                    break;
                case R.id.spinner_limit_switch:
                    elevatorSurveyFormRequest.setLimit_switch(item);
                    break;
                case R.id.spinner_magnet_vanes:
                    elevatorSurveyFormRequest.setMagnet_vanes(item);
                    break;
                case R.id.spinner_counter_weight:
                    elevatorSurveyFormRequest.setCounter_weight(item);
                    break;
                case R.id.spinner_emergency_alarm:
                    elevatorSurveyFormRequest.setEme_alarm(item);
                    break;
                case R.id.spinner_door_closer:
                    elevatorSurveyFormRequest.setDoor_closer(item);
                    break;
                case R.id.spinner_hall_button:
                    elevatorSurveyFormRequest.setHall_button(item);
                    break;
                case R.id.spinner_door_vf:
                    elevatorSurveyFormRequest.setDoor_Vf(item);
                    break;
                case R.id.spinner_car_type:
                    elevatorSurveyFormRequest.setCar_type(item);
                    break;
                case R.id.spinner_type_entrance:
                    elevatorSurveyFormRequest.setType_of_entrance(item);
                    break;
                case R.id.spinner_elevator_safe_operate:
                    elevatorSurveyFormRequest.setElevator_safe_op(item);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_survey_on: {
                strDateType = "txt_survey_on";
                callDatePicker();
            }
            break;
            case R.id.save_button: {
                signatureBitmap = signaturePad.getSignatureBitmap();
                Log.i(TAG, "onClick: signatureBitmap" + signatureBitmap);
                File file = new File(getFilesDir(), "Technician Signature" + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(file);
                    if (signatureBitmap != null) {
                        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    }
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                signaturePart = MultipartBody.Part.createFormData("sampleFile", se_user_id + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.e("Network", "" + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    uploadDigitalSignatureImageRequest(file);
                }
            }
            break;
            case R.id.clear_button: {
                signaturePad.clear();
            }
            break;
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.btn_submit: {
                validateElevatorSurveyFormRequest();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
