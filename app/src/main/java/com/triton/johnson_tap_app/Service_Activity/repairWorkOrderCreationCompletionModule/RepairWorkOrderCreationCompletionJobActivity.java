package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationCompletionModule;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestApprovalRequestListRpMechRequest;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestApprovalRequestListRpMechResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkOrderCreationCompletionJobActivity extends AppCompatActivity implements OnItemClickDataChangeListener, View.OnClickListener {

    ImageView img_back;
    RecyclerView rv_job_repair_work_order_creation_completion;
    EditText edtSearch;
    TextView txt_no_records;
    RelativeLayout Job;
    RepairWorkOrderCreationCompletionJobListAdapter repairWorkOrderCreationCompletionJobListAdapter;
    Context context;
    String TAG = RepairWorkOrderCreationCompletionJobActivity.class.getSimpleName(), se_user_mobile_no, se_user_name,
            se_user_id, check_id, service_title, message, networkStatus = "", se_user_location = "";
    SharedPreferences sharedPreferences;
    List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> repairWorkRequestApprovalRequestListRpMechResponseList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_order_creation_completion_job);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_repair_work_order_creation_completion = findViewById(R.id.rv_job_repair_work_order_creation_completion);
        Job = findViewById(R.id.rel_job);

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

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("str_title")) {
                str_title = extras.getString("str_title");
            }
        }
        Log.i(TAG, "onCreate: str_title -> " + str_title);
        txt_menu_name.setText(str_title);*/

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            NoInternetDialog();

        } else {
            callGetListByEngCode(se_user_id);
        }

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
                    rv_job_repair_work_order_creation_completion.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, repairWorkRequestApprovalRequestListRpMechResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edtSearch.getText().toString();

                rv_job_repair_work_order_creation_completion.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, repairWorkRequestApprovalRequestListRpMechResponseList);
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

    private void filter(String search, List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> repairWorkRequestApprovalRequestListRpMechResponseList) {

        List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> filterlist = new ArrayList<>();
        try {
            for (RepairWorkRequestApprovalRequestListRpMechResponse.Data item : repairWorkRequestApprovalRequestListRpMechResponseList) {
                Log.i(TAG, "filter: item -> " + new Gson().toJson(item));
                if (item.getCustomer_name().contains(search) ||
                        item.getCustomer_name().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: JobID -> " + item.getCustomer_name().contains(search.toLowerCase()));
                    filterlist.add(item);
                }
                if (item.getJob_no().contains(search) ||
                        item.getJob_no().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: JobID -> " + item.getJob_no().contains(search.toLowerCase()));
                    filterlist.add(item);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "filter: error -> " + e.getMessage());
        }

        if (filterlist.isEmpty()) {
            //Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_job_repair_work_order_creation_completion.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            repairWorkOrderCreationCompletionJobListAdapter.filterList(filterlist);
        }
    }

    private void callGetListByEngCode(String se_user_id) {
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            if (se_user_id != null && !se_user_id.isEmpty()) {
                getRepairWorkRequestApprovalRequestListRpMech(se_user_id);
            } else {
                Toast.makeText(context, "Enter valid Engineer Id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getRepairWorkRequestApprovalRequestListRpMech(String se_user_id) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkRequestApprovalRequestListRpMechRequest jobIdRequest = jobListRequest(se_user_id);

        if (jobIdRequest != null) {

            Call<RepairWorkRequestApprovalRequestListRpMechResponse> call = apiInterface.getRepairWorkRequestApprovalRequestListRpMech(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getRepairWorkRequestApprovalRequestListRpMech: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkRequestApprovalRequestListRpMechResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkRequestApprovalRequestListRpMechResponse> call, @NonNull Response<RepairWorkRequestApprovalRequestListRpMechResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getRepairWorkRequestApprovalRequestListRpMech: onResponse: FailureReportRequestListByMechCodeResponse -> " + new Gson().toJson(response.body()));
                    repairWorkRequestApprovalRequestListRpMechResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkRequestApprovalRequestListRpMechResponseList = response.body().getData();

                                if (repairWorkRequestApprovalRequestListRpMechResponseList.isEmpty()) {

                                    rv_job_repair_work_order_creation_completion.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(repairWorkRequestApprovalRequestListRpMechResponseList);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepairWorkRequestApprovalRequestListRpMechResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getRepairWorkRequestApprovalRequestListRpMech: onFailure: error --> " + t.getMessage());
                    rv_job_repair_work_order_creation_completion.setVisibility(View.GONE);
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

    private void setView(List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> repairWorkRequestApprovalRequestListRpMechResponseList) {
        rv_job_repair_work_order_creation_completion.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_repair_work_order_creation_completion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_repair_work_order_creation_completion.setItemAnimator(new DefaultItemAnimator());
        repairWorkOrderCreationCompletionJobListAdapter = new RepairWorkOrderCreationCompletionJobListAdapter(getApplicationContext(), repairWorkRequestApprovalRequestListRpMechResponseList, this);
        rv_job_repair_work_order_creation_completion.setAdapter(repairWorkOrderCreationCompletionJobListAdapter);
    }

    private RepairWorkRequestApprovalRequestListRpMechRequest jobListRequest(String strSearch) {

        RepairWorkRequestApprovalRequestListRpMechRequest job = new RepairWorkRequestApprovalRequestListRpMechRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setRepair_work_mech_id(strSearch);
            Log.i(TAG, "jobListRequest: RepairWorkRequestApprovalRequestListRpMechRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        RepairWorkRequestApprovalRequestListRpMechResponse.Data repairWorkRequestApprovalRequestListRpMechDataResponse = new RepairWorkRequestApprovalRequestListRpMechResponse.Data();
        repairWorkRequestApprovalRequestListRpMechDataResponse = repairWorkRequestApprovalRequestListRpMechResponseList.get(position);

        Intent intent = new Intent(context, RepairWorkApprovalRequestFormViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("repairWorkRequestApprovalRequestListRpMechDataResponse", repairWorkRequestApprovalRequestListRpMechDataResponse);
        context.startActivity(intent);

        Log.i(TAG, "itemClickDataChangeListener: repairWorkRequestApprovalRequestListRpMechDataResponse --> " + new Gson().toJson(repairWorkRequestApprovalRequestListRpMechDataResponse));
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