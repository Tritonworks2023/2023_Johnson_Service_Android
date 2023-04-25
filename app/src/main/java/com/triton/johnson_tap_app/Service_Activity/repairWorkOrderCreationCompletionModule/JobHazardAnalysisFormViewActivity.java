package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationCompletionModule;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationModule.RepairWorkOrderCompletionJobActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestFinalCompletionEditRequest;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestJobHazardByPrefIdRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestApprovalRequestListRpEngResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestJobHazardByPrefIdResponse;
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

public class JobHazardAnalysisFormViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static String formattedDate = "";
    private MultipartBody.Part signaturePart;
    private Button clear_button_engineer, save_button_engineer, btn_submit;
    private Bitmap signatureBitmap;
    private SignaturePad signaturePadEngineer;
    private ImageView img_back, img_mechanic_signature;
    private EditText edt_remark;
    private TextView txt_date, txt_job_id, txt_building_name, txt_mech_date, txt_eng_date, txt_comp_date, txt_branch_code,
            txt_nature_work, txt_mech_name, txt_mech_emp_num, txt_eng_name, txt_eng_emp_num, txt_remark,
            txt_team_aware_stand_repair, txt_comp_conduct, txt_rel_ppes, txt_prop_spec_tools, txt_mech_differ_level,
            txt_barr_main_display, txt_discon_elect_cic, txt_cert_hoist_tool, txt_access_mach_room, txt_light_adeq_machine_room,
            txt_floor_free_tripping, txt_oil_lub_closed, txt_speed_gov_func, txt_host_available, txt_stop_switch_easily_access_iden_car,
            txt_stop_switch_func_ver_car, txt_car_top_insp_func_prop_verif, txt_light_adeq_car_top, txt_car_top_barri_angle_install,
            txt_car_top_phy_dam_cab_wire_work, txt_tools_mater_repair, txt_mech_safety_gear, txt_light_adeq_hoist_way,
            txt_covers_fascia_install, txt_hoist_screen_protect_insta, txt_stop_switch_easily_access_iden_pit, txt_stop_switch_func_ver_pit,
            txt_light_adeq_pit, txt_pit_lad_install_access, txt_cwt_scr_guard_blo_buf_top, txt_dup_shaft_pit_not_done,
            txt_free_wat_oil_mat_trip_hazard;
    private String[] yesNoNaArray;
    private String strDateType = "", TAG = JobHazardAnalysisFormViewActivity.class.getSimpleName(), se_user_id = "",
            se_user_mobile_no = "", se_user_name = "", se_user_location = "", networkStatus, uploadImagePath = "",
            message = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private Context context;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private RepairWorkRequestApprovalRequestListRpEngResponse.Data repairWorkRequestApprovalRequestListRpEngDataResponse = new RepairWorkRequestApprovalRequestListRpEngResponse.Data();
    private RepairWorkRequestJobHazardByPrefIdResponse repairWorkRequestJobHazardByPrefIdResponse = new RepairWorkRequestJobHazardByPrefIdResponse();
    private RepairWorkRequestFinalCompletionEditRequest repairWorkRequestFinalCompletionEditRequest = new RepairWorkRequestFinalCompletionEditRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_hazard_analysis_form_view);

        context = JobHazardAnalysisFormViewActivity.this;

        img_back = findViewById(R.id.img_back);
        img_mechanic_signature = findViewById(R.id.img_mechanic_signature);

        btn_submit = findViewById(R.id.btn_submit);
        clear_button_engineer = findViewById(R.id.clear_button_engineer);
        save_button_engineer = findViewById(R.id.save_button_engineer);

        txt_date = findViewById(R.id.txt_date);
        txt_job_id = findViewById(R.id.txt_job_id);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_mech_date = findViewById(R.id.txt_mech_date);
        txt_eng_date = findViewById(R.id.txt_eng_date);
        txt_comp_date = findViewById(R.id.txt_comp_date);
        txt_branch_code = findViewById(R.id.txt_branch_code);
        txt_nature_work = findViewById(R.id.txt_nature_work);
        txt_mech_name = findViewById(R.id.txt_mech_name);
        txt_mech_emp_num = findViewById(R.id.txt_mech_emp_num);
        txt_eng_name = findViewById(R.id.txt_eng_name);
        txt_eng_emp_num = findViewById(R.id.txt_eng_emp_num);
        txt_team_aware_stand_repair = findViewById(R.id.txt_team_aware_stand_repair);
        txt_comp_conduct = findViewById(R.id.txt_comp_conduct);
        txt_rel_ppes = findViewById(R.id.txt_rel_ppes);
        txt_prop_spec_tools = findViewById(R.id.txt_prop_spec_tools);
        txt_mech_differ_level = findViewById(R.id.txt_mech_differ_level);
        txt_barr_main_display = findViewById(R.id.txt_barr_main_display);
        txt_discon_elect_cic = findViewById(R.id.txt_discon_elect_cic);
        txt_cert_hoist_tool = findViewById(R.id.txt_cert_hoist_tool);
        txt_access_mach_room = findViewById(R.id.txt_access_mach_room);
        txt_light_adeq_machine_room = findViewById(R.id.txt_light_adeq_machine_room);
        txt_floor_free_tripping = findViewById(R.id.txt_floor_free_tripping);
        txt_oil_lub_closed = findViewById(R.id.txt_oil_lub_closed);
        txt_speed_gov_func = findViewById(R.id.txt_speed_gov_func);
        txt_host_available = findViewById(R.id.txt_host_available);
        txt_stop_switch_easily_access_iden_car = findViewById(R.id.txt_stop_switch_easily_access_iden_car);
        txt_stop_switch_func_ver_car = findViewById(R.id.txt_stop_switch_func_ver_car);
        txt_car_top_insp_func_prop_verif = findViewById(R.id.txt_car_top_insp_func_prop_verif);
        txt_light_adeq_car_top = findViewById(R.id.txt_light_adeq_car_top);
        txt_car_top_barri_angle_install = findViewById(R.id.txt_car_top_barri_angle_install);
        txt_car_top_phy_dam_cab_wire_work = findViewById(R.id.txt_car_top_phy_dam_cab_wire_work);
        txt_tools_mater_repair = findViewById(R.id.txt_tools_mater_repair);
        txt_mech_safety_gear = findViewById(R.id.txt_mech_safety_gear);
        txt_light_adeq_hoist_way = findViewById(R.id.txt_light_adeq_hoist_way);
        txt_covers_fascia_install = findViewById(R.id.txt_covers_fascia_install);
        txt_hoist_screen_protect_insta = findViewById(R.id.txt_hoist_screen_protect_insta);
        txt_stop_switch_easily_access_iden_pit = findViewById(R.id.txt_stop_switch_easily_access_iden_pit);
        txt_stop_switch_func_ver_pit = findViewById(R.id.txt_stop_switch_func_ver_pit);
        txt_light_adeq_pit = findViewById(R.id.txt_light_adeq_pit);
        txt_pit_lad_install_access = findViewById(R.id.txt_pit_lad_install_access);
        txt_cwt_scr_guard_blo_buf_top = findViewById(R.id.txt_cwt_scr_guard_blo_buf_top);
        txt_dup_shaft_pit_not_done = findViewById(R.id.txt_dup_shaft_pit_not_done);
        txt_free_wat_oil_mat_trip_hazard = findViewById(R.id.txt_free_wat_oil_mat_trip_hazard);

        edt_remark = findViewById(R.id.edt_remark);

        signaturePadEngineer = findViewById(R.id.signaturePadEngineer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("repairWorkRequestApprovalRequestListRpEngDataResponse")) {
                repairWorkRequestApprovalRequestListRpEngDataResponse = extras.getParcelable("repairWorkRequestApprovalRequestListRpEngDataResponse");
            }
        }
        Log.i(TAG, "onCreate: repairWorkRequestApprovalRequestListRpEngDataResponse -> " + new Gson().toJson(repairWorkRequestApprovalRequestListRpEngDataResponse));

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);

        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        save_button_engineer.setOnClickListener(this);
        clear_button_engineer.setOnClickListener(this);
        txt_eng_date.setOnClickListener(this);
        txt_comp_date.setOnClickListener(this);

        signaturePadEngineer.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            public void onSigned() {
                save_button_engineer.setEnabled(true);
                clear_button_engineer.setEnabled(true);
            }

            public void onClear() {
                save_button_engineer.setEnabled(false);
                clear_button_engineer.setEnabled(false);
            }
        });

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            getRepairWorkRequestJobHazardByPrefId(repairWorkRequestApprovalRequestListRpEngDataResponse.get_id());
        }

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

        if (strDateType.equalsIgnoreCase("txt_comp_date")) {
            txt_comp_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_eng_date")) {
            txt_eng_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_comp_date.setText(dateTime);
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

            if (strDateType.equalsIgnoreCase("txt_comp_date")) {
                repairWorkRequestFinalCompletionEditRequest.setCompleted_date(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_eng_date")) {
                repairWorkRequestFinalCompletionEditRequest.setDate_signature_eng(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                repairWorkRequestFinalCompletionEditRequest.setCompleted_date(formattedDate);
                repairWorkRequestFinalCompletionEditRequest.setDate_signature_eng(formattedDate);
            }

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
        }
    }


    private void setView(RepairWorkRequestApprovalRequestListRpEngResponse.Data repairWorkRequestApprovalRequestListRpEngDataResponse, RepairWorkRequestJobHazardByPrefIdResponse repairWorkRequestJobHazardByPrefIdResponse) {

        txt_date.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getDate_of_sub());
        txt_job_id.setText(repairWorkRequestApprovalRequestListRpEngDataResponse.getJob_no());
        txt_building_name.setText(repairWorkRequestApprovalRequestListRpEngDataResponse.getCustomer_name());
        txt_mech_date.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getReport_date());
        txt_branch_code.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getBr_code());
        txt_nature_work.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getNature_of_work());
        txt_mech_name.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMech_name());
        txt_mech_emp_num.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMech_emp_id());
        txt_eng_name.setText(repairWorkRequestApprovalRequestListRpEngDataResponse.getZonal_eng_name());
        txt_eng_emp_num.setText(repairWorkRequestApprovalRequestListRpEngDataResponse.getZonal_eng_id());
        txt_team_aware_stand_repair.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW01());
        txt_comp_conduct.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW02());
        txt_rel_ppes.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW03());
        txt_prop_spec_tools.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW04());
        txt_mech_differ_level.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW05());
        txt_barr_main_display.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW06());
        txt_discon_elect_cic.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW07());
        txt_cert_hoist_tool.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getGRW08());
        txt_access_mach_room.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMRW01());
        txt_light_adeq_machine_room.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMRW02());
        txt_floor_free_tripping.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMRW03());
        txt_oil_lub_closed.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMRW04());
        txt_speed_gov_func.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMRW05());
        txt_host_available.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getMRW06());
        txt_stop_switch_easily_access_iden_car.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW01());
        txt_stop_switch_func_ver_car.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW02());
        txt_car_top_insp_func_prop_verif.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW03());
        txt_light_adeq_car_top.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW04());
        txt_car_top_barri_angle_install.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW05());
        txt_car_top_phy_dam_cab_wire_work.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW06());
        txt_tools_mater_repair.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW07());
        txt_mech_safety_gear.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getCRW08());
        txt_light_adeq_hoist_way.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getHRW01());
        txt_covers_fascia_install.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getHRW02());
        txt_hoist_screen_protect_insta.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getHRW03());
        txt_stop_switch_easily_access_iden_pit.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW01());
        txt_stop_switch_func_ver_pit.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW02());
        txt_light_adeq_pit.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW03());
        txt_pit_lad_install_access.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW04());
        txt_cwt_scr_guard_blo_buf_top.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW05());
        txt_dup_shaft_pit_not_done.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW06());
        txt_free_wat_oil_mat_trip_hazard.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getPRW07());

        edt_remark.setText(repairWorkRequestJobHazardByPrefIdResponse.getData().getRemarks());

        Picasso.get().load(repairWorkRequestJobHazardByPrefIdResponse.getData().getMech_signature()).into(img_mechanic_signature);
    }

    private RepairWorkRequestJobHazardByPrefIdRequest jobListRequest(String id) {

        RepairWorkRequestJobHazardByPrefIdRequest job = new RepairWorkRequestJobHazardByPrefIdRequest();

        if (nullPointerValidator(id)) {
            job.setPref_id(id);
            Log.i(TAG, "jobListRequest: RepairWorkRequestJobHazardByPrefIdRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void validateRepairWorkRequestFinalCompletionEdit() {

        repairWorkRequestFinalCompletionEditRequest.setJob_haz_id(repairWorkRequestJobHazardByPrefIdResponse.getData().get_id());
        repairWorkRequestFinalCompletionEditRequest.setRepair_pref_id(repairWorkRequestJobHazardByPrefIdResponse.getData().getPref_id());

        if (!nullPointerValidator(repairWorkRequestFinalCompletionEditRequest.getJob_haz_id())) {
            ErrorMsgDialog("Please Select Job Hazard ID.");
        } else if (!nullPointerValidator(repairWorkRequestFinalCompletionEditRequest.getRepair_pref_id())) {
            ErrorMsgDialog("Please Select Repair Pref ID.");
        } else if (!nullPointerValidator(repairWorkRequestFinalCompletionEditRequest.getDate_signature_eng())) {
            ErrorMsgDialog("Please Select Zonal/Route Engineer Signature Date.");
        } else if (!nullPointerValidator(repairWorkRequestFinalCompletionEditRequest.getSignature_eng())) {
            ErrorMsgDialog("Please Put and Save Zonal/Route Engineer Signature.");
        } else if (!nullPointerValidator(repairWorkRequestFinalCompletionEditRequest.getCompleted_date())) {
            ErrorMsgDialog("Please Select Completed Date.");
        } else {
            getRepairWorkRequestFinalCompletionEdit(repairWorkRequestFinalCompletionEditRequest);
        }

        Log.i(TAG, "validateRepairWorkRequestFinalCompletionEdit: repairWorkRequestFinalCompletionEditRequest -> " + new Gson().toJson(repairWorkRequestFinalCompletionEditRequest));

    }

    private void getRepairWorkRequestJobHazardByPrefId(String id) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkRequestJobHazardByPrefIdRequest jobIdRequest = jobListRequest(id);

        if (jobIdRequest != null) {

            Call<RepairWorkRequestJobHazardByPrefIdResponse> call = apiInterface.getRepairWorkRequestJobHazardByPrefId(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getRepairWorkRequestJobHazardByPrefId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkRequestJobHazardByPrefIdResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkRequestJobHazardByPrefIdResponse> call, @NonNull Response<RepairWorkRequestJobHazardByPrefIdResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getRepairWorkRequestJobHazardByPrefId: onResponse: RepairWorkRequestJobHazardByPrefIdResponse -> " + new Gson().toJson(response.body()));
                    repairWorkRequestJobHazardByPrefIdResponse = new RepairWorkRequestJobHazardByPrefIdResponse();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkRequestJobHazardByPrefIdResponse = response.body();
                                setView(repairWorkRequestApprovalRequestListRpEngDataResponse, repairWorkRequestJobHazardByPrefIdResponse);
                            } else {
                                ErrorMsgDialog(message);
                            }
                        } else {
                            ErrorMsgDialog("");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepairWorkRequestJobHazardByPrefIdResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getRepairWorkRequestJobHazardByPrefId: onFailure: error --> " + t.getMessage());
                    ErrorMsgDialog(t.getMessage());
                }
            });
        } else {
            dialog.dismiss();
        }
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
                        repairWorkRequestFinalCompletionEditRequest.setSignature_eng(uploadImagePath);
                        if (nullPointerValidator(uploadImagePath)) {
                            signaturePadEngineer.setEnabled(false);
                            save_button_engineer.setEnabled(false);
                            clear_button_engineer.setEnabled(false);
                            Toast.makeText(context, "Signature Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            signaturePadEngineer.setEnabled(true);
                            save_button_engineer.setEnabled(true);
                            clear_button_engineer.setEnabled(true);
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

    private void getRepairWorkRequestFinalCompletionEdit(RepairWorkRequestFinalCompletionEditRequest repairWorkRequestFinalCompletionEditRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getRepairWorkRequestFinalCompletionEdit(RestUtils.getContentType(), repairWorkRequestFinalCompletionEditRequest);
        Log.i(TAG, "getRepairWorkRequestFinalCompletionEdit: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getRepairWorkRequestJobHazardCreate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, RepairWorkOrderCompletionJobActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getRepairWorkRequestFinalCompletionEdit: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_eng_date:
                strDateType = "txt_eng_date";
                callDatePicker();
                break;
            case R.id.txt_comp_date:
                strDateType = "txt_comp_date";
                callDatePicker();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.save_button_engineer: {
                signatureBitmap = signaturePadEngineer.getSignatureBitmap();
                Log.i(TAG, "save_button_engineer: onClick: signatureBitmap" + signatureBitmap);
                File file = new File(getFilesDir(), "Engineer Signature" + ".jpg");

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
            case R.id.clear_button_engineer: {
                signaturePadEngineer.clear();
            }
            break;
            case R.id.btn_submit:
                validateRepairWorkRequestFinalCompletionEdit();
                break;
        }
    }

}