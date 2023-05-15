package com.triton.johnson_tap_app.activity;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.Location.GpsTracker;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.Agent_ProfileActivity;
import com.triton.johnson_tap_app.Service_Activity.ServicesActivity;
import com.triton.johnson_tap_app.ViewStatus.Job_StatusActivity;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.CheckAttenRequest;
import com.triton.johnson_tap_app.requestpojo.CountRequest;
import com.triton.johnson_tap_app.requestpojo.CreateRequest;
import com.triton.johnson_tap_app.requestpojo.LogoutRequest;
import com.triton.johnson_tap_app.responsepojo.CheckAttenResponse;
import com.triton.johnson_tap_app.responsepojo.CountResponse;
import com.triton.johnson_tap_app.responsepojo.CreateResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class Main_Menu_ServicesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_LOCATION = 1;
    private final String URLstring = "http://smart.johnsonliftsltd.com:3000/api/service_userdetails/logout_reason";
    LinearLayout menu_service, menu_view_status, menu_change_password, menu_agent_profile, menu_Notifications;
    ImageView iv_back, profile_gray, profile_green, profile_red;
    AlertDialog alertDialog1;
    String TAG = Main_Menu_ServicesActivity.class.getSimpleName(), str_value, message = "", userStatus = "Not Present";
    TextView spinner_txt, txt_service_count, txt_view_count;
    LinearLayout logout, ll_Menu;
    Spinner spinner;
    Dialog dialog;
    LocationManager locationManager;
    String latitude, longitude, no_of_hours;
    Geocoder geocoder;
    String address = "";
    List<Address> myAddress = new ArrayList<>();
    String endDateandTime, currentDateandTime, currentDate, str_spinner, current, start;
    long elapsedHours, elapsedMinutes;
    String se_user_mobile_no, se_user_name, se_id, check_id, view_count, service_count, notification_count, networkStatus = "";
    AlertDialog alertDialog;
    Context context;
    SharedPreferences sharedPreferences;
    TextView txt_NotficationCount;
    BroadcastReceiver broadcastReceiver;
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> students;
    private JSONArray result;
    private GpsTracker gpsTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu_services);
        context = this;

        menu_service = (LinearLayout) findViewById(R.id.menu_service);
        menu_view_status = (LinearLayout) findViewById(R.id.menu_view_status);
        menu_change_password = (LinearLayout) findViewById(R.id.menu_change_password);
        menu_agent_profile = (LinearLayout) findViewById(R.id.menu_agent_profile);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        profile_gray = (ImageView) findViewById(R.id.profile_gray);
        profile_green = (ImageView) findViewById(R.id.profile_green);
        profile_red = (ImageView) findViewById(R.id.profile_red);
        logout = (LinearLayout) findViewById(R.id.logout);
        txt_service_count = (TextView) findViewById(R.id.txt_service_count);
        txt_view_count = (TextView) findViewById(R.id.txt_view_count);
        ll_Menu = findViewById(R.id.ll_menu);
        menu_Notifications = findViewById(R.id.menu_notification);
        txt_NotficationCount = findViewById(R.id.txt_notifocationcount);

        // ll_Menu.setEnabled(false);
        hitLocation();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");

        Log.w(TAG, "userrole  : " + se_id + se_user_mobile_no);

//        broadcastReceiver = new ConnectionReceiver();
//        registerNetworkBroadcast();

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.e("Network", "" + networkStatus);
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();

        } else {
            CheckAttendanceResponseCall();

            Count();
        }


        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent send = new Intent(Main_Menu_ServicesActivity.this, Dashbaord_MainActivity.class);
                startActivity(send);

            }
        });

        profile_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitLocation();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main_Menu_ServicesActivity.this);
                alertDialogBuilder.setMessage("Your Profile Login?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                CreateResponseCall();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        menu_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "menu_service: onClick: message -> " + userStatus);

                if (userStatus.equals("Not Present")) {

                    alertDialog = new AlertDialog.Builder(context)
                            //.setTitle("Please Login to Continue!")
                            .setMessage("Mark Present to Continue!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .show();

                } else {

                    Intent send = new Intent(Main_Menu_ServicesActivity.this, ServicesActivity.class);
                    startActivity(send);
                }


            }
        });

        menu_view_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userStatus.equals("Not Present")) {

                    alertDialog = new AlertDialog.Builder(context)
                            //.setTitle("Please Login to Continue!")
                            .setMessage("Mark Present to Continue!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .show();

                } else {
                    Intent send = new Intent(Main_Menu_ServicesActivity.this, Job_StatusActivity.class);
                    startActivity(send);
                }


            }
        });

        menu_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userStatus.equals("Not Present")) {

                    alertDialog = new AlertDialog.Builder(context)
                            //.setTitle("Please Login to Continue!")
                            .setMessage("Mark Present to Continue!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .show();

                } else {
                    Intent send = new Intent(Main_Menu_ServicesActivity.this, Change_PasswordActivity.class);
                    startActivity(send);
                }


            }
        });

        menu_agent_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userStatus.equals("Not Present")) {

                    alertDialog = new AlertDialog.Builder(context)
                            //.setTitle("Please Login to Continue!")
                            .setMessage("Mark Present to Continue!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .show();

                } else {
                    Intent send = new Intent(Main_Menu_ServicesActivity.this, Agent_ProfileActivity.class);
                    startActivity(send);
                }

            }
        });

        menu_Notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userStatus.equals("Not Present")) {

                    alertDialog = new AlertDialog.Builder(context)
                            //.setTitle("Please Login to Continue!")
                            .setMessage("Mark Present to Continue!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .show();

                } else {
                    Intent send = new Intent(Main_Menu_ServicesActivity.this, Notification_Activity.class);
                    startActivity(send);
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main_Menu_ServicesActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
                spinner = (Spinner) mView.findViewById(R.id.spinner);
                Button yes = (Button) mView.findViewById(R.id.btn_yes);
                Button no = (Button) mView.findViewById(R.id.btn_no);

                students = new ArrayList<String>();

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                getData();
                hitLocation();

                // retrieveJSON();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        str_spinner = spinner.getSelectedItem().toString();

                        if (str_spinner.equals("Select Logout Reason")) {

                            Toast.makeText(Main_Menu_ServicesActivity.this, "Please Selected Logout Reason", Toast.LENGTH_SHORT).show();

                        } else {

                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            endDateandTime = sdf1.format(new Date());

                            //       Toast.makeText(Main_Menu_ServicesActivity.this, "current :" + start, Toast.LENGTH_SHORT).show();

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");


//                            try {
//                                Date date1 = simpleDateFormat.parse(start);
//                                Date date2 = simpleDateFormat.parse(endDateandTime);
//
//                                printDifference(date1, date2);
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }

//                            logoutResponseCall(check_id, endDateandTime, str_spinner,latitude,longitude,no_of_hours);
                            logoutResponseCall();
                            dialog.dismiss();

                        }

                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        profile_gray.setVisibility(View.VISIBLE);
//                        profile_green.setVisibility(View.GONE);
                        //   Toast.makeText(Main_Menu_ServicesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
            }
        });
    }

    private void getMYLocation() {

        gpsTracker = new GpsTracker(context);
        if (gpsTracker.canGetLocation()) {
            latitude = "" + gpsTracker.getLatitude();
            longitude = "" + gpsTracker.getLongitude();
            Log.i(TAG, "getMYLocation: latitude -> " + latitude + " longitude -> " + longitude);
            if (nullPointerValidator(latitude) && nullPointerValidator(longitude)
                    && !latitude.equalsIgnoreCase("0.0") && !longitude.equalsIgnoreCase("0.0")) {
                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    myAddress = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                    address = myAddress.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "getMYLocation: address -> " + address);
            } else {
                ErrorMyLocationAlert("Kindly enable the\nGPS Location and Try again");
//                Toast.makeText(context, "Kindly enable the GPS Location and Try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void hitLocation() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getMYLocation();
        }
    }

    public void printDifference(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        elapsedMinutes = different / minutesInMilli;

        no_of_hours = elapsedHours + "." + elapsedMinutes;

        Log.d("time", elapsedHours + "." + elapsedMinutes);
    }


    private void CheckAttendanceResponseCall() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<CheckAttenResponse> call = apiInterface.CheckAttenResponseCall(RestUtils.getContentType(), checkattRequest());
        Log.w(TAG, "AttendanceResponse url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<CheckAttenResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<CheckAttenResponse> call, @NonNull retrofit2.Response<CheckAttenResponse> response) {
                Log.i(TAG, "CheckAttendanceResponseCall: onResponse: response -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    userStatus = response.body().getMessage();
                    start = response.body().getData().getAtt_start_time();
                    check_id = response.body().getData().get_id();

                    if (userStatus.equals("Not Present")) {
                        profile_gray.setVisibility(View.GONE);
                        profile_green.setVisibility(View.GONE);
                        profile_red.setVisibility(View.VISIBLE);
                        logout.setVisibility(View.INVISIBLE);

//                        menu_service.setEnabled(false);
//                        menu_view_status.setEnabled(false);
//                        menu_agent_profile.setEnabled(false);
//                        menu_change_password.setEnabled(false);

                    } else if (userStatus.equals("Present")) {
                        logout.setVisibility(View.VISIBLE);
                        profile_gray.setVisibility(View.GONE);
                        profile_red.setVisibility(View.GONE);
                        profile_green.setVisibility(View.VISIBLE);
                    } else {
                        profile_gray.setVisibility(View.GONE);
                        profile_green.setVisibility(View.GONE);
                        profile_red.setVisibility(View.VISIBLE);
                        logout.setVisibility(View.INVISIBLE);
                        Toast.makeText(Main_Menu_ServicesActivity.this, "You have already logout. ", Toast.LENGTH_SHORT).show();
                    }


                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {


                        }


                    } else {
                        ErrorMyLocationAlert(response.body().getMessage());
//                        dialog.dismiss();
//                        Toasty.warning(getApplicationContext(),""+message,Toasty.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckAttenResponse> call, @NonNull Throwable t) {
                Log.e("OTP", "--->" + t.getMessage());
                ErrorMyLocationAlert(t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private CheckAttenRequest checkattRequest() {

        /**
         * user_id : 12345
         * user_password : 12345
         * last_login_time : 20-10-2021 11:00 AM
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());

        CheckAttenRequest checkRequest = new CheckAttenRequest();
        checkRequest.setUser_mobile_no(se_user_mobile_no);
        checkRequest.setAtt_date(currentDateandTime);
        Log.e(TAG, "Attendance Request " + new Gson().toJson(checkRequest));
        return checkRequest;
    }

    private void CreateResponseCall() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        CreateRequest createRequest = createRequest();

        if (createRequest != null) {
            Call<CreateResponse> call = apiInterface.CreateResponseCall(RestUtils.getContentType(), createRequest);

            Log.i(TAG, "CreateResponseCall: URL -> " + call.request().url().toString());
            call.enqueue(new Callback<CreateResponse>() {
                @SuppressLint("LogNotTimber")
                @Override
                public void onResponse(@NonNull Call<CreateResponse> call, @NonNull retrofit2.Response<CreateResponse> response) {
                    Log.i(TAG, "CreateResponseCall: onResponse: CreateResponse -> " + new Gson().toJson(response.body()));

                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {

                                Toasty.success(getApplicationContext(), "Add Successfully", Toast.LENGTH_SHORT, true).show();

                                finish();
                                startActivity(getIntent());

                                /*Intent send = new Intent(Main_Menu_ServicesActivity.this, Dashbaord_MainActivity.class);
                                startActivity(send);*/
                            }

                        } else {
                            ErrorMyLocationAlert(response.body().getMessage());
//                        Toasty.warning(getApplicationContext(),""+message,Toasty.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CreateResponse> call, @NonNull Throwable t) {

                    Log.e(TAG, "CreateResponseCall: onFailure: error -> " + t.getMessage());
                    ErrorMyLocationAlert(t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private CreateRequest createRequest() {

        /**
         * user_id : 12345
         * user_password : 12345
         * last_login_time : 20-10-2021 11:00 AM
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDate = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        current = sdf1.format(new Date());

        CreateRequest creaRequest = new CreateRequest();
        if (!nullPointerValidator(latitude) | !nullPointerValidator(longitude)
                | latitude.equalsIgnoreCase("0.0") | longitude.equalsIgnoreCase("0.0")) {

            Log.i(TAG, "createRequest: latitude -> " + latitude + " longitude -> " + longitude);
            ErrorMyLocationAlert("Kindly enable the\nGPS Location and Try again");
            return null;
        }
        creaRequest.setUser_mobile_no(se_user_mobile_no);
        creaRequest.setUser_name(se_user_name);
        creaRequest.setAtt_date(currentDate);
        creaRequest.setAtt_start_time(current);
        creaRequest.setAtt_status("Present");
        creaRequest.setAtt_start_lat(latitude);
        creaRequest.setAtt_start_long(longitude);
        creaRequest.setLogin_lat(latitude);
        creaRequest.setLogin_long(longitude);
        creaRequest.setLogin_address(address);

        Log.i(TAG, "createRequest: creaRequest -> " + new Gson().toJson(creaRequest));
        return creaRequest;
    }

    private void logoutResponseCall() {
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        LogoutRequest logoutRequest = addReviewRequest();

        if (logoutRequest != null) {

            Call<SuccessResponse> call = apiInterface.LogoutCall(RestUtils.getContentType(), logoutRequest);
            Log.w(TAG, "addReviewResponseCall url  :%s" + " " + call.request().url().toString());

            call.enqueue(new Callback<SuccessResponse>() {
                @Override
                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull retrofit2.Response<SuccessResponse> response) {

                    Log.w(TAG, "AddReviewResponse" + "--->" + new Gson().toJson(response.body()));

                    //    Log.w(TAG,"Response"+ "--->" + "_id : " + id + "," + "att_end_time : " + att_end_time + ","+ "att_reason : " + att_reason + "," + "att_end_lat : " + att_end_lat + "," + "att_end_long : " + att_end_long + ","+ "att_no_of_hrs : " + att_no_of_hrs);

                    if (response.body() != null) {
                        if (response.body().getCode() == 200) {

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Main_Menu_ServicesActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();

                            Toasty.success(getApplicationContext(), "Logout Sucessfully", Toast.LENGTH_SHORT, true).show();
                            Intent send = new Intent(Main_Menu_ServicesActivity.this, New_LoginActivity.class);
                            startActivity(send);
                        } else {
                            ErrorMyLocationAlert(response.body().getMessage());
                        }
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                    ErrorMyLocationAlert(t.getMessage());

                    Log.w(TAG, "AddReviewResponse flr" + "--->" + t.getMessage());
                }
            });
        }

    }

    private LogoutRequest addReviewRequest() {

        LogoutRequest addReviewRequest = new LogoutRequest();
        if (!nullPointerValidator(latitude) | !nullPointerValidator(longitude)
                | latitude.equalsIgnoreCase("0.0") | longitude.equalsIgnoreCase("0.0")) {

            Log.i(TAG, "addReviewRequest: latitude -> " + latitude + " longitude -> " + longitude);
            ErrorMyLocationAlert("Kindly enable the\nGPS Location and Try again");
            return null;
        }
//        addReviewRequest.set_id(id);
//        addReviewRequest.setAtt_end_time(att_end_time);
        addReviewRequest.setAtt_reason(str_spinner);
//        addReviewRequest.setAtt_end_lat(att_end_lat);
//        addReviewRequest.setAtt_end_long(att_end_long);
//        addReviewRequest.setAtt_no_of_hrs(att_no_hrs);
        addReviewRequest.setUser_mobile_no(se_user_mobile_no);
        addReviewRequest.setLogout_lat(latitude);
        addReviewRequest.setLogout_long(longitude);
        addReviewRequest.setLogout_address(address);
        Log.i(TAG, "addReviewRequest: addReviewRequest -> " + new Gson().toJson(addReviewRequest));
        return addReviewRequest;
    }

    private void Count() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<CountResponse> call = apiInterface.CountResponseCall(RestUtils.getContentType(), countRequest());
        Log.w(TAG, "SignupResponse url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<CountResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<CountResponse> call, @NonNull retrofit2.Response<CountResponse> response) {

                Log.w(TAG, "SignupResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            view_count = response.body().getData().getView_status();
                            service_count = response.body().getData().getServices_count();
                            notification_count = response.body().getData().getNotificaion_count();
                            txt_service_count.setText(service_count);
                            txt_view_count.setText(view_count);
                            txt_NotficationCount.setText("(" + notification_count + ")");


                            // Log.d("count", service_count + "," + view_count);

                        }


                    } else {
//                        dialog.dismiss();
                        ErrorMyLocationAlert(response.body().getMessage());
//                        Toasty.warning(getApplicationContext(),""+message,Toasty.LENGTH_LONG).show();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<CountResponse> call, @NonNull Throwable t) {
                Log.e("OTP", "--->" + t.getMessage());
                ErrorMyLocationAlert(t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private CountRequest countRequest() {

        /**
         * user_id : 12345
         * user_password : 12345
         * last_login_time : 20-10-2021 11:00 AM
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());

        CountRequest count = new CountRequest();
        count.setUser_mobile_no(se_user_mobile_no);
        Log.w(TAG, "loginRequest " + new Gson().toJson(count));
        return count;
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getData() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest("http://smart.johnsonliftsltd.com:3000/api/service_userdetails/logout_reason",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            result = j.getJSONArray("Data");
                            getStudents(result);
                        } catch (JSONException e) {
                            ErrorMyLocationAlert(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorMyLocationAlert(error.getMessage());

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        students.add("Select Logout Reason");
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                students.add(json.getString("logout_reason"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Main_Menu_ServicesActivity.this, R.layout.spinner_item, students);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        // spinner.setAdapter(new ArrayAdapter<String>(Main_Menu_ServicesActivity.this, R.layout.spinner_item, students));
    }

    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView arg0) {

    }

    @Override
    public void onBackPressed() {
        Intent send = new Intent(Main_Menu_ServicesActivity.this, Dashbaord_MainActivity.class);
        startActivity(send);
    }


    protected void registerNetworkBroadcast() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        } else {

        }

    }

    protected void unRegisterNetwork() {

        try {
            unregisterReceiver(broadcastReceiver);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}