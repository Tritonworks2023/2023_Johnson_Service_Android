package com.triton.johnson_tap_app.Service_Activity.Breakdown_Services;

import static android.view.View.GONE;
import static com.triton.johnson_tap_app.RestUtils.getContentType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.triton.johnson_tap_app.requestpojo.Breakdowm_Submit_Request;
import com.triton.johnson_tap_app.requestpojo.Job_status_updateRequest;
import com.triton.johnson_tap_app.responsepojo.Job_status_updateResponse;
import com.triton.johnson_tap_app.responsepojo.RetriveLocalValueBRResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Material_RequestActivity extends AppCompatActivity {

    TextView text;
    CardView yes, no;
    String value;
    ImageView iv_back;
    String job_id, feedback_group, feedback_details, bd_dta, feedback_remark, tech_signature = "", status, mr_status = "";
    String str_StartTime, str_BDDetails = "", str_mr_status = "", str_job_status = "", str_but_type = "", address = "";
    SharedPreferences sharedPreferences;
    Context context;
    String se_id, se_user_mobile_no, se_user_name, compno, sertype, message, service_title;
    TextView txt_Jobid, txt_Starttime;
    int PageNumber = 4;
    double Latitude, Logitude;
    private String TAG = Material_RequestActivity.class.getSimpleName();

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_material_request);
        context = this;

        text = findViewById(R.id.text);
        yes = findViewById(R.id.card_yes);
        no = findViewById(R.id.card_no);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        txt_Starttime = findViewById(R.id.txt_starttime);
        txt_Jobid = findViewById(R.id.txt_jobid);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        compno = sharedPreferences.getString("compno", "123");
        service_title = sharedPreferences.getString("service_title", "Services");
        sertype = sharedPreferences.getString("sertype", "123");
        job_id = sharedPreferences.getString("job_id", "123");
        feedback_remark = sharedPreferences.getString("feedback_remark", "123");
        feedback_group = sharedPreferences.getString("feedback_group", "123");
        str_StartTime = sharedPreferences.getString("starttime", "");
        str_StartTime = str_StartTime.replaceAll("[^0-9-:]", " ");
        address = sharedPreferences.getString("add", "Chennai");

        Latitude = Double.parseDouble(sharedPreferences.getString("lati", "0.00000"));
        Logitude = Double.parseDouble(sharedPreferences.getString("long", "0.00000"));
        Log.e("Start Time", str_StartTime);

        txt_Jobid.setText("Job ID : " + job_id);
        txt_Starttime.setText("Start Time : " + str_StartTime);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  feedback_group = extras.getString("feedback_group");
            status = extras.getString("status");
            Log.e("Status", status);
        }

        if (extras != null) {
            bd_dta = extras.getString("bd_details");
        }

        if (extras != null) {
            //   job_id = extras.getString("job_id");
        }

        if (extras != null) {
            feedback_details = extras.getString("feedback_details");
        }

        /*if (extras != null) {
            feedback_remark = extras.getString("feedback_remark");
        }*/

        if (extras != null) {
            tech_signature = extras.getString("tech_signature");
        }

        if (status.equals("paused")) {
            retrive_LocalValue();
        }

        Spannable name_Upload = new SpannableString("Raise MR ");
        name_Upload.setSpan(new ForegroundColorSpan(Material_RequestActivity.this.getResources().getColor(R.color.colorAccent)), 0, name_Upload.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(name_Upload);
        Spannable name_Upload1 = new SpannableString("*");
        name_Upload1.setSpan(new ForegroundColorSpan(Color.RED), 0, name_Upload1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(name_Upload1);

        yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                str_but_type = "btn_next";
                str_job_status = "Job Paused";
                str_mr_status = "yes";
                Job_status_update();
                createLocalValue();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                str_but_type = "btn_next";
                str_job_status = "Job Paused";
                str_mr_status = "no";
                Job_status_update();
                createLocalValue();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();

//                    Intent send = new Intent(Material_RequestActivity.this, Feedback_RemarkActivity.class);
//                    send.putExtra("feedback_details", feedback_details);
//                    send.putExtra("feedback_group", feedback_group);
//                    send.putExtra("bd_details", bd_dta);
//                   send.putExtra("job_id", job_id);
//                   send.putExtra("feedback_remark", feedback_remark);
//                    startActivity(send);
            }
        });
    }

    private Job_status_updateRequest job_status_updateRequest() {

        Job_status_updateRequest custom = new Job_status_updateRequest();
        custom.setUser_mobile_no(se_user_mobile_no);
        custom.setService_name(service_title);
        custom.setJob_id(job_id);
        custom.setStatus(str_job_status);
        custom.setSMU_SCH_COMPNO(compno);
        custom.setSMU_SCH_SERTYPE(sertype);
        custom.setJOB_START_LONG(Logitude);
        custom.setJOB_START_LAT(Latitude);
        custom.setJOB_LOCATION(address);
        Log.e("CompNo", "" + compno);
        Log.e("SertYpe", "" + sertype);
        Log.w(TAG, "loginRequest " + new Gson().toJson(custom));
        return custom;
    }

    private void Job_status_update() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Job_status_updateResponse> call = apiInterface.job_status_updateResponseCall(com.triton.johnson_tap_app.utils.RestUtils.getContentType(), job_status_updateRequest());

        Log.i(TAG, "Job_status_update: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<Job_status_updateResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Job_status_updateResponse> call, @NonNull Response<Job_status_updateResponse> response) {

                Log.i(TAG, "onResponse: Job_status_updateResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            Log.i(TAG, "onResponse: message -> " + message);
                        }
                    } else {
                        ErrorMsgDialog(message);
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                    }
                } else {
                    ErrorMsgDialog("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Job_status_updateResponse> call, @NonNull Throwable t) {
                ErrorMsgDialog(t.getMessage());
                Log.e(TAG, "onFailure: Job_status_update -> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createLocalValue() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.createLocalvalueBD(getContentType(), createLocalRequest());

        Log.i(TAG, "createLocalValue: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                Log.i(TAG, "createLocalValue: onResponse -> " + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (response.body().getCode() == 200) {

                        if (response.body().getData() != null) {

                            Log.d("msg", message);
                            moveNext();
                        }

                    } else {
                        ErrorMsgDialog(message);
                    }
                } else {
                    ErrorMsgDialog("");
                }

            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                ErrorMsgDialog(t.getMessage());
                Log.e(TAG, "onFailure: createLocalValue -> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveNext() {

        Intent send = new Intent();

        if (str_mr_status.equalsIgnoreCase("yes")) {
            send = new Intent(Material_RequestActivity.this, Material_Request_MR_ScreenActivity.class);
        } else if (str_mr_status.equalsIgnoreCase("no")) {
            send = new Intent(Material_RequestActivity.this, BD_StatusActivity.class);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("value", str_mr_status);
        editor.apply();
        send.putExtra("feedback_details", feedback_details);
        send.putExtra("feedback_group", feedback_group);
        send.putExtra("bd_details", bd_dta);
        send.putExtra("job_id", job_id);
        send.putExtra("feedback_remark", feedback_remark);
        send.putExtra("status", status);
        startActivity(send);

    }

    private Breakdowm_Submit_Request createLocalRequest() {

        feedback_group = feedback_group.replaceAll("\n", "").replaceAll("", "");
        Log.e("after ", feedback_group);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        Breakdowm_Submit_Request submitDailyRequest = new Breakdowm_Submit_Request();
        submitDailyRequest.setBd_details(str_BDDetails);
        //submitDailyRequest.setFeedback_details(sstring);
        submitDailyRequest.setFeedback_details(feedback_details);
        submitDailyRequest.setCode_list(feedback_group);
        submitDailyRequest.setFeedback_remark_text(feedback_remark);
        submitDailyRequest.setMr_status(str_mr_status);
        submitDailyRequest.setMr_1("");
        submitDailyRequest.setMr_2("");
        submitDailyRequest.setMr_3("");
        submitDailyRequest.setMr_4("");
        submitDailyRequest.setMr_5("");
        submitDailyRequest.setMr_6("");
        submitDailyRequest.setMr_7("");
        submitDailyRequest.setMr_8("");
        submitDailyRequest.setMr_9("");
        submitDailyRequest.setMr_10("");
        submitDailyRequest.setBreakdown_service("");
        submitDailyRequest.setTech_signature("");
        submitDailyRequest.setCustomer_name("");
        submitDailyRequest.setCustomer_number("");
        submitDailyRequest.setCustomer_acknowledgemnet("");
        submitDailyRequest.setDate_of_submission(currentDateandTime);
        submitDailyRequest.setUser_mobile_no(se_user_mobile_no);
        submitDailyRequest.setJob_id(job_id);
        submitDailyRequest.setSMU_SCH_COMPNO(compno);
        submitDailyRequest.setSMU_SCH_SERTYPE(sertype);
        submitDailyRequest.setPage_number(PageNumber);
        Log.i(TAG, "createLocalRequest: Breakdowm_Submit_Request - " + new Gson().toJson(submitDailyRequest));
        return submitDailyRequest;
    }

    @SuppressLint("LongLogTag")
    private void retrive_LocalValue() {

        APIInterface apiInterface = RetrofitClient.getClient().create((APIInterface.class));
        Call<RetriveLocalValueBRResponse> call = apiInterface.retriveLocalValueBRCall(RestUtils.getContentType(), localRequest());
        Log.e("Retrive Local Value url  :%s", " " + call.request().url().toString());

        call.enqueue(new Callback<RetriveLocalValueBRResponse>() {
            @Override
            public void onResponse(Call<RetriveLocalValueBRResponse> call, Response<RetriveLocalValueBRResponse> response) {

                Log.e("Retrive Response", "" + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (response.body().getCode() == 200) {

                        if (response.body().getData() != null) {
                            Log.d("msg", message);

                            mr_status = response.body().getData().getMr_status();
                            Log.e("mrData", "" + mr_status);

                            if (mr_status.equals("yes")) {
                                no.setVisibility(GONE);
                            } else if (mr_status.equals("no")) {
                                yes.setVisibility(GONE);
                            } else {
                                yes.setVisibility(View.VISIBLE);
                                no.setVisibility(View.VISIBLE);
                            }

                        } else {
                            ErrorMsgDialog(message);
                        }
                    } else {
                        ErrorMsgDialog(message);
//                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                    }
                } else {
                    ErrorMsgDialog("");
                }
            }

            @Override
            public void onFailure(Call<RetriveLocalValueBRResponse> call, Throwable t) {
                ErrorMsgDialog(t.getMessage());
                Log.e("On Failure", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ErrorMsgDialog(String strMsg) {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        if (CommonFunction.nullPointerValidator(strMsg)) {
            txt_Message.setText(strMsg);
        }

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

    private Job_status_updateRequest localRequest() {
        Job_status_updateRequest custom = new Job_status_updateRequest();
        custom.setUser_mobile_no(se_user_mobile_no);
        custom.setJob_id(job_id);
        custom.setSMU_SCH_COMPNO(compno);
        //  custom.setSMU_SCH_SERTYPE(sertype);
        Log.e("Request Data ", "" + new Gson().toJson(custom));
        return custom;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Intent intent = new Intent(context, Feedback_RemarkActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);
    }
}