package com.triton.johnson_tap_app.Service_Activity.ropeMaintenanceModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.CreateRopeMaintenanceRequest;
import com.triton.johnson_tap_app.requestpojo.RopeMaintenanceCheckDataRequest;
import com.triton.johnson_tap_app.responsepojo.JobListRopeMaintenanceResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RopeMaintenanceFormActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static String formattedDate = "";
    private Context context;
    private String strDateType = "", se_user_id, se_user_name, se_user_mobile_no, se_user_location,
            TAG = RopeMaintenanceFormActivity.class.getSimpleName();
    private TextView txt_job_id, txt_building_name, txt_act_date, txt_technician_name;
    private JobListRopeMaintenanceResponse.Data jobListRopeMaintenanceDataResponse;
    private CheckBox cb6Main, cb8Main, cb10Main, cb13Main, cb16Main, cb6OSG, cb8OSG, cbRopeClean, cbRopeLub,
            cbRopeAlig;
    private EditText edt_machine_type, edt_comments_failure;
    private Button btn_submit;
    private ImageView iv_back;
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private CreateRopeMaintenanceRequest createRopeMaintenanceRequest;
    private RopeMaintenanceCheckDataRequest ropeMaintenanceCheckDataRequest;
    private ArrayList<String> activity_code_list;
    private SharedPreferences sharedPreferences;
    private boolean checkDate = false;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rope_maintenance_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        iv_back = findViewById(R.id.iv_back);
        btn_submit = findViewById(R.id.btn_submit);

        txt_job_id = findViewById(R.id.txt_job_id);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_act_date = findViewById(R.id.txt_act_date);
        txt_technician_name = findViewById(R.id.txt_technician_name);

        cb6Main = findViewById(R.id.cb6Main);
        cb8Main = findViewById(R.id.cb8Main);
        cb10Main = findViewById(R.id.cb10Main);
        cb13Main = findViewById(R.id.cb13Main);
        cb16Main = findViewById(R.id.cb16Main);

        cb6OSG = findViewById(R.id.cb6OSG);
        cb8OSG = findViewById(R.id.cb8OSG);

        cbRopeClean = findViewById(R.id.cbRopeClean);
        cbRopeLub = findViewById(R.id.cbRopeLub);
        cbRopeAlig = findViewById(R.id.cbRopeAlig);

        edt_machine_type = findViewById(R.id.edt_machine_type);
        edt_comments_failure = findViewById(R.id.edt_comments_failure);

        jobListRopeMaintenanceDataResponse = new JobListRopeMaintenanceResponse.Data();
        createRopeMaintenanceRequest = new CreateRopeMaintenanceRequest();
        ropeMaintenanceCheckDataRequest = new RopeMaintenanceCheckDataRequest();

        activity_code_list = new ArrayList<>();

        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        txt_act_date.setOnClickListener(this);

        cb6Main.setOnCheckedChangeListener(this);
        cb8Main.setOnCheckedChangeListener(this);
        cb10Main.setOnCheckedChangeListener(this);
        cb13Main.setOnCheckedChangeListener(this);
        cb16Main.setOnCheckedChangeListener(this);

        cb6OSG.setOnCheckedChangeListener(this);
        cb8OSG.setOnCheckedChangeListener(this);

        cbRopeClean.setOnCheckedChangeListener(this);
        cbRopeLub.setOnCheckedChangeListener(this);
        cbRopeAlig.setOnCheckedChangeListener(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("jobListRopeMaintenanceDataResponse")) {
                jobListRopeMaintenanceDataResponse = extras.getParcelable("jobListRopeMaintenanceDataResponse");
            }
        }

        Log.i(TAG, "onCreate: jobListRopeMaintenanceDataResponse -> " + new Gson().toJson(jobListRopeMaintenanceDataResponse));

        txt_job_id.setText(jobListRopeMaintenanceDataResponse.getJOBNO());
        txt_building_name.setText(jobListRopeMaintenanceDataResponse.getCUST_NAME());
        txt_technician_name.setText(jobListRopeMaintenanceDataResponse.getTech_name());

        ropeMaintenanceCheckDataRequest.setJob_id(jobListRopeMaintenanceDataResponse.getJOBNO());
        ropeMaintenanceCheckDataRequest.setSubmitted_by_num(se_user_mobile_no);

        createRopeMaintenanceRequest.setJob_id(jobListRopeMaintenanceDataResponse.getJOBNO());
        createRopeMaintenanceRequest.setBuilding_name(jobListRopeMaintenanceDataResponse.getCUST_NAME());
        createRopeMaintenanceRequest.setTech_code(jobListRopeMaintenanceDataResponse.getTech_code());
        createRopeMaintenanceRequest.setTech_name(jobListRopeMaintenanceDataResponse.getTech_name());

        createRopeMaintenanceRequest.setSubmitted_by_emp_code(se_user_id);
        createRopeMaintenanceRequest.setSubmitted_by_name(se_user_name);
        createRopeMaintenanceRequest.setSubmitted_by_num(se_user_mobile_no);

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

        if (strDateType.equalsIgnoreCase("txt_act_date")) {
            txt_act_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_act_date.setText(dateTime);
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
            if (strDateType.equalsIgnoreCase("txt_act_date")) {
                createRopeMaintenanceRequest.setActivity_date(formattedDate);

                ropeMaintenanceCheckDataRequest.setSubmitted_by_on(formattedDate);

                getFailureReportCheckDate();
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                createRopeMaintenanceRequest.setSubmitted_by_on(formattedDate);
                createRopeMaintenanceRequest.setActivity_date(formattedDate);

                ropeMaintenanceCheckDataRequest.setSubmitted_by_on(formattedDate);

                getFailureReportCheckDate();
            }

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

    private void validateCreateRopeMaintenanceRequest() {
        Log.i(TAG, "validateCreateRopeMaintenanceRequest: createRopeMaintenanceRequest (1) -> " + new Gson().toJson(createRopeMaintenanceRequest));
        Log.i(TAG, "validateCreateRopeMaintenanceRequest: activity_code_list -> " + new Gson().toJson(activity_code_list));

        createRopeMaintenanceRequest.setMachine_type(nullPointer(edt_machine_type.getText().toString().trim()));
        createRopeMaintenanceRequest.setRemarks(nullPointer(edt_comments_failure.getText().toString().trim()));
        createRopeMaintenanceRequest.setActivity_code_list(activity_code_list);

        if (!checkDate) {
            ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getJob_id())) {
            ErrorMsgDialog("Please Select Job No.");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getBuilding_name())) {
            ErrorMsgDialog("Please Select Branch Name");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getMachine_type())) {
            ErrorMsgDialog("Please Select Machine Type");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getMain_rope_dia())) {
            ErrorMsgDialog("Please Select Main Rope Dia");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getOsg_rope_dia())) {
            ErrorMsgDialog("Please Select OSG Rope Dia");
        } else if (createRopeMaintenanceRequest.getActivity_code_list() == null
                || createRopeMaintenanceRequest.getActivity_code_list().isEmpty()) {
            ErrorMsgDialog("Please Select Activity Code");
        } /*else if (!nullPointerValidator(createRopeMaintenanceRequest.getActivity_code())) {
            ErrorMsgDialog("Please Select Activity Code");
        }*/ else if (!nullPointerValidator(createRopeMaintenanceRequest.getTech_code())) {
            ErrorMsgDialog("Please Select Technician Code");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getTech_name())) {
            ErrorMsgDialog("Please Select Technician Name");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getActivity_date())) {
            ErrorMsgDialog("Please Select Activity Date");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getSubmitted_by_name())) {
            ErrorMsgDialog("Please Select Submitted by Name");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getSubmitted_by_emp_code())) {
            ErrorMsgDialog("Please Select Submitted by Emp Code");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Select Submitted by Num");
        } else if (!nullPointerValidator(createRopeMaintenanceRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Select Submitted by On");
        } else {
            Log.i(TAG, "validateCreateRopeMaintenanceRequest: createRopeMaintenanceRequest (2) -> " + new Gson().toJson(createRopeMaintenanceRequest));
            getCreateRopeMaintenance();
        }
    }

    private void getCreateRopeMaintenance() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getRopeMaintenanceCreate(getContentType(), createRopeMaintenanceRequest);
        Log.i(TAG, "getCreateRopeMaintenance: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getCreateRopeMaintenance: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getCreateRopeMaintenance: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void getFailureReportCheckDate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Log.i(TAG, "getFailureReportCheckDate: ropeMaintenanceCheckDataRequest -> " + new Gson().toJson(ropeMaintenanceCheckDataRequest));
        Call<SuccessResponse> call = apiInterface.getRopeMaintenanceCheckDate(getContentType(), ropeMaintenanceCheckDataRequest);
        Log.i(TAG, "getFailureReportCheckDate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getFailureReportCheckDate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getFailureReportCheckDate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: {
                onBackPressed();
            }
            break;
            case R.id.txt_act_date: {
                strDateType = "txt_act_date";
                callDatePicker();
            }
            break;
            case R.id.btn_submit: {
                validateCreateRopeMaintenanceRequest();
            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            switch (compoundButton.getId()) {
                case R.id.cb6Main: {
//                    cb6Main.setChecked(false);
                    createRopeMaintenanceRequest.setMain_rope_dia("6 MM");
                    cb8Main.setChecked(false);
                    cb10Main.setChecked(false);
                    cb13Main.setChecked(false);
                    cb16Main.setChecked(false);
                }
                break;
                case R.id.cb8Main: {
                    cb6Main.setChecked(false);
//                    cb8Main.setChecked(false);
                    createRopeMaintenanceRequest.setMain_rope_dia("8 MM");
                    cb10Main.setChecked(false);
                    cb13Main.setChecked(false);
                    cb16Main.setChecked(false);
                }
                break;
                case R.id.cb10Main: {
                    cb6Main.setChecked(false);
                    cb8Main.setChecked(false);
//                    cb10Main.setChecked(false);
                    createRopeMaintenanceRequest.setMain_rope_dia("10 MM");
                    cb13Main.setChecked(false);
                    cb16Main.setChecked(false);
                }
                break;
                case R.id.cb13Main: {
                    cb6Main.setChecked(false);
                    cb8Main.setChecked(false);
                    cb10Main.setChecked(false);
//                    cb13Main.setChecked(false);
                    createRopeMaintenanceRequest.setMain_rope_dia("13 MM");
                    cb16Main.setChecked(false);
                }
                break;
                case R.id.cb16Main: {
                    cb6Main.setChecked(false);
                    cb8Main.setChecked(false);
                    cb10Main.setChecked(false);
                    cb13Main.setChecked(false);
//                    cb16Main.setChecked(false);
                    createRopeMaintenanceRequest.setMain_rope_dia("16 MM");
                }
                break;
                case R.id.cb6OSG: {
//                    cb6OSG.setChecked(false);
                    createRopeMaintenanceRequest.setOsg_rope_dia("6 MM");
                    cb8OSG.setChecked(false);
                }
                break;
                case R.id.cb8OSG: {
                    cb6OSG.setChecked(false);
//                    cb8OSG.setChecked(false);
                    createRopeMaintenanceRequest.setOsg_rope_dia("8 MM");
                }
                break;
                case R.id.cbRopeClean: {
                    /*cbRopeClean.setChecked(false);
                    cbRopeLub.setChecked(false);
                    cbRopeAlig.setChecked(false);
                    createRopeMaintenanceRequest.setActivity_code("Rope Cleaning");*/
                    if (!activity_code_list.contains("Rope Cleaning")) {
                        activity_code_list.add("Rope Cleaning");
                    }
                }
                break;
                case R.id.cbRopeLub: {
                    /*cbRopeClean.setChecked(false);
                    cbRopeLub.setChecked(false);
                    cbRopeAlig.setChecked(false);
                    createRopeMaintenanceRequest.setActivity_code("Rope Lubrication");*/
                    if (!activity_code_list.contains("Rope Lubrication")) {
                        activity_code_list.add("Rope Lubrication");
                    }
                }
                break;
                case R.id.cbRopeAlig: {
                    /*cbRopeClean.setChecked(false);
                    cbRopeLub.setChecked(false);
                    cbRopeAlig.setChecked(false);
                    createRopeMaintenanceRequest.setActivity_code("Rope Alignment");*/
                    if (!activity_code_list.contains("Rope Alignment")) {
                        activity_code_list.add("Rope Alignment");
                    }
                }
                break;
            }
        } else {
            if (compoundButton.getId() == R.id.cb6Main || compoundButton.getId() == R.id.cb8Main
                    || compoundButton.getId() == R.id.cb10Main || compoundButton.getId() == R.id.cb13Main
                    || compoundButton.getId() == R.id.cb16Main) {
                createRopeMaintenanceRequest.setMain_rope_dia("");
            } else if (compoundButton.getId() == R.id.cb6OSG || compoundButton.getId() == R.id.cb8OSG) {
                createRopeMaintenanceRequest.setOsg_rope_dia("");
            }/* else if (compoundButton.getId() == R.id.cbRopeClean || compoundButton.getId() == R.id.cbRopeLub
                    || compoundButton.getId() == R.id.cbRopeAlig) {
                createRopeMaintenanceRequest.setActivity_code("");
            }*/

            if (compoundButton.getId() == R.id.cbRopeClean && !activity_code_list.isEmpty()
                    && activity_code_list.contains("Rope Cleaning")) {
                activity_code_list.remove("Rope Cleaning");
            } else if (compoundButton.getId() == R.id.cbRopeLub && !activity_code_list.isEmpty()
                    && activity_code_list.contains("Rope Lubrication")) {
                activity_code_list.remove("Rope Lubrication");
            } else if (compoundButton.getId() == R.id.cbRopeAlig && !activity_code_list.isEmpty()
                    && activity_code_list.contains("Rope Alignment")) {
                activity_code_list.remove("Rope Alignment");
            }
        }
        Log.i(TAG, "onCheckedChanged: main_rope_dia_list - " + new Gson().toJson(activity_code_list));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}