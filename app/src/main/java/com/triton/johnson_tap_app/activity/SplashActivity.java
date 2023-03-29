package com.triton.johnson_tap_app.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.BuildConfig;
import com.triton.johnson_tap_app.NetworkUtility.NetworkChangeListener;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.Getlatestversionrequest;
import com.triton.johnson_tap_app.responsepojo.GetFetchLatestVersionResponse;
import com.triton.johnson_tap_app.session.SessionManager;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    /**
     * Session to check whether user is login or not.
     */
    SessionManager sessionManager;

    // user level
    String user_level;

    int haslocationpermission;
    String TAG = SplashActivity.class.getSimpleName(), ID;
    TextView device_id;
    Context context;
    String networkStatus = "";
    LinearLayout loginMainLinearLayout;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private SharedPreferences sharedpreferences;
    private String VersionUpdate, VersionUpdate1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        SimpleDateFormat currentDate = new SimpleDateFormat("dd.MM.yy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        TextView txt_version = (TextView) findViewById(R.id.txt_version);
        device_id = (TextView) findViewById(R.id.device_id);
        ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        device_id.setText(ID);
        Log.i(TAG, "onCreate: ID -> " + ID);

//        String[] permission = { Manifest.permission.READ_PHONE_NUMBERS};
//        requestPermissions(permission,102);
//        getMobileNumber();

        String versionName = BuildConfig.VERSION_NAME;
        String buildDateTime = "";

        Date buildDate = BuildConfig.BUILD_TIME;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        buildDateTime = df.format(buildDate);

        txt_version.setText(String.format("Test Build Version - %s V%s", buildDateTime, versionName));

        Log.i(TAG, "onCreate: display DateTimeVer -> " + String.format("%s V%s", buildDateTime, versionName));

        sessionManager = new SessionManager(this);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            getLatestVersion();
        }

    }


    @SuppressLint("HardwareIds")
    private void getMobileNumber() {
        Log.e("Get Mobile Number", "Hi");

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        txt_Mobilenumber.setText(telephonyManager.getLine1Number());
        Log.e("Mobile Number", "" + telephonyManager.getLine1Number());
    }

    private void getLatestVersion() {
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFetchLatestVersionResponse> call = apiInterface.getlatestversionrequestcall();

        Log.i(TAG, "getLatestVersion: URL -> " + call.request().url().toString());
        call.enqueue(new Callback<GetFetchLatestVersionResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetFetchLatestVersionResponse> call, @NonNull Response<GetFetchLatestVersionResponse> response) {

                Log.i(TAG, "getLatestVersion: onResponse: GetFetchLatestVersionResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    String Submitted_status = response.body().getStatus();

                    if (Submitted_status != null && Submitted_status.equalsIgnoreCase("Success")) {

                        if (response.body().getData().getVersion().equals(BuildConfig.APP_VERSION_DATE)) {
                            Thread timerThread = new Thread() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                public void run() {
                                    try {
                                        sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } finally {

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                                        String login = sharedPreferences.getString("login_execute", "false");
                                        if (login.equals("true")) {
                                            Intent intent = new Intent(SplashActivity.this, Dashbaord_MainActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(SplashActivity.this, New_LoginActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                            finish();
                                        }
                                    }
                                }
                            };
                            timerThread.start();
                        } else {
                            String apk_link = response.body().getData().getApk_link();
                            String apk_version = response.body().getData().getVersion();
                            Intent intent = new Intent(SplashActivity.this, DownloadApkFileActivity.class);
                            intent.putExtra("apk_link", apk_link);
                            intent.putExtra("apk_version", apk_version);
                            startActivity(intent);
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetFetchLatestVersionResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "getLatestVersion: onFailure: error -> " + t.getMessage());
            }
        });
    }

    private Getlatestversionrequest getlatestversionrequest() {
        Getlatestversionrequest getlatestversionrequest = new Getlatestversionrequest();
        getlatestversionrequest.setVersion("");
        return getlatestversionrequest;
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}