package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureReportRequestFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String formattedDate = "";
    private Context context;
    private ImageView img_back;
    private TextView txt_job_id, txt_building_name, txt_date;
    private Spinner spinner_matl_return_type, spinner_department, spinner_service_type, spinner_physical_cond,
            spinner_current_status, spinner_nature_of_failure, spinner_vvvf_trip_while, spinner_vvvf_trip_type,
            spinner_encoder_checked, spinner_load_inside_life, spinner_electric_supply, spinner_battery_check_status,
            spinner_battery_warranty_status;
    private FailureReportFetchDetailsByJobCodeResponse.Data failureReportFetchDetailsByJobCodeDataResponse = new FailureReportFetchDetailsByJobCodeResponse.Data();
    private FailureReportDropDownDataResponse failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private String TAG = FailureReportRequestFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", networkStatus = "", uploadImagePath = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private boolean checkDate = false;
    private ArrayList<String> matlReturnTypeList = new ArrayList<>(), departmentList = new ArrayList<>(),
            serviceTypeList = new ArrayList<>(), physicalCondList = new ArrayList<>(), currentStatusList = new ArrayList<>(),
            natureOfFailureList = new ArrayList<>(), vvvfTripWhileList = new ArrayList<>(), vvvfTripTypeList = new ArrayList<>(),
            encoderCheckedList = new ArrayList<>(), loadInsideLifeList = new ArrayList<>(), electricSupplyList = new ArrayList<>(),
            batteryCheckStatusList = new ArrayList<>(), batteryWarrantyStatusList = new ArrayList<>();

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
        txt_date = findViewById(R.id.txt_date);

        spinner_matl_return_type = findViewById(R.id.spinner_matl_return_type);
        spinner_department = findViewById(R.id.spinner_department);
        spinner_service_type = findViewById(R.id.spinner_service_type);
        spinner_physical_cond = findViewById(R.id.spinner_physical_cond);
        spinner_current_status = findViewById(R.id.spinner_current_status);
        spinner_nature_of_failure = findViewById(R.id.spinner_nature_of_failure);
        spinner_vvvf_trip_while = findViewById(R.id.spinner_vvvf_trip_while);
        spinner_vvvf_trip_type = findViewById(R.id.spinner_vvvf_trip_type);
        spinner_encoder_checked = findViewById(R.id.spinner_encoder_checked);
        spinner_load_inside_life = findViewById(R.id.spinner_load_inside_life);
        spinner_electric_supply = findViewById(R.id.spinner_electric_supply);
        spinner_battery_check_status = findViewById(R.id.spinner_battery_check_status);
        spinner_battery_warranty_status = findViewById(R.id.spinner_battery_warranty_status);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("failureReportFetchDetailsByJobCodeDataResponse")) {
                failureReportFetchDetailsByJobCodeDataResponse = extra.getParcelable("failureReportFetchDetailsByJobCodeDataResponse");
            }
        }

        Log.i(TAG, "onCreate: failureReportFetchDetailsByJobCodeDataResponse -> " + new Gson().toJson(failureReportFetchDetailsByJobCodeDataResponse));

        matlReturnTypeList.add("SELECT");
        departmentList.add("SELECT");
        serviceTypeList.add("SELECT");
        physicalCondList.add("SELECT");
        currentStatusList.add("SELECT");
        natureOfFailureList.add("SELECT");
        vvvfTripWhileList.add("SELECT");
        vvvfTripTypeList.add("SELECT");
        encoderCheckedList.add("SELECT");
        loadInsideLifeList.add("SELECT");
        electricSupplyList.add("SELECT");
        batteryCheckStatusList.add("SELECT");
        batteryWarrantyStatusList.add("SELECT");

        ArrayAdapter<String> matlReturnTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, matlReturnTypeList);
        matlReturnTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, departmentList);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> serviceTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, serviceTypeList);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> physicalCondAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, physicalCondList);
        physicalCondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> currentStatusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, currentStatusList);
        currentStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> natureOfFailureAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, natureOfFailureList);
        natureOfFailureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> vvvfTripWhileAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, vvvfTripWhileList);
        vvvfTripWhileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> vvvfTripTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, vvvfTripTypeList);
        vvvfTripTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> encoderCheckedAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, encoderCheckedList);
        encoderCheckedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> loadInsideLifeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, loadInsideLifeList);
        loadInsideLifeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> electricSupplyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, electricSupplyList);
        electricSupplyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> batteryCheckStatusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, batteryCheckStatusList);
        batteryCheckStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> batteryWarrantyStatusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, batteryWarrantyStatusList);
        batteryWarrantyStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_matl_return_type.setAdapter(matlReturnTypeAdapter);
        spinner_department.setAdapter(departmentAdapter);
        spinner_service_type.setAdapter(serviceTypeAdapter);
        spinner_physical_cond.setAdapter(physicalCondAdapter);
        spinner_current_status.setAdapter(currentStatusAdapter);
        spinner_nature_of_failure.setAdapter(natureOfFailureAdapter);
        spinner_vvvf_trip_while.setAdapter(vvvfTripWhileAdapter);
        spinner_vvvf_trip_type.setAdapter(vvvfTripTypeAdapter);
        spinner_encoder_checked.setAdapter(encoderCheckedAdapter);
        spinner_load_inside_life.setAdapter(loadInsideLifeAdapter);
        spinner_electric_supply.setAdapter(electricSupplyAdapter);
        spinner_battery_check_status.setAdapter(batteryCheckStatusAdapter);
        spinner_battery_warranty_status.setAdapter(batteryWarrantyStatusAdapter);

        spinner_matl_return_type.setOnItemSelectedListener(this);
        spinner_department.setOnItemSelectedListener(this);
        spinner_service_type.setOnItemSelectedListener(this);
        spinner_physical_cond.setOnItemSelectedListener(this);
        spinner_current_status.setOnItemSelectedListener(this);
        spinner_nature_of_failure.setOnItemSelectedListener(this);
        spinner_vvvf_trip_while.setOnItemSelectedListener(this);
        spinner_vvvf_trip_type.setOnItemSelectedListener(this);
        spinner_encoder_checked.setOnItemSelectedListener(this);
        spinner_load_inside_life.setOnItemSelectedListener(this);
        spinner_electric_supply.setOnItemSelectedListener(this);
        spinner_battery_check_status.setOnItemSelectedListener(this);
        spinner_battery_warranty_status.setOnItemSelectedListener(this);

        img_back.setOnClickListener(this);
        txt_date.setOnClickListener(this);

        initLoadingDialog();

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            getFailureReportDropDownData();
        }

        String[] separated = failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address().split(",");

        Log.i(TAG, "onCreate: Job_id -> " + failureReportFetchDetailsByJobCodeDataResponse.getJob_id());

        txt_job_id.setText(CommonFunction.nullPointer(failureReportFetchDetailsByJobCodeDataResponse.getJob_id()));
        txt_building_name.setText(CommonFunction.nullPointer(separated[0]));

        getTodayDate();
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
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

    private void callDatePicker() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        setDate(dayOfMonth, monthOfYear, year1), year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void getTodayDate() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        strDateType = "txt_both";
        setDate(day, month, year);
    }

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

        if (strDateType.equalsIgnoreCase("txt_date")) {
            txt_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_date.setText(dateTime);
        }

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);
            if (strDateType.equalsIgnoreCase("txt_date")) {
                /*createRopeMaintenanceRequest.setActivity_date(formattedDate);

                ropeMaintenanceCheckDataRequest.setSubmitted_by_on(formattedDate);

                getFailureReportCheckDate();*/
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                /*createRopeMaintenanceRequest.setSubmitted_by_on(formattedDate);
                createRopeMaintenanceRequest.setActivity_date(formattedDate);

                ropeMaintenanceCheckDataRequest.setSubmitted_by_on(formattedDate);

                getFailureReportCheckDate();*/
            }

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
        }
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

                        if (failureReportDropDownDataResponse.getData() != null) {
                            if (failureReportDropDownDataResponse.getData().getMatl_reture_type() != null
                                    && !failureReportDropDownDataResponse.getData().getMatl_reture_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Matl_reture_type matlReturnType : failureReportDropDownDataResponse.getData().getMatl_reture_type()) {
                                    matlReturnTypeList.add(matlReturnType.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getDepart() != null
                                    && !failureReportDropDownDataResponse.getData().getDepart().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Depart depart : failureReportDropDownDataResponse.getData().getDepart()) {
                                    departmentList.add(depart.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getServ_type() != null
                                    && !failureReportDropDownDataResponse.getData().getServ_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Serv_type servType : failureReportDropDownDataResponse.getData().getServ_type()) {
                                    serviceTypeList.add(servType.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getPys_condition() != null
                                    && !failureReportDropDownDataResponse.getData().getPys_condition().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Pys_condition pysCondition : failureReportDropDownDataResponse.getData().getPys_condition()) {
                                    physicalCondList.add(pysCondition.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getCuur_status() != null
                                    && !failureReportDropDownDataResponse.getData().getCuur_status().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Cuur_status cuurStatus : failureReportDropDownDataResponse.getData().getCuur_status()) {
                                    currentStatusList.add(cuurStatus.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getNatu_failure() != null
                                    && !failureReportDropDownDataResponse.getData().getNatu_failure().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Natu_failure natuFailure : failureReportDropDownDataResponse.getData().getNatu_failure()) {
                                    natureOfFailureList.add(natuFailure.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getVvvf_trip_while() != null
                                    && !failureReportDropDownDataResponse.getData().getVvvf_trip_while().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Vvvf_trip_while vvvfTripWhile : failureReportDropDownDataResponse.getData().getVvvf_trip_while()) {
                                    vvvfTripWhileList.add(vvvfTripWhile.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getVvvf_trip_type() != null
                                    && !failureReportDropDownDataResponse.getData().getVvvf_trip_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Vvvf_trip_type vvvfTripType : failureReportDropDownDataResponse.getData().getVvvf_trip_type()) {
                                    vvvfTripTypeList.add(vvvfTripType.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getEncoder_checked() != null
                                    && !failureReportDropDownDataResponse.getData().getEncoder_checked().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Encoder_checked encoderChecked : failureReportDropDownDataResponse.getData().getEncoder_checked()) {
                                    encoderCheckedList.add(encoderChecked.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getLd_inside_lift() != null
                                    && !failureReportDropDownDataResponse.getData().getLd_inside_lift().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Ld_inside_lift ldInsideLift : failureReportDropDownDataResponse.getData().getLd_inside_lift()) {
                                    loadInsideLifeList.add(ldInsideLift.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getElectric_supply() != null
                                    && !failureReportDropDownDataResponse.getData().getElectric_supply().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Electric_supply electricSupply : failureReportDropDownDataResponse.getData().getElectric_supply()) {
                                    electricSupplyList.add(electricSupply.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getBat_check_status() != null
                                    && !failureReportDropDownDataResponse.getData().getBat_check_status().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Bat_check_status batCheckStatus : failureReportDropDownDataResponse.getData().getBat_check_status()) {
                                    batteryCheckStatusList.add(batCheckStatus.getDisplay_name());
                                }
                            }
                            if (failureReportDropDownDataResponse.getData().getBat_warr_status() != null
                                    && !failureReportDropDownDataResponse.getData().getBat_warr_status().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Bat_warr_status batWarrStatus : failureReportDropDownDataResponse.getData().getBat_warr_status()) {
                                    batteryWarrantyStatusList.add(batWarrStatus.getDisplay_name());
                                }
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
                case R.id.spinner_department:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_service_type:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_physical_cond:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_current_status:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_nature_of_failure:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_vvvf_trip_while:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_vvvf_trip_type:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_encoder_checked:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_load_inside_life:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_electric_supply:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_battery_check_status:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_battery_warranty_status:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                default:
                    break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_matl_return_type:
                    Log.i(TAG, "onItemSelected: spinner_matl_return_type: Display_name -> " + failureReportDropDownDataResponse.getData().getMatl_reture_type().get(selPos).getDisplay_name() + " Value -> " + failureReportDropDownDataResponse.getData().getMatl_reture_type().get(selPos).getValue());
//                    elevatorSurveyFormRequest.setMachine_type(item);
                    break;
                case R.id.spinner_department:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_service_type:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_physical_cond:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_current_status:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_nature_of_failure:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_vvvf_trip_while:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_vvvf_trip_type:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_encoder_checked:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_load_inside_life:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_electric_supply:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_battery_check_status:
//                    elevatorSurveyFormRequest.setMachine_type("");

                    break;
                case R.id.spinner_battery_warranty_status:
//                    elevatorSurveyFormRequest.setMachine_type("");

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
            case R.id.txt_date: {
                strDateType = "txt_date";
                callDatePicker();
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