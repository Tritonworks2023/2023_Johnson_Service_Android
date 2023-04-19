package com.triton.johnson_tap_app.Service_Activity.repairWorkApprovalModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.RepairWorkEngBrCodeRequest;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestEditEngRequest;
import com.triton.johnson_tap_app.responsepojo.RepairWorkEngBrCodeResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestEditEngResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestFetchListEngIdResponse;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkApprovalFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String formattedDate = "", formattedDate3 = "";
    private String TAG = RepairWorkApprovalFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", networkStatus = "", uploadImagePath = "",
            message = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private TextView txt_jobid, txt_building_name, txt_req_on_date, txt_route, txt_technician_id, txt_technician_name,
            txt_mech_id, txt_mech_name, txt_mat_ava_site;
    private EditText edt_nature_work;
    private Spinner spinner_mat_ava_site, spinner_repair_work_engineer;
    private Button btn_submit;
    private ImageView iv_back;
    private Context context;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private boolean checkDate = false;
    private RepairWorkRequestEditEngRequest repairWorkRequestEditEngRequest = new RepairWorkRequestEditEngRequest();
    private RepairWorkRequestFetchListEngIdResponse.Data repairWorkRequestFetchListEngIdDataResponse = new RepairWorkRequestFetchListEngIdResponse.Data();
    private ArrayList<RepairWorkEngBrCodeResponse.Data> repairWorkEngBrCodeResponseList = new ArrayList<>();
    private ArrayList<String> repairWorkEngineerList = new ArrayList<>();
    private String[] yesNoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_approval_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("repairWorkRequestFetchListEngIdDataResponse")) {
                repairWorkRequestFetchListEngIdDataResponse = extra.getParcelable("repairWorkRequestFetchListEngIdDataResponse");
            }
        }

        Log.i(TAG, "onCreate: repairWorkRequestFetchListEngIdDataResponse -> " + new Gson().toJson(repairWorkRequestFetchListEngIdDataResponse));

        iv_back = findViewById(R.id.iv_back);

        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_req_on_date = findViewById(R.id.txt_req_on_date);
        txt_route = findViewById(R.id.txt_route);
        txt_technician_id = findViewById(R.id.txt_technician_id);
        txt_technician_name = findViewById(R.id.txt_technician_name);
        txt_mech_id = findViewById(R.id.txt_mech_id);
        txt_mech_name = findViewById(R.id.txt_mech_name);
        txt_mat_ava_site = findViewById(R.id.txt_mat_ava_site);

        edt_nature_work = findViewById(R.id.edt_nature_work);

        spinner_mat_ava_site = findViewById(R.id.spinner_mat_ava_site);
        spinner_repair_work_engineer = findViewById(R.id.spinner_repair_work_engineer);

        btn_submit = findViewById(R.id.btn_submit);

        iv_back.setOnClickListener(this);
//        txt_req_on_date.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        spinner_mat_ava_site.setOnItemSelectedListener(this);
        spinner_repair_work_engineer.setOnItemSelectedListener(this);

        yesNoArray = getResources().getStringArray(R.array.yes_no_array);

        ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoArray);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_mat_ava_site.setAdapter(yesNoAdapter);
        spinner_repair_work_engineer.setAdapter(yesNoAdapter);

        txt_jobid.setText(repairWorkRequestFetchListEngIdDataResponse.getJob_id());
        txt_building_name.setText(repairWorkRequestFetchListEngIdDataResponse.getSite_name());
        txt_route.setText(repairWorkRequestFetchListEngIdDataResponse.getRoute());
        txt_mat_ava_site.setText(repairWorkRequestFetchListEngIdDataResponse.getMat_available_sts());
        txt_technician_id.setText(repairWorkRequestFetchListEngIdDataResponse.getTech_code());
        txt_technician_name.setText(repairWorkRequestFetchListEngIdDataResponse.getTech_name());
        txt_mech_id.setText(repairWorkRequestFetchListEngIdDataResponse.getSubmitted_by_emp_code());
        txt_mech_name.setText(repairWorkRequestFetchListEngIdDataResponse.getSubmitted_by_name());
        edt_nature_work.setText(repairWorkRequestFetchListEngIdDataResponse.getRemarks());
        setDateForDate(repairWorkRequestFetchListEngIdDataResponse.getRequest_on());

        repairWorkRequestEditEngRequest.set_id(repairWorkRequestFetchListEngIdDataResponse.get_id());
        repairWorkRequestEditEngRequest.setStatus("APPROVED");

        repairWorkEngineerList.add("SELECT");

        ArrayAdapter<String> repairWorkEngineerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, repairWorkEngineerList);
        repairWorkEngineerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < yesNoArray.length; i++) {
            if (yesNoArray[i].equalsIgnoreCase(repairWorkRequestFetchListEngIdDataResponse.getMat_available_sts()))
                spinner_mat_ava_site.setSelection(i);
        }

        spinner_repair_work_engineer.setAdapter(repairWorkEngineerAdapter);

        initLoadingDialog();
        getTodayDate();

        getListRepairWorkEngBrCode(se_user_location);
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
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

        /*if (strDateType.equalsIgnoreCase("txt_req_on_date")) {
            txt_req_on_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_req_on_date.setText(dateTime);
        }*/
        txt_req_on_date.setText(dateTime);

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
            if (strDateType.equalsIgnoreCase("txt_req_on_date")) {
//                repairWorkRequestCreateTechRequest.setRequest_on(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
//                repairWorkRequestCreateTechRequest.setRequest_on(formattedDate);
//                repairWorkRequestCreateTechRequest.setSubmitted_by_on(formattedDate);
                repairWorkRequestEditEngRequest.setRepair_work_eng_date(formattedDate);
            }

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
        }
    }

    private void setDateForDate(String inputDateTime) {

        String inputPattern = "dd-MMM-yyyy";
//        String outputPattern = "dd-MM-yyyy hh:mm a";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDateTime);
            str = outputFormat.format(date);
            formattedDate3 = str;/*.replace("AM", "am").replace("PM", "pm")*/

            txt_req_on_date.setText(formattedDate3);
//            failureReportEditEngRequest.setFailure_date(inputDateTime);

            Log.i(TAG, "setDate: formattedDate3-> " + formattedDate3);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
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

    private RepairWorkEngBrCodeRequest jobListRequest(String strSearch) {

        RepairWorkEngBrCodeRequest job = new RepairWorkEngBrCodeRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setBrcode(strSearch);
            Log.i(TAG, "jobListRequest: RepairWorkEngBrCodeRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void validateRepairWorkRequestEditEng() {

        Log.i(TAG, "validateRepairWorkRequestEditEng: repairWorkRequestEditEngRequest -> " + new Gson().toJson(repairWorkRequestEditEngRequest));

        repairWorkRequestEditEngRequest.setRemarks(edt_nature_work.getText().toString().trim());

        if (!nullPointerValidator(repairWorkRequestEditEngRequest.get_id())) {
            ErrorMsgDialog("Please Select ID");
        } else if (!nullPointerValidator(repairWorkRequestEditEngRequest.getRemarks())) {
            ErrorMsgDialog("Please Enter Remarks");
        } else if (!nullPointerValidator(repairWorkRequestEditEngRequest.getRepair_work_eng_id())) {
            ErrorMsgDialog("Please Select Repair Work Engineer");
        } else if (!nullPointerValidator(repairWorkRequestEditEngRequest.getRepair_work_eng_name())) {
            ErrorMsgDialog("Please Select Repair Work Engineer");
        } else if (!nullPointerValidator(repairWorkRequestEditEngRequest.getRepair_work_eng_phone())) {
            ErrorMsgDialog("Please Select Repair Work Engineer");
        } else if (!nullPointerValidator(repairWorkRequestEditEngRequest.getStatus())) {
            ErrorMsgDialog("Please Select Status");
        } else {
            getRepairWorkRequestEditEng(repairWorkRequestEditEngRequest);
        }
    }

    private void getListRepairWorkEngBrCode(String strSearch) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkEngBrCodeRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<RepairWorkEngBrCodeResponse> call = apiInterface.getListRepairWorkEngBrCode(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getListRepairWorkEngBrCode: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkEngBrCodeResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkEngBrCodeResponse> call, @NonNull Response<RepairWorkEngBrCodeResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getListRepairWorkEngBrCode: onResponse: RepairWorkEngBrCodeResponse -> " + new Gson().toJson(response.body()));
                    repairWorkEngBrCodeResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkEngBrCodeResponseList = response.body().getData();

                                if (repairWorkEngBrCodeResponseList != null
                                        && !repairWorkEngBrCodeResponseList.isEmpty()) {
                                    for (RepairWorkEngBrCodeResponse.Data data : repairWorkEngBrCodeResponseList) {
                                        repairWorkEngineerList.add(data.getUser_id() + " - " + data.getUser_name() + " - " + data.getUser_mobile_no());
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
                public void onFailure(@NonNull Call<RepairWorkEngBrCodeResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getListRepairWorkEngBrCode: onFailure: error --> " + t.getMessage());
                    ErrorMsgDialog(t.getMessage());
                }
            });
        }

    }

    private void getRepairWorkRequestEditEng(RepairWorkRequestEditEngRequest repairWorkRequestEditEngRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<RepairWorkRequestEditEngResponse> call = apiInterface.getRepairWorkRequestEditEng(RestUtils.getContentType(), repairWorkRequestEditEngRequest);
        Log.i(TAG, "getRepairWorkRequestEditEng: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<RepairWorkRequestEditEngResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepairWorkRequestEditEngResponse> call, @NonNull Response<RepairWorkRequestEditEngResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getRepairWorkRequestEditEng: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
            public void onFailure(@NonNull Call<RepairWorkRequestEditEngResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getRepairWorkRequestEditEng: onFailure: error --> " + t.getMessage());
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
                case R.id.spinner_mat_ava_site:
//                    repairWorkRequestCreateTechRequest.setMat_available_sts("");
                    Log.i(TAG, "onItemSelected: repairWorkRequestEditEngRequest -> " + new Gson().toJson(repairWorkRequestEditEngRequest));
                    break;
                case R.id.spinner_repair_work_engineer:
                    repairWorkRequestEditEngRequest.setRepair_work_eng_id("");
                    repairWorkRequestEditEngRequest.setRepair_work_eng_name("");
                    repairWorkRequestEditEngRequest.setRepair_work_eng_phone("");
                    Log.i(TAG, "onItemSelected: repairWorkRequestEditEngRequest -> " + new Gson().toJson(repairWorkRequestEditEngRequest));
                    break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_mat_ava_site:
//                    repairWorkRequestCreateTechRequest.setMat_available_sts(item);
                    Log.i(TAG, "onItemSelected: repairWorkRequestEditEngRequest -> " + new Gson().toJson(repairWorkRequestEditEngRequest));
                    break;
                case R.id.spinner_repair_work_engineer:
                    repairWorkRequestEditEngRequest.setRepair_work_eng_id(repairWorkEngBrCodeResponseList.get(selPos).getUser_id());
                    repairWorkRequestEditEngRequest.setRepair_work_eng_name(repairWorkEngBrCodeResponseList.get(selPos).getUser_name());
                    repairWorkRequestEditEngRequest.setRepair_work_eng_phone(repairWorkEngBrCodeResponseList.get(selPos).getUser_mobile_no());
                    Log.i(TAG, "onItemSelected: repairWorkRequestEditEngRequest -> " + new Gson().toJson(repairWorkRequestEditEngRequest));
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
            case R.id.iv_back: {
                onBackPressed();
            }
            break;
            case R.id.txt_req_on_date: {
                strDateType = "txt_req_on_date";
                callDatePicker();
            }
            break;
            case R.id.btn_submit: {
                validateRepairWorkRequestEditEng();
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