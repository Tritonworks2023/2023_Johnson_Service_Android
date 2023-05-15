package com.triton.johnson_tap_app.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.BuildConfig;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.materialeditext.MaterialEditText;
import com.triton.johnson_tap_app.materialspinner.MaterialSpinner;
import com.triton.johnson_tap_app.requestpojo.FbTokenRequest;
import com.triton.johnson_tap_app.requestpojo.LoginRequest1;
import com.triton.johnson_tap_app.responsepojo.LoginResponse1;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.session.SessionManager;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.NumericKeyBoardTransformationMethod;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class New_LoginActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    MaterialEditText employeeMaterialEditText, userNameMaterialEditText, passwordMaterialEditText;
    MaterialSpinner mainMaterialSpinner;
    LinearLayout forgotLinearLayout, loginMainLinearLayout;
    RequestQueue requestQueue;
    TextView mainReasonCustomFontTextView, txt_version;

    Button loginButton;

    String networkStatus = "", stationId = "";
    String status, message = "", user_level = "", station_code = "", station_name = "", empid = "",
            name = "", username = "", mobile, ID;

    Dialog dialog;

    SessionManager sessionManager;
    TextView device_id, txt_dev_unique_id;
    Context context;
    AlertDialog alertDialog;
    private String TAG = New_LoginActivity.class.getSimpleName(), role = "", userid = "", token = "",
            uniqueID = "", android_id = "";
    private SharedPreferences sharedpreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        context = this;

        userNameMaterialEditText = findViewById(R.id.user_name);
        passwordMaterialEditText = findViewById(R.id.password);
        loginMainLinearLayout = findViewById(R.id.loginMainLinearLayout);
        txt_version = findViewById(R.id.txt_version);

        userNameMaterialEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        loginButton = findViewById(R.id.loginnnn_button);

        device_id = (TextView) findViewById(R.id.device_id);
        txt_dev_unique_id = (TextView) findViewById(R.id.txt_dev_unique_id);

        String versionName = BuildConfig.VERSION_NAME;
        String buildDateTime = "";

        Date buildDate = BuildConfig.BUILD_TIME;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        buildDateTime = df.format(buildDate);

        txt_version.setText(String.format("Device Lock V - %s V%s", buildDateTime, versionName));

        Log.i(TAG, "onCreate: display DateTimeVer -> " + String.format("%s V%s", buildDateTime, versionName));

        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.w(TAG, "token--->" + token);

                    });

        } catch (Exception e) {
            Log.w(TAG, "FCM : " + e.getLocalizedMessage());
            Log.w(TAG, "FCM Message : " + e.getMessage());
            e.printStackTrace();
        }

        ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        device_id.setText(ID);
        Log.i(TAG, "onCreate: ID -> " + ID);

        userNameMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });

        passwordMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });

        // check whether internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Snackbar snackbar = Snackbar
                    .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", view -> {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    });

            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

        loginButton.setOnClickListener(view -> {
           /* Intent intent = new Intent(CMRLLogin.this, CmrlLoginDashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
*/
            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                Snackbar snackbar = Snackbar
                        .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", view1 -> {

                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        });

                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                if (Objects.requireNonNull(userNameMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    Toasty.warning(getApplicationContext(), "Enter Phone Number", Toasty.LENGTH_LONG).show();

                } else if (Objects.requireNonNull(passwordMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    Toasty.warning(getApplicationContext(), "Enter Password", Toasty.LENGTH_LONG).show();
                } else {

                    LoginResponseCall();
                }
            }

        });
        getTeleDetails();
    }

    private void getTeleDetails() {

        try {
            uniqueID = UUID.randomUUID().toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getTeleDetails: error uniqueID -> " + e.getMessage());
        }
        if (!uniqueID.isEmpty()) {
            Log.i(TAG, "getTeleDetails: uniqueID -> " + uniqueID);
//            Toast.makeText(this, "uniqueID: " + uniqueID, Toast.LENGTH_LONG).show();
        }

        try {
            android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getTeleDetails: error android_id -> " + e.getMessage());
        }
        if (!android_id.isEmpty()) {
            Log.i(TAG, "getTeleDetails: android_id -> " + android_id);
//            Toast.makeText(this, "android_id: " + android_id, Toast.LENGTH_LONG).show();
        }

        txt_dev_unique_id.setText(uniqueID + "\n" + android_id);
    }

    private void LoginResponseCall() {
        dialog = new Dialog(New_LoginActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<LoginResponse1> call = apiInterface.LoginResponseCall(RestUtils.getContentType(), loginRequest());

        Log.i(TAG, "LoginResponseCall: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<LoginResponse1>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<LoginResponse1> call, @NonNull Response<LoginResponse1> response) {

                Log.i(TAG, "LoginResponseCall: onResponse: LoginResponse1 -> " + new Gson().toJson(response.body()));

                dialog.dismiss();
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        if (response.body().getData().getDevice_no() != null && !response.body().getData().getDevice_no().isEmpty()) {
//                            if (android_id.equalsIgnoreCase(response.body().getData().getDevice_no())) {
                            userid = response.body().getData().get_id();

                            String emp_Type = response.body().getData().getEmp_type();
                            String lastLogin = response.body().getData().getLast_login_time();

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(New_LoginActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("login_execute", "true");
                            editor.putString("_id", response.body().getData().get_id());
                            editor.putString("user_mobile_no", response.body().getData().getUser_mobile_no());
                            editor.putString("user_name", response.body().getData().getUser_name());
                            editor.putString("user_location", response.body().getData().getUser_location());
                            editor.putString("user_password", response.body().getData().getUser_password());
                            editor.putString("user_type", response.body().getData().getUser_type());
                            editor.putString("user_id", response.body().getData().getUser_id());
                            editor.putString("emp_type", emp_Type);
                            editor.putString("last_login", lastLogin);
                            editor.putString("device_no", response.body().getData().getDevice_no());
                            editor.apply();

                            fbTokenUpdateCall();
                            /*} else {
                                ErrorMsgDialog("Device ID Mismatch.\nContact Admin");
                            }*/
                        }


                    } else {
                        dialog.dismiss();
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse1> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "LoginResponseCall: onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void fbTokenUpdateCall() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.fBTokenUpdateResponseCall(RestUtils.getContentType(), fbTokenUpdateRequest());

        Log.i(TAG, "fbTokenUpdateCall: URL -> " + call.request().url().toString());
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.i(TAG, "fbTokenUpdateCall: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));

                dialog.dismiss();
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (response.body().getCode() == 200) {
                        Intent home = new Intent(New_LoginActivity.this, Dashbaord_MainActivity.class);
                        startActivity(home);
                    } else {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "fbTokenUpdateCall: onFailure: " + t.getMessage());
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private FbTokenRequest fbTokenUpdateRequest() {
        FbTokenRequest fbTokenRequest = new FbTokenRequest();
        fbTokenRequest.set_id(userid);
        fbTokenRequest.setUser_token(token);
        Log.w(TAG, "fbTokenUpdateRequest" + "" + new Gson().toJson(fbTokenRequest));
        return fbTokenRequest;
    }

    private LoginRequest1 loginRequest() {

        /**
         * user_id : 12345
         * user_password : 12345
         * last_login_time : 20-10-2021 11:00 AM
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        LoginRequest1 loginRequest = new LoginRequest1();
        loginRequest.setUser_mobile_no(userNameMaterialEditText.getText().toString().trim());
        loginRequest.setUser_password(passwordMaterialEditText.getText().toString());
        loginRequest.setDevice_id(ID);
        Log.w(TAG, "loginRequest " + new Gson().toJson(loginRequest));
        return loginRequest;
    }

    public void onBackPressed() {

        alertDialog = new AlertDialog.Builder(context)
                .setTitle("Are you sure to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                })
                .show();

    }

}