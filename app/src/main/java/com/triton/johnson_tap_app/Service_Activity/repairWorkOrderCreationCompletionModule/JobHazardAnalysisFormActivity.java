package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationCompletionModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestJobHazardCreateRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestApprovalRequestListRpMechResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobHazardAnalysisFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String formattedDate = "";
    private MultipartBody.Part signaturePart;
    private Button clear_button_mechanic, save_button_mechanic, btn_submit;
    private Bitmap signatureBitmap;
    private SignaturePad signaturePadMechanic;
    private ImageView img_back;
    private TextView txt_date, txt_job_id, txt_building_name, txt_mech_date, txt_eng_date, txt_branch_code,
            txt_nature_work, txt_mech_name, txt_mech_emp_num, txt_eng_name, txt_eng_emp_num;
    private EditText edt_remark;
    private Spinner spin_team_aware_stand_repair, spin_comp_conduct, spin_rel_ppes, spin_prop_spec_tools, spin_mech_differ_level,
            spin_barr_main_display, spin_discon_elect_cic, spin_cert_hoist_tool, spin_access_mach_room, spin_light_adeq_machine_room,
            spin_floor_free_tripping, spin_oil_lub_closed, spin_speed_gov_func, spin_host_available, spin_stop_switch_easily_access_iden_car,
            spin_stop_switch_func_ver_car, spin_car_top_insp_func_prop_verif, spin_light_adeq_car_top, spin_car_top_barri_angle_install,
            spin_car_top_phy_dam_cab_wire_work, spin_tools_mater_repair, spin_mech_safety_gear, spin_light_adeq_hoist_way,
            spin_covers_fascia_install, spin_hoist_screen_protect_insta, spin_stop_switch_easily_access_iden_pit, spin_stop_switch_func_ver_pit,
            spin_light_adeq_pit, spin_pit_lad_install_access, spin_cwt_scr_guard_blo_buf_top, spin_dup_shaft_pit_not_done,
            spin_free_wat_oil_mat_trip_hazard;
    private String[] yesNoNaArray;
    private String strDateType = "", TAG = JobHazardAnalysisFormActivity.class.getSimpleName(), se_user_id = "",
            se_user_mobile_no = "", se_user_name = "", se_user_location = "", networkStatus, uploadImagePath = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private Context context;
    private RepairWorkRequestApprovalRequestListRpMechResponse.Data repairWorkRequestApprovalRequestListRpMechDataResponse = new RepairWorkRequestApprovalRequestListRpMechResponse.Data();
    private RepairWorkRequestJobHazardCreateRequest repairWorkRequestJobHazardCreateRequest = new RepairWorkRequestJobHazardCreateRequest();
    private SharedPreferences sharedPreferences;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_hazard_analysis_form);

        context = JobHazardAnalysisFormActivity.this;

        img_back = findViewById(R.id.img_back);

        btn_submit = findViewById(R.id.btn_submit);
        clear_button_mechanic = findViewById(R.id.clear_button_mechanic);
        save_button_mechanic = findViewById(R.id.save_button_mechanic);

        txt_date = findViewById(R.id.txt_date);
        txt_job_id = findViewById(R.id.txt_job_id);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_mech_date = findViewById(R.id.txt_mech_date);
        txt_eng_date = findViewById(R.id.txt_eng_date);
        txt_branch_code = findViewById(R.id.txt_branch_code);
        txt_nature_work = findViewById(R.id.txt_nature_work);
        txt_mech_name = findViewById(R.id.txt_mech_name);
        txt_mech_emp_num = findViewById(R.id.txt_mech_emp_num);
        txt_eng_name = findViewById(R.id.txt_eng_name);
        txt_eng_emp_num = findViewById(R.id.txt_eng_emp_num);

        signaturePadMechanic = findViewById(R.id.signaturePadMechanic);

        edt_remark = findViewById(R.id.edt_remark);

        spin_team_aware_stand_repair = findViewById(R.id.spin_team_aware_stand_repair);
        spin_comp_conduct = findViewById(R.id.spin_comp_conduct);
        spin_rel_ppes = findViewById(R.id.spin_rel_ppes);
        spin_prop_spec_tools = findViewById(R.id.spin_prop_spec_tools);
        spin_mech_differ_level = findViewById(R.id.spin_mech_differ_level);
        spin_barr_main_display = findViewById(R.id.spin_barr_main_display);
        spin_discon_elect_cic = findViewById(R.id.spin_discon_elect_cic);
        spin_cert_hoist_tool = findViewById(R.id.spin_cert_hoist_tool);
        spin_access_mach_room = findViewById(R.id.spin_access_mach_room);
        spin_light_adeq_machine_room = findViewById(R.id.spin_light_adeq_machine_room);
        spin_floor_free_tripping = findViewById(R.id.spin_floor_free_tripping);
        spin_oil_lub_closed = findViewById(R.id.spin_oil_lub_closed);
        spin_speed_gov_func = findViewById(R.id.spin_speed_gov_func);
        spin_host_available = findViewById(R.id.spin_host_available);
        spin_stop_switch_easily_access_iden_car = findViewById(R.id.spin_stop_switch_easily_access_iden_car);
        spin_stop_switch_func_ver_car = findViewById(R.id.spin_stop_switch_func_ver_car);
        spin_car_top_insp_func_prop_verif = findViewById(R.id.spin_car_top_insp_func_prop_verif);
        spin_light_adeq_car_top = findViewById(R.id.spin_light_adeq_car_top);
        spin_car_top_barri_angle_install = findViewById(R.id.spin_car_top_barri_angle_install);
        spin_car_top_phy_dam_cab_wire_work = findViewById(R.id.spin_car_top_phy_dam_cab_wire_work);
        spin_tools_mater_repair = findViewById(R.id.spin_tools_mater_repair);
        spin_mech_safety_gear = findViewById(R.id.spin_mech_safety_gear);
        spin_light_adeq_hoist_way = findViewById(R.id.spin_light_adeq_hoist_way);
        spin_covers_fascia_install = findViewById(R.id.spin_covers_fascia_install);
        spin_hoist_screen_protect_insta = findViewById(R.id.spin_hoist_screen_protect_insta);
        spin_stop_switch_easily_access_iden_pit = findViewById(R.id.spin_stop_switch_easily_access_iden_pit);
        spin_stop_switch_func_ver_pit = findViewById(R.id.spin_stop_switch_func_ver_pit);
        spin_light_adeq_pit = findViewById(R.id.spin_light_adeq_pit);
        spin_pit_lad_install_access = findViewById(R.id.spin_pit_lad_install_access);
        spin_cwt_scr_guard_blo_buf_top = findViewById(R.id.spin_cwt_scr_guard_blo_buf_top);
        spin_dup_shaft_pit_not_done = findViewById(R.id.spin_dup_shaft_pit_not_done);
        spin_free_wat_oil_mat_trip_hazard = findViewById(R.id.spin_free_wat_oil_mat_trip_hazard);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("repairWorkRequestApprovalRequestListRpMechDataResponse")) {
                repairWorkRequestApprovalRequestListRpMechDataResponse = extras.getParcelable("repairWorkRequestApprovalRequestListRpMechDataResponse");
            }
        }
        Log.i(TAG, "onCreate: repairWorkRequestApprovalRequestListRpMechDataResponse -> " + new Gson().toJson(repairWorkRequestApprovalRequestListRpMechDataResponse));

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);

        yesNoNaArray = getResources().getStringArray(R.array.yes_no_na_array);

        ArrayAdapter<String> yesNoNaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoNaArray);
        yesNoNaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin_team_aware_stand_repair.setAdapter(yesNoNaAdapter);
        spin_comp_conduct.setAdapter(yesNoNaAdapter);
        spin_rel_ppes.setAdapter(yesNoNaAdapter);
        spin_prop_spec_tools.setAdapter(yesNoNaAdapter);
        spin_mech_differ_level.setAdapter(yesNoNaAdapter);
        spin_barr_main_display.setAdapter(yesNoNaAdapter);
        spin_discon_elect_cic.setAdapter(yesNoNaAdapter);
        spin_cert_hoist_tool.setAdapter(yesNoNaAdapter);
        spin_access_mach_room.setAdapter(yesNoNaAdapter);
        spin_light_adeq_machine_room.setAdapter(yesNoNaAdapter);
        spin_floor_free_tripping.setAdapter(yesNoNaAdapter);
        spin_oil_lub_closed.setAdapter(yesNoNaAdapter);
        spin_speed_gov_func.setAdapter(yesNoNaAdapter);
        spin_host_available.setAdapter(yesNoNaAdapter);
        spin_stop_switch_easily_access_iden_car.setAdapter(yesNoNaAdapter);
        spin_stop_switch_func_ver_car.setAdapter(yesNoNaAdapter);
        spin_car_top_insp_func_prop_verif.setAdapter(yesNoNaAdapter);
        spin_light_adeq_car_top.setAdapter(yesNoNaAdapter);
        spin_car_top_barri_angle_install.setAdapter(yesNoNaAdapter);
        spin_car_top_phy_dam_cab_wire_work.setAdapter(yesNoNaAdapter);
        spin_tools_mater_repair.setAdapter(yesNoNaAdapter);
        spin_mech_safety_gear.setAdapter(yesNoNaAdapter);
        spin_light_adeq_hoist_way.setAdapter(yesNoNaAdapter);
        spin_covers_fascia_install.setAdapter(yesNoNaAdapter);
        spin_hoist_screen_protect_insta.setAdapter(yesNoNaAdapter);
        spin_stop_switch_easily_access_iden_pit.setAdapter(yesNoNaAdapter);
        spin_stop_switch_func_ver_pit.setAdapter(yesNoNaAdapter);
        spin_light_adeq_pit.setAdapter(yesNoNaAdapter);
        spin_pit_lad_install_access.setAdapter(yesNoNaAdapter);
        spin_cwt_scr_guard_blo_buf_top.setAdapter(yesNoNaAdapter);
        spin_dup_shaft_pit_not_done.setAdapter(yesNoNaAdapter);
        spin_free_wat_oil_mat_trip_hazard.setAdapter(yesNoNaAdapter);

        spin_team_aware_stand_repair.setOnItemSelectedListener(this);
        spin_comp_conduct.setOnItemSelectedListener(this);
        spin_rel_ppes.setOnItemSelectedListener(this);
        spin_prop_spec_tools.setOnItemSelectedListener(this);
        spin_mech_differ_level.setOnItemSelectedListener(this);
        spin_barr_main_display.setOnItemSelectedListener(this);
        spin_discon_elect_cic.setOnItemSelectedListener(this);
        spin_cert_hoist_tool.setOnItemSelectedListener(this);
        spin_access_mach_room.setOnItemSelectedListener(this);
        spin_light_adeq_machine_room.setOnItemSelectedListener(this);
        spin_floor_free_tripping.setOnItemSelectedListener(this);
        spin_oil_lub_closed.setOnItemSelectedListener(this);
        spin_speed_gov_func.setOnItemSelectedListener(this);
        spin_host_available.setOnItemSelectedListener(this);
        spin_stop_switch_easily_access_iden_car.setOnItemSelectedListener(this);
        spin_stop_switch_func_ver_car.setOnItemSelectedListener(this);
        spin_car_top_insp_func_prop_verif.setOnItemSelectedListener(this);
        spin_light_adeq_car_top.setOnItemSelectedListener(this);
        spin_car_top_barri_angle_install.setOnItemSelectedListener(this);
        spin_car_top_phy_dam_cab_wire_work.setOnItemSelectedListener(this);
        spin_tools_mater_repair.setOnItemSelectedListener(this);
        spin_mech_safety_gear.setOnItemSelectedListener(this);
        spin_light_adeq_hoist_way.setOnItemSelectedListener(this);
        spin_covers_fascia_install.setOnItemSelectedListener(this);
        spin_hoist_screen_protect_insta.setOnItemSelectedListener(this);
        spin_stop_switch_easily_access_iden_pit.setOnItemSelectedListener(this);
        spin_stop_switch_func_ver_pit.setOnItemSelectedListener(this);
        spin_light_adeq_pit.setOnItemSelectedListener(this);
        spin_pit_lad_install_access.setOnItemSelectedListener(this);
        spin_cwt_scr_guard_blo_buf_top.setOnItemSelectedListener(this);
        spin_dup_shaft_pit_not_done.setOnItemSelectedListener(this);
        spin_free_wat_oil_mat_trip_hazard.setOnItemSelectedListener(this);

        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        save_button_mechanic.setOnClickListener(this);
        clear_button_mechanic.setOnClickListener(this);
        txt_date.setOnClickListener(this);
        txt_mech_date.setOnClickListener(this);
        txt_eng_date.setOnClickListener(this);

        signaturePadMechanic.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            public void onSigned() {
                save_button_mechanic.setEnabled(true);
                clear_button_mechanic.setEnabled(true);
            }

            public void onClear() {
                save_button_mechanic.setEnabled(false);
                clear_button_mechanic.setEnabled(false);
            }
        });

        txt_job_id.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getJob_no());
        txt_building_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getCustomer_name());
        txt_branch_code.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getBr_code());
        txt_nature_work.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getNature_of_work_name());
        txt_mech_name.setText(se_user_name);
        txt_mech_emp_num.setText(se_user_id);
        txt_eng_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getZonal_eng_name());
        txt_eng_emp_num.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getZonal_eng_id());

//        repairWorkRequestJobHazardCreateRequest.setPref_id(repairWorkRequestApprovalRequestListRpMechDataResponse.getPref_id());
        repairWorkRequestJobHazardCreateRequest.setPref_id(repairWorkRequestApprovalRequestListRpMechDataResponse.get_id());
        repairWorkRequestJobHazardCreateRequest.setJob_no(repairWorkRequestApprovalRequestListRpMechDataResponse.getJob_no());
        repairWorkRequestJobHazardCreateRequest.setSite_name(repairWorkRequestApprovalRequestListRpMechDataResponse.getCustomer_name());
        repairWorkRequestJobHazardCreateRequest.setMech_emp_id(se_user_id);
        repairWorkRequestJobHazardCreateRequest.setMech_name(se_user_name);
        repairWorkRequestJobHazardCreateRequest.setBr_code(repairWorkRequestApprovalRequestListRpMechDataResponse.getBr_code());
        repairWorkRequestJobHazardCreateRequest.setNature_of_work(repairWorkRequestApprovalRequestListRpMechDataResponse.getNature_of_work_name());

        repairWorkRequestJobHazardCreateRequest.setSubmitted_by_name(se_user_name);
        repairWorkRequestJobHazardCreateRequest.setSubmitted_by_emp_code(se_user_id);
        repairWorkRequestJobHazardCreateRequest.setSubmitted_by_num(se_user_mobile_no);

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

    private void ErrorMsgDialog(String strMsg) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        if (nullPointerValidator(strMsg)) {
            txt_Message.setText(strMsg);
        }

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void getTodayDate() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        strDateType = "txt_both";
        setDate(day, month, year);
    }

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

        if (strDateType.equalsIgnoreCase("txt_date")) {
            txt_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_mech_date")) {
            txt_mech_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_eng_date")) {
            txt_eng_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_date.setText(dateTime);
            txt_mech_date.setText(dateTime);
            txt_eng_date.setText(dateTime);
        }

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
            formattedDate = str;

            if (strDateType.equalsIgnoreCase("txt_date")) {
                repairWorkRequestJobHazardCreateRequest.setDate_of_sub(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_mech_date")) {
                repairWorkRequestJobHazardCreateRequest.setReport_date(formattedDate);
            } else /*if (strDateType.equalsIgnoreCase("txt_eng_date")) {
                repairWorkRequestJobHazardCreateRequest.setEng_sign_date(formattedDate);
            } else*/ if (strDateType.equalsIgnoreCase("txt_both")) {
                repairWorkRequestJobHazardCreateRequest.setDate_of_sub(formattedDate);
                repairWorkRequestJobHazardCreateRequest.setReport_date(formattedDate);
//                repairWorkRequestJobHazardCreateRequest.setEng_sign_date(formattedDate);
                repairWorkRequestJobHazardCreateRequest.setSubmitted_by_on(formattedDate);
            }

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
        }
    }

    private void validateRepairWorkRequestJobHazardCreateRequest() {
        repairWorkRequestJobHazardCreateRequest.setRemarks(nullPointer(edt_remark.getText().toString().trim()));
        Log.i(TAG, "validateRepairWorkRequestJobHazardCreateRequest: repairWorkRequestJobHazardCreateRequest -> " + new Gson().toJson(repairWorkRequestJobHazardCreateRequest));
        getRepairWorkRequestJobHazardCreate(repairWorkRequestJobHazardCreateRequest);
    }

    private void uploadDigitalSignatureImageRequest(File file) {
        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);
        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(signaturePart);

        Log.i(TAG, "uploadDigitalSignatureImageRequest: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                Log.i(TAG, "uploadDigitalSignatureImageRequest: onResponse: FileUploadResponse -> " + new Gson().toJson(response.body()));
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (200 == response.body().getCode()) {
                        uploadImagePath = response.body().getData();
                        repairWorkRequestJobHazardCreateRequest.setMech_signature(uploadImagePath);
                        if (nullPointerValidator(uploadImagePath)) {
                            signaturePadMechanic.setEnabled(false);
                            save_button_mechanic.setEnabled(false);
                            clear_button_mechanic.setEnabled(false);
                            Toast.makeText(context, "Signature Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            signaturePadMechanic.setEnabled(true);
                            save_button_mechanic.setEnabled(true);
                            clear_button_mechanic.setEnabled(true);
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
                dialog.dismiss();
                Log.e(TAG, "uploadDigitalSignatureImageRequest: onFailure: error -> " + t.getMessage());
                ErrorMsgDialog("Something Went Wrong.. Try Again Later");
            }
        });
    }

    private void getRepairWorkRequestJobHazardCreate(RepairWorkRequestJobHazardCreateRequest repairWorkRequestJobHazardCreateRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getRepairWorkRequestJobHazardCreate(RestUtils.getContentType(), repairWorkRequestJobHazardCreateRequest);
        Log.i(TAG, "getRepairWorkRequestJobHazardCreate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getRepairWorkRequestJobHazardCreate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, RepairWorkOrderCreationCompletionJobActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

//                        onBackPressed();
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getRepairWorkRequestJobHazardCreate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        int selPos = i - 1;

        Log.i(TAG, "onItemSelected: selPos -> " + selPos);

        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spin_team_aware_stand_repair: {
                    repairWorkRequestJobHazardCreateRequest.setGRW01("");
                }
                break;
                case R.id.spin_comp_conduct: {
                    repairWorkRequestJobHazardCreateRequest.setGRW02("");
                }
                break;
                case R.id.spin_rel_ppes: {
                    repairWorkRequestJobHazardCreateRequest.setGRW03("");
                }
                break;
                case R.id.spin_prop_spec_tools: {
                    repairWorkRequestJobHazardCreateRequest.setGRW04("");
                }
                break;
                case R.id.spin_mech_differ_level: {
                    repairWorkRequestJobHazardCreateRequest.setGRW05("");
                }
                break;
                case R.id.spin_barr_main_display: {
                    repairWorkRequestJobHazardCreateRequest.setGRW06("");
                }
                break;
                case R.id.spin_discon_elect_cic: {
                    repairWorkRequestJobHazardCreateRequest.setGRW07("");
                }
                break;
                case R.id.spin_cert_hoist_tool: {
                    repairWorkRequestJobHazardCreateRequest.setGRW08("");
                }
                break;
                case R.id.spin_access_mach_room: {
                    repairWorkRequestJobHazardCreateRequest.setMRW01("");
                }
                break;
                case R.id.spin_light_adeq_machine_room: {
                    repairWorkRequestJobHazardCreateRequest.setMRW02("");
                }
                break;
                case R.id.spin_floor_free_tripping: {
                    repairWorkRequestJobHazardCreateRequest.setMRW03("");
                }
                break;
                case R.id.spin_oil_lub_closed: {
                    repairWorkRequestJobHazardCreateRequest.setMRW04("");
                }
                break;
                case R.id.spin_speed_gov_func: {
                    repairWorkRequestJobHazardCreateRequest.setMRW05("");
                }
                break;
                case R.id.spin_host_available: {
                    repairWorkRequestJobHazardCreateRequest.setMRW06("");
                }
                break;
                case R.id.spin_stop_switch_easily_access_iden_car: {
                    repairWorkRequestJobHazardCreateRequest.setCRW01("");
                }
                break;
                case R.id.spin_stop_switch_func_ver_car: {
                    repairWorkRequestJobHazardCreateRequest.setCRW02("");
                }
                break;
                case R.id.spin_car_top_insp_func_prop_verif: {
                    repairWorkRequestJobHazardCreateRequest.setCRW03("");
                }
                break;
                case R.id.spin_light_adeq_car_top: {
                    repairWorkRequestJobHazardCreateRequest.setCRW04("");
                }
                break;
                case R.id.spin_car_top_barri_angle_install: {
                    repairWorkRequestJobHazardCreateRequest.setCRW05("");
                }
                break;
                case R.id.spin_car_top_phy_dam_cab_wire_work: {
                    repairWorkRequestJobHazardCreateRequest.setCRW06("");
                }
                break;
                case R.id.spin_tools_mater_repair: {
                    repairWorkRequestJobHazardCreateRequest.setCRW07("");
                }
                break;
                case R.id.spin_mech_safety_gear: {
                    repairWorkRequestJobHazardCreateRequest.setCRW08("");
                }
                break;
                case R.id.spin_light_adeq_hoist_way: {
                    repairWorkRequestJobHazardCreateRequest.setHRW01("");
                }
                break;
                case R.id.spin_covers_fascia_install: {
                    repairWorkRequestJobHazardCreateRequest.setHRW02("");
                }
                break;
                case R.id.spin_hoist_screen_protect_insta: {
                    repairWorkRequestJobHazardCreateRequest.setHRW03("");
                }
                break;
                case R.id.spin_stop_switch_easily_access_iden_pit: {
                    repairWorkRequestJobHazardCreateRequest.setPRW01("");
                }
                break;
                case R.id.spin_stop_switch_func_ver_pit: {
                    repairWorkRequestJobHazardCreateRequest.setPRW02("");
                }
                break;
                case R.id.spin_light_adeq_pit: {
                    repairWorkRequestJobHazardCreateRequest.setPRW03("");
                }
                break;
                case R.id.spin_pit_lad_install_access: {
                    repairWorkRequestJobHazardCreateRequest.setPRW04("");
                }
                break;
                case R.id.spin_cwt_scr_guard_blo_buf_top: {
                    repairWorkRequestJobHazardCreateRequest.setPRW05("");
                }
                break;
                case R.id.spin_dup_shaft_pit_not_done: {
                    repairWorkRequestJobHazardCreateRequest.setPRW06("");
                }
                break;
                case R.id.spin_free_wat_oil_mat_trip_hazard: {
                    repairWorkRequestJobHazardCreateRequest.setPRW07("");
                }
                break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spin_team_aware_stand_repair: {
                    repairWorkRequestJobHazardCreateRequest.setGRW01(item);
                }
                break;
                case R.id.spin_comp_conduct: {
                    repairWorkRequestJobHazardCreateRequest.setGRW02(item);
                }
                break;
                case R.id.spin_rel_ppes: {
                    repairWorkRequestJobHazardCreateRequest.setGRW03(item);
                }
                break;
                case R.id.spin_prop_spec_tools: {
                    repairWorkRequestJobHazardCreateRequest.setGRW04(item);
                }
                break;
                case R.id.spin_mech_differ_level: {
                    repairWorkRequestJobHazardCreateRequest.setGRW05(item);
                }
                break;
                case R.id.spin_barr_main_display: {
                    repairWorkRequestJobHazardCreateRequest.setGRW06(item);
                }
                break;
                case R.id.spin_discon_elect_cic: {
                    repairWorkRequestJobHazardCreateRequest.setGRW07(item);
                }
                break;
                case R.id.spin_cert_hoist_tool: {
                    repairWorkRequestJobHazardCreateRequest.setGRW08(item);
                }
                break;
                case R.id.spin_access_mach_room: {
                    repairWorkRequestJobHazardCreateRequest.setMRW01(item);
                }
                break;
                case R.id.spin_light_adeq_machine_room: {
                    repairWorkRequestJobHazardCreateRequest.setMRW02(item);
                }
                break;
                case R.id.spin_floor_free_tripping: {
                    repairWorkRequestJobHazardCreateRequest.setMRW03(item);
                }
                break;
                case R.id.spin_oil_lub_closed: {
                    repairWorkRequestJobHazardCreateRequest.setMRW04(item);
                }
                break;
                case R.id.spin_speed_gov_func: {
                    repairWorkRequestJobHazardCreateRequest.setMRW05(item);
                }
                break;
                case R.id.spin_host_available: {
                    repairWorkRequestJobHazardCreateRequest.setMRW06(item);
                }
                break;
                case R.id.spin_stop_switch_easily_access_iden_car: {
                    repairWorkRequestJobHazardCreateRequest.setCRW01(item);
                }
                break;
                case R.id.spin_stop_switch_func_ver_car: {
                    repairWorkRequestJobHazardCreateRequest.setCRW02(item);
                }
                break;
                case R.id.spin_car_top_insp_func_prop_verif: {
                    repairWorkRequestJobHazardCreateRequest.setCRW03(item);
                }
                break;
                case R.id.spin_light_adeq_car_top: {
                    repairWorkRequestJobHazardCreateRequest.setCRW04(item);
                }
                break;
                case R.id.spin_car_top_barri_angle_install: {
                    repairWorkRequestJobHazardCreateRequest.setCRW05(item);
                }
                break;
                case R.id.spin_car_top_phy_dam_cab_wire_work: {
                    repairWorkRequestJobHazardCreateRequest.setCRW06(item);
                }
                break;
                case R.id.spin_tools_mater_repair: {
                    repairWorkRequestJobHazardCreateRequest.setCRW07(item);
                }
                break;
                case R.id.spin_mech_safety_gear: {
                    repairWorkRequestJobHazardCreateRequest.setCRW08(item);
                }
                break;
                case R.id.spin_light_adeq_hoist_way: {
                    repairWorkRequestJobHazardCreateRequest.setHRW01(item);
                }
                break;
                case R.id.spin_covers_fascia_install: {
                    repairWorkRequestJobHazardCreateRequest.setHRW02(item);
                }
                break;
                case R.id.spin_hoist_screen_protect_insta: {
                    repairWorkRequestJobHazardCreateRequest.setHRW03(item);
                }
                break;
                case R.id.spin_stop_switch_easily_access_iden_pit: {
                    repairWorkRequestJobHazardCreateRequest.setPRW01(item);
                }
                break;
                case R.id.spin_stop_switch_func_ver_pit: {
                    repairWorkRequestJobHazardCreateRequest.setPRW02(item);
                }
                break;
                case R.id.spin_light_adeq_pit: {
                    repairWorkRequestJobHazardCreateRequest.setPRW03(item);
                }
                break;
                case R.id.spin_pit_lad_install_access: {
                    repairWorkRequestJobHazardCreateRequest.setPRW04(item);
                }
                break;
                case R.id.spin_cwt_scr_guard_blo_buf_top: {
                    repairWorkRequestJobHazardCreateRequest.setPRW05(item);
                }
                break;
                case R.id.spin_dup_shaft_pit_not_done: {
                    repairWorkRequestJobHazardCreateRequest.setPRW06(item);
                }
                break;
                case R.id.spin_free_wat_oil_mat_trip_hazard: {
                    repairWorkRequestJobHazardCreateRequest.setPRW07(item);
                }
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
            case R.id.txt_date:
                strDateType = "txt_date";
                callDatePicker();
                break;
            case R.id.txt_mech_date:
                strDateType = "txt_mech_date";
                callDatePicker();
                break;
            case R.id.txt_eng_date:
                strDateType = "txt_eng_date";
                callDatePicker();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.save_button_mechanic: {
                signatureBitmap = signaturePadMechanic.getSignatureBitmap();
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
            case R.id.clear_button_mechanic: {
                signaturePadMechanic.clear();
            }
            break;
            case R.id.btn_submit:
                validateRepairWorkRequestJobHazardCreateRequest();
                break;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}