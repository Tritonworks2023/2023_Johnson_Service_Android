package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.responsepojo.FailureReportDropDownDataResponse;
import com.triton.johnson_tap_app.responsepojo.FailureReportFetchDetailsByJobCodeResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureReportRequestFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private ImageView img_back;
    private TextView txt_job_id, txt_building_name;
    private Spinner spinner_matl_return_type;
    private FailureReportFetchDetailsByJobCodeResponse.Data failureReportFetchDetailsByJobCodeDataResponse = new FailureReportFetchDetailsByJobCodeResponse.Data();
    private FailureReportDropDownDataResponse failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private String TAG = FailureReportRequestFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", networkStatus = "", uploadImagePath = "";
    private ArrayList<String> machineTypeResponseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_request_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        img_back = findViewById(R.id.img_back);

        txt_job_id = findViewById(R.id.txt_job_id);
        txt_building_name = findViewById(R.id.txt_building_name);

        spinner_matl_return_type = findViewById(R.id.spinner_matl_return_type);

        img_back.setOnClickListener(this);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("failureReportFetchDetailsByJobCodeDataResponse")) {
                failureReportFetchDetailsByJobCodeDataResponse = extra.getParcelable("failureReportFetchDetailsByJobCodeDataResponse");
            }
        }

        Log.i(TAG, "onCreate: failureReportFetchDetailsByJobCodeDataResponse -> " + new Gson().toJson(failureReportFetchDetailsByJobCodeDataResponse));
        machineTypeResponseList.add("SELECT");

        ArrayAdapter<String> machineTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, machineTypeResponseList);
        machineTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_matl_return_type.setAdapter(machineTypeAdapter);

        spinner_matl_return_type.setOnItemSelectedListener(this);

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            getFailureReportDropDownData();
        }

        String[] separated = failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address().split(",");

        txt_job_id.setText(CommonFunction.nullPointer(failureReportFetchDetailsByJobCodeDataResponse.getJob_id()));
        txt_building_name.setText(CommonFunction.nullPointer(separated[0]));
    }

    public void NoInternetDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.dialog_nointernet, null);
        Button btn_Retry = mView.findViewById(R.id.btn_retry);

        mBuilder.setView(mView);
        final Dialog dialog1 = mBuilder.create();
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(false);

        btn_Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void ErrorMsgDialog(String strMsg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        txt_Message.setText(strMsg);

        alertDialogBuilder.setView(mView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void getFailureReportDropDownData() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<FailureReportDropDownDataResponse> call = apiInterface.getFailureReportDropDownData(getContentType());
        Log.i(TAG, "getFailureReportDropDownData: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FailureReportDropDownDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<FailureReportDropDownDataResponse> call, @NonNull Response<FailureReportDropDownDataResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getFailureReportDropDownData: onResponse: FailureReportDropDownDataResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();

                    failureReportDropDownDataResponse = response.body();
                    if (response.body().getCode() == 200) {

                        if (failureReportDropDownDataResponse.getData() != null && failureReportDropDownDataResponse.getData().getMatl_reture_type() != null && !failureReportDropDownDataResponse.getData().getMatl_reture_type().isEmpty()) {
                            for (FailureReportDropDownDataResponse.Matl_reture_type matlReturnType : failureReportDropDownDataResponse.getData().getMatl_reture_type()) {
                                machineTypeResponseList.add(matlReturnType.getDisplay_name());
                            }
                        }

                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FailureReportDropDownDataResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getFailureReportDropDownData: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "onItemSelected: view -> " + view.getId() + " i -> " + i + " l -> " + l);
        String item = adapterView.getItemAtPosition(i).toString();
        int selPos = -i;
        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_matl_return_type:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                default:
                    break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_matl_return_type:
                    Log.i(TAG, "onItemSelected: spinner_matl_return_type: Display_name -> " + failureReportDropDownDataResponse.getData().getMatl_reture_type().get(selPos).getDisplay_name());
                    Log.i(TAG, "onItemSelected: spinner_matl_return_type: Value -> " + failureReportDropDownDataResponse.getData().getMatl_reture_type().get(selPos).getValue());

//                    elevatorSurveyFormRequest.setMachine_type(item);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}