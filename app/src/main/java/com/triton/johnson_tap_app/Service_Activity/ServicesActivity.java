package com.triton.johnson_tap_app.Service_Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.PetBreedTypeSelectListener;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.RestUtils;
import com.triton.johnson_tap_app.Service_Adapter.AdditionalServiceListAdapter;
import com.triton.johnson_tap_app.Service_Adapter.ServiceListAdapter;
import com.triton.johnson_tap_app.activity.Main_Menu_ServicesActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.LoginRequest1;
import com.triton.johnson_tap_app.requestpojo.ServiceRequest;
import com.triton.johnson_tap_app.responsepojo.LoginResponse1;
import com.triton.johnson_tap_app.responsepojo.ServiceResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesActivity extends AppCompatActivity implements PetBreedTypeSelectListener {

    ImageView iv_back;
    String se_user_mobile_no, se_user_name, se_user_password, se_id, check_id, service_title;
    RecyclerView recyclerView, rv_add_list;
    String message, ID, emp_Type;
    TextView txt_NoRecords;
    ServiceListAdapter petBreedTypesListAdapter;
    AdditionalServiceListAdapter additionalServiceListAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Dialog dialog;
    Context context;
    private List<ServiceResponse.DataBean> breedTypedataBeanList;
    private List<ServiceResponse.DataBean> additionalList;
    private String PetBreedType = "", networkStatus = "";

    @SuppressLint({"MissingInflatedId", "HardwareIds"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_services);
        context = this;

        iv_back = (ImageView) findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.recyclerView);
        rv_add_list = findViewById(R.id.rv_add_list);
        txt_NoRecords = findViewById(R.id.txt_no_records);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        emp_Type = sharedPreferences.getString("emp_type", "ABCD");
        se_user_password = sharedPreferences.getString("user_password", "default value");
        Log.e("username", se_user_name);
        Log.e("password", se_user_password);
        Log.e("mobilenumber", se_user_mobile_no);

        Log.i(TAG, "onCreate: emp_Type --> " + emp_Type);

        if (emp_Type.equalsIgnoreCase("engineer")) {
            rv_add_list.setVisibility(View.VISIBLE);
        } else {
            rv_add_list.setVisibility(View.GONE);
        }

        ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("deviceid", ID);

        additionalList = new ArrayList<>();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent send = new Intent(ServicesActivity.this, Main_Menu_ServicesActivity.class);
                startActivity(send);

            }
        });

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.e("Network", "" + networkStatus);
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

//            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();

            NoInternetDialog();

        } else {

            jobFindResponseCall(se_user_mobile_no);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.e("Network", "" + networkStatus);
                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

//            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();

                    NoInternetDialog();

                } else {
                    LoginResponseCall();
                }
            }
        });

        additionalList.add(new ServiceResponse.DataBean("Escalator Survey Form", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Elevator Survey Form", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Service Visibility", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Failure Report", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Rope Maintenance", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Repair Work Approval Request", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Repair Work Approval", "0", "1", "0", "0", "0", "1"));
        additionalList.add(new ServiceResponse.DataBean("Safety Audit", "0", "1", "0", "0", "0", "1"));
        setAdditionalList(additionalList);
    }

    private void LoginResponseCall() {
//        dialog = new Dialog(ServicesActivity.this, R.style.NewProgressDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.progroess_popup);
//        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<LoginResponse1> call = apiInterface.LoginResponseCall(com.triton.johnson_tap_app.utils.RestUtils.getContentType(), loginRequest());
        Log.w(TAG, "SignupResponse url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<LoginResponse1>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<LoginResponse1> call, @NonNull Response<LoginResponse1> response) {

                Log.w(TAG, "SignupResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {

                        if (response.body().getData() != null) {

                            //  dialog.dismiss();

                            Log.e("msg", "" + message);

                            jobFindResponseCall(se_user_mobile_no);
                        }


                    } else {
                        // dialog.dismiss();
//                        Toasty.warning(getApplicationContext(),""+message,Toasty.LENGTH_LONG).show();

                        ErrorMyLocationAlert(message);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse1> call, @NonNull Throwable t) {
                //  dialog.dismiss();
                Log.e("OTP", "--->" + t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                ErrorMyLocationAlert(t.getMessage());
            }
        });

    }

    private void ErrorMyLocationAlert(String strMsg) {

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

    private LoginRequest1 loginRequest() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        LoginRequest1 loginRequest = new LoginRequest1();
        loginRequest.setUser_mobile_no(se_user_mobile_no);
        loginRequest.setUser_password(se_user_password);
        loginRequest.setDevice_id(ID);
        Log.w(TAG, "loginRequest " + new Gson().toJson(loginRequest));
        return loginRequest;
    }

    private void jobFindResponseCall(String job_no) {
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<ServiceResponse> call = apiInterface.ServiceResponseCall(RestUtils.getContentType(), serviceRequest(job_no));
        Log.w(TAG, "Jobno Find Response url  :%s" + " " + call.request().url().toString());
        call.enqueue(new Callback<ServiceResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<ServiceResponse> call, @NonNull Response<ServiceResponse> response) {
                Log.w(TAG, "Jobno Find Response" + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    message = response.body().getMessage();
                    Log.d("message", message);

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            breedTypedataBeanList = response.body().getData();

                            if (breedTypedataBeanList.size() == 0) {

                                recyclerView.setVisibility(View.GONE);
                                txt_NoRecords.setVisibility(View.VISIBLE);
                                txt_NoRecords.setText("No Records Found");
                            }
                            setBreedTypeView(breedTypedataBeanList);
                            Log.d("dataaaaa", String.valueOf(breedTypedataBeanList));
                        }

                    } /*else if (400 == response.body().getCode()) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("There is already a user registered with this email id. Please add new email id")) {

                        }
                    }*/ else {
                        ErrorMyLocationAlert(message);

//                        Toasty.warning(getApplicationContext(), "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<ServiceResponse> call, @NonNull Throwable t) {
                Log.e("Jobno Find ", "--->" + t.getMessage());
                ErrorMyLocationAlert(t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ServiceRequest serviceRequest(String job_no) {
        ServiceRequest service = new ServiceRequest();
        service.setUser_mobile_no(job_no);
        Log.w(TAG, "Jobno Find Request " + new Gson().toJson(service));
        return service;
    }

    private void setBreedTypeView(List<ServiceResponse.DataBean> breedTypedataBeanList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        petBreedTypesListAdapter = new ServiceListAdapter(getApplicationContext(), breedTypedataBeanList, this);
        recyclerView.setAdapter(petBreedTypesListAdapter);
    }

    private void setAdditionalList(List<ServiceResponse.DataBean> breedTypedataBeanList) {

        Log.i(TAG, "setAdditionalList: additionalList -> " + new Gson().toJson(breedTypedataBeanList));

        rv_add_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_add_list.setItemAnimator(new DefaultItemAnimator());
        additionalServiceListAdapter = new AdditionalServiceListAdapter(getApplicationContext(), breedTypedataBeanList, this);
        rv_add_list.setAdapter(additionalServiceListAdapter);
    }

    public void petBreedTypeSelectListener(String petbreedtitle, String petbreedid) {
        PetBreedType = petbreedtitle;
    }

    @Override
    public void onBackPressed() {
        Intent send = new Intent(ServicesActivity.this, Main_Menu_ServicesActivity.class);
        startActivity(send);
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
}