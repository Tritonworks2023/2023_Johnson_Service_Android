package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

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

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickRepairWorkRequestMachIdListResponseListener;
import com.triton.johnson_tap_app.interfaces.OnItemClickRepairWorkRequestNatureOfWorkResponseListener;
import com.triton.johnson_tap_app.requestpojo.JobIdRequest;
import com.triton.johnson_tap_app.requestpojo.RepairWorkMechBrCodeRequest;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestApprovalCreateRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportDropDownDataResponse;
import com.triton.johnson_tap_app.responsepojo.JobListRepairWorkRequestResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkMechBrCodeResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechIdListResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestNatureOfWorkResponse;
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

public class RepairWorkApprovalRequestFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnItemClickRepairWorkRequestMachIdListResponseListener, OnItemClickRepairWorkRequestNatureOfWorkResponseListener {

    private static String formattedDate = "";
    private Context context;
    private SharedPreferences sharedPreferences;
    private String TAG = RepairWorkApprovalRequestFormActivity.class.getSimpleName(), strDateType = "",
            se_user_mobile_no = "", se_user_name = "", se_id = "", se_user_id = "", check_id = "", service_title = "",
            message = "", paused_count = "", networkStatus = "", brCode = "", conType = "";
    private ImageView img_back, img_search_mech_sub_id_name, img_search_nat_work_pro_id_name;
    private Spinner spinner_ext_by, spinner_tech_trained, spinner_mat_ava_site, spinner_repair_toolkit, spinner_first_aid_kit,
            spinner_full_body_harness, spinner_hard_hat, spinner_safety_shoes, spinner_hand_gloves, spinner_chain_block_status, spinner_chain_block_capacity,
            spinner_webbing_belt, spinner_ladder_required, spinner_ms_pipe_required, spinner_rebelling_clamp_required,
            spinner_dShackle_required, spinner_bar_maint_dis_req, spinner_repair_work_tech;
    private EditText edt_assistant_name, /*edt_branch_office_no, edt_nature_work_id, edt_nature_work_name,
            edt_work_process_id, edt_work_process_name,*/
            edt_other_process, edt_man_power, edt_mr_no;
    private TextView txt_branch_code, txt_branch_name, txt_route, txt_mech_sub_id_name, txt_nat_work_id_name, txt_work_pro_id_name, txt_zonal_eng_name,
    /*txt_nature_work_id, txt_nature_work_name, txt_work_process_id, txt_work_process_name,*/ txt_work_start_date, txt_zonal_eng_id,
            txt_work_expected_date, /*txt_comp_date,*/
            txt_jobid, txt_building_name, txt_install_address, txt_service_type;
    private Button btn_submit;
    private String[] yesNoNaArray, yesNoArray, executionArray, chainBlockStatusArray, chainBlockCapacityArray;
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private RepairWorkRequestApprovalCreateRequest repairWorkRequestApprovalCreateRequest = new RepairWorkRequestApprovalCreateRequest();
    private RepairWorkRequestMechResponse.Data repairWorkRequestMechDataResponse = new RepairWorkRequestMechResponse.Data();
    private ArrayList<RepairWorkMechBrCodeResponse.Data> repairWorkMechBrCodeResponse = new ArrayList<>();
    private ArrayList<String> repairWorkMechList = new ArrayList<>(), serviceTypeList = new ArrayList<>();
    private List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList = new ArrayList<>();
    private Dialog dialog;
    private FailureReportDropDownDataResponse failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_approval_request_form);
        context = this;

        img_back = findViewById(R.id.img_back);
        img_search_mech_sub_id_name = findViewById(R.id.img_search_mech_sub_id_name);
        img_search_nat_work_pro_id_name = findViewById(R.id.img_search_nat_work_pro_id_name);

        txt_branch_code = findViewById(R.id.txt_branch_code);
        txt_branch_name = findViewById(R.id.txt_branch_name);
        txt_route = findViewById(R.id.txt_route);
        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_mech_sub_id_name = findViewById(R.id.txt_mech_sub_id_name);
        txt_nat_work_id_name = findViewById(R.id.txt_nat_work_id_name);
        txt_work_pro_id_name = findViewById(R.id.txt_work_pro_id_name);
        /*txt_nature_work_id = findViewById(R.id.txt_nature_work_id);
        txt_nature_work_name = findViewById(R.id.txt_nature_work_name);
        txt_work_process_id = findViewById(R.id.txt_work_process_id);
        txt_work_process_name = findViewById(R.id.txt_work_process_name);*/
        txt_work_start_date = findViewById(R.id.txt_work_start_date);
        txt_work_expected_date = findViewById(R.id.txt_work_expected_date);
        /*txt_comp_date = findViewById(R.id.txt_comp_date);*/

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

        txt_service_type = findViewById(R.id.txt_service_type);
        txt_zonal_eng_name = findViewById(R.id.txt_zonal_eng_name);
        txt_zonal_eng_id = findViewById(R.id.txt_zonal_eng_id);
        edt_assistant_name = findViewById(R.id.edt_assistant_name);
        txt_install_address = findViewById(R.id.txt_install_address);
        /*edt_branch_office_no = findViewById(R.id.edt_branch_office_no);
        edt_nature_work_id = findViewById(R.id.edt_nature_work_id);
        edt_nature_work_name = findViewById(R.id.edt_nature_work_name);
        edt_work_process_id = findViewById(R.id.edt_work_process_id);
        edt_work_process_name = findViewById(R.id.edt_work_process_name);*/
        edt_other_process = findViewById(R.id.edt_other_process);
        edt_man_power = findViewById(R.id.edt_man_power);
        edt_mr_no = findViewById(R.id.edt_mr_no);

        btn_submit = findViewById(R.id.btn_submit);

        initLoadingDialog();

        yesNoNaArray = getResources().getStringArray(R.array.yes_no_na_array);
        yesNoArray = getResources().getStringArray(R.array.yes_no_array);
        executionArray = new String[]{"SELECT", "JLPL", "SUBCON"};
        chainBlockStatusArray = new String[]{"SELECT", "CE - Certified", "NC - Not Certified", "NA - Not Applicable"};
        chainBlockCapacityArray = new String[]{"SELECT", "2 - 2 TON", "3 - 3 TON", "5 - 5 TON", "NA Not Applicable"};

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_id = sharedPreferences.getString("user_id", "default value");
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
            getFetchDataJobId(repairWorkRequestMechDataResponse.getJob_id());
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
        /*txt_comp_date.setOnClickListener(this);*/
        img_back.setOnClickListener(this);
        img_search_mech_sub_id_name.setOnClickListener(this);
        img_search_nat_work_pro_id_name.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        txt_branch_code.setText(repairWorkRequestMechDataResponse.getBr_code());
        txt_route.setText(repairWorkRequestMechDataResponse.getRoute());
        txt_jobid.setText(repairWorkRequestMechDataResponse.getJob_id());
        txt_building_name.setText(repairWorkRequestMechDataResponse.getSite_name());

        brCode = repairWorkRequestMechDataResponse.getBr_code();

        repairWorkRequestApprovalCreateRequest.setPref_id(repairWorkRequestMechDataResponse.get_id());
//        repairWorkRequestApprovalCreateRequest.setStatus("REQUESTED");
        repairWorkRequestApprovalCreateRequest.setStatus("APPROVED SUBMITTED TO TECH");
        repairWorkRequestApprovalCreateRequest.setBr_code(repairWorkRequestMechDataResponse.getBr_code());
        repairWorkRequestApprovalCreateRequest.setRoute_code(repairWorkRequestMechDataResponse.getRoute());
        repairWorkRequestApprovalCreateRequest.setJob_no(repairWorkRequestMechDataResponse.getJob_id());
        repairWorkRequestApprovalCreateRequest.setCustomer_name(repairWorkRequestMechDataResponse.getSite_name());

        repairWorkRequestApprovalCreateRequest.setSubmitted_by_emp_code(se_user_id);
        repairWorkRequestApprovalCreateRequest.setSubmitted_by_name(se_user_name);
        repairWorkRequestApprovalCreateRequest.setSubmitted_by_num(se_user_mobile_no);

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

    private void callFutureDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        setDate(dayOfMonth, monthOfYear, year1), year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

        if (strDateType.equalsIgnoreCase("txt_work_start_date")) {
            txt_work_start_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_work_expected_date")) {
            txt_work_expected_date.setText(dateTime);
        } else /*if (strDateType.equalsIgnoreCase("txt_comp_date")) {
            txt_comp_date.setText(dateTime);
        } else*/ if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_work_start_date.setText(dateTime);
            txt_work_expected_date.setText(dateTime);
//            txt_comp_date.setText(dateTime);
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
            } else /*if (strDateType.equalsIgnoreCase("txt_comp_date")) {
                repairWorkRequestApprovalCreateRequest.setCompleted_date(formattedDate);
            } else*/ if (strDateType.equalsIgnoreCase("txt_both")) {
                repairWorkRequestApprovalCreateRequest.setWork_start_date(formattedDate);
                repairWorkRequestApprovalCreateRequest.setWork_expected_date(formattedDate);
                /*repairWorkRequestApprovalCreateRequest.setCompleted_date(formattedDate);*/
                repairWorkRequestApprovalCreateRequest.setRequest_on(formattedDate);
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

        txt_service_type.setText(jobListRepairWorkRequestResponseList.get(0).getSERTYPE());
        txt_zonal_eng_id.setText(jobListRepairWorkRequestResponseList.get(0).getZone_code());
        txt_zonal_eng_name.setText(jobListRepairWorkRequestResponseList.get(0).getZone_name());
        txt_install_address.setText(String.format("%s, %s, %s, %s, %s",
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getINST_ADD()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getINST_ADD1()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getINST_ADD3()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getLANDMARK()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getPINCODE())));

        repairWorkRequestApprovalCreateRequest.setService_type(jobListRepairWorkRequestResponseList.get(0).getSERTYPE());
        repairWorkRequestApprovalCreateRequest.setZonal_eng_id(jobListRepairWorkRequestResponseList.get(0).getZone_code());
        repairWorkRequestApprovalCreateRequest.setZonal_eng_name(jobListRepairWorkRequestResponseList.get(0).getZone_name());
        repairWorkRequestApprovalCreateRequest.setInstall_address(String.format("%s, %s, %s, %s, %s",
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getINST_ADD()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getINST_ADD1()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getINST_ADD3()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getLANDMARK()),
                nullPointer(jobListRepairWorkRequestResponseList.get(0).getPINCODE())));

        /*repairWorkRequestApprovalCreateRequest.setRepair_work_mech_id(jobListRepairWorkRequestResponseList.get(0).getMech_code());
        repairWorkRequestApprovalCreateRequest.setRepair_work_mech_name(jobListRepairWorkRequestResponseList.get(0).getMech_name());*/
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
        if (!dialog.isShowing()) {
            dialog.show();
        }

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
//                                    getFailureReportDropDownData();
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

    private void getFailureReportDropDownData() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<FailureReportDropDownDataResponse> call = apiInterface.getFailureReportDropDownData(getContentType());
        Log.i(TAG, "getFailureReportDropDownData: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FailureReportDropDownDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<FailureReportDropDownDataResponse> call, @NonNull Response<FailureReportDropDownDataResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getFailureReportDropDownData: onResponse: FailureReportDropDownDataResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();

                    failureReportDropDownDataResponse = response.body();
                    if (response.body().getCode() == 200) {

                        if (failureReportDropDownDataResponse.getData() != null) {

                            if (failureReportDropDownDataResponse.getData().getServ_type() != null
                                    && !failureReportDropDownDataResponse.getData().getServ_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Serv_type servType : failureReportDropDownDataResponse.getData().getServ_type()) {
                                    serviceTypeList.add(servType.getDisplay_name());
                                }

                                if (!serviceTypeList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getServ_type().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getServ_type().get(i).getValue().equalsIgnoreCase(jobListRepairWorkRequestResponseList.get(0).getSERV_TYPE())) {
                                            txt_service_type.setText(failureReportDropDownDataResponse.getData().getServ_type().get(i).getDisplay_name());
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FailureReportDropDownDataResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getFailureReportDropDownData: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
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
        Log.i(TAG, "validateRepairWorkRequestApprovalCreate: RepairWorkRequestApprovalCreateRequest (1) -> " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));

        repairWorkRequestApprovalCreateRequest.setAssistant_name(nullPointer(edt_assistant_name.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setOther_process(nullPointer(edt_other_process.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setMan_power(nullPointer(edt_man_power.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setMr_no(nullPointer(edt_mr_no.getText().toString().trim()));
        repairWorkRequestApprovalCreateRequest.setMr_no(nullPointer(edt_mr_no.getText().toString().trim()));

        if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getJob_no())) {
            ErrorMsgDialog("Please Select Job ID.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getCustomer_name())) {
            ErrorMsgDialog("Please Select Building Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getBr_code())) {
            ErrorMsgDialog("Please Select Branch Code.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRoute_code())) {
            ErrorMsgDialog("Please Select Route Code.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getService_type())) {
            ErrorMsgDialog("Please Select Service Type.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getExecution_by())) {
            ErrorMsgDialog("Please Select Execution By.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getMech_id())
                || !nullPointerValidator(repairWorkRequestApprovalCreateRequest.getMech_name())) {
            ErrorMsgDialog("Please Select Mech/Subco ID & Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getZonal_eng_id())) {
            ErrorMsgDialog("Please Select Zonal Eng ID.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getZonal_eng_name())) {
            ErrorMsgDialog("Please Select Zonal Eng Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getAssistant_name())) {
            ErrorMsgDialog("Please Enter Assistant Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getNature_of_work_id())
                || !nullPointerValidator(repairWorkRequestApprovalCreateRequest.getNature_of_work_name())) {
            ErrorMsgDialog("Please Select Nature Of Work ID and Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getNature_of_work_process_id())
                || !nullPointerValidator(repairWorkRequestApprovalCreateRequest.getNature_of_work_process_name())) {
            ErrorMsgDialog("Please Select Work Progress ID and Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getOther_process())) {
            ErrorMsgDialog("Please Enter Other Process.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getWork_start_date())) {
            ErrorMsgDialog("Please Select Work Start Date.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getWork_expected_date())) {
            ErrorMsgDialog("Please Select Work Expected Date.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getTech_trained())) {
            ErrorMsgDialog("Please Select Tech Trained.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getMan_power())) {
            ErrorMsgDialog("Please Enter Man Power.");
        } else /*if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getMr_no())) {
            ErrorMsgDialog("Please Enter MR No.");
        } else*/ if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getMaterial_available_site())) {
            ErrorMsgDialog("Please Select Material Available at Site.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRepair_toolkit())) {
            ErrorMsgDialog("Please Select Repair Tool Kit.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getFirst_aid_kit())) {
            ErrorMsgDialog("Please Select First Aid Kit.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getFull_body_harness())) {
            ErrorMsgDialog("Please Select Full Body Harness.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getHard_hat())) {
            ErrorMsgDialog("Please Select Hard Hat.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getSafety_shoes())) {
            ErrorMsgDialog("Please Select Safety Shoes.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getHand_gloves())) {
            ErrorMsgDialog("Please Select Hand Gloves.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getChain_block_status())) {
            ErrorMsgDialog("Please Select Chain Block Status.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getChain_block_capacity())) {
            ErrorMsgDialog("Please Select Chain Block Capacity.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getWebbing_belt())) {
            ErrorMsgDialog("Please Select Webbing Belt.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getLadder_req())) {
            ErrorMsgDialog("Please Select Ladder Required.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getMs_pipe_req())) {
            ErrorMsgDialog("Please Select MS Pipe Required.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRebelling_clamp())) {
            ErrorMsgDialog("Please Select Rebelling Clamp Required.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getDshackle_req())) {
            ErrorMsgDialog("Please Select Dshackle Required.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getBarricate_main_req())) {
            ErrorMsgDialog("Please Select Barricade & Maintenance Display Required.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRepair_work_mech_id())
                || !nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRepair_work_mech_no())
                || !nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRepair_work_mech_name())) {
            ErrorMsgDialog("Please Select Repair Work Technician.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getSubmitted_by_emp_code())) {
            ErrorMsgDialog("Please Select Submitted By Emp Code.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getSubmitted_by_name())) {
            ErrorMsgDialog("Please Select Submitted By Name.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Select Submitted By Number.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Select Submitted By On.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getInstall_address())) {
            ErrorMsgDialog("Please Select Installed Address.");
        } else if (!nullPointerValidator(repairWorkRequestApprovalCreateRequest.getRequest_on())) {
            ErrorMsgDialog("Please Select Request On.");
        } else {
            getRepairWorkRequestApprovalCreate(repairWorkRequestApprovalCreateRequest);
        }

        Log.i(TAG, "validateRepairWorkRequestApprovalCreate: RepairWorkRequestApprovalCreateRequest (2) -> " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));
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
                    conType = "";
                    txt_mech_sub_id_name.setText("");
                    repairWorkRequestApprovalCreateRequest.setMech_id("");
                    repairWorkRequestApprovalCreateRequest.setMech_name("");
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
                    conType = item;
                    txt_mech_sub_id_name.setText("");
                    repairWorkRequestApprovalCreateRequest.setMech_id("");
                    repairWorkRequestApprovalCreateRequest.setMech_name("");
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
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_id(repairWorkMechBrCodeResponse.get(selPos).getUser_id());
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_name(repairWorkMechBrCodeResponse.get(selPos).getUser_name());
                    repairWorkRequestApprovalCreateRequest.setRepair_work_mech_no(repairWorkMechBrCodeResponse.get(selPos).getUser_mobile_no());
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
            case R.id.img_search_mech_sub_id_name: {
                if (nullPointerValidator(brCode) && nullPointerValidator(conType)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("MechSubConListFragment")
                            .add(android.R.id.content, new MechSubConListFragment(repairWorkRequestMechDataResponse.getBr_code(), conType, this), "MechSubConListFragment")
                            .commit();
                } else {
                    ErrorMsgDialog("Please Select Execution By");
                }
            }
            break;
            case R.id.img_search_nat_work_pro_id_name: {
                if (nullPointerValidator(brCode) && nullPointerValidator(conType)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("NatureOfWorkProgressListFragment")
                            .add(android.R.id.content, new NatureOfWorkProgressListFragment(this), "NatureOfWorkProgressListFragment")
                            .commit();
                } else {
                    ErrorMsgDialog("Please Select Execution By");
                }
            }
            break;
            case R.id.txt_work_start_date: {
                strDateType = "txt_work_start_date";
                callDatePicker();
            }
            break;
            case R.id.txt_work_expected_date: {
                strDateType = "txt_work_expected_date";
                callFutureDatePicker();
            }
            break;
            /*case R.id.txt_comp_date: {
                strDateType = "txt_comp_date";
                callDatePicker();
            }
            break;*/
            case R.id.btn_submit: {
                validateRepairWorkRequestApprovalCreate(repairWorkRequestApprovalCreateRequest);
            }
            break;
        }
    }

    @Override
    public void itemClickRepairWorkRequestMachIdListResponseListener(RepairWorkRequestMechIdListResponse.Data repairWorkRequestMechIdListDataResponse) {

        txt_mech_sub_id_name.setText(repairWorkRequestMechIdListDataResponse.getEMPNAME());

        repairWorkRequestApprovalCreateRequest.setMech_id(repairWorkRequestMechIdListDataResponse.getEMPCODE());
        repairWorkRequestApprovalCreateRequest.setMech_name(repairWorkRequestMechIdListDataResponse.getEMPNAME());

        Log.i(TAG, "itemClickRepairWorkRequestMachIdListResponseListener: repairWorkRequestApprovalCreateRequest - " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));

        onBackPressed();
    }

    @Override
    public void itemClickRepairWorkRequestNatureOfWorkResponseListener(RepairWorkRequestNatureOfWorkResponse.Data repairWorkRequestNatureOfWorkDataResponse) {

        txt_nat_work_id_name.setText(repairWorkRequestNatureOfWorkDataResponse.getP_ID() + " - " + repairWorkRequestNatureOfWorkDataResponse.getP_NAME());
        txt_work_pro_id_name.setText(repairWorkRequestNatureOfWorkDataResponse.getR_ID() + " - " + repairWorkRequestNatureOfWorkDataResponse.getR_NAME());

        repairWorkRequestApprovalCreateRequest.setNature_of_work_id(repairWorkRequestNatureOfWorkDataResponse.getP_ID());
        repairWorkRequestApprovalCreateRequest.setNature_of_work_name(repairWorkRequestNatureOfWorkDataResponse.getP_NAME());
        repairWorkRequestApprovalCreateRequest.setNature_of_work_process_id(repairWorkRequestNatureOfWorkDataResponse.getR_ID());
        repairWorkRequestApprovalCreateRequest.setNature_of_work_process_name(repairWorkRequestNatureOfWorkDataResponse.getR_NAME());

        Log.i(TAG, "itemClickRepairWorkRequestMachIdListResponseListener: repairWorkRequestApprovalCreateRequest - " + new Gson().toJson(repairWorkRequestApprovalCreateRequest));

        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
        /*super.onBackPressed();
        finish();*/
    }
}
