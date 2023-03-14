package com.triton.johnson_tap_app.Service_Activity.repairWorkApprovalRequestModule;

import static android.content.ContentValues.TAG;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.PetBreedTypeSelectListener;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.JobListRequest;
import com.triton.johnson_tap_app.responsepojo.NewJobListRepairWorkApprovalResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

public class NewJobRepairWorkApprovalRequestActivity extends AppCompatActivity implements PetBreedTypeSelectListener, OnItemClickDataChangeListener {

    ImageView img_back, img_clearsearch;
    RecyclerView rv_new_job_escalator_survey;
    EditText edtsearch;
    TextView txt_no_records, txt_menu_name;
    RelativeLayout Job;
    NewJobListRepairWorkApprovalRequestAdapter newJobListRepairWorkApprovalRequestAdapter;
    Context context;
    Button btn_search;
    String se_user_mobile_no, se_user_name, se_id, check_id, service_title, message, networkStatus = "";
    SharedPreferences sharedPreferences;
    List<NewJobListRepairWorkApprovalResponse.DataBean> breedTypedataBeanList = new ArrayList<>();
    private String PetBreedType = "";

//    ArrayList<String> arli_jobid = new ArrayList<String>();
//    ArrayList<String> arli_custname = new ArrayList<String>();
//    ArrayList<String> arli_auditdate = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_repair_work_approvel_request);
        context = this;

        img_back = (ImageView) findViewById(R.id.img_back);
        edtsearch = (EditText) findViewById(R.id.edt_search);
        img_clearsearch = (ImageView) findViewById(R.id.img_clearsearch);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_new_job_escalator_survey = findViewById(R.id.rv_new_job_escalator_survey);
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

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  service_title = extras.getString("service_title");
            status = extras.getString("status");
            //   Log.e("Name",":" + service_title);
            Log.i(TAG, "onCreate: status --> " + status);
        }
        if (status.equals("new")) {
            txt_menu_name.setText("Escalator Survey New Job List");
        } else {
            txt_menu_name.setText("Escalator Survey Pause Job List");
        }*/
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            NoInternetDialog();

        } else {

            /*if (status.equals("new")) {

                newJobList();
            } else {

                Job.setVisibility(View.GONE);
                edtsearch.setVisibility(View.GONE);
                pausedJobResponseCall();
            }*/
        }

//        arli_jobid.add("L-44291");
//        arli_jobid.add("L-44292");
//        arli_jobid.add("L-44293");
//        arli_jobid.add("L-44294");
//        arli_jobid.add("L-44295");
//
//        arli_custname.add("John");
//        arli_custname.add("Sam");
//        arli_custname.add("Devi");
//        arli_custname.add("Anisha");
//        arli_custname.add("Nishanth");
//
//        arli_auditdate.add("12-09-2022");
//        arli_auditdate.add("15-09-2022");
//        arli_auditdate.add("17-09-2022");
//        arli_auditdate.add("17-09-2022");
//        arli_auditdate.add("10-09-2022");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent send = new Intent(context, SiteAudit_Activity.class);
//                //send.putExtra("service_title",service_title);
//                send.putExtra("status", status);
//                startActivity(send);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breedTypedataBeanList.add(new NewJobListRepairWorkApprovalResponse.DataBean("T-I123",
                        "Shrinika Apartment", "Escalator", "Rajakilpakkam",
                        "Automatic", "12-01-2023", "20-01-2023", "25-01-2023"));

                setBreedTypeView(breedTypedataBeanList);
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String Search = edtsearch.getText().toString();
                if (Search.equals("")) {
                    rv_new_job_escalator_survey.setVisibility(View.VISIBLE);
                    img_clearsearch.setVisibility(View.INVISIBLE);
                } else {
                    filter(Search);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String Search = edtsearch.getText().toString();
                rv_new_job_escalator_survey.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);
                filter(Search);
            }
        });
    }

    private void filter(String search) {
        List<NewJobListRepairWorkApprovalResponse.DataBean> filterlist = new ArrayList<>();

        try {
            for (NewJobListRepairWorkApprovalResponse.DataBean item : breedTypedataBeanList) {
                if (item.getJob_no().toLowerCase().contains(search.toLowerCase()) ||
                        item.getBuilding_name().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: " + item.getJob_no().toLowerCase().contains(search.toLowerCase()));
                    filterlist.add(item);
                }
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "filter: error --> " + e.getMessage());
        }

        if (filterlist.isEmpty()) {
            // Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_new_job_escalator_survey.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            newJobListRepairWorkApprovalRequestAdapter.filterrList(filterlist);
        }
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

    /*private void newJobList() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<JobListResponse> call = apiInterface.NewJobAuditListCall(RestUtils.getContentType(), joblistRequest());

        Log.i(TAG, "newJobList: URL --> " + call.request().url().toString());
        call.enqueue(new Callback<JobListResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<JobListResponse> call, @NonNull Response<JobListResponse> response) {
                Log.i(TAG, "newJobList: onResponse: JobListResponse --> " + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    message = response.body().getMessage();

                    Log.i(TAG, "newJobList: onResponse: message --> " + message);

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            breedTypedataBeanList = response.body().getData();

                            if (breedTypedataBeanList.size() == 0) {

                                rv_new_job_escalator_survey.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                txt_no_records.setText("No Records Found");
//                                edtsearch.setEnabled(false);
                            }
                            setBreedTypeView(breedTypedataBeanList);

                            Log.i(TAG, "newJobList: onResponse: breedTypedataBeanList --> " + String.valueOf(breedTypedataBeanList));
                        }
                    } else if (400 == response.body().getCode()) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("There is already a user registered with this email id. Please add new email id")) {

                            rv_new_job_escalator_survey.setVisibility(View.GONE);
                            txt_no_records.setVisibility(View.VISIBLE);
                            txt_no_records.setText("Error 404 Found");
//                            edtsearch.setEnabled(false);
                        }
                    } else {

                        rv_new_job_escalator_survey.setVisibility(View.GONE);
                        txt_no_records.setVisibility(View.VISIBLE);
                        txt_no_records.setText("Error 404 Found");
//                        edtsearch.setEnabled(false);
                        Toasty.warning(getApplicationContext(), "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JobListResponse> call, @NonNull Throwable t) {

                Log.e(TAG, "newJobList: onFailure: error --> " + t.getMessage());
                rv_new_job_escalator_survey.setVisibility(View.GONE);
                txt_no_records.setVisibility(View.VISIBLE);
                txt_no_records.setText("Something Went Wrong.. Try Again Later");
//                edtsearch.setEnabled(false);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void setBreedTypeView(List<NewJobListRepairWorkApprovalResponse.DataBean> breedTypedataBeanList) {
        rv_new_job_escalator_survey.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_new_job_escalator_survey.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_new_job_escalator_survey.setItemAnimator(new DefaultItemAnimator());
        newJobListRepairWorkApprovalRequestAdapter = new NewJobListRepairWorkApprovalRequestAdapter(getApplicationContext(), breedTypedataBeanList, this);
        rv_new_job_escalator_survey.setAdapter(newJobListRepairWorkApprovalRequestAdapter);
    }

    private JobListRequest joblistRequest() {

        JobListRequest job = new JobListRequest();
        job.setUser_mobile_no(se_user_mobile_no);
        job.setService_name(service_title);

        Log.i(TAG, "joblistRequest: JobListRequest --> " + new Gson().toJson(job));
        return job;
    }

    public void petBreedTypeSelectListener(String petbreedtitle, String petbreedid) {
        PetBreedType = petbreedtitle;
    }

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        NewJobListRepairWorkApprovalResponse.DataBean breedTypedataBean = new NewJobListRepairWorkApprovalResponse.DataBean();
        breedTypedataBean = breedTypedataBeanList.get(position);

        Intent intent = new Intent(context, RepairWorkApprovalRequestFormActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // n_act.putExtra("service_title", s);
        context.startActivity(intent);

        Log.i(TAG, "itemClickDataChangeListener: breedTypedataBeanList --> " + new Gson().toJson(breedTypedataBean));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}