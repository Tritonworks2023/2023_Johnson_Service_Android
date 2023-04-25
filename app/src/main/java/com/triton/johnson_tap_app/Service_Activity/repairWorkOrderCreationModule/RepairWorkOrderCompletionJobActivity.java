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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationCompletionModule.RepairWorkApprovalRequestFormViewEngActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestApprovalRequestListRpEngRequest;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestApprovalRequestListRpEngResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkOrderCompletionJobActivity extends AppCompatActivity implements OnItemClickDataChangeListener, View.OnClickListener {

    ImageView img_back;
    RecyclerView rv_job_repair_work_order_completion_job;
    EditText edtSearch;
    TextView txt_no_records, txt_menu_name;
    RelativeLayout Job;
    RepairWorkOrderCompletionJobListAdapter repairWorkOrderCompletionJobListAdapter;
    Context context;
    String TAG = RepairWorkOrderCompletionJobActivity.class.getSimpleName(), se_user_mobile_no, se_user_name,
            se_user_id, check_id, service_title, message, networkStatus = "", str_title = "", se_user_location = "";
    SharedPreferences sharedPreferences;
    List<RepairWorkRequestApprovalRequestListRpEngResponse.Data> repairWorkRequestApprovalRequestListRpEngResponseList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_order_completion_job);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_repair_work_order_completion_job = findViewById(R.id.rv_job_repair_work_order_completion_job);
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
                    rv_job_repair_work_order_completion_job.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, repairWorkRequestApprovalRequestListRpEngResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edtSearch.getText().toString();

                rv_job_repair_work_order_completion_job.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, repairWorkRequestApprovalRequestListRpEngResponseList);
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

    private void filter(String search, List<RepairWorkRequestApprovalRequestListRpEngResponse.Data> repairWorkRequestMechResponseList) {

        List<RepairWorkRequestApprovalRequestListRpEngResponse.Data> filterlist = new ArrayList<>();
        try {
            for (RepairWorkRequestApprovalRequestListRpEngResponse.Data item : repairWorkRequestMechResponseList) {
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
            rv_job_repair_work_order_completion_job.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            repairWorkOrderCompletionJobListAdapter.filterList(filterlist);
        }
    }

    private void callGetListByEngCode(String se_user_id) {
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            if (se_user_id != null && !se_user_id.isEmpty()) {
                getRepairWorkRequestApprovalRequestListRpEng(se_user_id);
            } else {
                Toast.makeText(context, "Enter valid Engineer Id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getRepairWorkRequestApprovalRequestListRpEng(String se_user_id) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkRequestApprovalRequestListRpEngRequest jobIdRequest = jobListRequest(se_user_id);

        if (jobIdRequest != null) {

            Call<RepairWorkRequestApprovalRequestListRpEngResponse> call = apiInterface.getRepairWorkRequestApprovalRequestListRpEng(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getRepairWorkRequestApprovalRequestListRpEng: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkRequestApprovalRequestListRpEngResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkRequestApprovalRequestListRpEngResponse> call, @NonNull Response<RepairWorkRequestApprovalRequestListRpEngResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getRepairWorkRequestApprovalRequestListRpEng: onResponse: RepairWorkRequestApprovalRequestListRpEngResponse -> " + new Gson().toJson(response.body()));
                    repairWorkRequestApprovalRequestListRpEngResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkRequestApprovalRequestListRpEngResponseList = response.body().getData();

                                if (repairWorkRequestApprovalRequestListRpEngResponseList.isEmpty()) {

                                    rv_job_repair_work_order_completion_job.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(repairWorkRequestApprovalRequestListRpEngResponseList);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepairWorkRequestApprovalRequestListRpEngResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getRepairWorkRequestApprovalRequestListRpEng: onFailure: error --> " + t.getMessage());
                    rv_job_repair_work_order_completion_job.setVisibility(View.GONE);
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

    private void setView(List<RepairWorkRequestApprovalRequestListRpEngResponse.Data> repairWorkRequestApprovalRequestListRpEngResponseList) {
        rv_job_repair_work_order_completion_job.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_repair_work_order_completion_job.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_repair_work_order_completion_job.setItemAnimator(new DefaultItemAnimator());
        repairWorkOrderCompletionJobListAdapter = new RepairWorkOrderCompletionJobListAdapter(getApplicationContext(), repairWorkRequestApprovalRequestListRpEngResponseList, this);
        rv_job_repair_work_order_completion_job.setAdapter(repairWorkOrderCompletionJobListAdapter);
    }

    private RepairWorkRequestApprovalRequestListRpEngRequest jobListRequest(String strSearch) {

        RepairWorkRequestApprovalRequestListRpEngRequest job = new RepairWorkRequestApprovalRequestListRpEngRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setRepair_work_eng_id(strSearch);
            Log.i(TAG, "jobListRequest: RepairWorkRequestApprovalRequestListRpEngRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        RepairWorkRequestApprovalRequestListRpEngResponse.Data repairWorkRequestApprovalRequestListRpEngDataResponse = new RepairWorkRequestApprovalRequestListRpEngResponse.Data();
        repairWorkRequestApprovalRequestListRpEngDataResponse = repairWorkRequestApprovalRequestListRpEngResponseList.get(position);

        Intent intent = new Intent(context, RepairWorkApprovalRequestFormViewEngActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("repairWorkRequestApprovalRequestListRpEngDataResponse", repairWorkRequestApprovalRequestListRpEngDataResponse);
        context.startActivity(intent);

        Log.i(TAG, "itemClickDataChangeListener: repairWorkRequestApprovalRequestListRpEngDataResponse --> " + new Gson().toJson(repairWorkRequestApprovalRequestListRpEngDataResponse));
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