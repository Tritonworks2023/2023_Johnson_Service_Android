package com.triton.johnson_tap_app.Service_Activity.Preventive_Services;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.Db.CommonUtil;
import com.triton.johnson_tap_app.Db.DbHelper;
import com.triton.johnson_tap_app.Db.DbUtil;
import com.triton.johnson_tap_app.GetFieldListResponse;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.ReAdapter;
import com.triton.johnson_tap_app.RestUtils;
import com.triton.johnson_tap_app.Service_Activity.ServicesActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.GetNumberListener;
import com.triton.johnson_tap_app.interfaces.GetSpinnerListener;
import com.triton.johnson_tap_app.interfaces.GetStringListener;
import com.triton.johnson_tap_app.requestpojo.GetFieldListRequest;
import com.triton.johnson_tap_app.requestpojo.Job_status_updateRequest;
import com.triton.johnson_tap_app.requestpojo.Preventive_Submit_Request;
import com.triton.johnson_tap_app.responsepojo.Job_status_updateResponse;
import com.triton.johnson_tap_app.responsepojo.RetriveResponsePR;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recycler_SpinnerActivity extends AppCompatActivity implements GetSpinnerListener, GetStringListener, GetNumberListener {

    private final String TAG = Recycler_SpinnerActivity.class.getSimpleName();
    public int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE = 6;
    public int ITEMS_REMAINING;
    public int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
    List<GetFieldListResponse.DataBean> dataBeanList;
    List<RetriveResponsePR.FieldValueDatum> datumList;
    ReAdapter activityBasedListAdapter;
    String Data_values;
    Button btn_prev;
    Button btn_next;
    Button btn_success;
    Button btn_complete;
    Button btn_pending;
    Button btn_clear;
    String Result1;
    Dialog dialog;
    String networkStatus = "";
    String string_value, message, service_id, activity_id, job_id = "", group_id, status = "", job_detail_no, value = "";
    String s1, _id, statustype = "", service_title = "", data, data1, data2, data3, data4, field_value, field_name, field_comments, field_cat_id, field_group_id, str, str1, str2, str3, str4;
    AlertDialog alertDialog;
    ImageView iv_back, img_Pause;
    LinearLayout footerView;
    String List = "", se_id, se_user_mobile_no, se_user_name, compno, sertype, str_job_status;
    Context context;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    TextView txt_Jobid, txt_Starttime;
    String str_StartTime;
    int PageNumber = 2;
    int SubPage_Number = 0;
    ArrayList<String> mydata = new ArrayList<>();
    double Latitude, Logitude;
    String address = "";
    ArrayList<String> myData = new ArrayList<>();
    String form1_value = "";
    String form1_name;
    String form1_comments;
    String form1_cat_id;
    String form1_group_id;
    ArrayList<String> myFieldValue = new ArrayList();
    ArrayList<String> myname = new ArrayList();
    ArrayList<String> comments = new ArrayList();
    ArrayList<String> catid = new ArrayList();
    ArrayList<String> groupid = new ArrayList();
    private boolean firstHit = true;
    private RecyclerView recyclerView;
    private int currentPage = 0;
    private int totalPages;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recycler_spinner);
        context = this;

        CommonUtil.dbUtil = new DbUtil(context);
        CommonUtil.dbUtil.open();
        CommonUtil.dbHelper = new DbHelper(context);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_success = (Button) findViewById(R.id.btn_success);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        btn_pending = (Button) findViewById(R.id.btn_pending);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        footerView = (LinearLayout) findViewById(R.id.footerView);
        img_Pause = findViewById(R.id.img_paused);
        txt_Starttime = findViewById(R.id.txt_starttime);
        txt_Jobid = findViewById(R.id.txt_jobid);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("service_title")) {
                service_title = extras.getString("service_title");
            }
            if (extras.containsKey("job_id")) {
                job_id = extras.getString("job_id");
            }
            if (extras.containsKey("value")) {
                value = extras.getString("value");
            }
            if (extras.containsKey("status")) {
                status = extras.getString("status");
            }

            Log.i(TAG, "onCreate: status -> " + status +
                    " value -> " + value + "job_id -> " + job_id +
                    " service_title -> " + service_title);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        List = sharedPreferences.getString("List", "");
        service_title = sharedPreferences.getString("service_title", "default value");
        job_id = sharedPreferences.getString("job_id", "L1234");
        statustype = sharedPreferences.getString("statustype", "");
        compno = sharedPreferences.getString("compno", "123");
        sertype = sharedPreferences.getString("sertype", "123");
        Log.i(TAG, "onCreate: service_title -> " + service_title + " job_id -> " + job_id + " statustype -> " + statustype);

        str_StartTime = sharedPreferences.getString("starttime", "");
        str_StartTime = str_StartTime.replaceAll("[^0-9-:]", " ");
        Log.e("Start Time", str_StartTime);
        txt_Jobid.setText("Job ID : " + job_id);
        txt_Starttime.setText("Start Time : " + str_StartTime);
        currentPage = sharedPreferences.getInt("currentpage", 0);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

//        Intent intent = getIntent();
//        Bundle args = intent.getBundleExtra("BUNDLE");
//        ArrayList<String > object = (ArrayList<String>) args.getSerializable("ARRAYLIST");
//        Log.e("Current Page",""+currentPage);

        Log.i(TAG, "onCreate: List -> " + List);
        Log.i(TAG, "onCreate: currentPage - " + currentPage);

        Latitude = Double.parseDouble(sharedPreferences.getString("lati", "0.00000"));
        Logitude = Double.parseDouble(sharedPreferences.getString("long", "0.00000"));
        address = sharedPreferences.getString("add", "Chennai");
        Log.i(TAG, "onCreate: Latitude -> " + Latitude + " Logitude -> " + Logitude + " address -> " + address);

//        getPreventivecheck();
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.e("Network", "" + networkStatus);
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            if (status.equals("new")) {
                jobFindResponseCall();
                //   getSpinnerData();
            } else {
                retriveLocalvalue();
            }
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
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

                                List<GetFieldListResponse.DataBean> dataBeanListS = new ArrayList<>();
                                int startItem = currentPage * ITEMS_PER_PAGE;

                                myFieldValue = new ArrayList();
                                myname = new ArrayList();
                                comments = new ArrayList();
                                catid = new ArrayList();
                                groupid = new ArrayList();

                                if (dataBeanList != null && !dataBeanList.isEmpty()) {

                                    for (GetFieldListResponse.DataBean dataBean : dataBeanList) {
                                        form1_value = dataBean.getField_value().toString();
                                        myFieldValue.add(form1_value);
                                        form1_name = dataBean.getField_name().toString();
                                        myname.add(form1_name);
                                        form1_comments = dataBean.getField_comments().toString();
                                        comments.add(form1_comments);
                                        form1_cat_id = dataBean.getCat_id().toString();
                                        catid.add(form1_cat_id);
                                        form1_group_id = dataBean.getGroup_id().toString();
                                        groupid.add(form1_group_id);
                                    }
                                }


                                /*if (currentPage == 0) {
                                    for (int j = 0; j < 6; j++) {

                                        form1_value = dataBeanList.get(j).getField_value().toString();
                                        myFieldValue.add(form1_value);
                                        form1_name = dataBeanList.get(j).getField_name().toString();
                                        myname.add(form1_name);
                                        form1_comments = dataBeanList.get(j).getField_comments().toString();
                                        comments.add(form1_comments);
                                        form1_cat_id = dataBeanList.get(j).getCat_id().toString();
                                        catid.add(form1_cat_id);
                                        form1_group_id = dataBeanList.get(j).getGroup_id().toString();
                                        groupid.add(form1_group_id);

                                        Log.e("Resuktt", form1_value);
                                        Log.e("Resuktt", "" + myFieldValue.size());
                                    }

                                } else {
                                    int enditem = (currentPage + 1) * ITEMS_PER_PAGE;
                                    Log.w(TAG, "currentPage else currentPage : " + currentPage + " startItem : " + startItem + " enditem : " + enditem + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE);

                                    Log.w(TAG, "btnnext enditem : " + enditem);

                                    for (int j = startItem; j < dataBeanList.size(); j++) {
                                        //   Result1 = dataBeanList.get(i).getField_value().toString();

                                        form1_value = dataBeanList.get(j).getField_value().toString();
                                        myFieldValue.add(form1_value);
                                        form1_name = dataBeanList.get(j).getField_name().toString();
                                        myname.add(form1_name);
                                        form1_comments = dataBeanList.get(j).getField_comments().toString();
                                        comments.add(form1_comments);
                                        form1_cat_id = dataBeanList.get(j).getCat_id().toString();
                                        catid.add(form1_cat_id);
                                        form1_group_id = dataBeanList.get(j).getGroup_id().toString();
                                        groupid.add(form1_group_id);

                                        Log.e("Resuktt", form1_value);
                                    }
                                }*/

                                Toast.makeText(context, "Lat : " + Latitude + "Long : " + Logitude + "Add : " + address, Toast.LENGTH_LONG).show();

                                str_job_status = "Job Paused";
                                Job_status_update();
                                createLocalValue();
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

        btn_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (dataBeanList != null && !dataBeanList.isEmpty()) {
                    Log.i(TAG, "onClick: currentPage -> " + currentPage);

                    boolean flag = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }, 100);

                    //Log.w(TAG, "btnnext currentPage : " + currentPage);
                    int currentpagesize = currentPage;
                    //Log.w(TAG, "btnnext totalPages  : " + totalPages+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS+" currentpagesize : "+currentpagesize);
                    List<GetFieldListResponse.DataBean> dataBeanListS = new ArrayList<>();
                    int startItem = currentPage * ITEMS_PER_PAGE;
                    int enditem = (currentPage + 1) * ITEMS_PER_PAGE;

                    Log.i(TAG, "btn_next: onClick: currentPage - " + currentPage + " ITEMS_PER_PAGE - " + ITEMS_PER_PAGE);
                    Log.i(TAG, "btn_next: onClick: startItem - " + startItem + " enditem - " + enditem);

                    myFieldValue = new ArrayList();
                    myname = new ArrayList();
                    comments = new ArrayList();
                    catid = new ArrayList();
                    groupid = new ArrayList();

                    for (int i = 0; i < enditem; i++) {
                        form1_value = dataBeanList.get(i).getField_value().toString();
                        myFieldValue.add(form1_value);
                        form1_name = dataBeanList.get(i).getField_name().toString();
                        myname.add(form1_name);
                        form1_comments = dataBeanList.get(i).getField_comments().toString();
                        comments.add(form1_comments);
                        form1_cat_id = dataBeanList.get(i).getCat_id().toString();
                        catid.add(form1_cat_id);
                        form1_group_id = dataBeanList.get(i).getGroup_id().toString();
                        groupid.add(form1_group_id);

                        if (dataBeanList.get(i).getField_value().isEmpty()
                                || dataBeanList.get(i).getField_value().equalsIgnoreCase("Select")) {
                            if (dataBeanList.get(i).getField_type() != null && dataBeanList.get(i).getField_type().equalsIgnoreCase("Lift")) {
                                dataBeanList.get(i).setField_value("LIFT");
                            }
                            flag = false;
                        }
                    }

                    if (flag) {
                        currentPage += 1;
                        startItem = currentPage * ITEMS_PER_PAGE;
                        Log.w(TAG, "currentPage flag : " + currentPage + " startItem : " + startItem + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE);
                    }

                    int condition = 0;

                    ITEMS_REMAINING = ITEMS_REMAINING - ITEMS_PER_PAGE;

                    Log.w(TAG, "btnnext ITEMS_REMAINING : " + ITEMS_REMAINING);
                    Log.w(TAG, "btnnext TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                    double LAST_PAGE = ((double) TOTAL_NUM_ITEMS / ITEMS_PER_PAGE);

                    Log.w(TAG, "btnnext LAST_PAGE : " + LAST_PAGE + " currentPage : " + currentPage);

                    if (currentPage == LAST_PAGE - 1) {
                        Log.w(TAG, "btnnext if condition----->");
                        Log.w(TAG, "btnnext if ITEMS_REMAINING----->" + ITEMS_REMAINING);

                        if (ITEMS_REMAINING == 0) {
                            condition = startItem + ITEMS_PER_PAGE;
                            Log.w(TAG, "btnnext if condition----->" + condition);
                        } else {
                            condition = startItem + ITEMS_REMAINING;
                            Log.w(TAG, "btnnext if else condition----->" + condition);
                        }
                        Log.w(TAG, "btnnext startItem----->" + startItem + " condition -->" + condition);

                        for (int i = startItem; i < dataBeanList.size(); i++) {
                            dataBeanListS.add(dataBeanList.get(i));
                        }
                        setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                        Log.w(TAG, "btnnext  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS : " + new Gson().toJson(dataBeanListS));

                        // enableDisableButtons();
                        // rv.setAdapter(new MyAdapter(MainActivity.this, p.generatePage(currentPage)));
                        btn_next.setEnabled(false);

                        btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
                        btn_prev.setTextColor(getResources().getColor(R.color.white));
                        btn_prev.setEnabled(true);
                        btn_next.setVisibility(View.GONE);
                        btn_success.setVisibility(View.VISIBLE);

                    } else {
                        if (flag) {
                            Log.w(TAG, "btnnext else condition----->");
                            condition = startItem + ITEMS_PER_PAGE;
                            Log.w(TAG, "btnnext  else startItem : " + startItem + " condition : " + condition + "size : " + dataBeanList.size());

                            for (int i = startItem; i < dataBeanList.size(); i++) {
                                Log.w(TAG, "dataBeanList.get(i) : " + dataBeanList.get(i));
                                dataBeanListS.add(dataBeanList.get(i));
                            }

                            Log.w(TAG, "btnnext else dataBeanList" + new Gson().toJson(dataBeanListS));
                            setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                            Log.w(TAG, "btnnext else setView " + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS :  " + new Gson().toJson(dataBeanListS));

                            toggleButtons();
                            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                            Log.e("Network", "" + networkStatus);
                            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                                NoInternetDialog();
                            } else {
                                createLocalFormcheck();
                            }
                        } else {

                            showErrorLoading();
                        }
                    }

                } else {
                    ErrorMyLocationAlert("Please Try Again Later");
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_complete.setVisibility(View.GONE);
                btn_pending.setVisibility(View.GONE);

                recyclerView.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }, 50);
                currentPage -= 1;
                List<GetFieldListResponse.DataBean> dataBeanListS = new ArrayList<>();
                int startItem = currentPage * ITEMS_PER_PAGE;
                int condition;
                int ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
                int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
                if (currentPage == 0 || (currentPage >= 1 && currentPage <= totalPages)) {
                    condition = startItem + ITEMS_PER_PAGE;
                } else {
                    condition = startItem + ITEMS_REMAINING;
                }
                for (int i = startItem; i < condition; i++) {
                    Log.w(TAG, "generatePage: dataBeanList" + new Gson().toJson(dataBeanList.get(i)));
                    dataBeanListS.add(dataBeanList.get(i));
                }

                Log.w(TAG, "btnprev dataBeanList" + new Gson().toJson(dataBeanList));

                Log.w(TAG, "btnprev  setView " + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS : " + new Gson().toJson(dataBeanListS));

                setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                // rv.setAdapter(new MyAdapter(MainActivity.this, p.generatePage(currentPage)));

                toggleButtons();
            }
        });

        btn_success.setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                    Log.e("Hi", "No Internet");
                    progressDialog.dismiss();

                    Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

                } else {
                    Log.e("Hi", "Have Internet");

                    boolean flag = true;
                    for (int i = 0; i < dataBeanList.size(); i++) {

                        Log.w(TAG, "loop fieldvalue : " + dataBeanList.get(i).getField_value() + " i : " + i);
                        if (dataBeanList.get(i).getField_value().isEmpty() || dataBeanList.get(i).getField_value().equalsIgnoreCase("Select Value")) {
                            if (dataBeanList.get(i).getField_type() != null && dataBeanList.get(i).getField_type().equalsIgnoreCase("Lift")) {
                                dataBeanList.get(i).setField_value("LIFT");
                            }/*else if(dataBeanList.get(i).getField_type() !=  null && dataBeanList.get(i).getField_type().equalsIgnoreCase("File upload")){
                                dataBeanList.get(i).setField_value("File upload");
                            }*/
                            flag = false;

                        }
                    }

                    Log.w(TAG, "flag " + flag);

                    if (flag) {

                        Log.e(TAG, "inside");
                        progressDialog.show();

                        for (int i = 0; i < dataBeanList.size(); i++) {

                            data = data + "," + dataBeanList.get(i).getField_value().toString();
                            data1 = data1 + "," + dataBeanList.get(i).getField_name().toString();
                            data2 = data2 + "," + dataBeanList.get(i).getField_comments().toString();
                            data3 = data3 + "," + dataBeanList.get(i).getCat_id().toString();
                            data4 = data4 + "," + dataBeanList.get(i).getGroup_id().toString();

                            field_value = data.replace("null", "");
                            field_name = data1.replace("null", "");
                            field_comments = data2.replace("null", "");
                            field_cat_id = data3.replace("null", "");
                            field_group_id = data4.replace("null", "");

                            myFieldValue.add(field_value);
                            myname.add(field_name);
                            comments.add(field_comments);
                            catid.add(field_cat_id);
                            groupid.add(field_group_id);

                            //      Log.e("Size Value",""+myFieldValue.size());


                            str = field_value;
                            str1 = field_name;
                            str2 = field_comments;
                            str3 = field_cat_id;
                            str4 = field_group_id;
                            str = str.substring(1);
                            str1 = str1.substring(1);
                            str2 = str2.substring(1);
                            str3 = str3.substring(1);
                            str4 = str4.substring(1);

                            //   Log.d("Form1_comments",str2);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Form1_value", str);
                            editor.putString("Form1_name", str1);
                            editor.putString("Form1_comments", str2);
                            editor.putString("Form1_cat_id", str3);
                            editor.putString("Form1_group_id", str4);
                            editor.apply();

                            //  createLocalValue();

                            //  createLocalFormcheck();

                        }
                        Log.e("Form Final Size ", "" + myFieldValue.size());


                        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                        Log.e("Network", "" + networkStatus);
                        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                            NoInternetDialog();

                        } else {

                            createLocalFormcheck();
                            long delayInMillis = 5000;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            }, delayInMillis);

                            Intent send = new Intent(Recycler_SpinnerActivity.this, Material_Request_PreventiveActivity.class);
//                            send.putExtra("job_id",job_id);
//                            send.putExtra("value",value);
//                            send.putExtra("service_title",service_title);
//                            send.putExtra("Form1_value",str);
//                            send.putExtra("Form1_name",str1);
//                            send.putExtra("Form1_comments",str2);
//                            send.putExtra("Form1_cat_id",str3);
//                            send.putExtra("Form1_group_id",str4);
                            send.putExtra("status", status);
                            startActivity(send);
                        }

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "please enter all required data", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        // toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                        toast.show();
                    }
                }
            }
        });
    }

    private void directToPage() {

        firstHit = false;
        boolean flag = true;
        int condition = 0;

        Log.i(TAG, "directToPage: currentPage : " + currentPage + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE);

        int startItem = currentPage * ITEMS_PER_PAGE;
        List<GetFieldListResponse.DataBean> dataBeanListS = new ArrayList<>();

        if (flag) {

            condition = startItem + ITEMS_PER_PAGE;
            Log.i(TAG, "directToPage: startItem : " + startItem + " condition : " + condition + " dataBeanList.size : " + dataBeanList.size());

            if (startItem < 0) {
                startItem = 0;
            }

            for (int i = startItem; i < dataBeanList.size(); i++) {
                Log.w(TAG, "dataBeanList.get(i) : " + dataBeanList.get(i));
                dataBeanListS.add(dataBeanList.get(i));
            }

            Log.w(TAG, "btnnext else dataBeanList" + new Gson().toJson(dataBeanListS));

            setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
            Log.w(TAG, "btnnext else setView " + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS :  " + new Gson().toJson(dataBeanListS));

            toggleButtons();
        }
    }

    @SuppressLint("LongLogTag")
    private void retriveLocalvalue() {

        progressDialog.show();
        APIInterface apiInterface = RetrofitClient.getClient().create((APIInterface.class));
        Call<RetriveResponsePR> call = apiInterface.retriveLocalValuePRCall(RestUtils.getContentType(), localRequest());

        Log.i(TAG, "retriveLocalvalue: URL - " + call.request().url().toString());

        call.enqueue(new Callback<RetriveResponsePR>() {
            @Override
            public void onResponse(@NonNull Call<RetriveResponsePR> call, @NonNull Response<RetriveResponsePR> response) {

                progressDialog.dismiss();
                Log.i(TAG, "retriveLocalvalue: onResponse: RetriveResponsePR - " + new Gson().toJson(response.body()));

                if (statustype == null || statustype.isEmpty()) {
                    if (response.body().getData().getJob_status_type() != null && !response.body().getData().getJob_status_type().isEmpty()) {
                        statustype = response.body().getData().getJob_status_type();
                    }
                }

                Log.i(TAG, "retriveLocalvalue: onResponse: statustype- " + statustype);
                if (List.isEmpty() && response.body().getData().getJob_date() != null && !response.body().getData().getJob_date().isEmpty()) {
                    List = response.body().getData().getJob_date();
                }
                Log.i(TAG, "retriveLocalvalue: onResponse: List -> " + List + " statustype -> " + statustype);

                checklocalvaluecall();
            }

            @Override
            public void onFailure(@NonNull Call<RetriveResponsePR> call, @NonNull Throwable t) {

                progressDialog.dismiss();
                Log.e(TAG, "retriveLocalvalue: onFailure: retriveLocalvalue -> " + t.getMessage());
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
        Log.i(TAG, "localRequest: Job_status_updateRequest - " + new Gson().toJson(custom));
        return custom;
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

    private void getPreventivecheck() {

        Cursor cur = CommonUtil.dbUtil.getMonthlist(job_id, service_title, "1");
        Log.e("Checklist", "" + cur.getCount());
        mydata = new ArrayList<>();
        if (cur.getCount() > 0 && cur.moveToFirst()) {

            do {
                @SuppressLint("Range")
                String abc = cur.getString(cur.getColumnIndex(DbHelper.MONTH));
                Log.e("Datas", "" + abc);
                mydata.add(abc);
            } while (cur.moveToNext());

        } else {

            Log.e("Datasss", "" + cur);

        }

        ArrayList<String> outputList = new ArrayList<String>();
        for (String item : mydata) {
            //outputList.add("\""+item+"\"");
            outputList.add("" + item + "");
        }
        List = String.valueOf(outputList);
        Log.i(TAG, "getPreventivecheck: List -> " + List);

    }

    private void createLocalFormcheck() {

        progressDialog.show();
        Preventive_Submit_Request preventiveSubmitRequest = createLocalRequest();

        if (preventiveSubmitRequest != null) {
            APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
            Call<SuccessResponse> call = apiInterface.createLocalValueformcheckPM(RestUtils.getContentType(), preventiveSubmitRequest);

            Log.i(TAG, "createLocalFormcheck: URL - " + call.request().url().toString());
            call.enqueue(new Callback<SuccessResponse>() {
                @Override
                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                    Log.i(TAG, "createLocalFormcheck: onResponse: SuccessResponse - " + new Gson().toJson(response.body()));

                    progressDialog.dismiss();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {

                                Log.d("msg", message);

                            }
                        } else {
                            Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "createLocalFormcheck: onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
        }

    }

    private void Job_status_update() {

        progressDialog.show();
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Job_status_updateResponse> call = apiInterface.job_status_update_PreventiveResponseCall(com.triton.johnson_tap_app.utils.RestUtils.getContentType(), job_status_updateRequest());

        Log.i(TAG, "Job_status_update: URL - " + call.request().url().toString());
        call.enqueue(new Callback<Job_status_updateResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Job_status_updateResponse> call, @NonNull retrofit2.Response<Job_status_updateResponse> response) {
                Log.i(TAG, "Job_status_update: onResponse: Job_status_updateResponse - " + new Gson().toJson(response.body()));

                progressDialog.dismiss();
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            Log.d("msg", message);
                        }


                    } else {
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Job_status_updateResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Job_status_update: onFailure: " + t.getMessage());
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
        custom.setJOB_START_LONG(Logitude);
        custom.setJOB_START_LAT(Latitude);
        custom.setJOB_LOCATION(address);

        Log.i(TAG, "job_status_updateRequest: compno - " + compno);
        Log.i(TAG, "job_status_updateRequest: sertype - " + sertype);
        Log.i(TAG, "job_status_updateRequest: Job_status_updateRequest - " + new Gson().toJson(custom));
        return custom;
    }

    private void checklocalvaluecall() {

        progressDialog.show();

        GetFieldListRequest getFieldListRequest = getChecklistRequest();

        if (getFieldListRequest != null) {

            APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
            Call<GetFieldListResponse> call = apiInterface.checkLocalValuecall(RestUtils.getContentType(), getFieldListRequest);
            Log.w(TAG, "Check Local Response url  :%s" + " " + call.request().url().toString());

            call.enqueue(new Callback<GetFieldListResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {

                    progressDialog.dismiss();
                    Log.i(TAG, "onResponse: checklocalvaluecall GetFieldListResponse - " + new Gson().toJson(response.body()));
                    if (response.body() != null) {

                        String message = response.body().getMessage();
                        Log.d("message", message);

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                dataBeanList = response.body().getData();
                                footerView.setVisibility(View.VISIBLE);
                                if (dataBeanList.size() < 6 || dataBeanList.size() == 6) {
                                    btn_prev.setVisibility(View.INVISIBLE);
                                    btn_next.setVisibility(View.GONE);
                                    btn_success.setVisibility(View.VISIBLE);
                                }

                                totalPages = dataBeanList.size() / 6;
                                TOTAL_NUM_ITEMS = dataBeanList.size();
                                Log.i(TAG, "checklocalvaluecall: onResponse: totalPages - " + totalPages + "  TOTAL_NUM_ITEMS - " + TOTAL_NUM_ITEMS);

                                ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;

                                Log.w(TAG, " getfieldListResponseCall  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));

                                setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);

//                            if (dataBeanList != null && dataBeanList.size() > 0) {
//                                footerView.setVisibility(View.VISIBLE);
//                                if (dataBeanList.size() < 6 || dataBeanList.size() == 6) {
//                                    btn_prev.setVisibility(View.INVISIBLE);
//                                    btn_next.setVisibility(View.GONE);
//                                    btn_success.setVisibility(View.VISIBLE);
//                                }
//
//                                totalPages = dataBeanList.size() / 6;
//                                TOTAL_NUM_ITEMS = dataBeanList.size();
//                                Log.w(TAG, "totalPages  : " + totalPages + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);
//
//                                ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;
//
//                                Log.w(TAG, " getfieldListResponseCall  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));
//
//
//                                setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
//
//                               // txt_no_records.setVisibility(View.GONE);
//                            } else {
//                                footerView.setVisibility(View.GONE);
////                                txt_no_records.setVisibility(View.VISIBLE);
////                                txt_no_records.setText("No Input Fields Available");
//                            }

                            }

                        } else if (400 == response.body().getCode()) {
                            if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("There is already a user registered with this email id. Please add new email id")) {

                            }
                        } else {

                            Toasty.warning(getApplicationContext(), "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetFieldListResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Log.e(TAG, "onFailure: checklocalvaluecall - " + t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
        }
    }

    private GetFieldListRequest getChecklistRequest() {
        GetFieldListRequest getFieldListRequest = new GetFieldListRequest();
        if (job_id != null && !job_id.isEmpty()) {
            if (statustype != null && !statustype.isEmpty()) {
                if (List != null && !List.isEmpty()) {
                    if (se_user_mobile_no != null && !se_user_mobile_no.isEmpty()) {
                        if (compno != null && !compno.isEmpty()) {
                            getFieldListRequest.setJob_id(job_id);
                            getFieldListRequest.setJob_status_type(statustype);
                            getFieldListRequest.setJob_date(List);
                            getFieldListRequest.setUser_mobile_no(se_user_mobile_no);
                            getFieldListRequest.setSMU_SCH_COMPNO(compno);
                            Log.i(TAG, "getChecklistRequest: GetFieldListRequest " + new Gson().toJson(getFieldListRequest));
                            return getFieldListRequest;
                        } else {
                            ErrorMyLocationAlert("Select company no");
                        }
                    } else {
                        ErrorMyLocationAlert("Select mobile no");
                    }
                } else {
                    ErrorMyLocationAlert("Select Job Date");
                }
            } else {
                ErrorMyLocationAlert("Select status type");
            }
        } else {
            ErrorMyLocationAlert("Select Job Id");
        }
        return null;
    }

    private void createLocalValue() {

        progressDialog.show();

        Preventive_Submit_Request preventiveSubmitRequest = createLocalRequest();

        if (preventiveSubmitRequest != null) {

            APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
            Call<SuccessResponse> call = apiInterface.createLocalValueCallPM(RestUtils.getContentType(), preventiveSubmitRequest);

            Log.i(TAG, "createLocalValue: URL - " + call.request().url().toString());

            call.enqueue(new Callback<SuccessResponse>() {
                @Override
                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                    Log.i(TAG, "createLocalValue: onResponse: SuccessResponse - " + new Gson().toJson(response.body()));

                    progressDialog.dismiss();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {

                                Log.d("msg", message);

                                Intent send = new Intent(context, ServicesActivity.class);
                                startActivity(send);

                            }
                        } else {
                            Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "createLocalValue: onFailure: error " + t.getMessage());

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
        }
    }

    private Preventive_Submit_Request createLocalRequest() {

        Preventive_Submit_Request localRequest = new Preventive_Submit_Request();
        if (List != null && !List.isEmpty()) {
            if (job_id != null && !job_id.isEmpty()) {
                if (statustype != null && !statustype.isEmpty()) {
                    if (se_user_mobile_no != null && !se_user_mobile_no.isEmpty()) {
                        if (compno != null && !compno.isEmpty()) {
                            if (sertype != null && !sertype.isEmpty()) {
                                localRequest.setJob_date(List);
                                localRequest.setJob_id(job_id);
                                localRequest.setJob_status_type(statustype);
//                                localRequest.setJob_status_type("MOD");
                                localRequest.setMr_status("");
                                localRequest.setMr_1("");
                                localRequest.setMr_2("");
                                localRequest.setMr_3("");
                                localRequest.setMr_4("");
                                localRequest.setMr_5("");
                                localRequest.setMr_6("");
                                localRequest.setMr_7("");
                                localRequest.setMr_8("");
                                localRequest.setMr_9("");
                                localRequest.setMr_10("");
                                localRequest.setPreventive_check("");
                                localRequest.setAction_req_customer("");
                                localRequest.setPm_status("");
                                localRequest.setTech_signature("");
                                localRequest.setCustomer_name("");
                                localRequest.setCustomer_number("");
                                localRequest.setCustomer_signature("");
                                localRequest.setUser_mobile_no(se_user_mobile_no);
                                localRequest.setSMU_SCH_COMPNO(compno);
                                localRequest.setSMU_SCH_SERTYPE(sertype);
                                localRequest.setPage_number(PageNumber);
                                localRequest.setSubPage_number(currentPage);

                                Log.i(TAG, "createLocalRequest: statustype - " + statustype + " PageNumber - " + PageNumber + " currentPage - " + currentPage
                                        + " compno - " + compno + " sertype - " + sertype + " job_id - " + job_id);

                                List<Preventive_Submit_Request.Field_valueDatum> fielddata = new ArrayList<>();

                                for (int j = 0; j < myFieldValue.size(); j++) {

                                    Preventive_Submit_Request.Field_valueDatum myfiled = new Preventive_Submit_Request.Field_valueDatum();

                                    myfiled.setField_value((String) myFieldValue.get(j));
                                    myfiled.setField_name(myname.get(j));
                                    myfiled.setField_comments(comments.get(j));
                                    myfiled.setField_cat_id(catid.get(j));
                                    myfiled.setField_group_id(groupid.get(j));
                                    fielddata.add(myfiled);
                                }

                                localRequest.setField_valueData(fielddata);

                                Log.i(TAG, "createLocalRequest: Preventive_Submit_Request - " + new Gson().toJson(localRequest));
                                return localRequest;

                            } else {
                                ErrorMyLocationAlert("Select service type");
                            }
                        } else {
                            ErrorMyLocationAlert("Select company no");
                        }
                    } else {
                        ErrorMyLocationAlert("Select mobile no");
                    }
                } else {
                    ErrorMyLocationAlert("Select status type");
                }
            } else {
                ErrorMyLocationAlert("Select job id");
            }
        } else {
            ErrorMyLocationAlert("Select Date");
        }
        return null;
    }

    private void getSpinnerData() {

        Cursor curs = CommonUtil.dbUtil.getSpinnerChecklist(job_id, service_title, "3");
        Log.e("Spinner Count", "" + curs.getCount());

        if (curs.getCount() > 0 && curs.moveToFirst()) {

            do {
                @SuppressLint("Range")
                String spinnerdata = curs.getString(curs.getColumnIndex(DbHelper.CHECKLIST));
                myData.add(spinnerdata);
            } while (curs.moveToNext());
        }

        Log.e("My Data", "" + myData);

    }

    private void toggleButtons() {
        if (currentPage == 0) {
            btn_next.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_success.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setEnabled(true);
            btn_prev.setEnabled(false);
            btn_prev.setBackgroundResource(R.drawable.edit_background_with_border);
            btn_prev.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (currentPage == totalPages) {
            btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_prev.setTextColor(getResources().getColor(R.color.white));
            btn_prev.setEnabled(true);
            btn_next.setVisibility(View.GONE);
            btn_success.setVisibility(View.VISIBLE);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            btn_next.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
            btn_success.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_prev.setTextColor(getResources().getColor(R.color.white));

//            directToPage();
        }
    }

    private void jobFindResponseCall() {
        progressDialog.show();
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFieldListResponse> call = apiInterface.getfieldList_preResponseCall(RestUtils.getContentType(), getFieldListRequest());

        Log.i(TAG, "jobFindResponseCall: URL -> " + call.request().url().toString());
        call.enqueue(new Callback<GetFieldListResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {

                progressDialog.dismiss();
                Log.i(TAG, "jobFindResponseCall: onResponse: GetFieldListResponse --> " + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    String message = response.body().getMessage();
                    Log.d("message", message);

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            dataBeanList = response.body().getData();

                            footerView.setVisibility(View.VISIBLE);
                            if (dataBeanList.size() < 6 || dataBeanList.size() == 6) {
                                btn_prev.setVisibility(View.INVISIBLE);
                                btn_next.setVisibility(View.GONE);
                                btn_success.setVisibility(View.VISIBLE);
                            }

                            totalPages = dataBeanList.size() / 6;
                            TOTAL_NUM_ITEMS = dataBeanList.size();
                            Log.w(TAG, "totalPages  : " + totalPages + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                            ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;

                            Log.i(TAG, " getfieldListResponseCall  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));

                            setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);

//                            if (dataBeanList != null && dataBeanList.size() > 0) {
//                                footerView.setVisibility(View.VISIBLE);
//                                if (dataBeanList.size() < 6 || dataBeanList.size() == 6) {
//                                    btn_prev.setVisibility(View.INVISIBLE);
//                                    btn_next.setVisibility(View.GONE);
//                                    btn_success.setVisibility(View.VISIBLE);
//                                }
//
//                                totalPages = dataBeanList.size() / 6;
//                                TOTAL_NUM_ITEMS = dataBeanList.size();
//                                Log.w(TAG, "totalPages  : " + totalPages + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);
//
//                                ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;
//
//                                Log.w(TAG, " getfieldListResponseCall  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));
//
//
//                                setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
//
//                               // txt_no_records.setVisibility(View.GONE);
//                            } else {
//                                footerView.setVisibility(View.GONE);
////                                txt_no_records.setVisibility(View.VISIBLE);
////                                txt_no_records.setText("No Input Fields Available");
//                            }

                        }

                    } else if (400 == response.body().getCode()) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("There is already a user registered with this email id. Please add new email id")) {

                        }
                    } else {

                        Toasty.warning(getApplicationContext(), "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetFieldListResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "jobFindResponseCall: onFailure: error --> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private GetFieldListRequest getFieldListRequest() {
        GetFieldListRequest getFieldListRequest = new GetFieldListRequest();
        getFieldListRequest.setJob_id(job_id);
        getFieldListRequest.setJob_status_type(statustype);
        getFieldListRequest.setJob_date(List);
        getFieldListRequest.setUser_mobile_no(se_user_mobile_no);
        getFieldListRequest.setSMU_SCH_COMPNO(compno);
        Log.w(TAG, "GetFieldListRequest " + new Gson().toJson(getFieldListRequest));
        return getFieldListRequest;
    }

    private void setView(List<GetFieldListResponse.DataBean> dataBeanList,
                         int ITEMS_PER_PAGE, int TOTAL_NUM_ITEMS) {
        Log.i(TAG, "setView: ITEMS_PER_PAGE - " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS - " + TOTAL_NUM_ITEMS + " dataBeanList - " + new Gson().toJson(dataBeanList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityBasedListAdapter = new ReAdapter(getApplicationContext(), dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS, currentPage, this, this, this);
        recyclerView.setAdapter(activityBasedListAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstHit) {
                    directToPage();
//                toggleButtons();
                }
            }
        }, 50);
    }

    @Override
    public void getSpinnerListener(Spinner spr_dropdown, int positions, String
            sprvalue, String field_length) {
        dataBeanList.get(positions).setField_value(sprvalue);
        // Toast.makeText(context, sprvalue.toString(), Toast.LENGTH_SHORT).show();
    }

    public void getStringListener(EditText edt_string, String s, int position, String
            field_length) {
        dataBeanList.get(position).setField_value(s);

    }

    public void getNumberListener(EditText edt_number, String s, int position, String
            field_length) {
        Log.w(TAG, "getNumberListener : " + "s : " + s + " position : " + position + " field_length : " + field_length);
        dataBeanList.get(position).setField_value(s);
    }


    public void showErrorLoading() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("please enter all required data");
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoading());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ErrorMyLocationAlert(String strMsg) {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
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

    public void hideLoading() {
        try {
            alertDialog.dismiss();
        } catch (Exception ignored) {

        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Log.i(TAG, "onBackPressed: status - " + status);
        Intent intent = new Intent();
        intent.putExtra("status", status);
        setResult(123, intent);
        finish();

        /*if (statustype == null || statustype.equals("POD") || statustype.equals("SEMPOD") || statustype.equals("MOD")) {

            Intent send = new Intent(Recycler_SpinnerActivity.this, Monthlist_Preventive_Activity.class);
            send.putExtra("status", status);
            startActivity(send);

        } else {
            Intent send = new Intent(Recycler_SpinnerActivity.this, ESCTRV.class);
            send.putExtra("status", status);
            startActivity(send);
        }*/
    }
}