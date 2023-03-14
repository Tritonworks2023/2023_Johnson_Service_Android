package com.triton.johnson_tap_app.Service_Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.activity.Main_Menu_ServicesActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.AgentRequest;
import com.triton.johnson_tap_app.responsepojo.AgentResponse;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Agent_ProfileActivity extends AppCompatActivity {

    ImageView iv_back;
    Button submit;
    TextView agent_name, agent_id, agent_location, agent_type, agent_mobile, agent_status, last_logout_time /*completed_jobs, pending_jobs, invalid_jobs, services_mapped*/;
    String s_agent_code, s_agent_name, s_agent_status;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    String status, message = "", TAG = Agent_ProfileActivity.class.getSimpleName(), se_user_mobile_no, formattedDate = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_agent_profile);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        agent_name = (TextView) findViewById(R.id.agent_name);
        agent_id = (TextView) findViewById(R.id.agent_id);
        agent_location = (TextView) findViewById(R.id.agent_location);
        agent_mobile = (TextView) findViewById(R.id.agent_mobile);
        agent_type = (TextView) findViewById(R.id.agent_type);
        agent_status = (TextView) findViewById(R.id.agent_status);
        last_logout_time = (TextView) findViewById(R.id.last_logout_time);
        /*completed_jobs = (TextView) findViewById(R.id.completed_jobs);
        pending_jobs = (TextView) findViewById(R.id.pending_jobs);
        invalid_jobs = (TextView) findViewById(R.id.invalid_jobs);
        services_mapped = (TextView) findViewById(R.id.services_mapped);*/

        //   LoginResponseCall();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent send = new Intent(Agent_ProfileActivity.this, Main_Menu_ServicesActivity.class);
                startActivity(send);

            }
        });

        AgentResponseCall();
    }

    private void AgentResponseCall() {

        progressDialog = new ProgressDialog(Agent_ProfileActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<AgentResponse> call = apiInterface.AgentResponseCall(RestUtils.getContentType(), loginRequest());

        Log.i(TAG, "AgentResponseCall: URL -> " + call.request().url().toString());
        call.enqueue(new Callback<AgentResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<AgentResponse> call, @NonNull Response<AgentResponse> response) {
                Log.i(TAG, "AgentResponseCall: onResponse: AgentResponse -> " + new Gson().toJson(response.body()));
                progressDialog.dismiss();
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            agent_name.setText(response.body().getData().getUser_name());
                            agent_id.setText(response.body().getData().getUser_id());
                            agent_mobile.setText(response.body().getData().getUser_mobile_no());
                            agent_location.setText(response.body().getData().getUser_location());
                            agent_type.setText(response.body().getData().getEmp_type());
                            agent_status.setText(response.body().getData().getStatus());
//                            last_logout_time.setText(response.body().getData().getLast_logout_time());
                            setDate(response.body().getData().getLast_logout_time());
                        }

                    } else {
                        ErrorMyLocationAlert(message);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<AgentResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "AgentResponseCall: onFailure: error " + t.getMessage());
                ErrorMyLocationAlert(t.getMessage());
            }
        });

    }

    private void setDate(String inputDateTime) {

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "dd-MMM-yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDateTime);
            str = outputFormat.format(date);
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/

            last_logout_time.setText(formattedDate);
            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void ErrorMyLocationAlert(String strMsg) {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(getApplicationContext());
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        txt_Message.setText(strMsg);

        mBuilder.setView(mView);
        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private AgentRequest loginRequest() {

        /**
         * user_id : 12345
         * user_password : 12345
         * last_login_time : 20-10-2021 11:00 AM
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        AgentRequest loginRequest = new AgentRequest();
        loginRequest.setUser_mobile_no(se_user_mobile_no);
        Log.w(TAG, "loginRequest " + new Gson().toJson(loginRequest));
        return loginRequest;
    }

}