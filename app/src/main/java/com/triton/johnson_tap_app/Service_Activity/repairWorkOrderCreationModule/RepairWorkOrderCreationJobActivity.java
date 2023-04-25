package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationModule;

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
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestMechRequest;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkOrderCreationJobActivity extends AppCompatActivity implements OnItemClickDataChangeListener, View.OnClickListener {

    ImageView img_back;
    RecyclerView rv_job_failure_report_pending_request;
    EditText edtSearch;
    TextView txt_no_records, txt_menu_name;
    RelativeLayout Job;
    RepairWorkOrderCreationJobListAdapter repairWorkOrderCreationJobListAdapter;
    Context context;
    String TAG = RepairWorkOrderCreationJobActivity.class.getSimpleName(), se_user_mobile_no, se_user_name,
            se_user_id, check_id, service_title, message, networkStatus = "", str_title = "", se_user_location = "";
    SharedPreferences sharedPreferences;
    List<RepairWorkRequestMechResponse.Data> repairWorkRequestMechResponseList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_order_creation_job);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_failure_report_pending_request = findViewById(R.id.rv_job_failure_report_pending_request);
        Job = findViewById(R.id.rel_job);
        txt_menu_name = findViewById(R.id.txt_menu_name);

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "onCreate: service_title --> " + service_title);
        Log.i(TAG, "onCreate: se_user_mobile_no --> " + se_user_mobile_no);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("str_title")) {
                str_title = extras.getString("str_title");
            }
        }
        Log.i(TAG, "onCreate: str_title -> " + str_title);
        txt_menu_name.setText(str_title);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            NoInternetDialog();

        } else {
            callGetListByEngCode(se_user_id);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_back.setOnClickListener(this);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String Search = edtSearch.getText().toString();

                Log.i(TAG, "onTextChanged: edtSearch -> " + Search);

                if (Search.equals("")) {
                    rv_job_failure_report_pending_request.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, repairWorkRequestMechResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edtSearch.getText().toString();

                rv_job_failure_report_pending_request.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, repairWorkRequestMechResponseList);
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

    private void filter(String search, List<RepairWorkRequestMechResponse.Data> repairWorkRequestMechResponseList) {

        List<RepairWorkRequestMechResponse.Data> filterlist = new ArrayList<>();
        try {
            for (RepairWorkRequestMechResponse.Data item : repairWorkRequestMechResponseList) {
                Log.i(TAG, "filter: item -> " + new Gson().toJson(item));
                if (item.getSite_name().contains(search) ||
                        item.getSite_name().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: JobID -> " + item.getSite_name().contains(search.toLowerCase()));
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
            rv_job_failure_report_pending_request.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            repairWorkOrderCreationJobListAdapter.filterList(filterlist);
        }
    }

    private void callGetListByEngCode(String se_user_id) {
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            if (se_user_id != null && !se_user_id.isEmpty()) {
                getFailureReportListByMechCode(se_user_id);
            } else {
                Toast.makeText(context, "Enter valid Engineer Id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getFailureReportListByMechCode(String se_user_id) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkRequestMechRequest jobIdRequest = jobListRequest(se_user_id);

        if (jobIdRequest != null) {

            Call<RepairWorkRequestMechResponse> call = apiInterface.getRepairWorkRequestMechRequest(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFailureReportListByMechCode: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkRequestMechResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkRequestMechResponse> call, @NonNull Response<RepairWorkRequestMechResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFailureReportListByMechCode: onResponse: FailureReportRequestListByMechCodeResponse -> " + new Gson().toJson(response.body()));
                    repairWorkRequestMechResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkRequestMechResponseList = response.body().getData();

                                if (repairWorkRequestMechResponseList.isEmpty()) {

                                    rv_job_failure_report_pending_request.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(repairWorkRequestMechResponseList);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepairWorkRequestMechResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFailureReportListByMechCode: onFailure: error --> " + t.getMessage());
                    rv_job_failure_report_pending_request.setVisibility(View.GONE);
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

    private void setView(List<RepairWorkRequestMechResponse.Data> repairWorkRequestMechResponseList) {
        rv_job_failure_report_pending_request.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_failure_report_pending_request.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_failure_report_pending_request.setItemAnimator(new DefaultItemAnimator());
        repairWorkOrderCreationJobListAdapter = new RepairWorkOrderCreationJobListAdapter(getApplicationContext(), repairWorkRequestMechResponseList, this);
        rv_job_failure_report_pending_request.setAdapter(repairWorkOrderCreationJobListAdapter);
    }

    private RepairWorkRequestMechRequest jobListRequest(String strSearch) {

        RepairWorkRequestMechRequest job = new RepairWorkRequestMechRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setRep_eng_code(strSearch);
            Log.i(TAG, "jobListRequest: RepairWorkRequestMechRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        RepairWorkRequestMechResponse.Data repairWorkRequestMechDataResponse = new RepairWorkRequestMechResponse.Data();
        repairWorkRequestMechDataResponse = repairWorkRequestMechResponseList.get(position);

        Intent intent = new Intent(context, RepairWorkApprovalRequestFormActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("repairWorkRequestMechDataResponse", repairWorkRequestMechDataResponse);
        context.startActivity(intent);

        Log.i(TAG, "itemClickDataChangeListener: repairWorkRequestMechDataResponse --> " + new Gson().toJson(repairWorkRequestMechDataResponse));
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callGetListByEngCode(se_user_id);
    }
}