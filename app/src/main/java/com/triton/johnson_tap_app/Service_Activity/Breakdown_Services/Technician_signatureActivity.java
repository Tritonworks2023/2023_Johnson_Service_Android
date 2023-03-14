package com.triton.johnson_tap_app.Service_Activity.Breakdown_Services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.triton.johnson_tap_app.Db.CommonUtil;
import com.triton.johnson_tap_app.Db.DbHelper;
import com.triton.johnson_tap_app.Db.DbUtil;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.ServicesActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.Breakdowm_Submit_Request;
import com.triton.johnson_tap_app.requestpojo.Job_status_updateRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.Job_status_updateResponse;
import com.triton.johnson_tap_app.responsepojo.RetriveLocalValueBRResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Technician_signatureActivity extends AppCompatActivity {

    SignaturePad signaturePad;
    Button saveButton, clearButton;
    MultipartBody.Part siganaturePart;
    String userid;
    ImageView image, iv_back, img_Siganture, img_Pause;
    String TAG = Technician_signatureActivity.class.getSimpleName(), value = "", job_id, feedback_group,
            feedback_details, bd_dta, feedback_remark = "", mr1 = "", mr2 = "", mr3 = "", mr4 = "", mr5 = "",
            mr6 = "", mr7 = "", mr8 = "", mr9 = "", mr10 = "", breakdown_servies = "", str_tech_signature = "",
            message;
    ProgressDialog progressDialog;
    Bitmap signatureBitmap;
    SharedPreferences sharedPreferences;
    String se_id, se_user_mobile_no, se_user_name, service_title, signfile, status, compno, sertype;
    Context context;
    TextView txt_Jobid, txt_Starttime;
    String str_StartTime, networkStatus = "", str_BDDetails = "", str_feedback_details = "";
    AlertDialog alertDialog;
    String str_job_status = "";
    ArrayList<String> mydata = new ArrayList<>();
    double Latitude, Logitude;
    String address = "";
    int PageNumber = 8;
    private Button btnSelection, btn_prev;
    private String uploadimagepath = "";

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_technician_signature);
        context = this;

        CommonUtil.dbUtil = new DbUtil(context);
        CommonUtil.dbUtil.open();
        CommonUtil.dbHelper = new DbHelper(context);

        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        saveButton = (Button) findViewById(R.id.saveButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        btnSelection = (Button) findViewById(R.id.btn_next);
        btn_prev = (Button) findViewById(R.id.btn_show);
        image = (ImageView) findViewById(R.id.image);
        img_Siganture = (ImageView) findViewById(R.id.img_sign);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        txt_Starttime = findViewById(R.id.txt_starttime);
        txt_Jobid = findViewById(R.id.txt_jobid);
        img_Pause = findViewById(R.id.ic_paused);


        btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
        btn_prev.setTextColor(getResources().getColor(R.color.white));
        btn_prev.setEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        job_id = sharedPreferences.getString("job_id", "default value");
        service_title = sharedPreferences.getString("service_title", "default value");
        compno = sharedPreferences.getString("compno", "123");
        sertype = sharedPreferences.getString("sertype", "123");
        uploadimagepath = sharedPreferences.getString("tech_sign", "");
        str_StartTime = sharedPreferences.getString("starttime", "");
        str_StartTime = str_StartTime.replaceAll("[^0-9-:]", " ");
        Log.e("JobID", "" + job_id);
        Log.e("Name", "" + service_title);
        Log.e("Start Time", str_StartTime);

        txt_Jobid.setText("Job ID : " + job_id);
        txt_Starttime.setText("Start Time : " + str_StartTime);

        Latitude = Double.parseDouble(sharedPreferences.getString("lati", "0.00000"));
        Logitude = Double.parseDouble(sharedPreferences.getString("long", "0.00000"));
        address = sharedPreferences.getString("add", "Chennai");
        Log.e("Location", "" + Latitude + "" + Logitude + "" + address);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("value");
            status = extras.getString("status");
            Log.e("Status", status);
        }
        if (extras != null) {
            //  feedback_group = extras.getString("feedback_group");
        }

        if (extras != null) {
            //  bd_dta = extras.getString("bd_details");
        }

        if (extras != null) {
            //   job_id = extras.getString("job_id");
        }

        if (extras != null) {
            //  feedback_details = extras.getString("feedback_details");
        }

        if (extras != null) {
            //  feedback_remark = extras.getString("feedback_remark");
        }

        if (extras != null) {
            mr1 = extras.getString("mr1");
        }
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
            breakdown_servies = extras.getString("breakdown_service");
//            Log.e("A",breakdown_servies);
        }
        if (extras != null) {
//            str_tech_signature = extras.getString("tech_signature");
//            Picasso.get().load(str_tech_signature).into(image);
        }

        getBDDetails();
        getFeedbackGroup();
        getFeedBackDesc();
        getFeedback();
        getData(job_id, service_title);
        getSign(job_id, service_title);

        if (status.equals("paused")) {
            retrive_LocalValue();

            //   Picasso.get().load(uploadimagepath).into(img_Siganture);

        } else {
            Log.e("Way", "New");

        }

        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                signatureBitmap = signaturePad.getSignatureBitmap();
                Log.w(TAG, "signatureBitmap" + signatureBitmap);
                File file = new File(getFilesDir(), "Technician Signature" + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(file);
                    if (signatureBitmap != null) {
                        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    }
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                siganaturePart = MultipartBody.Part.createFormData("sampleFile", userid + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.e("Network", "" + networkStatus);
                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                    NoInternetDialog();

                } else {

                    uploadDigitalSignatureImageRequest(file);

                }

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

        btnSelection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                if (signatureBitmap == null && uploadimagepath == null) {
//                    Toast.makeText(Technician_signatureActivity.this, "Please Drop Signature", Toast.LENGTH_SHORT).show();
//                }

                Cursor cur = CommonUtil.dbUtil.getEngSign(job_id, service_title);
                Log.e("ENg Sign", " " + cur.getCount());

                if (signatureBitmap == null && cur.getCount() == 0) {
                    Toast.makeText(context, "Please Drop Signature", Toast.LENGTH_SHORT).show();
                } else {
                    Intent send = new Intent(Technician_signatureActivity.this, Customer_Details_BreakdownActivity.class);
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
                    send.putExtra("breakdown_service", breakdown_servies);
                    send.putExtra("tech_signature", uploadimagepath);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tech_sign", uploadimagepath);
                    editor.apply();
                    Log.e("Tech Sign", "" + uploadimagepath);
                    send.putExtra("status", status);
                    startActivity(send);
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();
//                if(uploadimagepath.equals("")) {
//                    Intent send = new Intent(Technician_signatureActivity.this, BD_StatusActivity.class);
//                    send.putExtra("value", value);
//                    send.putExtra("feedback_details", feedback_details);
//                    send.putExtra("feedback_group", feedback_group);
//                    send.putExtra("bd_details", bd_dta);
//                    send.putExtra("job_id", job_id);
//                    send.putExtra("feedback_remark", feedback_remark);
//                    startActivity(send);
//                }
//                else {
//                    Intent send = new Intent(Technician_signatureActivity.this, BD_StatusActivity.class);
//                    send.putExtra("value", value);
//                    send.putExtra("feedback_details", feedback_details);
//                    send.putExtra("feedback_group", feedback_group);
//                    send.putExtra("bd_details", bd_dta);
//                    send.putExtra("job_id", job_id);
//                    send.putExtra("feedback_remark", feedback_remark);
//                    send.putExtra("tech_signature", uploadimagepath);
//                    startActivity(send);
//                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();
//                if(uploadimagepath.equals("")) {
//                    Intent send = new Intent(Technician_signatureActivity.this, BD_StatusActivity.class);
//                    send.putExtra("value", value);
//                    send.putExtra("feedback_details", feedback_details);
//                    send.putExtra("feedback_group", feedback_group);
//                    send.putExtra("bd_details", bd_dta);
//                    send.putExtra("job_id", job_id);
//                    send.putExtra("feedback_remark", feedback_remark);
//                    startActivity(send);
//                }
//                else {
//                    Intent send = new Intent(Technician_signatureActivity.this, BD_StatusActivity.class);
//                    send.putExtra("value", value);
//                    send.putExtra("feedback_details", feedback_details);
//                    send.putExtra("feedback_group", feedback_group);
//                    send.putExtra("bd_details", bd_dta);
//                    send.putExtra("job_id", job_id);
//                    send.putExtra("feedback_remark", feedback_remark);
//                    send.putExtra("tech_signature", uploadimagepath);
//                    startActivity(send);
//                }
            }
        });

        img_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());

                alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Are you sure to pause this job ?")
                        .setMessage(date)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Lat : " + Latitude + "Long : " + Logitude + "Add : " + address, Toast.LENGTH_LONG).show();
                                str_job_status = "Job Paused";
                                Job_status_update();
                                createLocalvalue();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void Job_status_update() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Job_status_updateResponse> call = apiInterface.job_status_updateResponseCall(com.triton.johnson_tap_app.utils.RestUtils.getContentType(), job_status_updateRequest());
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
                        } else {
                            ErrorMsgDialog(message);
                        }
                    } else {
//                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                        ErrorMsgDialog(message);
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

    private void createLocalvalue() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.createLocalvalueBD(com.triton.johnson_tap_app.utils.RestUtils.getContentType(), createLocalRequest());
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

                            Intent send = new Intent(context, ServicesActivity.class);
                            startActivity(send);
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
        submitDailyRequest.setBreakdown_service(breakdown_servies);
        submitDailyRequest.setTech_signature(uploadimagepath);
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

    private void retrive_LocalValue() {

        APIInterface apiInterface = RetrofitClient.getClient().create((APIInterface.class));
        Call<RetriveLocalValueBRResponse> call = apiInterface.retriveLocalValueBRCall(RestUtils.getContentType(), localRequest());
        Log.w(TAG, "Retrive Local Value url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<RetriveLocalValueBRResponse>() {
            @Override
            public void onResponse(@NonNull Call<RetriveLocalValueBRResponse> call, @NonNull Response<RetriveLocalValueBRResponse> response) {

                Log.e(TAG, "Retrive Response" + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (response.body().getCode() == 200) {

                        if (response.body().getData() != null) {
                            Log.d("msg", message);

                            feedback_remark = response.body().getData().getFeedback_remark_text();
                            value = response.body().getData().getMr_status();
                            if (response.body().getData().getMr_1() != null && !response.body().getData().getMr_1().isEmpty()) {
                                mr1 = response.body().getData().getMr_1();
                            }
                            if (response.body().getData().getMr_2() != null && !response.body().getData().getMr_2().isEmpty()) {
                                mr2 = response.body().getData().getMr_2();
                            }
                            if (response.body().getData().getMr_3() != null && !response.body().getData().getMr_3().isEmpty()) {
                                mr3 = response.body().getData().getMr_3();
                            }
                            if (response.body().getData().getMr_4() != null && !response.body().getData().getMr_4().isEmpty()) {
                                mr4 = response.body().getData().getMr_4();
                            }
                            if (response.body().getData().getMr_5() != null && !response.body().getData().getMr_5().isEmpty()) {
                                mr5 = response.body().getData().getMr_5();
                            }
                            if (response.body().getData().getMr_6() != null && !response.body().getData().getMr_6().isEmpty()) {
                                mr6 = response.body().getData().getMr_6();
                            }
                            if (response.body().getData().getMr_7() != null && !response.body().getData().getMr_7().isEmpty()) {
                                mr7 = response.body().getData().getMr_7();
                            }
                            if (response.body().getData().getMr_8() != null && !response.body().getData().getMr_8().isEmpty()) {
                                mr8 = response.body().getData().getMr_8();
                            }
                            if (response.body().getData().getMr_9() != null && !response.body().getData().getMr_9().isEmpty()) {
                                mr9 = response.body().getData().getMr_9();
                            }
                            if (response.body().getData().getMr_10() != null && !response.body().getData().getMr_10().isEmpty()) {
                                mr10 = response.body().getData().getMr_10();
                            }
                            breakdown_servies = response.body().getData().getBreakdown_service();

                            uploadimagepath = response.body().getData().getTech_signature();
                            Log.e("Tech Sign Retrive", "" + uploadimagepath);

                            if (uploadimagepath.isEmpty()) {

                                Log.e("hi", "sign empty");
                            } else {
                                Picasso.get().load(uploadimagepath).into(img_Siganture);
                            }
                        } else {
                            ErrorMsgDialog(message);
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
                ErrorMsgDialog(t.getMessage());
                Log.e("On Failure", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Job_status_updateRequest localRequest() {
        Job_status_updateRequest custom = new Job_status_updateRequest();
        custom.setUser_mobile_no(se_user_mobile_no);
        custom.setJob_id(job_id);
        custom.setSMU_SCH_COMPNO(compno);
        //  custom.setSMU_SCH_SERTYPE(sertype);
        Log.w(TAG, "Request Data " + new Gson().toJson(custom));
        return custom;
    }

    @SuppressLint("Range")
    private void getSign(String job_id, String service_title) {
        Log.e("Sign", "Hi");
        Log.e("Nish", "" + job_id);
        Log.e("Nish", "" + service_title);
        Cursor cur = CommonUtil.dbUtil.getEngSign(job_id, service_title);
        Cursor curs = CommonUtil.dbUtil.getEngSign();
        Log.e("ENg Sign", " " + cur.getCount());

        if (cur.getCount() > 0 && cur.moveToLast()) {

            //  do{
            signfile = cur.getString(cur.getColumnIndex(DbHelper.SIGN_FILE));
            String jon = cur.getString(cur.getColumnIndex(DbHelper.JOBID));
            String ss = cur.getString(cur.getColumnIndex(DbHelper.MYACTIVITY));
            uploadimagepath = cur.getString(cur.getColumnIndex(DbHelper.SIGN_PATH));

            Log.e("job", "" + jon);
            Log.e("act", "" + ss);
            Log.e("path", "" + uploadimagepath);

            Picasso.get().load(uploadimagepath).into(img_Siganture);


            ///  BitmapDrawable drawable = (BitmapDrawable) img_Siganture.getDrawable();
            //  Bitmap bitmap = drawable.getBitmap();
            ///  bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
            //  photo.setImageBitmap(bitmap)
            // Bitmap image = ((BitmapDrawable)img_Siganture.getDrawable()).getBitmap();


            //  File file = new File(signfile);
            //  String filePath = file.getPath();
            //   Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            // img_Siganture.setImageBitmap(bitmap);
            // signaturePad.setSignatureBitmap(bitmap);
            //  signaturePad.setSignatureBitmap(bitmap);
            // signatureBitmap = signaturePad.getSignatureBitmap();
            //    }while (cur.moveToNext());


        }
    }

    private void uploadDigitalSignatureImageRequest(File file) {

        progressDialog = new ProgressDialog(Technician_signatureActivity.this);
        progressDialog.setMessage("Please Wait Image Upload ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);
        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(siganaturePart);
        Log.w(TAG, "url  :%s" + call.request().url().toString());


        call.enqueue(new Callback<FileUploadResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {

                progressDialog.dismiss();
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        Log.w(TAG, "Profpic" + "--->" + new Gson().toJson(response.body()));

                        uploadimagepath = response.body().getData();

                        Log.d("image", uploadimagepath);
                        if (uploadimagepath != null) {

                            //   Picasso.get().load(uploadimagepath).into(image);

                            Toast.makeText(Technician_signatureActivity.this, "Signature Saved", Toast.LENGTH_SHORT).show();
                            img_Siganture.setVisibility(View.GONE);
                            CommonUtil.dbUtil.addEngSign(job_id, service_title, uploadimagepath, file, "0");
                            Log.e("a", "" + job_id);
                            Log.e("b", "" + service_title);
                            Log.e("c", "" + uploadimagepath);
                            Log.e("d", "" + file);

                        }
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                } else {
                    ErrorMsgDialog("");
                }
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                ErrorMsgDialog(t.getMessage());
                Log.e(TAG, "ServerUrlImagePath" + "On failure working" + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

//       super.onBackPressed();
        Intent intent = new Intent(context, BD_StatusActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("breakdown_service", breakdown_servies);
        startActivity(intent);
    }

    @SuppressLint("Range")
    private void getBDDetails() {

        Cursor curs = CommonUtil.dbUtil.getBDdetails(job_id, service_title, "1");
        Log.e("BD Count", "" + curs.getCount());

        if (curs.getCount() > 0 && curs.moveToLast()) {

            str_BDDetails = curs.getString(curs.getColumnIndex(DbHelper.BD_DETAILS));
            Log.e("BD Data Get", "" + str_BDDetails);
        }


    }

    private void getFeedbackGroup() {

        mydata = new ArrayList<>();

        Cursor cur = CommonUtil.dbUtil.getFeedbackgroup(job_id, service_title, "2");
        Log.e("Checklist get Data", "" + cur.getCount());

        if (cur.getCount() > 0 && cur.moveToFirst()) {

            do {
                @SuppressLint("Range")
                String abc = cur.getString(cur.getColumnIndex(DbHelper.FEEDBACK_GROUP));
                Log.e("Data Get", "" + abc);
                mydata.add(abc);

            } while (cur.moveToNext());

        }

        feedback_group = String.valueOf(mydata);
        Log.e("FeedBack Group", "" + feedback_group);
    }

    private void getFeedBackDesc() {

        Cursor cur = CommonUtil.dbUtil.getFeedbackDesc(job_id, service_title, "3");
        Log.e("Feedback Desc get Data", "" + cur.getCount());
        mydata = new ArrayList<>();
        if (cur.getCount() > 0 && cur.moveToFirst()) {

            do {
                @SuppressLint("Range")
                String abc = cur.getString(cur.getColumnIndex(DbHelper.FEEDBACK_DESCRIPTION));
                Log.e("Data Get", "" + abc);
                mydata.add(abc);
//                outputList = new ArrayList<String>();
//                for (String item : mydata) {
//                    //outputList.add("\""+item+"\"");
//                    outputList.add("" + item + "");
//                    outputList.remove("null");
//                }
            } while (cur.moveToNext());

        }
        str_feedback_details = String.valueOf(mydata);
    }

    @SuppressLint("Range")
    private void getFeedback() {

        Cursor cur = CommonUtil.dbUtil.getFeedback(job_id, service_title, "4");

        Log.e("GET FEEDBACK ", "" + cur.getCount());

        if (cur.getCount() > 0 && cur.moveToLast()) {

            feedback_remark = cur.getString(cur.getColumnIndex(DbHelper.FEEDBACK_REMARKS));
            Log.e("Remarks", "" + feedback_remark);

        }

    }

    private void getData(String job_id, String service_title) {

        Log.e("JobId", "" + job_id);
        Log.e("Activity", "" + service_title);


        Cursor cur = CommonUtil.dbUtil.getBreakdownMrList(job_id, service_title);

        Log.e("MRLIST", "" + cur.getCount());

        if (cur.getCount() > 0 && cur.moveToFirst()) {

            do {
                mr1 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR1));
                mr2 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR2));
                mr3 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR3));
                mr4 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR4));
                mr5 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR5));
                mr6 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR6));
                mr7 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR7));
                mr8 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR8));
                mr9 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR9));
                mr10 = cur.getString(cur.getColumnIndexOrThrow(DbHelper.MR10));
            } while (cur.moveToNext());
        }
        cur.close();
    }
}