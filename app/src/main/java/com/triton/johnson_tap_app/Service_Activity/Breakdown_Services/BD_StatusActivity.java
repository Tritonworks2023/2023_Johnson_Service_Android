package com.triton.johnson_tap_app.Service_Activity.Breakdown_Services;

import static android.view.View.GONE;
import static com.triton.johnson_tap_app.utils.RestUtils.getContentType;

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
import com.triton.johnson_tap_app.Service_Activity.ServicesActivity;
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

public class BD_StatusActivity extends AppCompatActivity {

    TextView text, txt_Lift;
    CardView full, material, lift;
    String value = "", job_id = "", feedback_group = "", feedback_details = "", bd_dta = "", feedback_remark = "",
            mr1 = "", mr2 = "", mr3 = "", mr4 = "", mr5 = "", mr6 = "", mr7 = "", mr8 = "", mr9 = "",
            mr10 = "", tech_signature = "", status = "", message = "";
    ImageView iv_back;
    SharedPreferences sharedPreferences;
    Context context;
    String se_id, se_user_mobile_no, sertype, compno, se_user_name, service_title;
    TextView txt_Jobid, txt_Starttime;
    String str_StartTime, str_job_status = "", str_but_type = "", str_breakdown_services = "", address = "",
            str_feedback_details = "", str_BDDetails = "";
    double Latitude, Logitude;
    int PageNumber = 7;
    private String TAG = BD_StatusActivity.class.getSimpleName();

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bd_status);
        context = this;

        text = findViewById(R.id.text);
        full = findViewById(R.id.card_full);
        material = findViewById(R.id.card_material);
        lift = findViewById(R.id.card_lift);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        txt_Starttime = findViewById(R.id.txt_starttime);
        txt_Jobid = findViewById(R.id.txt_jobid);
        txt_Lift = findViewById(R.id.txt_lift);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // value = extras.getString("value");
            status = extras.getString("status");
            Log.e("Status", status);
        }
        if (extras != null) {
            feedback_group = extras.getString("feedback_group");
        }

        if (extras != null) {
            bd_dta = extras.getString("bd_details");
        }

        if (extras != null) {
            // job_id = extras.getString("job_id");
        }

        if (extras != null) {
            feedback_details = extras.getString("feedback_details");
        }

        if (extras != null) {
            feedback_remark = extras.getString("feedback_remark");
        }

        if (extras != null) {
            mr1 = extras.getString("mr1");
        }
        Log.i(TAG, "onCreate: mr1 -> " + mr1);
        if (extras != null) {
            mr2 = extras.getString("mr2");
        }
        if (extras != null) {
            mr3 = extras.getString("mr3");
        }
        if (extras != null) {
            mr4 = extras.getString("mr4");
        }
        if (extras != null) {
            mr5 = extras.getString("mr5");
        }
        if (extras != null) {
            mr6 = extras.getString("mr6");
        }
        if (extras != null) {
            mr7 = extras.getString("mr7");
        }
        if (extras != null) {
            mr8 = extras.getString("mr8");
        }
        if (extras != null) {
            mr9 = extras.getString("mr9");
        }
        if (extras != null) {
            mr10 = extras.getString("mr10");
        }
        if (extras != null) {
            tech_signature = extras.getString("tech_signature");
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        value = sharedPreferences.getString("value", "default value");
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        compno = sharedPreferences.getString("compno", "123");
        service_title = sharedPreferences.getString("service_title", "default value");
        sertype = sharedPreferences.getString("sertype", "123");
        job_id = sharedPreferences.getString("job_id", "123");
        Log.e("Value", value);
        str_StartTime = sharedPreferences.getString("starttime", "");
        str_StartTime = str_StartTime.replaceAll("[^0-9-:]", " ");
        Latitude = Double.parseDouble(sharedPreferences.getString("lati", "0.00000"));
        Logitude = Double.parseDouble(sharedPreferences.getString("long", "0.00000"));
        address = sharedPreferences.getString("add", "Chennai");
        Log.i(TAG, "onCreate: Latitude -> " + Latitude + " Logitude -> " + Logitude + " address -> " + address);

        Log.e("Start Time", str_StartTime);

        txt_Jobid.setText("Job ID : " + job_id);
        txt_Starttime.setText("Start Time : " + str_StartTime);

        String LiftType = job_id.substring(0, 1);
        Log.e("Hi", "Lift Type " + LiftType);

        if (LiftType.equals("E")) {

            txt_Lift.setText("Escalator Shutdown");
        }

        Spannable name_Upload = new SpannableString("Breakdown Service ");
        name_Upload.setSpan(new ForegroundColorSpan(BD_StatusActivity.this.getResources().getColor(R.color.colorAccent)), 0, name_Upload.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(name_Upload);
        Spannable name_Upload1 = new SpannableString("*");
        name_Upload1.setSpan(new ForegroundColorSpan(Color.RED), 0, name_Upload1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(name_Upload1);

        if (value.equals("yes")) {
            full.setVisibility(View.GONE);
            material.setVisibility(View.VISIBLE);
            lift.setVisibility(View.VISIBLE);
        } else {
            full.setVisibility(View.VISIBLE);
            material.setVisibility(View.VISIBLE);
            lift.setVisibility(View.VISIBLE);
        }

        if (status.equals("paused")) {
            retrive_LocalValue();
        }

        full.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                str_but_type = "btn_next";
                str_job_status = "Job Paused";
                str_breakdown_services = "Completed in full";
                Job_status_update();
                createLocalValue();
            }
        });

        material.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                str_but_type = "btn_next";
                str_job_status = "Job Paused";
                str_breakdown_services = "Completed but material to be replaced";
                Job_status_update();
                createLocalValue();
            }
        });

        lift.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                str_but_type = "btn_next";
                str_job_status = "Job Paused";
                str_breakdown_services = "Lift Shutdown";
                Job_status_update();
                createLocalValue();

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    private void moveNext() {
        Intent send = new Intent(BD_StatusActivity.this, Technician_signatureActivity.class);
        send.putExtra("value", value);
        send.putExtra("feedback_details", feedback_details);
        send.putExtra("feedback_group", feedback_group);
        send.putExtra("bd_details", bd_dta);
        send.putExtra("job_id", job_id);
        send.putExtra("feedback_remark", feedback_remark);
        send.putExtra("mr1", mr1);
        send.putExtra("mr2", mr2);
        send.putExtra("mr3", mr3);
        send.putExtra("mr4", mr4);
        send.putExtra("mr5", mr5);
        send.putExtra("mr6", mr6);
        send.putExtra("mr7", mr7);
        send.putExtra("mr8", mr8);
        send.putExtra("mr9", mr9);
        send.putExtra("mr10", mr10);
        send.putExtra("breakdown_service", "str_service_type");
        send.putExtra("tech_signature", tech_signature);
        send.putExtra("status", status);
        startActivity(send);
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
        Call<Job_status_updateResponse> call = apiInterface.job_status_updateResponseCall(getContentType(), job_status_updateRequest());
        Log.w(TAG, "SignupResponse url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<Job_status_updateResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Job_status_updateResponse> call, @NonNull Response<Job_status_updateResponse> response) {

                Log.w(TAG, "SignupResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            Log.d("msg", message);
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
            public void onFailure(@NonNull Call<Job_status_updateResponse> call, @NonNull Throwable t) {
                Log.e("OTP", "--->" + t.getMessage());
                ErrorMsgDialog(t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Breakdowm_Submit_Request createLocalRequest() {

        str_feedback_details = str_feedback_details.replaceAll("\n", "").replaceAll("", "");
        Log.e("after ", str_feedback_details);

        feedback_group = feedback_group.replaceAll("\n", "").replaceAll("", "");
        Log.e("after ", feedback_group);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        Breakdowm_Submit_Request submitDailyRequest = new Breakdowm_Submit_Request();
        submitDailyRequest.setBd_details(str_BDDetails);
        //submitDailyRequest.setFeedback_details(sstring);
        submitDailyRequest.setFeedback_details(str_feedback_details);
        submitDailyRequest.setCode_list(feedback_group);
        submitDailyRequest.setFeedback_remark_text(feedback_remark);
        submitDailyRequest.setMr_status(value);
        submitDailyRequest.setMr_1(mr1);
        submitDailyRequest.setMr_2(mr2);
        submitDailyRequest.setMr_3(mr3);
        submitDailyRequest.setMr_4(mr4);
        submitDailyRequest.setMr_5(mr5);
        submitDailyRequest.setMr_6(mr6);
        submitDailyRequest.setMr_7(mr7);
        submitDailyRequest.setMr_8(mr8);
        submitDailyRequest.setMr_9(mr9);
        submitDailyRequest.setMr_10(mr10);
        submitDailyRequest.setBreakdown_service(str_breakdown_services);
        submitDailyRequest.setCustomer_name("");
        submitDailyRequest.setCustomer_number("");
        submitDailyRequest.setCustomer_acknowledgemnet("");
        submitDailyRequest.setDate_of_submission(currentDateandTime);
        submitDailyRequest.setUser_mobile_no(se_user_mobile_no);
        submitDailyRequest.setJob_id(job_id);
        submitDailyRequest.setSMU_SCH_COMPNO(compno);
        submitDailyRequest.setSMU_SCH_SERTYPE(sertype);
        submitDailyRequest.setPage_number(PageNumber);
        Log.e("CompNo", "" + compno);
        Log.e("SertYpe", "" + sertype);
        Log.w(TAG, " Create Local Value Request" + new Gson().toJson(submitDailyRequest));
        return submitDailyRequest;
    }

    private void createLocalValue() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.createLocalvalueBD(getContentType(), createLocalRequest());
        Log.w(TAG, "Create Local Value Response url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                Log.w(TAG, "Create Local Value Response" + "" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (response.body().getCode() == 200) {

                        if (response.body().getData() != null) {

                            Log.d("msg", message);

                            if (str_but_type.equalsIgnoreCase("img_Pause")) {
                                Intent send = new Intent(context, ServicesActivity.class);
                                startActivity(send);
                            } else if (str_but_type.equalsIgnoreCase("btn_next")) {
                                moveNext();
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
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                ErrorMsgDialog(t.getMessage());
                Log.e("On Failure", "--->" + t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void retrive_LocalValue() {

        APIInterface apiInterface = RetrofitClient.getClient().create((APIInterface.class));
        Call<RetriveLocalValueBRResponse> call = apiInterface.retriveLocalValueBRCall(RestUtils.getContentType(), localRequest());
        Log.e("Retrive Local Value url  :%s", " " + call.request().url().toString());

        call.enqueue(new Callback<RetriveLocalValueBRResponse>() {
            @Override
            public void onResponse(@NonNull Call<RetriveLocalValueBRResponse> call, @NonNull Response<RetriveLocalValueBRResponse> response) {
                Log.i(TAG, "retrive_LocalValue: onResponse: RetriveLocalValueBRResponse -> " + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (response.body().getCode() == 200) {

                        if (response.body().getData() != null) {
                            Log.d("msg", message);

                            str_feedback_details = response.body().getData().getFeedback_details();
                            str_BDDetails = response.body().getData().getBd_details();

                            String breakdownservice = response.body().getData().getBreakdown_service();
                            Log.e("mrData", "" + breakdownservice);
                            value = response.body().getData().getMr_status();

                            if (breakdownservice.equals("Completed in full")) {
                                material.setVisibility(GONE);
                                lift.setVisibility(GONE);
                            } else if (breakdownservice.equals("Completed but material to be replaced")) {
                                full.setVisibility(GONE);
                                lift.setVisibility(GONE);
                            } else if (breakdownservice.equals("Lift Shutdown") || breakdownservice.equals("Escalator Shutdown")) {
                                full.setVisibility(GONE);
                                material.setVisibility(GONE);
                            }
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
            public void onFailure(@NonNull Call<RetriveLocalValueBRResponse> call, @NonNull Throwable t) {

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

        //  super.onBackPressed();

        if (value.equals("yes")) {

            Intent intent = new Intent(context, Material_Request_MR_ScreenActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        } else {

            Intent intent = new Intent(context, Material_RequestActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        }

    }

}