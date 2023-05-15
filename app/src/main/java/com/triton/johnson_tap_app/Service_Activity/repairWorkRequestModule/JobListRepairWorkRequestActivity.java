package com.triton.johnson_tap_app.Service_Activity.repairWorkRequestModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.safetyAuditModule.SafetyAuditJobActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.JobIdRequest;
import com.triton.johnson_tap_app.responsepojo.JobListRepairWorkRequestResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListRepairWorkRequestActivity extends AppCompatActivity implements OnItemClickDataChangeListener,
        View.OnClickListener {

    ImageView img_back;
    RecyclerView rv_job_repair_work_request;
    EditText edtSearch;
    TextView txt_no_records;
    RelativeLayout Job;
    JobListRepairWorkRequestAdapter jobListRepairWorkRequestAdapter;
    Context context;
    Button btn_search;
    String TAG = JobListRepairWorkRequestActivity.class.getSimpleName(), se_user_mobile_no = "", se_user_location = "",
            se_user_name = "", se_id = "", check_id = "", service_title = "", message = "", networkStatus = "";
    SharedPreferences sharedPreferences;
    List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_list_repair_work_request);

        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_repair_work_request = findViewById(R.id.rv_job_repair_work_request);
        Job = findViewById(R.id.rel_job);
        btn_search = findViewById(R.id.btn_search);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "");
        se_user_name = sharedPreferences.getString("user_name", "");
        service_title = sharedPreferences.getString("service_title", "");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Log.i(TAG, "onCreate: service_title --> " + service_title);
        Log.i(TAG, "onCreate: se_user_mobile_no --> " + se_user_mobile_no);

        img_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
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

    private void showErrorAlert(String message) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.remarks_popup, null);

        EditText edt_Remarks = mView.findViewById(R.id.edt_remarks);
        Button btn_Submit = mView.findViewById(R.id.btn_submit);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txt_Message = mView.findViewById(R.id.txt_message);
        btn_Submit.setText("OK");
        edt_Remarks.setVisibility(View.GONE);
        txt_Message.setVisibility(View.VISIBLE);

        mBuilder.setView(mView);
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        txt_Message.setText(message);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
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

                                if (jobListRepairWorkRequestResponseList.isEmpty()) {

                                    rv_job_repair_work_request.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
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
                    rv_job_repair_work_request.setVisibility(View.GONE);
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

    private void setView(List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList) {
        rv_job_repair_work_request.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_repair_work_request.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_repair_work_request.setItemAnimator(new DefaultItemAnimator());
        jobListRepairWorkRequestAdapter = new JobListRepairWorkRequestAdapter(jobListRepairWorkRequestResponseList, this);
        rv_job_repair_work_request.setAdapter(jobListRepairWorkRequestAdapter);
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

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        JobListRepairWorkRequestResponse.Data jobListRepairWorkRequestResponse = new JobListRepairWorkRequestResponse.Data();
        jobListRepairWorkRequestResponse = jobListRepairWorkRequestResponseList.get(position);

        Log.i(TAG, "itemClickDataChangeListener: se_user_location -> " + se_user_location + " getBRCODE -> " + jobListRepairWorkRequestResponse.getBRCODE());

        if (jobListRepairWorkRequestResponse.getBRCODE().equalsIgnoreCase(se_user_location)) {
            Intent intent = new Intent(context, RepairWorkRequestFormActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("jobListRepairWorkRequestResponse", jobListRepairWorkRequestResponse);
            context.startActivity(intent);
        } else {
            showErrorAlert("This Job is Not Belongs to your Branch");
        }

        Log.i(TAG, "itemClickDataChangeListener: jobListRepairWorkRequestResponse --> " + new Gson().toJson(jobListRepairWorkRequestResponse));
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
            case R.id.btn_search: {
                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    if (edtSearch.getText().toString().toUpperCase().trim() != null && !edtSearch.getText().toString().toUpperCase().trim().isEmpty()) {
                        getFetchDataJobId(edtSearch.getText().toString().toUpperCase().trim());
                    } else {
                        Toast.makeText(context, "Enter valid Job Id", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}