package com.triton.johnson_tap_app.Service_Activity.repairWorkRequestModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
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
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestCreateTechRequest;
import com.triton.johnson_tap_app.responsepojo.JobListRepairWorkRequestResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkRequestFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String formattedDate = "";
    private String TAG = RepairWorkRequestFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", networkStatus = "", uploadImagePath = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private TextView txt_jobid, txt_building_name, txt_req_on_date, txt_route, txt_technician_id, txt_technician_name, txt_mech_id, txt_mech_name;
    private EditText edt_nature_work;
    private Spinner spinner_mat_ava_site;
    private Button btn_submit;
    private ImageView iv_back;
    private Context context;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private boolean checkDate = false;
    private JobListRepairWorkRequestResponse.Data jobListRepairWorkRequestResponse = new JobListRepairWorkRequestResponse.Data();
    private RepairWorkRequestCreateTechRequest repairWorkRequestCreateTechRequest = new RepairWorkRequestCreateTechRequest();
    private String[] yesNoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_request_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("jobListRepairWorkRequestResponse")) {
                jobListRepairWorkRequestResponse = extra.getParcelable("jobListRepairWorkRequestResponse");
            }
        }

        Log.i(TAG, "onCreate: jobListRepairWorkRequestResponse -> " + new Gson().toJson(jobListRepairWorkRequestResponse));

        iv_back = findViewById(R.id.iv_back);

        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_req_on_date = findViewById(R.id.txt_req_on_date);
        txt_route = findViewById(R.id.txt_route);
        txt_technician_id = findViewById(R.id.txt_technician_id);
        txt_technician_name = findViewById(R.id.txt_technician_name);
        txt_mech_id = findViewById(R.id.txt_mech_id);
        txt_mech_name = findViewById(R.id.txt_mech_name);

        edt_nature_work = findViewById(R.id.edt_nature_work);

        spinner_mat_ava_site = findViewById(R.id.spinner_mat_ava_site);

        btn_submit = findViewById(R.id.btn_submit);

        iv_back.setOnClickListener(this);
        txt_req_on_date.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        spinner_mat_ava_site.setOnItemSelectedListener(this);

        yesNoArray = getResources().getStringArray(R.array.yes_no_array);

        ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoArray);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_mat_ava_site.setAdapter(yesNoAdapter);

        txt_jobid.setText(jobListRepairWorkRequestResponse.getJOBNO());
        txt_building_name.setText(jobListRepairWorkRequestResponse.getCUST_NAME());
        txt_route.setText(jobListRepairWorkRequestResponse.getSROUTE());
        txt_technician_id.setText(jobListRepairWorkRequestResponse.getTech_code());
        txt_technician_name.setText(jobListRepairWorkRequestResponse.getTech_name());
        txt_mech_id.setText(se_user_id);
        txt_mech_name.setText(se_user_name);

        repairWorkRequestCreateTechRequest.setRoute(jobListRepairWorkRequestResponse.getSROUTE());
        repairWorkRequestCreateTechRequest.setBr_code(se_user_location);
        repairWorkRequestCreateTechRequest.setStatus("REQUESTED");
        repairWorkRequestCreateTechRequest.setJob_id(jobListRepairWorkRequestResponse.getJOBNO());
        repairWorkRequestCreateTechRequest.setSite_name(jobListRepairWorkRequestResponse.getCUST_NAME());
        repairWorkRequestCreateTechRequest.setTech_code(jobListRepairWorkRequestResponse.getTech_code());
        repairWorkRequestCreateTechRequest.setTech_name(jobListRepairWorkRequestResponse.getTech_name());
        repairWorkRequestCreateTechRequest.setSubmitted_by_emp_code(se_user_id);
        repairWorkRequestCreateTechRequest.setSubmitted_by_name(se_user_name);
        repairWorkRequestCreateTechRequest.setSubmitted_by_num(se_user_mobile_no);

        initLoadingDialog();
        getTodayDate();
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
                repairWorkRequestCreateTechRequest.setRequest_on(formattedDate);
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                repairWorkRequestCreateTechRequest.setRequest_on(formattedDate);
                repairWorkRequestCreateTechRequest.setSubmitted_by_on(formattedDate);
            }

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
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

    private void validateRepairWorkRequestCreateTech() {
        Log.i(TAG, "validateRepairWorkRequestCreateTech: repairWorkRequestCreateTechRequest -> " + new Gson().toJson(repairWorkRequestCreateTechRequest));

        repairWorkRequestCreateTechRequest.setRemarks(nullPointer(edt_nature_work.getText().toString().trim()));

        if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getRequest_on())) {
            ErrorMsgDialog("Please Select Request On");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getRoute())) {
            ErrorMsgDialog("Please Select Route");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getBr_code())) {
            ErrorMsgDialog("Please Select Br_code");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getStatus())) {
            ErrorMsgDialog("Please Select Status");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getMat_available_sts())) {
            ErrorMsgDialog("Please Select Material Available at Site");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getRemarks())) {
            ErrorMsgDialog("Please Enter Nature of Work");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getTech_code())) {
            ErrorMsgDialog("Please Select Technician ID");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getTech_name())) {
            ErrorMsgDialog("Please Select Technician Name");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getSubmitted_by_emp_code())) {
            ErrorMsgDialog("Please Select Submitted by ID");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getSubmitted_by_name())) {
            ErrorMsgDialog("Please Select Submitted by Name");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Select Submitted by Number");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Enter Submitted Date");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getJob_id())) {
            ErrorMsgDialog("Please Enter Job Id");
        } else if (!nullPointerValidator(repairWorkRequestCreateTechRequest.getSite_name())) {
            ErrorMsgDialog("Please Enter Site Name");
        } else {
            getRepairWorkRequestCreateTech(repairWorkRequestCreateTechRequest);
        }
        Log.i(TAG, "validateRepairWorkRequestCreateTech: repairWorkRequestCreateTechRequest (2) " + new Gson().toJson(repairWorkRequestCreateTechRequest));
    }

    private void getRepairWorkRequestCreateTech(RepairWorkRequestCreateTechRequest repairWorkRequestCreateTechRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }
        Log.i(TAG, "getRepairWorkRequestCreateTech: repairWorkRequestCreateTechRequest -> " + new Gson().toJson(repairWorkRequestCreateTechRequest));
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getRepairWorkRequestCreateTech(RestUtils.getContentType(), repairWorkRequestCreateTechRequest);
        Log.i(TAG, "getRepairWorkRequestCreateTech: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getRepairWorkRequestCreateTech: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getRepairWorkRequestCreateTech: onFailure: error --> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "onItemSelected: view -> " + view.getId() + " i -> " + i + " l -> " + l);
        String item = adapterView.getItemAtPosition(i).toString();

        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_mat_ava_site:
                    repairWorkRequestCreateTechRequest.setMat_available_sts("");
                    Log.i(TAG, "onItemSelected: repairWorkRequestCreateTechRequest -> " + new Gson().toJson(repairWorkRequestCreateTechRequest));
                    break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_mat_ava_site:
                    repairWorkRequestCreateTechRequest.setMat_available_sts(item);
                    Log.i(TAG, "onItemSelected: repairWorkRequestCreateTechRequest -> " + new Gson().toJson(repairWorkRequestCreateTechRequest));
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
                validateRepairWorkRequestCreateTech();
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