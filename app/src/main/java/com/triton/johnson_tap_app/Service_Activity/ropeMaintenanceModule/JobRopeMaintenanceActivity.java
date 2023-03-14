package com.triton.johnson_tap_app.Service_Activity.ropeMaintenanceModule;

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
import com.triton.johnson_tap_app.requestpojo.JobIdRequest;
import com.triton.johnson_tap_app.responsepojo.JobListRopeMaintenanceResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRopeMaintenanceActivity extends AppCompatActivity implements OnItemClickDataChangeListener, View.OnClickListener {

    ImageView img_back, img_clearsearch;
    RecyclerView rv_job_rope_maintenance;
    EditText edtSearch;
    TextView txt_no_records, txt_menu_name;
    RelativeLayout Job;
    JobListRopeMaintenanceAdapter jobListRopeMaintenanceAdapter;
    Context context;
    Button btn_search;
    String TAG = JobRopeMaintenanceActivity.class.getSimpleName(), se_user_mobile_no, se_user_name,
            se_id, service_title, message, networkStatus = "";
    SharedPreferences sharedPreferences;
    List<JobListRopeMaintenanceResponse.Data> jobListRopeMaintenanceDataResponse = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_rope_maintenance);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        img_clearsearch = findViewById(R.id.img_clearsearch);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_rope_maintenance = findViewById(R.id.rv_job_rope_maintenance);
        Job = findViewById(R.id.rel_job);
        txt_menu_name = findViewById(R.id.txt_menu_name);
        btn_search = findViewById(R.id.btn_search);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        service_title = sharedPreferences.getString("service_title", "Services");

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

    private void getFetchDataJobId(String strSearch) {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        JobIdRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<JobListRopeMaintenanceResponse> call = apiInterface.getRopeMaintenanceFetchDataJobId(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<JobListRopeMaintenanceResponse>() {
                @Override
                public void onResponse(@NonNull Call<JobListRopeMaintenanceResponse> call, @NonNull Response<JobListRopeMaintenanceResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: JobListRopeMaintenanceResponse -> " + new Gson().toJson(response.body()));
                    jobListRopeMaintenanceDataResponse = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                jobListRopeMaintenanceDataResponse = response.body().getData();

                                if (jobListRopeMaintenanceDataResponse.isEmpty()) {

                                    rv_job_rope_maintenance.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(jobListRopeMaintenanceDataResponse);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JobListRopeMaintenanceResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    rv_job_rope_maintenance.setVisibility(View.GONE);
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

    private void setView(List<JobListRopeMaintenanceResponse.Data> breedTypedataBeanList) {
        rv_job_rope_maintenance.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_rope_maintenance.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_rope_maintenance.setItemAnimator(new DefaultItemAnimator());
        jobListRopeMaintenanceAdapter = new JobListRopeMaintenanceAdapter(getApplicationContext(), breedTypedataBeanList, this);
        rv_job_rope_maintenance.setAdapter(jobListRopeMaintenanceAdapter);
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
        JobListRopeMaintenanceResponse.Data jobListRopeMaintenanceDataResponse = new JobListRopeMaintenanceResponse.Data();
        jobListRopeMaintenanceDataResponse = this.jobListRopeMaintenanceDataResponse.get(position);

        Intent intent = new Intent(context, RopeMaintenanceFormActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("jobListRopeMaintenanceDataResponse", jobListRopeMaintenanceDataResponse);
        context.startActivity(intent);

        Log.i(TAG, "itemClickDataChangeListener: jobListRopeMaintenanceDataResponse --> " + new Gson().toJson(jobListRopeMaintenanceDataResponse));
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