package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationModule;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.CountPausedRequest;
import com.triton.johnson_tap_app.responsepojo.Count_pasusedResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class RepairWorkOrderCreationActivity extends AppCompatActivity {

    ImageView iv_back;
    CardView cv_new_job, cv_paused_job;
    String TAG = RepairWorkOrderCreationActivity.class.getSimpleName(), service_title = "", se_user_mobile_no = "",
            se_user_name = "", se_id = "", check_id = "", message = "", paused_count = "", networkStatus = "",
            str_title = "";
    Context context;
    TextView pasused_count, title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_order_creation);

        context = this;

        iv_back = findViewById(R.id.iv_back);
        cv_new_job = findViewById(R.id.cv_new_job);
        cv_paused_job = findViewById(R.id.cv_pasused_job);
        pasused_count = findViewById(R.id.paused_count);
        title_name = findViewById(R.id.title_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("str_title")) {
                str_title = extras.getString("str_title");
            }
        }
        Log.i(TAG, "onCreate: str_title -> " + str_title);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        service_title = sharedPreferences.getString("service_title", "default");
        Log.e("Name", "" + service_title);

        title_name.setText(str_title);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        /*Log.e("Network", "" + networkStatus);
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            NoInternetDialog();
        } else {
            Count_paused();
        }*/

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                /*Intent send = new Intent(context, ServicesActivity.class);
                startActivity(send);*/
            }
        });

        cv_new_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(context, RepairWorkOrderCreationJobActivity.class);
                send.putExtra("str_title", str_title);
                send.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(send);
            }
        });

        cv_paused_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(context, RepairWorkOrderCompletionJobActivity.class);
                send.putExtra("str_title", str_title);
                send.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(send);
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

    private void ErrorMsgDialog(String strMsg) {

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

    private void Count_paused() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Count_pasusedResponse> call = apiInterface.Count_JobstatuscountPrventiveMRCall(RestUtils.getContentType(), countPausedRequest());
        Log.w(TAG, "SignupResponse url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<Count_pasusedResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<Count_pasusedResponse> call, @NonNull retrofit2.Response<Count_pasusedResponse> response) {

                Log.w(TAG, "SignupResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            paused_count = response.body().getData().getPaused_count();
                            pasused_count.setText("(" + paused_count + ")");
                        }

                    } else {
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                        ErrorMsgDialog(message);
                    }
                } else {
                    ErrorMsgDialog("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Count_pasusedResponse> call, @NonNull Throwable t) {
                ErrorMsgDialog(t.getMessage());
                Log.e("OTP", "--->" + t.getMessage());
            }
        });
    }

    private CountPausedRequest countPausedRequest() {

        CountPausedRequest count = new CountPausedRequest();
        count.setUser_mobile_no(se_user_mobile_no);
        count.setService_name(service_title);
        Log.w(TAG, "loginRequest " + new Gson().toJson(count));
        return count;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}