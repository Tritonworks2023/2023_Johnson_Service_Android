package com.triton.johnson_tap_app.Service_Activity.escalatorSurveyModule;

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
import com.triton.johnson_tap_app.requestpojo.EscalatorCheckDateRequest;
import com.triton.johnson_tap_app.requestpojo.EscalatorSurveyFormRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.MachineControllerTypeResponse;
import com.triton.johnson_tap_app.responsepojo.NewJobListEscalatorSurveyResponse;
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

public class EscalatorSurveyFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static String formattedDate = "";
    private MultipartBody.Part signaturePart;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String TAG = EscalatorSurveyFormActivity.class.getSimpleName(), strDateType = "", se_user_mobile_no,
            se_user_name, se_user_id, service_title, networkStatus = "";
    private ImageView img_back;
    private Spinner spinner_machine_type, spinner_controller_type, spinner_break_Arm_liner, spinner_gear,
            spinner_motor, spinner_break_motor_nim, spinner_plc_mic, spinner_replay, spinner_contractor,
            spinner_ctrl_inspec_plug, spinner_ctrl_cool_fan, spinner_vvvf_condition, spinner_all_pcbs, spinner_main_staff_break_condition,
            spinner_step_roll, spinner_step_chain_roll, spinner_hand_condition, spinner_hand_tens_wheel, spinner_comb_teeth,
            spinner_steps_condition, spinner_skirt_brush, spinner_glass, spinner_ss_balustrade, spinner_end_rev_break, spinner_sub_roller,
            spinner_hand_pedes_bear_assum, spinner_main_dri_chai_hand_chai_con, spinner_pin_wheel_cond, spinner_dri_chain_slid,
            spinner_tang_rail, spinner_all_saf_switch_condition, spinner_cable_condition, spinner_smps_board,
            spinner_emerg_stop_button, spinner_key_switch, spinner_sensors, spinner_insp_plug, spinner_step_gap_light,
            spinner_skirting_lights, spinner_pit_lights, spinner_comb_light, spinner_traffic_lights, spinner_handrail_lights,
            spinner_esc_safe_operate;
    private EditText edt_maj_con_area_if, edt_recom_is, edt_cust_name, edt_desig, edt_contact_no;
    private TextView txt_survey_on, txt_survey_conducted_by;
    private SignaturePad signaturePad;
    private LinearLayout buttons_container;
    private Button clear_button, save_button, btn_submit;
    private Bitmap signatureBitmap;
    private NewJobListEscalatorSurveyResponse.Data newJobListEscalatorSurveyDataResponse = new NewJobListEscalatorSurveyResponse.Data();
    private String[] okNotRepArray, okNoArray;
    private EscalatorSurveyFormRequest escalatorSurveyFormRequest = new EscalatorSurveyFormRequest();
    private EscalatorCheckDateRequest escalatorCheckDateRequest = new EscalatorCheckDateRequest();
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
        setContentView(R.layout.activity_escalator_survey_form);
        context = this;
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("newJobListEscalatorSurveyDataResponse")) {
                newJobListEscalatorSurveyDataResponse = extra.getParcelable("newJobListEscalatorSurveyDataResponse");
            }
        }
        Log.i(TAG, "onCreate: newJobListEscalatorSurveyDataResponse -> " + new Gson().toJson(newJobListEscalatorSurveyDataResponse));

        img_back = findViewById(R.id.img_back);

        txt_survey_on = findViewById(R.id.txt_survey_on);
        txt_survey_conducted_by = findViewById(R.id.txt_survey_conducted_by);

        spinner_machine_type = findViewById(R.id.spinner_machine_type);
        spinner_controller_type = findViewById(R.id.spinner_controller_type);
        spinner_break_Arm_liner = findViewById(R.id.spinner_break_Arm_liner);
        spinner_gear = findViewById(R.id.spinner_gear);
        spinner_motor = findViewById(R.id.spinner_motor);
        spinner_break_motor_nim = findViewById(R.id.spinner_break_motor_nim);
        spinner_plc_mic = findViewById(R.id.spinner_plc_mic);
        spinner_replay = findViewById(R.id.spinner_replay);
        spinner_contractor = findViewById(R.id.spinner_contractor);
        spinner_ctrl_inspec_plug = findViewById(R.id.spinner_ctrl_inspec_plug);
        spinner_ctrl_cool_fan = findViewById(R.id.spinner_ctrl_cool_fan);
        spinner_vvvf_condition = findViewById(R.id.spinner_vvvf_condition);
        spinner_all_pcbs = findViewById(R.id.spinner_all_pcbs);
        spinner_main_staff_break_condition = findViewById(R.id.spinner_main_staff_break_condition);
        spinner_step_roll = findViewById(R.id.spinner_step_roll);
        spinner_step_chain_roll = findViewById(R.id.spinner_step_chain_roll);
        spinner_hand_condition = findViewById(R.id.spinner_hand_condition);
        spinner_hand_tens_wheel = findViewById(R.id.spinner_hand_tens_wheel);
        spinner_comb_teeth = findViewById(R.id.spinner_comb_teeth);
        spinner_steps_condition = findViewById(R.id.spinner_steps_condition);
        spinner_skirt_brush = findViewById(R.id.spinner_skirt_brush);
        spinner_glass = findViewById(R.id.spinner_glass);
        spinner_ss_balustrade = findViewById(R.id.spinner_ss_balustrade);
        spinner_end_rev_break = findViewById(R.id.spinner_end_rev_break);
        spinner_sub_roller = findViewById(R.id.spinner_sub_roller);
        spinner_hand_pedes_bear_assum = findViewById(R.id.spinner_hand_pedes_bear_assum);
        spinner_main_dri_chai_hand_chai_con = findViewById(R.id.spinner_main_dri_chai_hand_chai_con);
        spinner_pin_wheel_cond = findViewById(R.id.spinner_pin_wheel_cond);
        spinner_dri_chain_slid = findViewById(R.id.spinner_dri_chain_slid);
        spinner_tang_rail = findViewById(R.id.spinner_tang_rail);
        spinner_all_saf_switch_condition = findViewById(R.id.spinner_all_saf_switch_condition);
        spinner_cable_condition = findViewById(R.id.spinner_cable_condition);
        spinner_smps_board = findViewById(R.id.spinner_smps_board);
        spinner_emerg_stop_button = findViewById(R.id.spinner_emerg_stop_button);
        spinner_key_switch = findViewById(R.id.spinner_key_switch);
        spinner_sensors = findViewById(R.id.spinner_sensors);
        spinner_insp_plug = findViewById(R.id.spinner_insp_plug);
        spinner_step_gap_light = findViewById(R.id.spinner_step_gap_light);
        spinner_skirting_lights = findViewById(R.id.spinner_skirting_lights);
        spinner_pit_lights = findViewById(R.id.spinner_pit_lights);
        spinner_comb_light = findViewById(R.id.spinner_comb_light);
        spinner_traffic_lights = findViewById(R.id.spinner_traffic_lights);
        spinner_handrail_lights = findViewById(R.id.spinner_handrail_lights);
        spinner_esc_safe_operate = findViewById(R.id.spinner_esc_safe_operate);

        edt_maj_con_area_if = findViewById(R.id.edt_maj_con_area_if);
        edt_recom_is = findViewById(R.id.edt_recom_is);
        edt_cust_name = findViewById(R.id.edt_cust_name);
        edt_desig = findViewById(R.id.edt_desig);
        edt_contact_no = findViewById(R.id.edt_contact_no);

        signaturePad = findViewById(R.id.signaturePad);

        buttons_container = findViewById(R.id.buttons_container);

        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);
        btn_submit = findViewById(R.id.btn_submit);

        okNotRepArray = getResources().getStringArray(R.array.ok_not_rep_array);
        okNoArray = getResources().getStringArray(R.array.yes_no_array);

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

        escalatorCheckDateRequest.setJob_id(newJobListEscalatorSurveyDataResponse.getJOBNO());
        escalatorCheckDateRequest.setSubmitted_by_num(se_user_mobile_no);

        setDate(newJobListEscalatorSurveyDataResponse.getINST_ON());
        escalatorSurveyFormRequest.setJob_id(newJobListEscalatorSurveyDataResponse.getJOBNO());
        escalatorSurveyFormRequest.setBuilding_name(newJobListEscalatorSurveyDataResponse.getCUST_NAME());
        escalatorSurveyFormRequest.setCus_address(String.format("%s, %s, %s, %s, %s", nullPointer(newJobListEscalatorSurveyDataResponse.getINST_ADD()), nullPointer(newJobListEscalatorSurveyDataResponse.getINST_ADD1()), nullPointer(newJobListEscalatorSurveyDataResponse.getINST_ADD3()), nullPointer(newJobListEscalatorSurveyDataResponse.getLANDMARK()), nullPointer(newJobListEscalatorSurveyDataResponse.getPINCODE())));
        escalatorSurveyFormRequest.setSubmitted_by_num(se_user_mobile_no);
        escalatorSurveyFormRequest.setSubmitted_by_name(se_user_name);
        escalatorSurveyFormRequest.setSubmitted_by_emp_code(se_user_id);

        txt_survey_conducted_by.setText(escalatorSurveyFormRequest.getSubmitted_by_name());

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

        ArrayAdapter<String> okNoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, okNoArray);
        okNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_machine_type.setAdapter(machineTypeAdapter);
        spinner_controller_type.setAdapter(controllerTypeAdapter);
        spinner_break_Arm_liner.setAdapter(okNotRepAdapter);
        spinner_gear.setAdapter(okNotRepAdapter);
        spinner_motor.setAdapter(okNotRepAdapter);
        spinner_break_motor_nim.setAdapter(okNotRepAdapter);
        spinner_plc_mic.setAdapter(okNotRepAdapter);
        spinner_replay.setAdapter(okNotRepAdapter);
        spinner_contractor.setAdapter(okNotRepAdapter);
        spinner_ctrl_inspec_plug.setAdapter(okNotRepAdapter);
        spinner_ctrl_cool_fan.setAdapter(okNotRepAdapter);
        spinner_vvvf_condition.setAdapter(okNotRepAdapter);
        spinner_all_pcbs.setAdapter(okNotRepAdapter);
        spinner_main_staff_break_condition.setAdapter(okNotRepAdapter);
        spinner_step_roll.setAdapter(okNotRepAdapter);
        spinner_step_chain_roll.setAdapter(okNotRepAdapter);
        spinner_hand_condition.setAdapter(okNotRepAdapter);
        spinner_hand_tens_wheel.setAdapter(okNotRepAdapter);
        spinner_comb_teeth.setAdapter(okNotRepAdapter);
        spinner_steps_condition.setAdapter(okNotRepAdapter);
        spinner_skirt_brush.setAdapter(okNotRepAdapter);
        spinner_glass.setAdapter(okNotRepAdapter);
        spinner_ss_balustrade.setAdapter(okNotRepAdapter);
        spinner_end_rev_break.setAdapter(okNotRepAdapter);
        spinner_sub_roller.setAdapter(okNotRepAdapter);
        spinner_hand_pedes_bear_assum.setAdapter(okNotRepAdapter);
        spinner_main_dri_chai_hand_chai_con.setAdapter(okNotRepAdapter);
        spinner_pin_wheel_cond.setAdapter(okNotRepAdapter);
        spinner_dri_chain_slid.setAdapter(okNotRepAdapter);
        spinner_tang_rail.setAdapter(okNotRepAdapter);
        spinner_all_saf_switch_condition.setAdapter(okNotRepAdapter);
        spinner_cable_condition.setAdapter(okNotRepAdapter);
        spinner_smps_board.setAdapter(okNotRepAdapter);
        spinner_emerg_stop_button.setAdapter(okNotRepAdapter);
        spinner_key_switch.setAdapter(okNotRepAdapter);
        spinner_sensors.setAdapter(okNotRepAdapter);
        spinner_insp_plug.setAdapter(okNotRepAdapter);
        spinner_step_gap_light.setAdapter(okNotRepAdapter);
        spinner_skirting_lights.setAdapter(okNotRepAdapter);
        spinner_pit_lights.setAdapter(okNotRepAdapter);
        spinner_comb_light.setAdapter(okNotRepAdapter);
        spinner_traffic_lights.setAdapter(okNotRepAdapter);
        spinner_handrail_lights.setAdapter(okNotRepAdapter);
        spinner_esc_safe_operate.setAdapter(okNoAdapter);

        spinner_machine_type.setOnItemSelectedListener(this);
        spinner_controller_type.setOnItemSelectedListener(this);
        spinner_break_Arm_liner.setOnItemSelectedListener(this);
        spinner_gear.setOnItemSelectedListener(this);
        spinner_motor.setOnItemSelectedListener(this);
        spinner_break_motor_nim.setOnItemSelectedListener(this);
        spinner_plc_mic.setOnItemSelectedListener(this);
        spinner_replay.setOnItemSelectedListener(this);
        spinner_contractor.setOnItemSelectedListener(this);
        spinner_ctrl_inspec_plug.setOnItemSelectedListener(this);
        spinner_ctrl_cool_fan.setOnItemSelectedListener(this);
        spinner_vvvf_condition.setOnItemSelectedListener(this);
        spinner_all_pcbs.setOnItemSelectedListener(this);
        spinner_main_staff_break_condition.setOnItemSelectedListener(this);
        spinner_step_roll.setOnItemSelectedListener(this);
        spinner_step_chain_roll.setOnItemSelectedListener(this);
        spinner_hand_condition.setOnItemSelectedListener(this);
        spinner_hand_tens_wheel.setOnItemSelectedListener(this);
        spinner_comb_teeth.setOnItemSelectedListener(this);
        spinner_steps_condition.setOnItemSelectedListener(this);
        spinner_skirt_brush.setOnItemSelectedListener(this);
        spinner_glass.setOnItemSelectedListener(this);
        spinner_ss_balustrade.setOnItemSelectedListener(this);
        spinner_end_rev_break.setOnItemSelectedListener(this);
        spinner_sub_roller.setOnItemSelectedListener(this);
        spinner_hand_pedes_bear_assum.setOnItemSelectedListener(this);
        spinner_main_dri_chai_hand_chai_con.setOnItemSelectedListener(this);
        spinner_pin_wheel_cond.setOnItemSelectedListener(this);
        spinner_dri_chain_slid.setOnItemSelectedListener(this);
        spinner_tang_rail.setOnItemSelectedListener(this);
        spinner_all_saf_switch_condition.setOnItemSelectedListener(this);
        spinner_cable_condition.setOnItemSelectedListener(this);
        spinner_smps_board.setOnItemSelectedListener(this);
        spinner_emerg_stop_button.setOnItemSelectedListener(this);
        spinner_key_switch.setOnItemSelectedListener(this);
        spinner_sensors.setOnItemSelectedListener(this);
        spinner_insp_plug.setOnItemSelectedListener(this);
        spinner_step_gap_light.setOnItemSelectedListener(this);
        spinner_skirting_lights.setOnItemSelectedListener(this);
        spinner_pit_lights.setOnItemSelectedListener(this);
        spinner_comb_light.setOnItemSelectedListener(this);
        spinner_traffic_lights.setOnItemSelectedListener(this);
        spinner_handrail_lights.setOnItemSelectedListener(this);
        spinner_esc_safe_operate.setOnItemSelectedListener(this);

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

            escalatorSurveyFormRequest.setInstalled_on(formattedDate);
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
                escalatorSurveyFormRequest.setSubmitted_by_on(formattedDate);
                escalatorSurveyFormRequest.setSurvey_no(formattedDate);
                escalatorCheckDateRequest.setSurvey_no(formattedDate);
                getEscalatorCheckDate();
            } else if (strDateType.equalsIgnoreCase("txt_survey_on")) {
                escalatorSurveyFormRequest.setSurvey_no(formattedDate);
                escalatorCheckDateRequest.setSurvey_no(formattedDate);
                getEscalatorCheckDate();
            }

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void uploadDigitalSignatureImageRequest(File file) {

        progressDialog = new ProgressDialog(EscalatorSurveyFormActivity.this);
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
                        escalatorSurveyFormRequest.setCus_survey_by_signature(uploadimagepath);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "onItemSelected: view -> " + view.getId() + " i -> " + i + " l -> " + l);
        String item = adapterView.getItemAtPosition(i).toString();

        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_machine_type:
                    escalatorSurveyFormRequest.setMachine_type("");
                    break;
                case R.id.spinner_controller_type:
                    escalatorSurveyFormRequest.setController_type("");
                    break;
                case R.id.spinner_break_Arm_liner:
                    escalatorSurveyFormRequest.setMachine("");
                    break;
                case R.id.spinner_gear:
                    escalatorSurveyFormRequest.setGear("");
                    break;
                case R.id.spinner_motor:
                    escalatorSurveyFormRequest.setMotor("");
                    break;
                case R.id.spinner_break_motor_nim:
                    escalatorSurveyFormRequest.setBrake_motor("");
                    break;
                case R.id.spinner_plc_mic:
                    escalatorSurveyFormRequest.setMicro_process("");
                    break;
                case R.id.spinner_replay:
                    escalatorSurveyFormRequest.setRelays("");
                    break;
                case R.id.spinner_contractor:
                    escalatorSurveyFormRequest.setContractor("");
                    break;
                case R.id.spinner_ctrl_inspec_plug:
                    escalatorSurveyFormRequest.setController_inspec("");
                    break;
                case R.id.spinner_ctrl_cool_fan:
                    escalatorSurveyFormRequest.setController_colling_fan("");
                    break;
                case R.id.spinner_vvvf_condition:
                    escalatorSurveyFormRequest.setVvvf_conducation("");
                    break;
                case R.id.spinner_all_pcbs:
                    escalatorSurveyFormRequest.setAll_pcbs("");
                    break;
                case R.id.spinner_main_staff_break_condition:
                    escalatorSurveyFormRequest.setMain_shaft("");
                    break;
                case R.id.spinner_step_roll:
                    escalatorSurveyFormRequest.setStep_roller("");
                    break;
                case R.id.spinner_step_chain_roll:
                    escalatorSurveyFormRequest.setStep_chain_roller("");
                    break;
                case R.id.spinner_hand_condition:
                    escalatorSurveyFormRequest.setHandrail_con("");
                    break;
                case R.id.spinner_hand_tens_wheel:
                    escalatorSurveyFormRequest.setHandrail_tension("");
                    break;
                case R.id.spinner_comb_teeth:
                    escalatorSurveyFormRequest.setComb_teeth("");
                    break;
                case R.id.spinner_steps_condition:
                    escalatorSurveyFormRequest.setSteps_conducation("");
                    break;
                case R.id.spinner_skirt_brush:
                    escalatorSurveyFormRequest.setSkirting_brush("");
                    break;
                case R.id.spinner_glass:
                    escalatorSurveyFormRequest.setGlass("");
                    break;
                case R.id.spinner_ss_balustrade:
                    escalatorSurveyFormRequest.setSs_balustrade("");
                    break;
                case R.id.spinner_end_rev_break:
                    escalatorSurveyFormRequest.setEnd_revesable_bear("");
                    break;
                case R.id.spinner_sub_roller:
                    escalatorSurveyFormRequest.setSub_roller("");
                    break;
                case R.id.spinner_hand_pedes_bear_assum:
                    escalatorSurveyFormRequest.setHandrail_ped("");
                    break;
                case R.id.spinner_main_dri_chai_hand_chai_con:
                    escalatorSurveyFormRequest.setMain_drive("");
                    break;
                case R.id.spinner_pin_wheel_cond:
                    escalatorSurveyFormRequest.setPinion_wheel("");
                    break;
                case R.id.spinner_dri_chain_slid:
                    escalatorSurveyFormRequest.setDrive_chain_slider("");
                    break;
                case R.id.spinner_tang_rail:
                    escalatorSurveyFormRequest.setTangent_rail("");
                    break;
                case R.id.spinner_all_saf_switch_condition:
                    escalatorSurveyFormRequest.setAll_safety_switchs("");
                    break;
                case R.id.spinner_cable_condition:
                    escalatorSurveyFormRequest.setCable_conducation("");
                    break;
                case R.id.spinner_smps_board:
                    escalatorSurveyFormRequest.setSmps_board("");
                    break;
                case R.id.spinner_emerg_stop_button:
                    escalatorSurveyFormRequest.setEme_stop_button("");
                    break;
                case R.id.spinner_key_switch:
                    escalatorSurveyFormRequest.setKey_switch("");
                    break;
                case R.id.spinner_sensors:
                    escalatorSurveyFormRequest.setSensors("");
                    break;
                case R.id.spinner_insp_plug:
                    escalatorSurveyFormRequest.setInspec_plugs("");
                    break;
                case R.id.spinner_step_gap_light:
                    escalatorSurveyFormRequest.setStep_gap_light("");
                    break;
                case R.id.spinner_skirting_lights:
                    escalatorSurveyFormRequest.setSkirting_light("");
                    break;
                case R.id.spinner_pit_lights:
                    escalatorSurveyFormRequest.setPit_light("");
                    break;
                case R.id.spinner_comb_light:
                    escalatorSurveyFormRequest.setComb_light("");
                    break;
                case R.id.spinner_traffic_lights:
                    escalatorSurveyFormRequest.setTraffic_light("");
                    break;
                case R.id.spinner_handrail_lights:
                    escalatorSurveyFormRequest.setHandrail_light("");
                    break;
                case R.id.spinner_esc_safe_operate:
                    escalatorSurveyFormRequest.setEscaltor_safe_op("");
                    break;
                default:

                    break;
            }
        } else {

            switch (adapterView.getId()) {
                case R.id.spinner_machine_type:
                    escalatorSurveyFormRequest.setMachine_type(item);
                    break;
                case R.id.spinner_controller_type:
                    escalatorSurveyFormRequest.setController_type(item);
                    break;
                case R.id.spinner_break_Arm_liner:
                    escalatorSurveyFormRequest.setMachine(item);
                    break;
                case R.id.spinner_gear:
                    escalatorSurveyFormRequest.setGear(item);
                    break;
                case R.id.spinner_motor:
                    escalatorSurveyFormRequest.setMotor(item);
                    break;
                case R.id.spinner_break_motor_nim:
                    escalatorSurveyFormRequest.setBrake_motor(item);
                    break;
                case R.id.spinner_plc_mic:
                    escalatorSurveyFormRequest.setMicro_process(item);
                    break;
                case R.id.spinner_replay:
                    escalatorSurveyFormRequest.setRelays(item);
                    break;
                case R.id.spinner_contractor:
                    escalatorSurveyFormRequest.setContractor(item);
                    break;
                case R.id.spinner_ctrl_inspec_plug:
                    escalatorSurveyFormRequest.setController_inspec(item);
                    break;
                case R.id.spinner_ctrl_cool_fan:
                    escalatorSurveyFormRequest.setController_colling_fan(item);
                    break;
                case R.id.spinner_vvvf_condition:
                    escalatorSurveyFormRequest.setVvvf_conducation(item);
                    break;
                case R.id.spinner_all_pcbs:
                    escalatorSurveyFormRequest.setAll_pcbs(item);
                    break;
                case R.id.spinner_main_staff_break_condition:
                    escalatorSurveyFormRequest.setMain_shaft(item);
                    break;
                case R.id.spinner_step_roll:
                    escalatorSurveyFormRequest.setStep_roller(item);
                    break;
                case R.id.spinner_step_chain_roll:
                    escalatorSurveyFormRequest.setStep_chain_roller(item);
                    break;
                case R.id.spinner_hand_condition:
                    escalatorSurveyFormRequest.setHandrail_con(item);
                    break;
                case R.id.spinner_hand_tens_wheel:
                    escalatorSurveyFormRequest.setHandrail_tension(item);
                    break;
                case R.id.spinner_comb_teeth:
                    escalatorSurveyFormRequest.setComb_teeth(item);
                    break;
                case R.id.spinner_steps_condition:
                    escalatorSurveyFormRequest.setSteps_conducation(item);
                    break;
                case R.id.spinner_skirt_brush:
                    escalatorSurveyFormRequest.setSkirting_brush(item);
                    break;
                case R.id.spinner_glass:
                    escalatorSurveyFormRequest.setGlass(item);
                    break;
                case R.id.spinner_ss_balustrade:
                    escalatorSurveyFormRequest.setSs_balustrade(item);
                    break;
                case R.id.spinner_end_rev_break:
                    escalatorSurveyFormRequest.setEnd_revesable_bear(item);
                    break;
                case R.id.spinner_sub_roller:
                    escalatorSurveyFormRequest.setSub_roller(item);
                    break;
                case R.id.spinner_hand_pedes_bear_assum:
                    escalatorSurveyFormRequest.setHandrail_ped(item);
                    break;
                case R.id.spinner_main_dri_chai_hand_chai_con:
                    escalatorSurveyFormRequest.setMain_drive(item);
                    break;
                case R.id.spinner_pin_wheel_cond:
                    escalatorSurveyFormRequest.setPinion_wheel(item);
                    break;
                case R.id.spinner_dri_chain_slid:
                    escalatorSurveyFormRequest.setDrive_chain_slider(item);
                    break;
                case R.id.spinner_tang_rail:
                    escalatorSurveyFormRequest.setTangent_rail(item);
                    break;
                case R.id.spinner_all_saf_switch_condition:
                    escalatorSurveyFormRequest.setAll_safety_switchs(item);
                    break;
                case R.id.spinner_cable_condition:
                    escalatorSurveyFormRequest.setCable_conducation(item);
                    break;
                case R.id.spinner_smps_board:
                    escalatorSurveyFormRequest.setSmps_board(item);
                    break;
                case R.id.spinner_emerg_stop_button:
                    escalatorSurveyFormRequest.setEme_stop_button(item);
                    break;
                case R.id.spinner_key_switch:
                    escalatorSurveyFormRequest.setKey_switch(item);
                    break;
                case R.id.spinner_sensors:
                    escalatorSurveyFormRequest.setSensors(item);
                    break;
                case R.id.spinner_insp_plug:
                    escalatorSurveyFormRequest.setInspec_plugs(item);
                    break;
                case R.id.spinner_step_gap_light:
                    escalatorSurveyFormRequest.setStep_gap_light(item);
                    break;
                case R.id.spinner_skirting_lights:
                    escalatorSurveyFormRequest.setSkirting_light(item);
                    break;
                case R.id.spinner_pit_lights:
                    escalatorSurveyFormRequest.setPit_light(item);
                    break;
                case R.id.spinner_comb_light:
                    escalatorSurveyFormRequest.setComb_light(item);
                    break;
                case R.id.spinner_traffic_lights:
                    escalatorSurveyFormRequest.setTraffic_light(item);
                    break;
                case R.id.spinner_handrail_lights:
                    escalatorSurveyFormRequest.setHandrail_light(item);
                    break;
                case R.id.spinner_esc_safe_operate:
                    escalatorSurveyFormRequest.setEscaltor_safe_op(item);
                    break;
                default:

                    break;
            }
        }
        Log.i(TAG, "onItemSelected: item -> " + item);
        Log.i(TAG, "onItemSelected: escalatorSurveyFormRequest -> " + new Gson().toJson(escalatorSurveyFormRequest));
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
                validateEscalatorSurveyFormRequest();
            }
            break;
        }
    }

    private void validateEscalatorSurveyFormRequest() {
        Log.i(TAG, "validateEscalatorSurveyFormRequest: escalatorSurveyFormRequest (1) -> " + new Gson().toJson(escalatorSurveyFormRequest));

        escalatorSurveyFormRequest.setCus_contract(nullPointer(edt_contact_no.getText().toString().trim()));
        escalatorSurveyFormRequest.setCus_desg(nullPointer(edt_desig.getText().toString().trim()));
        escalatorSurveyFormRequest.setCus_name(nullPointer(edt_cust_name.getText().toString().trim()));
        escalatorSurveyFormRequest.setRecommendation(nullPointer(edt_recom_is.getText().toString().trim()));
        escalatorSurveyFormRequest.setMajor_concern(nullPointer(edt_maj_con_area_if.getText().toString().trim()));

        if (!checkDate) {
            ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getMachine_type())) {
            ErrorMsgDialog("Please Select Machine Type");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getController_type())) {
            ErrorMsgDialog("Please Select Controller Type");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getMachine())) {
            ErrorMsgDialog("Please Select Break / Arm / Liner");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getGear())) {
            ErrorMsgDialog("Please Select Gear");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getMotor())) {
            ErrorMsgDialog("Please Select Motor");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getBrake_motor())) {
            ErrorMsgDialog("Please Select Break Motor/ NINGBO XINDA");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getMicro_process())) {
            ErrorMsgDialog("Please Select P.L.C./ Micro Processor");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getRelays())) {
            ErrorMsgDialog("Please Select Relays");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getContractor())) {
            ErrorMsgDialog("Please Select Contractor");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getController_inspec())) {
            ErrorMsgDialog("Please Select Controller Inspection Plug");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getController_colling_fan())) {
            ErrorMsgDialog("Please Select Controller Colling Fan");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getVvvf_conducation())) {
            ErrorMsgDialog("Please Select VVVF Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getAll_pcbs())) {
            ErrorMsgDialog("Please Select ALL PCBS");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getMain_shaft())) {
            ErrorMsgDialog("Please Select Main Shaft Breaking Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getStep_roller())) {
            ErrorMsgDialog("Please Select Step Roller");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getStep_chain_roller())) {
            ErrorMsgDialog("Please Select Step Chain Roller");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getHandrail_con())) {
            ErrorMsgDialog("Please Select Handrail Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getHandrail_tension())) {
            ErrorMsgDialog("Please Select Handrail Tension Wheel");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getComb_teeth())) {
            ErrorMsgDialog("Please Select Comb Teeth");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSteps_conducation())) {
            ErrorMsgDialog("Please Select Step Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSkirting_brush())) {
            ErrorMsgDialog("Please Select Skirting Brush");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getGlass())) {
            ErrorMsgDialog("Please Select Glass");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSs_balustrade())) {
            ErrorMsgDialog("Please Select S.S. Balustrade");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getEnd_revesable_bear())) {
            ErrorMsgDialog("Please Select End Reversible Bear");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSub_roller())) {
            ErrorMsgDialog("Please Select Sub Roller");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getHandrail_ped())) {
            ErrorMsgDialog("Please Select Handrail Pedestal braking Assembly");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getMain_drive())) {
            ErrorMsgDialog("Please Select Main Drive Chain / Handrail Chain Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getPinion_wheel())) {
            ErrorMsgDialog("Please Select Pinion WHeel Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getDrive_chain_slider())) {
            ErrorMsgDialog("Please Select Drive Chain Slider");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getTangent_rail())) {
            ErrorMsgDialog("Please Select Tangent Rail");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getAll_safety_switchs())) {
            ErrorMsgDialog("Please Select All Safety Switches Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getCable_conducation())) {
            ErrorMsgDialog("Please Select Cable Condition");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSmps_board())) {
            ErrorMsgDialog("Please Select S.M.P.S. Board");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getEme_stop_button())) {
            ErrorMsgDialog("Please Select Emergency Stop Button");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getKey_switch())) {
            ErrorMsgDialog("Please Select Key Switches");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSensors())) {
            ErrorMsgDialog("Please Select Sensors");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getInspec_plugs())) {
            ErrorMsgDialog("Please Select Inspection Plugs");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getStep_gap_light())) {
            ErrorMsgDialog("Please Select Step Gap Light");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSkirting_light())) {
            ErrorMsgDialog("Please Select Skirting Light");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getPit_light())) {
            ErrorMsgDialog("Please Select Pit Light");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getComb_light())) {
            ErrorMsgDialog("Please Select Comb Light");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getTraffic_light())) {
            ErrorMsgDialog("Please Select Traffic Light");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getHandrail_light())) {
            ErrorMsgDialog("Please Select Handrail Light");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getEscaltor_safe_op())) {
            ErrorMsgDialog("Please Select Escalator Safe To Operate");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getCus_name())) {
            ErrorMsgDialog("Please Select Customer Name");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getCus_desg())) {
            ErrorMsgDialog("Please Select Customer Designation");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getCus_contract())) {
            ErrorMsgDialog("Please Select Contact No");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getCus_name())) {
            ErrorMsgDialog("Please Select Survey Conducted By");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSurvey_no())) {
            ErrorMsgDialog("Please Select Survey Conducted On");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getCus_survey_by_signature())) {
            ErrorMsgDialog("Please Select Survey Conductor Signature");
        } else if (!nullPointerValidator(escalatorSurveyFormRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Select Survey Submitted On");
        } else {
            Log.i(TAG, "validateEscalatorSurveyFormRequest: escalatorSurveyFormRequest (2) -> " + new Gson().toJson(escalatorSurveyFormRequest));
            getCreateEscalatorSurvey();
        }
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

    private void getCreateEscalatorSurvey() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getCreateEscalatorSurvey(getContentType(), escalatorSurveyFormRequest);
        Log.i(TAG, "getCreateEscalatorSurvey: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getCreateEscalatorSurvey: onResponse: NewJobListEscalatorSurveyResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getCreateEscalatorSurvey: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void getEscalatorCheckDate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Log.i(TAG, "getEscalatorCheckDate: escalatorCheckDateRequest -> " + new Gson().toJson(escalatorCheckDateRequest));
        Call<SuccessResponse> call = apiInterface.getEscalatorCheckDate(getContentType(), escalatorCheckDateRequest);
        Log.i(TAG, "getEscalatorCheckDate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getEscalatorCheckDate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getEscalatorCheckDate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }
}