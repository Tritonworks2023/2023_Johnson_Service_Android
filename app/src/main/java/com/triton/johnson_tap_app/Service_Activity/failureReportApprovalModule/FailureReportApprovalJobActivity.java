package com.triton.johnson_tap_app.Service_Activity.failureReportApprovalModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.FailureReportRequestListByEngCodeRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportRequestListByEngCodeResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureReportApprovalJobActivity extends AppCompatActivity
        implements OnItemClickDataChangeListener, View.OnClickListener {

    ImageView img_back;
    RecyclerView rv_job_failure_report_approval;
    EditText edtSearch;
    TextView txt_no_records, txt_menu_name;
    RelativeLayout Job;
    JobListFailureReportApprovalAdapter jobListFailureReportApprovalAdapter;
    Context context;
    String TAG = FailureReportApprovalJobActivity.class.getSimpleName(), se_user_mobile_no, se_user_name,
            se_user_id, check_id, service_title, message, networkStatus = "";
    SharedPreferences sharedPreferences;
    List<FailureReportRequestListByEngCodeResponse.Data> failureReportRequestListByEngCodeResponseList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_approval_job);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_failure_report_approval = findViewById(R.id.rv_job_failure_report_approval);
        Job = findViewById(R.id.rel_job);
        txt_menu_name = findViewById(R.id.txt_menu_name);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "onCreate: service_title --> " + service_title);
        Log.i(TAG, "onCreate: se_user_mobile_no --> " + se_user_mobile_no);

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);

        img_back.setOnClickListener(this);
        callGetListByEngCode(se_user_id);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String Search = edtSearch.getText().toString();

                Log.i(TAG, "onTextChanged: edtSearch -> " + Search);

                if (Search.equals("")) {
                    rv_job_failure_report_approval.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, failureReportRequestListByEngCodeResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edtSearch.getText().toString();

                rv_job_failure_report_approval.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, failureReportRequestListByEngCodeResponseList);
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

    private void filter(String search, List<FailureReportRequestListByEngCodeResponse.Data> failureReportRequestListByEngCodeResponseList) {

        List<FailureReportRequestListByEngCodeResponse.Data> filterlist = new ArrayList<>();
        try {
            for (FailureReportRequestListByEngCodeResponse.Data item : failureReportRequestListByEngCodeResponseList) {
                Log.i(TAG, "filter: item -> " + new Gson().toJson(item));
                if (item.getApp_status().contains(search) ||
                        item.getApp_status().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: JobID -> " + item.getApp_status().contains(search.toLowerCase()));
                    filterlist.add(item);
                }
                if (item.getJob_id().contains(search) ||
                        item.getJob_id().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: JobID -> " + item.getJob_id().contains(search.toLowerCase()));
                    filterlist.add(item);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "filter: error -> " + e.getMessage());
        }

        if (filterlist.isEmpty()) {
            //Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_job_failure_report_approval.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            jobListFailureReportApprovalAdapter.filterList(filterlist);
        }
    }

    private void callGetListByEngCode(String se_id) {
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            if (se_id != null && !se_id.isEmpty()) {
                getFetchDataJobId(se_id);
            } else {
                Toast.makeText(context, "Enter valid Engineer Id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getFetchDataJobId(String strSearch) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        FailureReportRequestListByEngCodeRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<FailureReportRequestListByEngCodeResponse> call = apiInterface.getListByEngCode(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<FailureReportRequestListByEngCodeResponse>() {
                @Override
                public void onResponse(@NonNull Call<FailureReportRequestListByEngCodeResponse> call, @NonNull Response<FailureReportRequestListByEngCodeResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: FailureReportRequestListByEngCodeResponse -> " + new Gson().toJson(response.body()));
                    failureReportRequestListByEngCodeResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                failureReportRequestListByEngCodeResponseList = response.body().getData();

                                if (failureReportRequestListByEngCodeResponseList.isEmpty()) {

                                    rv_job_failure_report_approval.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(failureReportRequestListByEngCodeResponseList);
                                }
                            }
                        } else {

                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(@NonNull Call<FailureReportRequestListByEngCodeResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    rv_job_failure_report_approval.setVisibility(View.GONE);
                    txt_no_records.setVisibility(View.VISIBLE);
                    txt_no_records.setText("Something Went Wrong.. Try Again Later");
//                edtSearch.setEnabled(false);
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    private void setView(List<FailureReportRequestListByEngCodeResponse.Data> failureReportRequestListByEngCodeResponseList) {
        rv_job_failure_report_approval.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_failure_report_approval.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_failure_report_approval.setItemAnimator(new DefaultItemAnimator());
        jobListFailureReportApprovalAdapter = new JobListFailureReportApprovalAdapter(getApplicationContext(), failureReportRequestListByEngCodeResponseList, this);
        rv_job_failure_report_approval.setAdapter(jobListFailureReportApprovalAdapter);
    }

    private FailureReportRequestListByEngCodeRequest jobListRequest(String strSearch) {

        FailureReportRequestListByEngCodeRequest job = new FailureReportRequestListByEngCodeRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setEng_code(strSearch);
            Log.i(TAG, "jobListRequest: FailureReportRequestListByEngCodeRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        FailureReportRequestListByEngCodeResponse.Data failureReportRequestListByEngCodeDateResponse = new FailureReportRequestListByEngCodeResponse.Data();
        failureReportRequestListByEngCodeDateResponse = failureReportRequestListByEngCodeResponseList.get(position);

        if (!failureReportRequestListByEngCodeDateResponse.getApp_status().equalsIgnoreCase("submitted")) {
            Intent intent = new Intent(context, FailureReportApprovalFormActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("failureReportRequestListByEngCodeDateResponse", failureReportRequestListByEngCodeDateResponse);
            context.startActivity(intent);
        } else {
            ErrorMsgDialog("Job already Submitted");
        }
        Log.i(TAG, "itemClickDataChangeListener: failureReportRequestListByEngCodeDateResponse --> " + new Gson().toJson(failureReportRequestListByEngCodeDateResponse));
    }

    private void ErrorMsgDialog(String strMsg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        if (nullPointerValidator(strMsg)) {
            txt_Message.setText(strMsg);
        }

        alertDialogBuilder.setView(mView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callGetListByEngCode(se_user_id);
    }
}