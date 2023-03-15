package com.triton.johnson_tap_app.Service_Activity.serviceVisibilityModule;

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
import com.triton.johnson_tap_app.requestpojo.ImieBrcdRequest;
import com.triton.johnson_tap_app.requestpojo.JobIdRequest;
import com.triton.johnson_tap_app.responsepojo.EmployeeDetailsListResponse;
import com.triton.johnson_tap_app.responsepojo.NewJobListServiceVisibilityResponse;
import com.triton.johnson_tap_app.responsepojo.ServiceTypeLocalListResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceVisibilityJobActivity extends AppCompatActivity implements OnItemClickDataChangeListener, View.OnClickListener {

    boolean init = false;
    private ImageView img_back, img_clearsearch;
    private RecyclerView rv_job_service_visibility, rv_employee_service_visibility;
    private EditText edtSearch;
    private TextView txt_no_records, txt_menu_name, txt_cat_name;
    private RelativeLayout rel_job;
    private ServiceVisibilityJobListAdapter serviceVisibilityJobListAdapter;
    private EmployeeDetailListAdapter employeeDetailListAdapter;
    private ServiceTypeListAdapter serviceTypeListAdapter;
    private Context context;
    private Button btn_search, btn_change_cat;
    private String networkStatus = "", se_user_location = "";
    private ArrayList<NewJobListServiceVisibilityResponse.Data> newJobListServiceVisibilityDataListResponse = new ArrayList<>();
    private ArrayList<ServiceTypeLocalListResponse> serviceTypeLocalListResponse = new ArrayList<>();
    private ArrayList<EmployeeDetailsListResponse.Data> employeeDetailsDataListResponse = new ArrayList<>();
    private String strSelectedCatName = "", strSelectedCatCode = "";
    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private String TAG = ServiceVisibilityJobActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_service_visibility_job);
        context = this;

        img_back = findViewById(R.id.img_back);
        edtSearch = findViewById(R.id.edt_search);
        img_clearsearch = findViewById(R.id.img_clearsearch);
        txt_no_records = findViewById(R.id.txt_no_records);
        rv_job_service_visibility = findViewById(R.id.rv_job_service_visibility);
        rv_employee_service_visibility = findViewById(R.id.rv_employee_service_visibility);
        rel_job = findViewById(R.id.rel_job);
        txt_menu_name = findViewById(R.id.txt_menu_name);
        txt_cat_name = findViewById(R.id.txt_cat_name);
        btn_search = findViewById(R.id.btn_search);
        btn_change_cat = findViewById(R.id.btn_change_cat);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_location = sharedPreferences.getString("user_location", "default value");

        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("SOCAWA", "SOCIETY AWARENESS"));
        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("RESTRA", "RESCUE TRAINING"));
        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("SCHSAF", "SCHOOL SAFETY"));
        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("CUSMET", "CUSTOMER MEETING"));
        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("CUSCOM", "CUSTOMER COMMENDATION"));
        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("5S", "5 'S' SAFETY AND LEAN"));
        serviceTypeLocalListResponse.add(new ServiceTypeLocalListResponse("SMART", "SMART PHONE CONDITION"));

        Log.i(TAG, "onCreate: serviceTypeLocalListResponse --> " + new Gson().toJson(serviceTypeLocalListResponse));

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus --> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            NoInternetDialog();

        } else {

        }

        img_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_change_cat.setOnClickListener(this);

        showJobTypeListDialog();
    }

    private void showJobTypeListDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ServiceVisibilityJobActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.popup_job_type, null);

        RecyclerView rv_service_job_type = mView.findViewById(R.id.rv_service_job_type);
        Button btn_back = mView.findViewById(R.id.btn_back);

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        rv_service_job_type.setLayoutManager(new LinearLayoutManager(ServiceVisibilityJobActivity.this));
        rv_service_job_type.setItemAnimator(new DefaultItemAnimator());

        serviceTypeListAdapter = new ServiceTypeListAdapter(ServiceVisibilityJobActivity.this, serviceTypeLocalListResponse, new OnItemClickDataChangeListener() {
            @Override
            public void itemClickDataChangeListener(int position, String strParam, String strData) {

                strSelectedCatName = serviceTypeLocalListResponse.get(position).getS_name();
                strSelectedCatCode = serviceTypeLocalListResponse.get(position).getS_code();

                txt_cat_name.setText(strSelectedCatName);

                if (strSelectedCatCode.equalsIgnoreCase("smart")) {
                    edtSearch.setHint("Enter and Search Employee ID");
                    rel_job.setVisibility(View.GONE);
                    rv_job_service_visibility.setVisibility(View.GONE);
                    rv_employee_service_visibility.setVisibility(View.VISIBLE);
                    getImieBrcd();
                } else if (strSelectedCatCode.equalsIgnoreCase("SCHSAF")) {

                    Intent intent = new Intent(context, SchSafServiceVisibilityFormActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    /*intent.putExtra("newJobListServiceVisibilityDataResponse", newJobListServiceVisibilityDataResponse);*/
                    intent.putExtra("cat_code", strSelectedCatCode);
                    intent.putExtra("cat_name", strSelectedCatName);
                    context.startActivity(intent);
                } else {
                    edtSearch.setHint("Enter and Search Job Id");
                    rel_job.setVisibility(View.VISIBLE);
                    rv_job_service_visibility.setVisibility(View.VISIBLE);
                    rv_employee_service_visibility.setVisibility(View.GONE);
                }
                edtSearch.setText("");
                mDialog.dismiss();
            }
        });

        rv_service_job_type.setAdapter(serviceTypeListAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (!init) {
                    onBackPressed();
                }
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

    private void getFetchDataJobId(String strSearch) {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        JobIdRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<NewJobListServiceVisibilityResponse> call = apiInterface.getServiceVisibilityFetchDataJobId(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<NewJobListServiceVisibilityResponse>() {
                @SuppressLint("LogNotTimber")
                @Override
                public void onResponse(@NonNull Call<NewJobListServiceVisibilityResponse> call, @NonNull Response<NewJobListServiceVisibilityResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: NewJobListEscalatorSurveyResponse -> " + new Gson().toJson(response.body()));
                    newJobListServiceVisibilityDataListResponse = new ArrayList<>();
                    if (response.body() != null) {

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                newJobListServiceVisibilityDataListResponse = response.body().getData();

                                if (newJobListServiceVisibilityDataListResponse.isEmpty()) {

                                    rv_job_service_visibility.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                                } else {
                                    setView(newJobListServiceVisibilityDataListResponse);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewJobListServiceVisibilityResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    rv_job_service_visibility.setVisibility(View.GONE);
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

    private void getImieBrcd() {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        ImieBrcdRequest imieBrcdRequest = new ImieBrcdRequest();
        imieBrcdRequest.setBrcode(se_user_location);
        Log.i(TAG, "getImieBrcd: ImieBrcdRequest -> " + new Gson().toJson(imieBrcdRequest));
        Call<EmployeeDetailsListResponse> call = apiInterface.getImieBrcd(RestUtils.getContentType(), imieBrcdRequest);
        Log.i(TAG, "getImieBrcd: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<EmployeeDetailsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeDetailsListResponse> call, @NonNull Response<EmployeeDetailsListResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getImieBrcd: onResponse: EmployeeDetailsListResponse -> " + new Gson().toJson(response.body()));

                employeeDetailsDataListResponse = new ArrayList<>();
                if (response.body() != null) {

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            employeeDetailsDataListResponse = response.body().getData();

                            if (employeeDetailsDataListResponse.isEmpty()) {

                                rv_employee_service_visibility.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                txt_no_records.setText("No Records Found");
//                                edtSearch.setEnabled(false);
                            } else {
                                setEmployeeView(employeeDetailsDataListResponse);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeeDetailsListResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getImieBrcd: onFailure: error --> " + t.getMessage());
                rv_employee_service_visibility.setVisibility(View.GONE);
                txt_no_records.setVisibility(View.VISIBLE);
                txt_no_records.setText("Something Went Wrong.. Try Again Later");
//                edtSearch.setEnabled(false);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView(ArrayList<NewJobListServiceVisibilityResponse.Data> newJobListServiceVisibilityDataListResponse) {
        rv_job_service_visibility.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_job_service_visibility.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_job_service_visibility.setItemAnimator(new DefaultItemAnimator());
        serviceVisibilityJobListAdapter = new ServiceVisibilityJobListAdapter(newJobListServiceVisibilityDataListResponse, this);
        rv_job_service_visibility.setAdapter(serviceVisibilityJobListAdapter);
    }

    private void setEmployeeView(ArrayList<EmployeeDetailsListResponse.Data> employeeDetailsDataListResponse) {
        rv_employee_service_visibility.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_employee_service_visibility.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_employee_service_visibility.setItemAnimator(new DefaultItemAnimator());
        employeeDetailListAdapter = new EmployeeDetailListAdapter(employeeDetailsDataListResponse, this);
        rv_employee_service_visibility.setAdapter(employeeDetailListAdapter);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.btn_change_cat: {
                init = true;
                showJobTypeListDialog();
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

    @Override
    public void itemClickDataChangeListener(int position, String strParam, String strData) {
        Intent intent;
        if (strSelectedCatCode.equalsIgnoreCase("SMART")) {
            EmployeeDetailsListResponse.Data employeeDetailsDataResponse = new EmployeeDetailsListResponse.Data();
            employeeDetailsDataResponse = this.employeeDetailsDataListResponse.get(position);

            Log.i(TAG, "itemClickDataChangeListener: employeeDetailsDataResponse --> " + new Gson().toJson(employeeDetailsDataResponse));
            intent = new Intent(context, SmartServiceVisibilityFormActivity.class);
            intent.putExtra("employeeDetailsDataResponse", employeeDetailsDataResponse);
        } else {
            NewJobListServiceVisibilityResponse.Data newJobListServiceVisibilityDataResponse = new NewJobListServiceVisibilityResponse.Data();
            newJobListServiceVisibilityDataResponse = this.newJobListServiceVisibilityDataListResponse.get(position);

            Log.i(TAG, "itemClickDataChangeListener: newJobListServiceVisibilityDataResponse --> " + new Gson().toJson(newJobListServiceVisibilityDataResponse));
            intent = new Intent(context, OtherServiceVisibilityFormActivity.class);
            intent.putExtra("newJobListServiceVisibilityDataResponse", newJobListServiceVisibilityDataResponse);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cat_code", strSelectedCatCode);
        intent.putExtra("cat_name", strSelectedCatName);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}