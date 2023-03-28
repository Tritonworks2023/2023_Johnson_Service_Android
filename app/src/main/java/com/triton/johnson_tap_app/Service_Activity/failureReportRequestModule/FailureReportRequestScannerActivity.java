package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.FailureReportFetchDetailsByJobCodeRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportFetchDetailsByJobCodeResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureReportRequestScannerActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = FailureReportRequestScannerActivity.class.getSimpleName(), se_user_mobile_no,
            se_user_name, se_id, check_id, service_title, message, networkStatus = "";
    private SharedPreferences sharedPreferences;
    private Context context;
    private ImageView img_back;
    private Button btn_qr_scan, btn_barcode_scan, btn_submit;
    private EditText edt_qr_scan, edt_barcode_number, edt_job_num;
    private Dialog dialog;
    private FailureReportFetchDetailsByJobCodeResponse failureReportFetchDetailsByJobCodeResponse = new FailureReportFetchDetailsByJobCodeResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_request_scanner);

        context = this;

        img_back = findViewById(R.id.img_back);

        btn_qr_scan = findViewById(R.id.btn_qr_scan);
        btn_barcode_scan = findViewById(R.id.btn_barcode_scan);
        btn_submit = findViewById(R.id.btn_submit);

        edt_qr_scan = findViewById(R.id.edt_qr_scan);
        edt_barcode_number = findViewById(R.id.edt_barcode_number);
        edt_job_num = findViewById(R.id.edt_job_num);

        img_back.setOnClickListener(this);
        btn_qr_scan.setOnClickListener(this);
        btn_barcode_scan.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
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

    private FailureReportFetchDetailsByJobCodeRequest jobListRequest(String strSearch) {

        FailureReportFetchDetailsByJobCodeRequest job = new FailureReportFetchDetailsByJobCodeRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setJob_id(strSearch);
            Log.i(TAG, "jobListRequest: FailureReportFetchDetailsByJobCodeRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private FailureReportFetchDetailsByJobCodeRequest jobListCodeRequest(String strSearch) {

        FailureReportFetchDetailsByJobCodeRequest job = new FailureReportFetchDetailsByJobCodeRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setFACTBARCODE(strSearch);
            Log.i(TAG, "jobListRequest: FailureReportFetchDetailsByJobCodeRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void getFetchDataJobId(String strSearch) {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        FailureReportFetchDetailsByJobCodeRequest failureReportFetchDetailsByJobCodeRequest = jobListRequest(strSearch);

        if (failureReportFetchDetailsByJobCodeRequest != null) {

            Call<FailureReportFetchDetailsByJobCodeResponse> call = apiInterface.getFailureReportFetchDetailsByJobId(RestUtils.getContentType(), failureReportFetchDetailsByJobCodeRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<FailureReportFetchDetailsByJobCodeResponse>() {
                @Override
                public void onResponse(@NonNull Call<FailureReportFetchDetailsByJobCodeResponse> call, @NonNull Response<FailureReportFetchDetailsByJobCodeResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: FailureReportFetchDetailsByJobCodeResponse -> " + new Gson().toJson(response.body()));

                    failureReportFetchDetailsByJobCodeResponse = new FailureReportFetchDetailsByJobCodeResponse();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                failureReportFetchDetailsByJobCodeResponse = response.body();
//                                failureReportFetchDetailsByJobDataResponseList = response.body().getData();

                                if (failureReportFetchDetailsByJobCodeResponse.getData().isEmpty()) {
                                    ErrorMsgDialog(response.body().getMessage());
                                    /*rv_job_safety_audit.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
                                    edtSearch.setEnabled(false);*/
                                } else {
                                    moveToNext(failureReportFetchDetailsByJobCodeResponse);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FailureReportFetchDetailsByJobCodeResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    ErrorMsgDialog(t.getMessage());
                    /*rv_job_safety_audit.setVisibility(View.GONE);
                    txt_no_records.setVisibility(View.VISIBLE);
                    txt_no_records.setText("Something Went Wrong.. Try Again Later");
                    edtSearch.setEnabled(false);*/
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    private void getFetchDataJobCode(String strSearch) {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        FailureReportFetchDetailsByJobCodeRequest failureReportFetchDetailsByJobCodeRequest = jobListCodeRequest(strSearch);

        if (failureReportFetchDetailsByJobCodeRequest != null) {

            Call<FailureReportFetchDetailsByJobCodeResponse> call = apiInterface.getFailureReportFetchDetailsByCode(RestUtils.getContentType(), failureReportFetchDetailsByJobCodeRequest);
            Log.i(TAG, "getFetchDataJobCode: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<FailureReportFetchDetailsByJobCodeResponse>() {
                @Override
                public void onResponse(@NonNull Call<FailureReportFetchDetailsByJobCodeResponse> call, @NonNull Response<FailureReportFetchDetailsByJobCodeResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobCode: onResponse: FailureReportFetchDetailsByJobCodeResponse -> " + new Gson().toJson(response.body()));

                    failureReportFetchDetailsByJobCodeResponse = new FailureReportFetchDetailsByJobCodeResponse();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                failureReportFetchDetailsByJobCodeResponse = response.body();
//                                failureReportFetchDetailsByJobDataResponseList = response.body().getData();

                                if (failureReportFetchDetailsByJobCodeResponse.getData().isEmpty()) {
                                    ErrorMsgDialog(response.body().getMessage());
                                    /*rv_job_safety_audit.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
                                    edtSearch.setEnabled(false);*/
                                } else {
                                    moveToNext(failureReportFetchDetailsByJobCodeResponse);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FailureReportFetchDetailsByJobCodeResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobCode: onFailure: error --> " + t.getMessage());
                    ErrorMsgDialog(t.getMessage());
                    /*rv_job_safety_audit.setVisibility(View.GONE);
                    txt_no_records.setVisibility(View.VISIBLE);
                    txt_no_records.setText("Something Went Wrong.. Try Again Later");
                    edtSearch.setEnabled(false);*/
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    private void moveToNext(FailureReportFetchDetailsByJobCodeResponse failureReportFetchDetailsByJobCodeResponse) {

        FailureReportFetchDetailsByJobCodeResponse.Data failureReportFetchDetailsByJobCodeDataResponse = new FailureReportFetchDetailsByJobCodeResponse.Data();
        failureReportFetchDetailsByJobCodeDataResponse = failureReportFetchDetailsByJobCodeResponse.getData().get(0);

        Intent intent = new Intent(context, FailureReportRequestFormActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("failureReportFetchDetailsByJobCodeDataResponse", failureReportFetchDetailsByJobCodeDataResponse);
        context.startActivity(intent);

        Log.i(TAG, "moveToNext: failureReportFetchDetailsByJobCodeDataResponse --> " + new Gson().toJson(failureReportFetchDetailsByJobCodeDataResponse));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.btn_qr_scan: {
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_qr_scan.getText().toString().toUpperCase().trim() != null && !edt_qr_scan.getText().toString().toUpperCase().trim().isEmpty()) {
                        getFetchDataJobCode(edt_qr_scan.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid QR Code", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case R.id.btn_barcode_scan: {
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_barcode_number.getText().toString().toUpperCase().trim() != null && !edt_barcode_number.getText().toString().toUpperCase().trim().isEmpty()) {
                        getFetchDataJobCode(edt_barcode_number.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid Barcode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case R.id.btn_submit: {
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_job_num.getText().toString().toUpperCase().trim() != null && !edt_job_num.getText().toString().toUpperCase().trim().isEmpty()) {
                        getFetchDataJobId(edt_job_num.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid Job Id", Toast.LENGTH_SHORT).show();
                    }
                }
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