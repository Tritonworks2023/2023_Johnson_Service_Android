package com.triton.johnson_tap_app.Service_Activity.failureReportModule;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.CreateFailureReportRequest;
import com.triton.johnson_tap_app.requestpojo.FailureReportCheckDataRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.JobListFailureReportResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

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

public class FailureReportFormActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static String formattedDate = "", formattedDate2 = "", formattedDate3 = "";
    private ImageView img_back;
    private CheckBox cbPremier, cbGold, cbSilver, cbF, cbNA;
    private TextView txt_date, txt_branch_name, txt_job_id, txt_sit_name, txt_device_ins_date, txt_failure_date,
            txt_mechanic_name;
    private EditText edt_dept, edt_dev_com_name, edt_mod_mak, edt_rating, edt_serial_no,
            edt_fail_observe, edt_inc_sup_volt, edt_comments_failure, edt_environmental_condition;
    private String strDateType = "", TAG = FailureReportFormActivity.class.getSimpleName(),
            se_user_id, se_user_location, se_user_mobile_no, se_user_name, networkStatus = "", uploadImagePath="";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private MultipartBody.Part signaturePart;
    private Button clear_button_mechanic, save_button_mechanic, btn_submit;
    private Bitmap signatureBitmap;
    private SignaturePad signaturePadMechanic;
    private Context context;
    private CreateFailureReportRequest createFailureReportRequest;
    private FailureReportCheckDataRequest failureReportCheckDataRequest;
    private JobListFailureReportResponse.Data jobListFailureReportDataResponse = new JobListFailureReportResponse.Data();
    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private boolean checkDate = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey("jobListFailureReportDataResponse")) {
                jobListFailureReportDataResponse = extra.getParcelable("jobListFailureReportDataResponse");
            }
        }
        Log.i(TAG, "onCreate: jobListFailureReportDataResponse -> " + new Gson().toJson(jobListFailureReportDataResponse));

        img_back = findViewById(R.id.img_back);

        txt_date = findViewById(R.id.txt_date);
        txt_branch_name = findViewById(R.id.txt_branch_name);
        txt_job_id = findViewById(R.id.txt_job_id);
        txt_sit_name = findViewById(R.id.txt_sit_name);
        txt_device_ins_date = findViewById(R.id.txt_device_ins_date);
        txt_failure_date = findViewById(R.id.txt_failure_date);
        txt_mechanic_name = findViewById(R.id.txt_mechanic_name);

        edt_dept = findViewById(R.id.edt_dept);
        edt_dev_com_name = findViewById(R.id.edt_dev_com_name);
        edt_mod_mak = findViewById(R.id.edt_mod_mak);
        edt_rating = findViewById(R.id.edt_rating);
        edt_serial_no = findViewById(R.id.edt_serial_no);
        edt_fail_observe = findViewById(R.id.edt_fail_observe);
        edt_inc_sup_volt = findViewById(R.id.edt_inc_sup_volt);
        edt_comments_failure = findViewById(R.id.edt_comments_failure);
        edt_environmental_condition = findViewById(R.id.edt_environmental_condition);

        cbPremier = findViewById(R.id.cbPremier);
        cbGold = findViewById(R.id.cbGold);
        cbSilver = findViewById(R.id.cbSilver);
        cbF = findViewById(R.id.cbF);
        cbNA = findViewById(R.id.cbNA);

        signaturePadMechanic = findViewById(R.id.signaturePadMechanic);

        clear_button_mechanic = findViewById(R.id.clear_button_mechanic);
        save_button_mechanic = findViewById(R.id.save_button_mechanic);
        btn_submit = findViewById(R.id.btn_submit);

        createFailureReportRequest = new CreateFailureReportRequest();
        failureReportCheckDataRequest = new FailureReportCheckDataRequest();

        txt_branch_name.setText(CommonFunction.nullPointer(se_user_location));
        txt_mechanic_name.setText(CommonFunction.nullPointer(se_user_name));
        txt_job_id.setText(CommonFunction.nullPointer(jobListFailureReportDataResponse.getJOBNO()));
        txt_sit_name.setText(CommonFunction.nullPointer(jobListFailureReportDataResponse.getCUST_NAME()));

        cbPremier.setOnCheckedChangeListener(this);
        cbGold.setOnCheckedChangeListener(this);
        cbSilver.setOnCheckedChangeListener(this);
        cbF.setOnCheckedChangeListener(this);
        cbNA.setOnCheckedChangeListener(this);

        txt_date.setOnClickListener(this);
        txt_failure_date.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        save_button_mechanic.setOnClickListener(this);
        clear_button_mechanic.setOnClickListener(this);

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

        createFailureReportRequest.setBranch_name(se_user_location);
        createFailureReportRequest.setJob_id(jobListFailureReportDataResponse.getJOBNO());
        createFailureReportRequest.setSite_name(jobListFailureReportDataResponse.getCUST_NAME());
        createFailureReportRequest.setMech_name(se_user_name);

        createFailureReportRequest.setSubmitted_by_emp_code(se_user_id);
        createFailureReportRequest.setSubmitted_by_name(se_user_name);
        createFailureReportRequest.setSubmitted_by_num(se_user_mobile_no);

        failureReportCheckDataRequest.setJob_id(jobListFailureReportDataResponse.getJOBNO());
        failureReportCheckDataRequest.setSubmitted_by_num(se_user_mobile_no);

        setDate(txt_device_ins_date, jobListFailureReportDataResponse.getINST_ON());
        initLoadingDialog();
        getTodayDate();
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_date:
                strDateType = "txt_date";
                callDatePicker();
                break;
            case R.id.txt_device_ins_date:
                strDateType = "txt_device_ins_date";
                callDatePicker();
                break;
            case R.id.txt_failure_date:
                strDateType = "txt_failure_date";
                callDatePicker();
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
            case R.id.btn_submit: {
                validateCreateFailureReportRequest();
            }
            break;
            case R.id.img_back: {
                onBackPressed();
            }
            break;
        }
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

        if (strDateType.equalsIgnoreCase("txt_date")) {
            txt_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_failure_date")) {
            txt_failure_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_date.setText(dateTime);
            txt_failure_date.setText(dateTime);
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

            if (strDateType.equalsIgnoreCase("txt_date")) {
                createFailureReportRequest.setReport_date(formattedDate);
                failureReportCheckDataRequest.setReport_date(formattedDate);
                getFailureReportCheckDate();
            } else if (strDateType.equalsIgnoreCase("txt_failure_date")) {
                createFailureReportRequest.setDate_of_failure(dateTime);
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                createFailureReportRequest.setReport_date(formattedDate);
                createFailureReportRequest.setDate_of_failure(formattedDate);
                createFailureReportRequest.setSubmitted_by_on(formattedDate);
                failureReportCheckDataRequest.setReport_date(formattedDate);
                getFailureReportCheckDate();
            }
            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void setDate(TextView textView, String inputDateTime) {

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//        String outputPattern = "dd-MM-yyyy hh:mm a";
        String outputPattern = "dd/MM/yyyy";
        String outputPattern2 = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat outputFormat2 = new SimpleDateFormat(outputPattern2);

        Date date = null;
        String str = null;
        String str2 = null;

        try {
            date = inputFormat.parse(inputDateTime);
            str = outputFormat.format(date);
            str2 = outputFormat2.format(date);
            formattedDate3 = str;/*.replace("AM", "am").replace("PM", "pm")*/
            formattedDate2 = str2;/*.replace("AM", "am").replace("PM", "pm")*/

            textView.setText(formattedDate3);
            createFailureReportRequest.setDevice_ins_date(formattedDate2);

            Log.i(TAG, "setDate: formattedDate3-> " + formattedDate3);
            Log.i(TAG, "setDate: formattedDate2-> " + formattedDate2);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void validateCreateFailureReportRequest() {
        Log.i(TAG, "validateCreateFailureReportRequest: createFailureReportRequest (1) -> " + new Gson().toJson(createFailureReportRequest));

        createFailureReportRequest.setDepartment_name(nullPointer(edt_dept.getText().toString().trim()));
        createFailureReportRequest.setDevice_name(nullPointer(edt_dev_com_name.getText().toString().trim()));
        createFailureReportRequest.setModel_no(nullPointer(edt_mod_mak.getText().toString().trim()));
        createFailureReportRequest.setRating(nullPointer(edt_rating.getText().toString().trim()));
        createFailureReportRequest.setSerial_no(nullPointer(edt_serial_no.getText().toString().trim()));
        createFailureReportRequest.setFailure_obs(nullPointer(edt_fail_observe.getText().toString().trim()));
        createFailureReportRequest.setIncom_suppy_voltage(nullPointer(edt_inc_sup_volt.getText().toString().trim()));
        createFailureReportRequest.setComments(nullPointer(edt_comments_failure.getText().toString().trim()));
        createFailureReportRequest.setEnv_condition(nullPointer(edt_environmental_condition.getText().toString().trim()));

        if (!checkDate) {
            ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
        } else if (!nullPointerValidator(createFailureReportRequest.getReport_date())) {
            ErrorMsgDialog("Please Select Date");
        } else if (!nullPointerValidator(createFailureReportRequest.getBranch_name())) {
            ErrorMsgDialog("Please Select Branch Name");
        } else if (!nullPointerValidator(createFailureReportRequest.getJob_id())) {
            ErrorMsgDialog("Please Select Job No.");
        } else if (!nullPointerValidator(createFailureReportRequest.getSite_name())) {
            ErrorMsgDialog("Please Select Site Name");
        } else if (!nullPointerValidator(createFailureReportRequest.getDepartment_name())) {
            ErrorMsgDialog("Please Select Department (Testing / Service)");
        } else if (!nullPointerValidator(createFailureReportRequest.getType_of_ser())) {
            ErrorMsgDialog("Please Select Type of Service");
        } else if (!nullPointerValidator(createFailureReportRequest.getDevice_name())) {
            ErrorMsgDialog("Please Select Device Name");
        } else if (!nullPointerValidator(createFailureReportRequest.getModel_no())) {
            ErrorMsgDialog("Please Select Model No. & Make");
        } else if (!nullPointerValidator(createFailureReportRequest.getRating())) {
            ErrorMsgDialog("Please Select Rating");
        } else if (!nullPointerValidator(createFailureReportRequest.getSerial_no())) {
            ErrorMsgDialog("Please Select Serial No.");
        } else if (!nullPointerValidator(createFailureReportRequest.getDevice_ins_date())) {
            ErrorMsgDialog("Please Select Device / Component installation Date");
        } else if (!nullPointerValidator(createFailureReportRequest.getDate_of_failure())) {
            ErrorMsgDialog("Please Failure Date");
        } else if (!nullPointerValidator(createFailureReportRequest.getFailure_obs())) {
            ErrorMsgDialog("Please Select Failure Observation");
        } else if (!nullPointerValidator(createFailureReportRequest.getIncom_suppy_voltage())) {
            ErrorMsgDialog("Please Select Incoming Supply Voltage");
        } else if (!nullPointerValidator(createFailureReportRequest.getMech_name())) {
            ErrorMsgDialog("Please Select Mechanic Name");
        } else if (!nullPointerValidator(createFailureReportRequest.getMech_signature())) {
            ErrorMsgDialog("Please Select Mechanic Signature");
        } else {
            Log.i(TAG, "validateCreateFailureReportRequest: createFailureReportRequest (2) -> " + new Gson().toJson(createFailureReportRequest));
            getFailureReportCreate();
        }
    }

    private void getFailureReportCheckDate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Log.i(TAG, "getFailureReportCheckDate: failureReportCheckDataRequest -> " + new Gson().toJson(failureReportCheckDataRequest));
        Call<SuccessResponse> call = apiInterface.getFailureReportCheckDate(getContentType(), failureReportCheckDataRequest);
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

    private void uploadDigitalSignatureImageRequest(File file) {

        progressDialog = new ProgressDialog(context);
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
                        uploadImagePath = response.body().getData();
                        createFailureReportRequest.setMech_signature(uploadImagePath);
                        if (uploadImagePath != null) {
                            signaturePadMechanic.setEnabled(false);
                            save_button_mechanic.setEnabled(false);
                            clear_button_mechanic.setEnabled(false);
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

    private void getFailureReportCreate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getFailureReportCreate(getContentType(), createFailureReportRequest);
        Log.i(TAG, "getFailureReportCreate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getFailureReportCreate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getFailureReportCreate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            switch (compoundButton.getId()) {
                case R.id.cbPremier: {
//                cbPremier.setChecked(true);
                    cbGold.setChecked(false);
                    cbSilver.setChecked(false);
                    cbF.setChecked(false);
                    cbNA.setChecked(false);
                    createFailureReportRequest.setType_of_ser("premier");
                }
                break;
                case R.id.cbGold: {
                    cbPremier.setChecked(false);
//                cbGold.setChecked(true);
                    cbSilver.setChecked(false);
                    cbF.setChecked(false);
                    cbNA.setChecked(false);
                    createFailureReportRequest.setType_of_ser("gold");
                }
                break;
                case R.id.cbSilver: {
                    cbPremier.setChecked(false);
                    cbGold.setChecked(false);
//                cbSilver.setChecked(true);
                    cbF.setChecked(false);
                    cbNA.setChecked(false);
                    createFailureReportRequest.setType_of_ser("silver");
                }
                break;
                case R.id.cbF: {
                    cbPremier.setChecked(false);
                    cbGold.setChecked(false);
                    cbSilver.setChecked(false);
//                cbF.setChecked(true);
                    cbNA.setChecked(false);
                    createFailureReportRequest.setType_of_ser("free");
                }
                break;
                case R.id.cbNA: {
                    cbPremier.setChecked(false);
                    cbGold.setChecked(false);
                    cbSilver.setChecked(false);
                    cbF.setChecked(false);
//                cbNA.setChecked(true);
                    createFailureReportRequest.setType_of_ser("n/a");
                }
                break;
            }
        } else {
            createFailureReportRequest.setType_of_ser("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}