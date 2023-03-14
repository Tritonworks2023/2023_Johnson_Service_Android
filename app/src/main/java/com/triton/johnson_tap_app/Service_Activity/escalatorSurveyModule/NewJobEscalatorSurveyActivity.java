package com.triton.johnson_tap_app.Service_Activity.escalatorSurveyModule;

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
import com.triton.johnson_tap_app.responsepojo.NewJobListEscalatorSurveyResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewJobEscalatorSurveyActivity extends AppCompatActivity implements OnItemClickDataChangeListener, View.OnClickListener {

    ImageView img_back, img_clearsearch;
    RecyclerView rv_new_job_escalator_survey;
    EditText edtSearch;
    TextView txt_no_records, txt_menu_name;
    RelativeLayout Job;
    NewJobListEscalatorSurveyAdapter newJobListEscalatorSurveyAdapter;
    Context context;
    Button btn_search;
    String TAG = NewJobEscalatorSurveyActivity.class.getSimpleName(), networkStatus = "";
    List<NewJobListEscalatorSurveyResponse.Data> newJobListEscalatorSurveyDataResponseList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_job_escalator_survey);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        img_clearsearch = findViewById(R.id.img_clearsearch);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_new_job_escalator_survey = findViewById(R.id.rv_new_job_escalator_survey);
        Job = findViewById(R.id.rel_job);
        txt_menu_name = findViewById(R.id.txt_menu_name);
        btn_search = findViewById(R.id.btn_search);

        img_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        /*edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String Search = edtSearch.getText().toString();
                if (Search.equals("")) {
                    rv_new_job_escalator_survey.setVisibility(View.VISIBLE);
                    img_clearsearch.setVisibility(View.INVISIBLE);
                } else {
                    filter(Search);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String Search = edtSearch.getText().toString();
                rv_new_job_escalator_survey.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);
                filter(Search);
            }
        });*/
    }

    /*private void filter(String search) {
        List<NewJobListEscalatorSurveyResponse.Data> filterlist = new ArrayList<>();

        try {
            for (NewJobListEscalatorSurveyResponse.Data item : breedTypedataBeanList) {
                if (item.getJOBNO().toLowerCase().contains(search.toLowerCase()) ||
                        item.getCUST_NAME().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: " + item.getJOBNO().toLowerCase().contains(search.toLowerCase()));
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
            newJobListEscalatorSurveyAdapter.filterrList(filterlist);
        }
    }*/

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

            Call<NewJobListEscalatorSurveyResponse> call = apiInterface.getEscalatorFetchDataJobId(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<NewJobListEscalatorSurveyResponse>() {
                @SuppressLint("LogNotTimber")
                @Override
                public void onResponse(@NonNull Call<NewJobListEscalatorSurveyResponse> call, @NonNull Response<NewJobListEscalatorSurveyResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: NewJobListEscalatorSurveyResponse -> " + new Gson().toJson(response.body()));
                    newJobListEscalatorSurveyDataResponseList = new ArrayList<>();
                    if (response.body() != null) {

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                newJobListEscalatorSurveyDataResponseList = response.body().getData();

                                if (newJobListEscalatorSurveyDataResponseList.isEmpty()) {

                                    rv_new_job_escalator_survey.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(newJobListEscalatorSurveyDataResponseList);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewJobListEscalatorSurveyResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    rv_new_job_escalator_survey.setVisibility(View.GONE);
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

    private void setView(List<NewJobListEscalatorSurveyResponse.Data> newJobListEscalatorSurveyDataResponseList) {
        rv_new_job_escalator_survey.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_new_job_escalator_survey.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_new_job_escalator_survey.setItemAnimator(new DefaultItemAnimator());
        newJobListEscalatorSurveyAdapter = new NewJobListEscalatorSurveyAdapter(newJobListEscalatorSurveyDataResponseList, this);
        rv_new_job_escalator_survey.setAdapter(newJobListEscalatorSurveyAdapter);
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
        NewJobListEscalatorSurveyResponse.Data newJobListEscalatorSurveyDataResponse = new NewJobListEscalatorSurveyResponse.Data();
        newJobListEscalatorSurveyDataResponse = newJobListEscalatorSurveyDataResponseList.get(position);

        Intent intent = new Intent(context, EscalatorSurveyFormActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("newJobListEscalatorSurveyDataResponse", newJobListEscalatorSurveyDataResponse);
        // n_act.putExtra("service_title", s);
        context.startActivity(intent);

        Log.i(TAG, "itemClickDataChangeListener: newJobListEscalatorSurveyDataResponse --> " + new Gson().toJson(newJobListEscalatorSurveyDataResponse));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
