package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnScannerDataListener;
import com.triton.johnson_tap_app.requestpojo.FailureReportFetchDetailsByJobCodeRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportFetchDetailsByJobCodeResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureReportRequestScannerActivity extends AppCompatActivity implements View.OnClickListener, OnScannerDataListener {

    private String TAG = FailureReportRequestScannerActivity.class.getSimpleName(), se_user_mobile_no = "",
            se_user_name = "", se_id = "", check_id = "", service_title = "", message = "", networkStatus = "",
            strScanType = "", strScanData = "", str_title = "";
    private SharedPreferences sharedPreferences;
    private Context context;
    private ImageView img_back, img_search_qr_code, img_scan_qr_code, img_search_bar_code, img_scan_bar_code, img_search_job_num;
    private EditText edt_qr_code_number, edt_bar_code_number, edt_job_num;
    private Dialog dialog;
    private TextView txt_menu_name;
    private Button btn_search_bar_code, btn_search_job_num;
    private FailureReportFetchDetailsByJobCodeResponse failureReportFetchDetailsByJobCodeResponse = new FailureReportFetchDetailsByJobCodeResponse();
    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private int PERMISSION_CLINIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_request_scanner);

        context = this;

        img_back = findViewById(R.id.img_back);
        img_search_qr_code = findViewById(R.id.img_search_qr_code);
        img_search_bar_code = findViewById(R.id.img_search_bar_code);

        img_scan_qr_code = findViewById(R.id.img_scan_qr_code);
        img_scan_bar_code = findViewById(R.id.img_scan_bar_code);
        img_search_job_num = findViewById(R.id.img_search_job_num);

        edt_qr_code_number = findViewById(R.id.edt_qr_code_number);
        edt_bar_code_number = findViewById(R.id.edt_bar_code_number);
        edt_job_num = findViewById(R.id.edt_job_num);

        txt_menu_name = findViewById(R.id.txt_menu_name);
        btn_search_bar_code = findViewById(R.id.btn_search_bar_code);
        btn_search_job_num = findViewById(R.id.btn_search_job_num);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("str_title")) {
                str_title = extras.getString("str_title");
            }
        }

        txt_menu_name.setText(str_title);

        img_back.setOnClickListener(this);
        img_search_qr_code.setOnClickListener(this);
        img_search_bar_code.setOnClickListener(this);
        img_scan_qr_code.setOnClickListener(this);
        img_scan_bar_code.setOnClickListener(this);
        img_search_job_num.setOnClickListener(this);
        btn_search_bar_code.setOnClickListener(this);
        btn_search_job_num.setOnClickListener(this);

        initLoadingDialog();
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
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

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.i(TAG, "hasPermissions: SDK version1 -> " + Build.VERSION.SDK_INT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } else {
            Log.i(TAG, "hasPermissions: SDK version2 -> " + Build.VERSION.SDK_INT);
        }
        return true;
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

        if (!dialog.isShowing()) {
            dialog.show();
        }

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
                                    strScanData = strSearch;
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

        if (!dialog.isShowing()) {
            dialog.show();
        }

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
                                    strScanData = strSearch;
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
        intent.putExtra("strScanType", strScanType);
        intent.putExtra("str_title", str_title);
        intent.putExtra("strScanData", strScanData);

        context.startActivity(intent);

        Log.i(TAG, "moveToNext: failureReportFetchDetailsByJobCodeDataResponse --> " + new Gson().toJson(failureReportFetchDetailsByJobCodeDataResponse));
    }

    @Override
    public void scannerDataListener(String scannerType, String scannerData) {
        Log.i(TAG, "scannerDataListener: scannerType -> " + scannerType + " scannerData -> " + scannerData);
        if (scannerType.equalsIgnoreCase("qr_scanner")) {
            edt_qr_code_number.setText(scannerData);
            img_search_qr_code.performClick();
        } else if (scannerType.equalsIgnoreCase("bar_scanner")) {
            edt_bar_code_number.setText(scannerData);
            img_search_bar_code.performClick();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.img_search_qr_code: {
                Log.i(TAG, "onClick: img_search_qr_code");
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onClick: img_search_qr_code ->" + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_qr_code_number.getText().toString().toUpperCase().trim() != null && !edt_qr_code_number.getText().toString().toUpperCase().trim().isEmpty()) {
                        strScanType = "qr_code_scan";
                        getFetchDataJobCode(edt_qr_code_number.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid QR Code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case R.id.img_search_bar_code:
            case R.id.btn_search_bar_code: {
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onClick: img_search_bar_code ->" + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_bar_code_number.getText().toString().toUpperCase().trim() != null && !edt_bar_code_number.getText().toString().toUpperCase().trim().isEmpty()) {
                        strScanType = "bar_code_scan";
                        getFetchDataJobCode(edt_bar_code_number.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid BAR Code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case R.id.img_scan_qr_code: {

                if (!hasPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("QRCodeScannerFragment")
                            .add(android.R.id.content, new QRCodeScannerFragment(this), "QRCodeScannerFragment")
                            .commit();
                }
            }
            break;
            case R.id.img_scan_bar_code: {
                /*networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_bar_code_number.getText().toString().toUpperCase().trim() != null && !edt_bar_code_number.getText().toString().toUpperCase().trim().isEmpty()) {

                       strScanType="bar_code_scan";
                        getFetchDataJobCode(edt_bar_code_number.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid Barcode", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (!hasPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
//                            .add(android.R.id.content, new BarCodeScannerFragment(this), "BarCodeScannerFragment")
                            .add(android.R.id.content, new SimpleBarCodeScannerFragment(this), "BarCodeScannerFragment")
                            .commit();
                }
            }
            break;
            case R.id.img_search_job_num:
            case R.id.btn_search_job_num: {
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edt_job_num.getText().toString().toUpperCase().trim() != null && !edt_job_num.getText().toString().toUpperCase().trim().isEmpty()) {
                        strScanType = "job_id";
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