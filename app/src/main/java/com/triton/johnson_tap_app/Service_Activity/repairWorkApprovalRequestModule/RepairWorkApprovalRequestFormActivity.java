package com.triton.johnson_tap_app.Service_Activity.repairWorkApprovalRequestModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.repairWorkRequestModule.JobListRepairWorkRequestAdapter;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.JobIdRequest;
import com.triton.johnson_tap_app.requestpojo.RepairWorkMechBrCodeRequest;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestApprovalCreateRequest;
import com.triton.johnson_tap_app.responsepojo.JobListRepairWorkRequestResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkMechBrCodeResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkApprovalRequestFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static String formattedDate = "";
    List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList = new ArrayList<>();
    private Context context;
    private SharedPreferences sharedPreferences;
    private String TAG = RepairWorkApprovalRequestFormActivity.class.getSimpleName(), strDateType = "",
            se_user_mobile_no, se_user_name, se_id, check_id, service_title,
            message, paused_count, networkStatus = "";
    private ImageView img_back;
    private Spinner spinner_ext_by, spinner_tech_trained, spinner_mat_ava_site, spinner_repair_toolkit, spinner_first_aid_kit,
            spinner_full_body_harness, spinner_hard_hat, spinner_safety_shoes, spinner_hand_gloves, spinner_chain_block_status, spinner_chain_block_capacity,
            spinner_webbing_belt, spinner_ladder_required, spinner_ms_pipe_required, spinner_rebelling_clamp_required,
            spinner_dShackle_required, spinner_bar_maint_dis_req, spinner_repair_work_tech;
    private EditText edt_service_type, edt_mech_sub_id, edt_mech_sub_name, edt_zonal_eng_name,
            edt_zonal_eng_id, edt_assistant_name, edt_branch_office_no, edt_install_address, edt_nature_work_id,
            edt_nature_work_name, edt_work_process_id, edt_work_process_name, edt_other_process, edt_man_power, edt_mr_no;
    private TextView txt_branch_code, txt_branch_name, txt_route, txt_nature_work_id,
            txt_nature_work_name, txt_work_process_id, txt_work_process_name, txt_work_start_date,
            txt_work_expected_date, txt_comp_date, txt_jobid, txt_building_name;
    private Button btn_submit;
    private String[] yesNoNaArray, yesNoArray, executionArray, chainBlockStatusArray, chainBlockCapacityArray;
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private RepairWorkRequestApprovalCreateRequest repairWorkRequestApprovalCreateRequest = new RepairWorkRequestApprovalCreateRequest();
    private RepairWorkRequestMechResponse.Data repairWorkRequestMechDataResponse = new RepairWorkRequestMechResponse.Data();
    private ArrayList<RepairWorkMechBrCodeResponse.Data> repairWorkMechBrCodeResponse = new ArrayList<>();
    private ArrayList<String> repairWorkMechList = new ArrayList<>();
    private Dialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_approval_request_form);
        context = this;

        img_back = findViewById(R.id.img_back);

        txt_branch_code = findViewById(R.id.txt_branch_code);
        txt_branch_name = findViewById(R.id.txt_branch_name);
        txt_route = findViewById(R.id.txt_route);
        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_nature_work_id = findViewById(R.id.txt_nature_work_id);
        txt_nature_work_name = findViewById(R.id.txt_nature_work_name);
        txt_work_process_id = findViewById(R.id.txt_work_process_id);
        txt_work_process_name = findViewById(R.id.txt_work_process_name);
        txt_work_start_date = findViewById(R.id.txt_work_start_date);
        txt_work_expected_date = findViewById(R.id.txt_work_expected_date);
        txt_comp_date = findViewById(R.id.txt_comp_date);

        spinner_ext_by = findViewById(R.id.spinner_ext_by);
        spinner_tech_trained = findViewById(R.id.spinner_tech_trained);
        spinner_mat_ava_site = findViewById(R.id.spinner_mat_ava_site);
        spinner_repair_toolkit = findViewById(R.id.spinner_repair_toolkit);
        spinner_first_aid_kit = findViewById(R.id.spinner_first_aid_kit);
        spinner_full_body_harness = findViewById(R.id.spinner_full_body_harness);
        spinner_hard_hat = findViewById(R.id.spinner_hard_hat);
        spinner_safety_shoes = findViewById(R.id.spinner_safety_shoes);
        spinner_hand_gloves = findViewById(R.id.spinner_hand_gloves);
        spinner_chain_block_status = findViewById(R.id.spinner_chain_block_status);
        spinner_chain_block_capacity = findViewById(R.id.spinner_chain_block_capacity);
        spinner_webbing_belt = findViewById(R.id.spinner_webbing_belt);
        spinner_ladder_required = findViewById(R.id.spinner_ladder_required);
        spinner_ms_pipe_required = findViewById(R.id.spinner_ms_pipe_required);
        spinner_rebelling_clamp_required = findViewById(R.id.spinner_rebelling_clamp_required);
        spinner_dShackle_required = findViewById(R.id.spinner_dShackle_required);
        spinner_bar_maint_dis_req = findViewById(R.id.spinner_bar_maint_dis_req);
        spinner_repair_work_tech = findViewById(R.id.spinner_repair_work_tech);

        edt_service_type = findViewById(R.id.edt_service_type);
        edt_mech_sub_id = findViewById(R.id.edt_mech_sub_id);
        edt_mech_sub_name = findViewById(R.id.edt_mech_sub_name);
        edt_zonal_eng_name = findViewById(R.id.edt_zonal_eng_name);
        edt_zonal_eng_id = findViewById(R.id.edt_zonal_eng_id);
        edt_assistant_name = findViewById(R.id.edt_assistant_name);
        edt_branch_office_no = findViewById(R.id.edt_branch_office_no);
        edt_install_address = findViewById(R.id.edt_install_address);
        edt_nature_work_id = findViewById(R.id.edt_nature_work_id);
        edt_nature_work_name = findViewById(R.id.edt_nature_work_name);
        edt_work_process_id = findViewById(R.id.edt_work_process_id);
        edt_work_process_name = findViewById(R.id.edt_work_process_name);
        edt_other_process = findViewById(R.id.edt_other_process);
        edt_man_power = findViewById(R.id.edt_man_power);
        edt_mr_no = findViewById(R.id.edt_mr_no);

        btn_submit = findViewById(R.id.btn_submit);

        initLoadingDialog();

        yesNoNaArray = getResources().getStringArray(R.array.yes_no_na_array);
        yesNoArray = getResources().getStringArray(R.array.yes_no_array);
        executionArray = new String[]{"SELECT", "JLCPL", "SUBCON"};
        chainBlockStatusArray = new String[]{"SELECT", "CE - Certified", "NC - Not Certified", "NA - Not Applicable"};
        chainBlockCapacityArray = new String[]{"SELECT", "2 - 2 TON", "3 - 3 TON", "5 - 5 TON", "NA Not Applicable"};

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "onCreate: service_title -> " + service_title);
        Log.i(TAG, "onCreate: se_user_mobile_no -> " + se_user_mobile_no);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("repairWorkRequestMechDataResponse")) {
                repairWorkRequestMechDataResponse = extras.getParcelable("repairWorkRequestMechDataResponse");
            }
        }
        Log.i(TAG, "onCreate: repairWorkRequestMechDataResponse -> " + new Gson().toJson(repairWorkRequestMechDataResponse));

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            getRepairWorkMechBrCode(repairWorkRequestMechDataResponse.getBr_code());
        }

        repairWorkMechList.add("SELECT");

        ArrayAdapter<String> repairWorkMechAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, repairWorkMechList);
        repairWorkMechAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> yesNoNaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoNaArray);
        yesNoNaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoArray);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> executionAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, executionArray);
        executionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> chainBlockStatusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, chainBlockStatusArray);
        chainBlockStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> chainBlockCapacityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, chainBlockCapacityArray);
        chainBlockCapacityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_ext_by.setAdapter(executionAdapter);
        spinner_chain_block_status.setAdapter(chainBlockStatusAdapter);
        spinner_chain_block_capacity.setAdapter(chainBlockCapacityAdapter);
        spinner_tech_trained.setAdapter(yesNoAdapter);
        spinner_mat_ava_site.setAdapter(yesNoAdapter);
        spinner_repair_toolkit.setAdapter(yesNoAdapter);
        spinner_first_aid_kit.setAdapter(yesNoAdapter);
        spinner_full_body_harness.setAdapter(yesNoAdapter);
        spinner_hard_hat.setAdapter(yesNoAdapter);
        spinner_safety_shoes.setAdapter(yesNoAdapter);
        spinner_hand_gloves.setAdapter(yesNoAdapter);

        spinner_webbing_belt.setAdapter(yesNoNaAdapter);
        spinner_ladder_required.setAdapter(yesNoNaAdapter);
        spinner_ms_pipe_required.setAdapter(yesNoNaAdapter);
        spinner_rebelling_clamp_required.setAdapter(yesNoNaAdapter);
        spinner_dShackle_required.setAdapter(yesNoNaAdapter);
        spinner_bar_maint_dis_req.setAdapter(yesNoNaAdapter);

        spinner_repair_work_tech.setAdapter(repairWorkMechAdapter);

        spinner_ext_by.setOnItemSelectedListener(this);
        spinner_chain_block_status.setOnItemSelectedListener(this);
        spinner_chain_block_capacity.setOnItemSelectedListener(this);
        spinner_tech_trained.setOnItemSelectedListener(this);
        spinner_mat_ava_site.setOnItemSelectedListener(this);
        spinner_repair_toolkit.setOnItemSelectedListener(this);
        spinner_first_aid_kit.setOnItemSelectedListener(this);
        spinner_full_body_harness.setOnItemSelectedListener(this);
        spinner_hard_hat.setOnItemSelectedListener(this);
        spinner_safety_shoes.setOnItemSelectedListener(this);
        spinner_hand_gloves.setOnItemSelectedListener(this);
        spinner_webbing_belt.setOnItemSelectedListener(this);
        spinner_ladder_required.setOnItemSelectedListener(this);
        spinner_ms_pipe_required.setOnItemSelectedListener(this);
        spinner_rebelling_clamp_required.setOnItemSelectedListener(this);
        spinner_dShackle_required.setOnItemSelectedListener(this);
        spinner_bar_maint_dis_req.setOnItemSelectedListener(this);
        spinner_repair_work_tech.setOnItemSelectedListener(this);

        txt_work_start_date.setOnClickListener(this);
        txt_work_expected_date.setOnClickListener(this);
        txt_comp_date.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        txt_branch_code.setText(repairWorkRequestMechDataResponse.getBr_code());
        txt_route.setText(repairWorkRequestMechDataResponse.getRoute());
        txt_jobid.setText(repairWorkRequestMechDataResponse.getJob_id());
        txt_building_name.setText(repairWorkRequestMechDataResponse.getSite_name());

        repairWorkRequestApprovalCreateRequest.setBr_code(repairWorkRequestMechDataResponse.getBr_code());
        repairWorkRequestApprovalCreateRequest.setRoute_code(repairWorkRequestMechDataResponse.getRoute());
        repairWorkRequestApprovalCreateRequest.setJob_no(repairWorkRequestMechDataResponse.getJob_id());
        repairWorkRequestApprovalCreateRequest.setCustomer_name(repairWorkRequestMechDataResponse.getSite_name());

        repairWorkRequestApprovalCreateRequest.setSubmitted_by_emp_code(repairWorkRequestMechDataResponse.getSite_name());
        repairWorkRequestApprovalCreateRequest.setSubmitted_by_name(repairWorkRequestMechDataResponse.getSite_name());
        repairWorkRequestApprovalCreateRequest.setSubmitted_by_num(repairWorkRequestMechDataResponse.getSite_name());

        getTodayDate();
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
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

        if (strDateType.equalsIgnoreCase("txt_work_start_date")) {
            txt_work_start_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_work_expected_date")) {
            txt_work_expected_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_comp_date")) {
            txt_comp_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_work_start_date.setText(dateTime);
            txt_work_expected_date.setText(dateTime);
            txt_comp_date.setText(dateTime);
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
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/
            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

            if (strDateType.equalsIgnoreCase("txt_work_start_date")) {
                repairWorkRequestApprovalCreateRequest.setWork_start_date(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_work_expected_date")) {
                repairWorkRequestApprovalCreateRequest.setWork_expected_date(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_comp_date")) {
                repairWorkRequestApprovalCreateRequest.setCompleted_date(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                repairWorkRequestApprovalCreateRequest.setWork_start_date(formattedDate);
                repairWorkRequestApprovalCreateRequest.setWork_expected_date(formattedDate);
                repairWorkRequestApprovalCreateRequest.setCompleted_date(formattedDate);
                repairWorkRequestApprovalCreateRequest.setSubmitted_by_on(formattedDate);
            }

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
        }

        Log.i(TAG, "setDate: repairWorkRequestApprovalCreateRequest -> " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));
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

        txt_Message.setText(strMsg);

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

    private void setView(List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList) {
        edt_mech_sub_id.setText(jobListRepairWorkRequestResponseList.get(0).getMech_code());
        edt_mech_sub_name.setText(jobListRepairWorkRequestResponseList.get(0).getMech_name());
        edt_zonal_eng_id.setText(jobListRepairWorkRequestResponseList.get(0).getZone_code());
        edt_zonal_eng_name.setText(jobListRepairWorkRequestResponseList.get(0).getZone_name());
    }

    private RepairWorkMechBrCodeRequest brCodeRequest(String strSearch) {

        RepairWorkMechBrCodeRequest job = new RepairWorkMechBrCodeRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setBrcode(strSearch);
            Log.i(TAG, "jobListRequest: RepairWorkEngBrCodeRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private JobIdRequest jobListRequest(String strSearch) {

        JobIdRequest job = new JobIdRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setJob_id(strSearch);
            Log.i(TAG, "jobListRequest: JobIdRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void getRepairWorkMechBrCode(String strSearch) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkMechBrCodeRequest jobIdRequest = brCodeRequest(strSearch);

        if (jobIdRequest != null) {

            Call<RepairWorkMechBrCodeResponse> call = apiInterface.getRepairWorkMechBrCode(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getRepairWorkMechBrCode: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkMechBrCodeResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkMechBrCodeResponse> call, @NonNull Response<RepairWorkMechBrCodeResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getRepairWorkMechBrCode: onResponse: RepairWorkMechBrCodeResponse -> " + new Gson().toJson(response.body()));
                    repairWorkMechBrCodeResponse = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkMechBrCodeResponse = response.body().getData();

                                if (repairWorkMechBrCodeResponse != null
                                        && !repairWorkMechBrCodeResponse.isEmpty()) {
                                    for (RepairWorkMechBrCodeResponse.Data data : repairWorkMechBrCodeResponse) {
                                        repairWorkMechList.add(data.getUser_id() + " - " + data.getUser_name() + " - " + data.getUser_mobile_no());
                                    }
                                } else {
                                    ErrorMsgDialog("Repair Work Engineer List is Empty");
                                }
                            } else {
                                ErrorMsgDialog("");
                            }
                        } else {
                            ErrorMsgDialog(message);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepairWorkMechBrCodeResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getRepairWorkMechBrCode: onFailure: error --> " + t.getMessage());
                    ErrorMsgDialog(t.getMessage());
                }
            });
        }
    }

    private void getFetchDataJobId(String strSearch) {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        JobIdRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<JobListRepairWorkRequestResponse> call = apiInterface.getRepairWorkRequestFetchDataJobId(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<JobListRepairWorkRequestResponse>() {
                @Override
                public void onResponse(@NonNull Call<JobListRepairWorkRequestResponse> call, @NonNull Response<JobListRepairWorkRequestResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: JobListRopeMaintenanceResponse -> " + new Gson().toJson(response.body()));
                    jobListRepairWorkRequestResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                jobListRepairWorkRequestResponseList = response.body().getData();

                                if (!jobListRepairWorkRequestResponseList.isEmpty()) {
                                    setView(jobListRepairWorkRequestResponseList);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JobListRepairWorkRequestResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    private void getRepairWorkRequestApprovalCreate(RepairWorkRequestApprovalCreateRequest repairWorkRequestApprovalCreateRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getRepairWorkRequestApprovalCreate(RestUtils.getContentType(), repairWorkRequestApprovalCreateRequest);
        Log.i(TAG, "getRepairWorkRequestApprovalCreate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getRepairWorkRequestApprovalCreate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getRepairWorkRequestApprovalCreate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void validateRepairWorkRequestApprovalCreate(RepairWorkRequestApprovalCreateRequest repairWorkRequestApprovalCreateRequest) {
        Log.i(TAG, "validateRepairWorkRequestApprovalCreate: RepairWorkRequestApprovalCreateRequest(1) -> " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));

        repairWorkRequestApprovalCreateRequest.setService_type(nullPointer(edt_service_type.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setRepair_work_mech_id(nullPointer(edt_mech_sub_id.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setRepair_work_mech_name(nullPointer(edt_mech_sub_name.getText().toString().trim()));
//        repairWorkRequestApprovalCreateRequest.setRepair_work_mech_no();
        repairWorkRequestApprovalCreateRequest.setZonal_eng_id(nullPointer(edt_zonal_eng_id.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setZonal_eng_name(nullPointer(edt_zonal_eng_name.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setAssistant_name(nullPointer(edt_assistant_name.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setBranch_office_no(nullPointer(edt_branch_office_no.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setInstall_address(nullPointer(edt_install_address.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setNature_of_work_id(nullPointer(edt_nature_work_id.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setNature_of_work_name(nullPointer(edt_nature_work_name.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setNature_of_work_process_id(nullPointer(edt_work_process_id.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setNature_of_work_process_name(nullPointer(edt_work_process_name.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setOther_process(nullPointer(edt_other_process.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setMan_power(nullPointer(edt_man_power.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setMr_no(nullPointer(edt_mr_no.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setMr_no(nullPointer(edt_mr_no.getText().toString().trim()));

        getRepairWorkRequestApprovalCreate(repairWorkRequestApprovalCreateRequest);
        Log.i(TAG, "validateRepairWorkRequestApprovalCreate: RepairWorkRequestApprovalCreateRequest(2) -> " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        int selPos = i - 1;

        Log.i(TAG, "onItemSelected: selPos -> " + selPos);

        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_ext_by: {
                    repairWorkRequestApprovalCreateRequest.setExecution_by("");
                }
                break;
                case R.id.spinner_chain_block_status: {
                    repairWorkRequestApprovalCreateRequest.setChain_block_status("");
                }
                break;
                case R.id.spinner_chain_block_capacity: {
                    repairWorkRequestApprovalCreateRequest.setChain_block_capacity("");
                }
                break;
                case R.id.spinner_tech_trained: {
                    repairWorkRequestApprovalCreateRequest.setTech_trained("");
                }
                break;
                case R.id.spinner_mat_ava_site: {
                    repairWorkRequestApprovalCreateRequest.setMaterial_available_site("");
                }
                break;
                case R.id.spinner_repair_toolkit: {
                    repairWorkRequestApprovalCreateRequest.setRepair_toolkit("");
                }
                break;
                case R.id.spinner_first_aid_kit: {
                    repairWorkRequestApprovalCreateRequest.setFirst_aid_kit("");
                }
                break;
                case R.id.spinner_full_body_harness: {
                    repairWorkRequestApprovalCreateRequest.setFull_body_harness("");
                }
                break;
                case R.id.spinner_hard_hat: {
                    repairWorkRequestApprovalCreateRequest.setHard_hat("");
                }
                break;
                case R.id.spinner_safety_shoes: {
                    repairWorkRequestApprovalCreateRequest.setSafety_shoes("");
                }
                break;
                case R.id.spinner_hand_gloves: {
                    repairWorkRequestApprovalCreateRequest.setHand_gloves("");
                }
                break;
                case R.id.spinner_webbing_belt: {
                    repairWorkRequestApprovalCreateRequest.setWebbing_belt("");
                }
                break;
                case R.id.spinner_ladder_required: {
                    repairWorkRequestApprovalCreateRequest.setLadder_req("");
                }
                break;
                case R.id.spinner_ms_pipe_required: {
                    repairWorkRequestApprovalCreateRequest.setMs_pipe_req("");
                }
                break;
                case R.id.spinner_rebelling_clamp_required: {
                    repairWorkRequestApprovalCreateRequest.setRebelling_clamp("");
                }
                break;
                case R.id.spinner_dShackle_required: {
                    repairWorkRequestApprovalCreateRequest.setDshackle_req("");
                }
                break;
                case R.id.spinner_bar_maint_dis_req: {
                    repairWorkRequestApprovalCreateRequest.setBarricate_main_req("");
                }
                break;
                case R.id.spinner_repair_work_tech: {
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_id("");
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_name("");
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_no("");
                }
                break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_ext_by: {
                    repairWorkRequestApprovalCreateRequest.setExecution_by(item);
                }
                break;
                case R.id.spinner_chain_block_status: {
                    repairWorkRequestApprovalCreateRequest.setChain_block_status(item);
                }
                break;
                case R.id.spinner_chain_block_capacity: {
                    repairWorkRequestApprovalCreateRequest.setChain_block_capacity(item);
                }
                break;
                case R.id.spinner_tech_trained: {
                    repairWorkRequestApprovalCreateRequest.setTech_trained(item);
                }
                break;
                case R.id.spinner_mat_ava_site: {
                    repairWorkRequestApprovalCreateRequest.setMaterial_available_site(item);
                }
                break;
                case R.id.spinner_repair_toolkit: {
                    repairWorkRequestApprovalCreateRequest.setRepair_toolkit(item);
                }
                break;
                case R.id.spinner_first_aid_kit: {
                    repairWorkRequestApprovalCreateRequest.setFirst_aid_kit(item);
                }
                break;
                case R.id.spinner_full_body_harness: {
                    repairWorkRequestApprovalCreateRequest.setFull_body_harness(item);
                }
                break;
                case R.id.spinner_hard_hat: {
                    repairWorkRequestApprovalCreateRequest.setHard_hat(item);
                }
                break;
                case R.id.spinner_safety_shoes: {
                    repairWorkRequestApprovalCreateRequest.setSafety_shoes(item);
                }
                break;
                case R.id.spinner_hand_gloves: {
                    repairWorkRequestApprovalCreateRequest.setHand_gloves(item);
                }
                break;
                case R.id.spinner_webbing_belt: {
                    repairWorkRequestApprovalCreateRequest.setWebbing_belt(item);
                }
                break;
                case R.id.spinner_ladder_required: {
                    repairWorkRequestApprovalCreateRequest.setLadder_req(item);
                }
                break;
                case R.id.spinner_ms_pipe_required: {
                    repairWorkRequestApprovalCreateRequest.setMs_pipe_req(item);
                }
                break;
                case R.id.spinner_rebelling_clamp_required: {
                    repairWorkRequestApprovalCreateRequest.setRebelling_clamp(item);
                }
                break;
                case R.id.spinner_dShackle_required: {
                    repairWorkRequestApprovalCreateRequest.setDshackle_req(item);
                }
                break;
                case R.id.spinner_bar_maint_dis_req: {
                    repairWorkRequestApprovalCreateRequest.setBarricate_main_req(item);
                }
                break;
                case R.id.spinner_repair_work_tech: {
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_id(repairWorkRequestMechDataResponse.getRepair_work_eng_id());
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_name(repairWorkRequestMechDataResponse.getRepair_work_eng_name());
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_no(repairWorkRequestMechDataResponse.getRepair_work_eng_phone());
                }
                break;
            }
        }

        Log.i(TAG, "onItemSelected: repairWorkRequestApprovalCreateRequest -> " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.txt_work_start_date: {
                strDateType = "txt_work_start_date";
                callDatePicker();
            }
            break;
            case R.id.txt_work_expected_date: {
                strDateType = "txt_work_expected_date";
                callDatePicker();
            }
            break;
            case R.id.txt_comp_date: {
                strDateType = "txt_comp_date";
                callDatePicker();
            }
            break;
            case R.id.btn_submit: {
                validateRepairWorkRequestApprovalCreate(repairWorkRequestApprovalCreateRequest);
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
