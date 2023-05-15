package com.triton.johnson_tap_app.Service_Activity.Breakdown_Services;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;
import static com.triton.johnson_tap_app.utils.RestUtils.getContentType;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.Db.CommonUtil;
import com.triton.johnson_tap_app.Db.DbHelper;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.RestUtils;
import com.triton.johnson_tap_app.Service_Activity.ServicesActivity;
import com.triton.johnson_tap_app.Service_Adapter.Feedback_GroupAdapter;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickCheckBoxFeedbackGroupDetails;
import com.triton.johnson_tap_app.requestpojo.Breakdowm_Submit_Request;
import com.triton.johnson_tap_app.requestpojo.Feedback_GroupRequest;
import com.triton.johnson_tap_app.requestpojo.Job_status_updateRequest;
import com.triton.johnson_tap_app.responsepojo.Feedback_GroupResponse;
import com.triton.johnson_tap_app.responsepojo.Job_status_updateResponse;
import com.triton.johnson_tap_app.responsepojo.RetriveLocalValueBRResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback_GroupActivity extends AppCompatActivity implements OnItemClickCheckBoxFeedbackGroupDetails {

    TextView text, txt_no_records;
    Button btnSelection, btn_prev;
    ImageView iv_back, img_clearsearch, img_Pause;
    List<Feedback_GroupResponse.DataBean> dataBeanList;
    Feedback_GroupAdapter activityBasedListAdapter;
    String Title, Codes, petimage, bd_dta, job_id, message, search_string, service_title, pre_check = "", status,
            str_job_status = "";
    String se_id, se_user_mobile_no, se_user_name, compno, sertype, TAG = Feedback_GroupActivity.class.getSimpleName();
    int textlength = 0;
    EditText etsearch;
    AlertDialog alertDialog;
    Context context;
    SharedPreferences sharedPreferences;
    ArrayList<String> mydata = new ArrayList<>();
    ArrayList<String> feedback_group_list = new ArrayList<>();
    TextView txt_Jobid, txt_Starttime;
    String str_StartTime, str_BDDetails = "", feedback_group = "";
    String networkStatus = "", str_but_type = "";
    double Latitude, Logitude;
    String address = "";
    int PageNumber = 2;
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_feedback_group);
        context = this;

        /*CommonUtil.dbUtil = new DbUtil(context);
        CommonUtil.dbUtil.open();
        CommonUtil.dbHelper = new DbHelper(context);*/

        text = findViewById(R.id.text);
        txt_no_records = findViewById(R.id.txt_no_records);
        btnSelection = (Button) findViewById(R.id.btn_next);
        btn_prev = (Button) findViewById(R.id.btn_show);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        img_clearsearch = (ImageView) findViewById(R.id.img_clearsearch);
        etsearch = (EditText) findViewById(R.id.edt_search);
        txt_Starttime = findViewById(R.id.txt_starttime);
        txt_Jobid = findViewById(R.id.txt_jobid);
        img_Pause = findViewById(R.id.ic_paused);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        job_id = sharedPreferences.getString("job_id", "default value");
        service_title = sharedPreferences.getString("service_title", "default value");
        str_StartTime = sharedPreferences.getString("starttime", "");
        str_StartTime = str_StartTime.replaceAll("[^0-9-:]", " ");
        Log.e("JobID", "" + job_id);
        Log.e("Name", "" + service_title);
        Log.e("Start Time", str_StartTime);
        compno = sharedPreferences.getString("compno", "123");
        sertype = sharedPreferences.getString("sertype", "123");
        // CommonUtil.dbUtil.reportDeletePreventiveListDelete(job_id,service_title);

        Latitude = Double.parseDouble(sharedPreferences.getString("lati", "0.00000"));
        Logitude = Double.parseDouble(sharedPreferences.getString("long", "0.00000"));
        address = sharedPreferences.getString("add", "Chennai");
        Log.e("Location", "" + Latitude + "" + Logitude + "" + address);

        txt_Jobid.setText("Job ID : " + job_id);
        txt_Starttime.setText("Start Time : " + str_StartTime);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bd_dta = extras.getString("bd_details");
            status = extras.getString("status");
            Log.e("Status", status);
        }

        if (extras != null) {
            //  job_id = extras.getString("job_id");
        }

        Spannable name_Upload = new SpannableString("Description ");
        name_Upload.setSpan(new ForegroundColorSpan(Feedback_GroupActivity.this.getResources().getColor(R.color.colorAccent)), 0, name_Upload.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(name_Upload);
        Spannable name_Upload1 = new SpannableString("*");
        name_Upload1.setSpan(new ForegroundColorSpan(Color.RED), 0, name_Upload1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(name_Upload1);

        btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
        btn_prev.setTextColor(getResources().getColor(R.color.white));
        btn_prev.setEnabled(true);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.e("Network", "" + networkStatus);
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            NoInternetDialog();
        } else {

            retrive_LocalValue();

            img_Pause.setVisibility(View.VISIBLE);
        }

//        getBDDetails();

        /*if (status.equals("new")) {


        } else {

            if (!Objects.equals(networkStatus, "Not connected to Internet")) {

                retrive_LocalValue();
            } else {
                NoInternetDialog();
            }
        }*/

        btnSelection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!feedback_group_list.isEmpty()) {
                    feedback_group = "";
                    for (String feedback : feedback_group_list) {
                        feedback_group = String.format("%s,%s", feedback_group, feedback);
                    }

                    str_but_type = "btn_next";
                    str_job_status = "Job Paused";
                    Job_status_update();
                    createLocalvalue();
                } else {
                    alertDialog = new AlertDialog.Builder(Feedback_GroupActivity.this)
                            .setTitle("Please Selected Value")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();

//                Intent send = new Intent(Feedback_GroupActivity.this, BD_DetailsActivity.class);
//                send.putExtra("bd_details",bd_dta);
//                send.putExtra("job_id",job_id);
//                startActivity(send);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();
//
//                Intent send = new Intent(Feedback_GroupActivity.this, BD_DetailsActivity.class);
//                send.putExtra("bd_details",bd_dta);
//                send.putExtra("job_id",job_id);
//                startActivity(send);
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
                                str_but_type = "img_Pause";
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

        etsearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String Searchvalue = etsearch.getText().toString();

                recyclerView.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Searchvalue);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Searchvalue = etsearch.getText().toString();

                if (Searchvalue.equals("")) {
                    recyclerView.setVisibility(View.VISIBLE);
//                    jobFindResponseCall(job_id);
                    img_clearsearch.setVisibility(View.INVISIBLE);
                } else {
                    //   Log.w(TAG,"Search Value---"+Searchvalue);
                    filter(Searchvalue);
                }
            }
        });

        img_clearsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etsearch.setText("");
                recyclerView.setVisibility(View.VISIBLE);
//                jobFindResponseCall(job_id);
                img_clearsearch.setVisibility(View.INVISIBLE);
            }
        });

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

    public void NoInternetDialog() {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
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

    private void filter(String s) {
        List<Feedback_GroupResponse.DataBean> filteredlist = new ArrayList<>();
        for (Feedback_GroupResponse.DataBean item : dataBeanList) {
            if (item.getTitle().toLowerCase().contains(s.toLowerCase())) {
                Log.w(TAG, "filter----" + item.getTitle().toLowerCase().contains(s.toLowerCase()));
                filteredlist.add(item);

            }
        }
        if (filteredlist.isEmpty()) {
//            Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            activityBasedListAdapter.filterList(filteredlist);
        }

    }

    private void jobFindResponseCall(String job_no) {
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Feedback_GroupResponse> call = apiInterface.feedback_groupResponseCall(RestUtils.getContentType(), serviceRequest(job_no));
        Log.w(TAG, "Jobno Find Response url  :%s" + " " + call.request().url().toString());
        call.enqueue(new Callback<Feedback_GroupResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Feedback_GroupResponse> call, @NonNull Response<Feedback_GroupResponse> response) {
                Log.w(TAG, "Jobno Find Response" + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    message = response.body().getMessage();
                    Log.d("message", message);

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            dataBeanList = response.body().getData();

                            setView(dataBeanList);
                            Log.i(TAG, "onResponse: dataBeanList -> " + new Gson().toJson(dataBeanList));
                        }
                    } else if (400 == response.body().getCode()) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("There is already a user registered with this email id. Please add new email id")) {

                        }
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                        Toasty.warning(getApplicationContext(), "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                } else {
                    ErrorMsgDialog("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Feedback_GroupResponse> call, @NonNull Throwable t) {
                Log.e("Jobno Find ", "--->" + t.getMessage());
                ErrorMsgDialog(t.getMessage());
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

    private Feedback_GroupRequest serviceRequest(String job_no) {
        Feedback_GroupRequest service = new Feedback_GroupRequest();
        service.setJob_id(job_no);
        Log.w(TAG, "Jobno Find Request " + new Gson().toJson(service));
        return service;
    }

    private void Job_status_update() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Job_status_updateResponse> call = apiInterface.job_status_updateResponseCall(getContentType(), job_status_updateRequest());
        Log.i(TAG, "Job_status_update: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<Job_status_updateResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Job_status_updateResponse> call, @NonNull Response<Job_status_updateResponse> response) {

                Log.i(TAG, "Job_status_update: onResponse: " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            Log.d("msg", message);
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
            public void onFailure(@NonNull Call<Job_status_updateResponse> call, @NonNull Throwable t) {
                Log.e("OTP", "--->" + t.getMessage());
                ErrorMsgDialog(t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
        custom.setJOB_START_LAT(Latitude);
        custom.setJOB_START_LONG(Logitude);
        custom.setJOB_LOCATION(address);

        Log.i(TAG, "job_status_updateRequest: compno -> " + compno);
        Log.i(TAG, "job_status_updateRequest: sertype -> " + sertype);
        Log.i(TAG, "job_status_updateRequest: " + new Gson().toJson(custom));
        return custom;
    }

    private void setView(List<Feedback_GroupResponse.DataBean> dataBeanList) {

//        getFeedbackGroup();

        for (int i = 0; i < dataBeanList.size(); i++) {
            for (String feedback_group_code : feedback_group_list) {
                if (dataBeanList.get(i).getCodes().equalsIgnoreCase(feedback_group_code)) {
                    dataBeanList.get(i).setSelected(true);
                }
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityBasedListAdapter = new Feedback_GroupAdapter(getApplicationContext(), dataBeanList, mydata, this);
        recyclerView.setAdapter(activityBasedListAdapter);
    }

    private void createLocalvalue() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Breakdowm_Submit_Request breakdowmSubmitRequest = createLocalRequest();

        if (breakdowmSubmitRequest != null) {
            Call<SuccessResponse> call = apiInterface.createLocalvalueBD(getContentType(), breakdowmSubmitRequest);
            Log.i(TAG, "createLocalvalue: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<SuccessResponse>() {
                @Override
                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                    Log.i(TAG, "onResponse: createLocalvalue -> " + new Gson().toJson(response.body()));

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
                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: createLocalvalue -> " + t.getMessage());
                    ErrorMsgDialog(t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void moveNext() {

        /*String data = "";
        for (int i = 0; i < dataBeanList.size(); i++) {
            Feedback_GroupResponse.DataBean singleStudent = dataBeanList.get(i);
            if (singleStudent.isSelected()) {
                data = data + "," + dataBeanList.get(i).getCodes().toString();
            }
        }

//        getFeedbackGroup();

        ArrayList<String> outputList = new ArrayList<String>();
        for (String item : mydata) {
            //outputList.add("\""+item+"\"");
            outputList.add("" + item + "");
            // outputList.remove("null");
        }

//        pre_check = pre_check.replaceAll("\\[", "").replaceAll("\\]", "");
//        System.out.println("EEEEEEEEEEE" + ddd);
        Log.e("FEEDBACK GROUP", String.valueOf(mydata));
        // Log.e("FEEDBACK GROUP", String.valueOf(outputList));

        // sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);*/
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("feedbackgroup", feedback_group);
        editor.putString("Hi", "Nishanth");
        Log.e("FeedBack Group", "dsfds");
        editor.apply();

        //  if(data.equals("")){
//        if (mydata.isEmpty()) {
        if (feedback_group.isEmpty()) {

            alertDialog = new AlertDialog.Builder(Feedback_GroupActivity.this)
                    .setTitle("Please Selected Value")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    })
                    .show();
        } else {
            Intent send = new Intent(Feedback_GroupActivity.this, Feedback_DetailsActivity.class);
            send.putExtra("feedback_group", feedback_group);
            send.putExtra("bd_details", bd_dta);
            send.putExtra("job_id", job_id);
            send.putExtra("status", status);
            startActivity(send);
        }

    }

    private Breakdowm_Submit_Request createLocalRequest() {

        getFeedbackGroup();

        Log.i(TAG, "createLocalRequest: feedback_group -> " + feedback_group);

        feedback_group = String.valueOf(feedback_group).replaceAll("\n", "").replaceAll("", "");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        Breakdowm_Submit_Request submitDailyRequest = new Breakdowm_Submit_Request();

        if (nullPointerValidator(str_BDDetails)) {
            submitDailyRequest.setBd_details(str_BDDetails);
            //submitDailyRequest.setFeedback_details(sstring);
            submitDailyRequest.setFeedback_details("");
            submitDailyRequest.setCode_list(feedback_group);
            submitDailyRequest.setCode_list1(feedback_group_list);
            submitDailyRequest.setFeedback_remark_text("");
            submitDailyRequest.setMr_status("");
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

            Log.i(TAG, "createLocalRequest: compno -> " + compno);
            Log.i(TAG, "createLocalRequest: sertype -> " + sertype);
            Log.i(TAG, "createLocalRequest: Breakdowm_Submit_Request -> " + new Gson().toJson(submitDailyRequest));
            return submitDailyRequest;
        } else {
            return null;
        }
    }

    @SuppressLint("LongLogTag")
    private void retrive_LocalValue() {

        APIInterface apiInterface = RetrofitClient.getClient().create((APIInterface.class));
        Call<RetriveLocalValueBRResponse> call = apiInterface.retriveLocalValueBRCall(getContentType(), localRequest());

        Log.i(TAG, "retrive_LocalValue: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<RetriveLocalValueBRResponse>() {
            @Override
            public void onResponse(@NonNull Call<RetriveLocalValueBRResponse> call, @NonNull Response<RetriveLocalValueBRResponse> response) {

                Log.i(TAG, "retrive_LocalValue: onResponse: RetriveLocalValueBRResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    message = response.body().getMessage();

                    if (response.body().getCode() == 200) {

                        if (response.body().getData() != null) {

                            str_BDDetails = response.body().getData().getBd_details();
                            feedback_group = response.body().getData().getCode_list();
                            feedback_group_list = response.body().getData().getCode_list1();
                            jobFindResponseCall(job_id);
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
        Log.e("Request Data ", "" + new Gson().toJson(custom));
        return custom;

    }

    /*private void getFeedbackGroup() {

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
    }*/

    private void getFeedbackGroup() {

        feedback_group = "";

        for (Feedback_GroupResponse.DataBean dataList : dataBeanList) {
            if (dataList.isSelected()) {
                feedback_group = feedback_group + "," + dataList.getCodes();
            }
        }

    }

    @Override
    public void onBackPressed() {

        // super.onBackPressed();
        Intent intent = new Intent(context, BD_DetailsActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);

    }

    @Override
    public void itemClickCheckBoxFeedbackGroupDetails(int newPosition, Feedback_GroupResponse.DataBean selectedItem) {

        Log.i(TAG, "itemClickCheckBoxFeedbackGroupDetails: newPosition -> " + newPosition + " selectedItem -> " + new Gson().toJson(selectedItem));

        Log.i(TAG, "itemClickCheckBoxFeedbackGroupDetails: dataBeanList -> " + new Gson().toJson(dataBeanList));

        if (feedback_group_list.contains(selectedItem.getCodes())) {
            feedback_group_list.remove(selectedItem.getCodes());
        } else {
            feedback_group_list.add(selectedItem.getCodes());
        }

        Log.i(TAG, "itemClickCheckBoxFeedbackGroupDetails: feedback_group_list -> " + new Gson().toJson(feedback_group_list));
    }
}