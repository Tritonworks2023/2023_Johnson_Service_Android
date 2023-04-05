package com.triton.johnson_tap_app.Service_Activity.failureReportApprovalModule;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImage;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.Adapter.PetCurrentImageList2Adapter;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.PetAppointmentCreateRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportFetchDetailsByJobCodeResponse;
import com.triton.johnson_tap_app.responsepojo.FailureReportRequestListByEngCodeResponse;
import com.triton.johnson_tap_app.responsepojo.JobListFailureReportResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;

public class FailureReportApprovalFormActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static String formattedDate = "", formattedDate2 = "", formattedDate3 = "";
    int PERMISSION_CLINIC = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private FailureReportRequestListByEngCodeResponse.Data failureReportRequestListByEngCodeDateResponse = new FailureReportRequestListByEngCodeResponse.Data();
    private Context context;
    private LinearLayout ll_eng_privilege;
    private ImageView img_back, img_search_comp_device;
    private Button btn_upload_image, btn_submit;
    private RecyclerView rv_upload_images_list;
    private TextView txt_job_id, txt_building_name, /*txt_date,*/
            txt_failure_date, txt_installed_date, txt_bc_qr_code,
            txt_status, txt_material_id, txt_branch, txt_comp_device_name, txt_mechanic_id,
            txt_mechanic_name, txt_mechanic_phone, txt_engineer_id, txt_engineer_name, txt_engineer_phone,
            txt_ser_mast_cust_name_add, txt_install_address, txt_route;
    private EditText edt_comp_device_id, edt_model_make, edt_rating, edt_serial_no, edt_observation, edt_supply_vol,
            edt_tech_exp_comment, edt_curlss_no, edt_prvlss_no, edt_vvf_remark,
            edt_vvf_item, edt_electric_volt;
    private Spinner spinner_matl_return_type, spinner_department, spinner_service_type, spinner_physical_cond,
            spinner_current_status, spinner_nature_of_failure, spinner_vvvf_trip_while, spinner_vvvf_trip_type,
            spinner_encoder_checked, spinner_load_inside_life, spinner_electric_supply, spinner_battery_check_status,
            spinner_battery_warranty_status, spinner_reason_code;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private String TAG = FailureReportApprovalFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", emp_Type = "", networkStatus = "", uploadImagePath = "",
            strScanType = "", strScanData = "", message = "", userid = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private ArrayList<String> matlReturnTypeList = new ArrayList<>(), departmentList = new ArrayList<>(),
            serviceTypeList = new ArrayList<>(), physicalCondList = new ArrayList<>(), currentStatusList = new ArrayList<>(),
            natureOfFailureList = new ArrayList<>(), vvvfTripWhileList = new ArrayList<>(), vvvfTripTypeList = new ArrayList<>(),
            encoderCheckedList = new ArrayList<>(), loadInsideLifeList = new ArrayList<>(), electricSupplyList = new ArrayList<>(),
            batteryCheckStatusList = new ArrayList<>(), batteryWarrantyStatusList = new ArrayList<>(), reasonCodeList = new ArrayList<>();
    private List<PetAppointmentCreateRequest.PetImgBean> pet_imgList = new ArrayList();
    private MultipartBody.Part filePart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_approval_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");
        emp_Type = sharedPreferences.getString("emp_type", "ABCD");

        Log.i(TAG, "onCreate: emp_Type -> " + emp_Type);

        ll_eng_privilege = findViewById(R.id.ll_eng_privilege);

        img_back = findViewById(R.id.img_back);
        img_search_comp_device = findViewById(R.id.img_search_comp_device);

        btn_upload_image = findViewById(R.id.btn_upload_image);
        btn_submit = findViewById(R.id.btn_submit);

        rv_upload_images_list = findViewById(R.id.rv_upload_images_list);

        txt_job_id = findViewById(R.id.txt_job_id);
        txt_building_name = findViewById(R.id.txt_building_name);
        /*txt_date = findViewById(R.id.txt_date);*/
        txt_failure_date = findViewById(R.id.txt_failure_date);
        txt_installed_date = findViewById(R.id.txt_installed_date);
        txt_bc_qr_code = findViewById(R.id.txt_bc_qr_code);
        txt_status = findViewById(R.id.txt_status);
        txt_material_id = findViewById(R.id.txt_material_id);
        txt_branch = findViewById(R.id.txt_branch);
        txt_comp_device_name = findViewById(R.id.txt_comp_device_name);
        txt_mechanic_id = findViewById(R.id.txt_mechanic_id);
        txt_mechanic_name = findViewById(R.id.txt_mechanic_name);
        txt_mechanic_phone = findViewById(R.id.txt_mechanic_phone);
        txt_engineer_id = findViewById(R.id.txt_engineer_id);
        txt_engineer_name = findViewById(R.id.txt_engineer_name);
        txt_engineer_phone = findViewById(R.id.txt_engineer_phone);
        txt_ser_mast_cust_name_add = findViewById(R.id.txt_ser_mast_cust_name_add);
        txt_install_address = findViewById(R.id.txt_install_address);
        txt_route = findViewById(R.id.txt_route);

        edt_comp_device_id = findViewById(R.id.edt_comp_device_id);
        edt_model_make = findViewById(R.id.edt_model_make);
        edt_rating = findViewById(R.id.edt_rating);
        edt_serial_no = findViewById(R.id.edt_serial_no);
        edt_observation = findViewById(R.id.edt_observation);
        edt_supply_vol = findViewById(R.id.edt_supply_vol);
        edt_tech_exp_comment = findViewById(R.id.edt_tech_exp_comment);
        edt_curlss_no = findViewById(R.id.edt_curlss_no);
        edt_prvlss_no = findViewById(R.id.edt_prvlss_no);
        edt_vvf_remark = findViewById(R.id.edt_vvf_remark);
        edt_vvf_item = findViewById(R.id.edt_vvf_item);
        edt_electric_volt = findViewById(R.id.edt_electric_volt);

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
        spinner_reason_code = findViewById(R.id.spinner_reason_code);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("failureReportRequestListByEngCodeDateResponse")) {
                failureReportRequestListByEngCodeDateResponse = extra.getParcelable("failureReportRequestListByEngCodeDateResponse");
            }
            /*if (extra.containsKey("strScanType")) {
                strScanType = extra.getString("strScanType");
            }
            if (extra.containsKey("strScanData")) {
                strScanData = extra.getString("strScanData");
            }*/
        }

        Log.i(TAG, "onCreate: failureReportRequestListByEngCodeDateResponse -> " + new Gson().toJson(failureReportRequestListByEngCodeDateResponse));

        if (emp_Type.equalsIgnoreCase("engineer")) {
            ll_eng_privilege.setVisibility(View.VISIBLE);
        } else {
            ll_eng_privilege.setVisibility(View.GONE);
        }

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
        reasonCodeList.add("SELECT");

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

        ArrayAdapter<String> reasonCodeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, reasonCodeList);
        reasonCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        spinner_reason_code.setAdapter(reasonCodeAdapter);

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
        spinner_reason_code.setOnItemSelectedListener(this);

        img_back.setOnClickListener(this);
        img_search_comp_device.setOnClickListener(this);
        btn_upload_image.setOnClickListener(this);
        /*txt_date.setOnClickListener(this);*/
        txt_failure_date.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        initLoadingDialog();

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {

            /*getFailureReportDropDownData();

            if (CommonFunction.nullPointerValidator(failureReportFetchDetailsByJobCodeDataResponse.getJob_id())) {
                getFetchDataJobId(failureReportFetchDetailsByJobCodeDataResponse.getJob_id());
            } else {
                Toast.makeText(context, "Enter valid Job Id", Toast.LENGTH_SHORT).show();
            }*/
        }

        /*String[] separated = failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address().split(",");

        Log.i(TAG, "onCreate: Job_id -> " + failureReportFetchDetailsByJobCodeDataResponse.getJob_id());

        txt_job_id.setText(CommonFunction.nullPointer(failureReportFetchDetailsByJobCodeDataResponse.getJob_id()));
        txt_building_name.setText(CommonFunction.nullPointer(separated[0]));

        if (CommonFunction.nullPointer(strScanType).equalsIgnoreCase("qr_code_scan")) {
            txt_bc_qr_code.setText(CommonFunction.nullPointer(strScanData));
            failureReportCreateTechRequest.setQr_bar_code(strScanData);
        } else if (CommonFunction.nullPointer(strScanType).equalsIgnoreCase("bar_code_scan")) {
            txt_bc_qr_code.setText(CommonFunction.nullPointer(strScanData));
            failureReportCreateTechRequest.setBar_code_job_no(strScanData);
        }

        failureReportCreateTechRequest.setJob_id(failureReportFetchDetailsByJobCodeDataResponse.getJob_id());
        failureReportCreateTechRequest.setSubmitted_by_emp_code(se_user_id);
        failureReportCreateTechRequest.setSubmitted_by_num(se_user_mobile_no);
        failureReportCreateTechRequest.setSubmitted_by_name(se_user_name);*/

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

        if (CommonFunction.nullPointerValidator(strMsg)) {
            txt_Message.setText(strMsg);
        }

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

    private void choosePetImage() {
        if (pet_imgList != null && pet_imgList.size() >= 4) {
            Toasty.warning(getApplicationContext(), " Sorry You Can't Add More Than 4", Toast.LENGTH_SHORT).show();
        } else {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
            } else {
                CropImage.activity().start(this);
            }
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.i(TAG, "hasPermissions: SDK version1 -> " + Build.VERSION.SDK_INT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } else {
            Log.i(TAG, "hasPermissions: SDK version2 -> " + Build.VERSION.SDK_INT);
        }
        return true;
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

        /*if (strDateType.equalsIgnoreCase("txt_date")) {
            txt_date.setText(dateTime);
        } else*/
        if (strDateType.equalsIgnoreCase("txt_failure_date")) {
            txt_failure_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            /*txt_date.setText(dateTime);*/
            txt_failure_date.setText(dateTime);
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
            /*if (strDateType.equalsIgnoreCase("txt_date")) {
                createRopeMaintenanceRequest.setActivity_date(formattedDate);
                ropeMaintenanceCheckDataRequest.setSubmitted_by_on(formattedDate);
                getFailureReportCheckDate();
            } else*/
            /*if (strDateType.equalsIgnoreCase("txt_failure_date")) {

                failureReportCreateTechRequest.setFailure_date(formattedDate);

            } else if (strDateType.equalsIgnoreCase("txt_both")) {

                failureReportCreateTechRequest.setFailure_date(formattedDate);
                failureReportCreateTechRequest.setSubmitted_by_on(formattedDate);

            }*/

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> " + e.getMessage());
        }
    }

    private void setDate(String inputDateTime) {

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//        String outputPattern = "dd-MM-yyyy hh:mm a";
        String outputPattern = "dd/MM/yyyy";
        String outputPattern2 = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat outputFormat2 = new SimpleDateFormat(outputPattern2);

        Date date = null;
        String str = null;
        String str2 = null;

        try {
            date = inputFormat.parse(inputDateTime);
            str = outputFormat.format(date);
            str2 = outputFormat2.format(date);
            formattedDate3 = str;/*.replace("AM", "am").replace("PM", "pm")*/
            formattedDate2 = str2;/*.replace("AM", "am").replace("PM", "pm")*/

            txt_installed_date.setText(formattedDate3);
//            failureReportCreateTechRequest.setInst_date(formattedDate2);

            Log.i(TAG, "setDate: formattedDate3-> " + formattedDate3);
            Log.i(TAG, "setDate: formattedDate2-> " + formattedDate2);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void setView(FailureReportFetchDetailsByJobCodeResponse.Data failureReportFetchDetailsByJobCodeDataResponse, List<JobListFailureReportResponse.Data> jobListFailureReportDataResponse) {
        setDate(jobListFailureReportDataResponse.get(0).getINST_ON());

        if (failureReportFetchDetailsByJobCodeDataResponse.getStatus() != null && !failureReportFetchDetailsByJobCodeDataResponse.getStatus().isEmpty()) {
            if (failureReportFetchDetailsByJobCodeDataResponse.getStatus().equalsIgnoreCase("a")) {
                txt_status.setText("Active");
            } else {
                txt_status.setText(failureReportFetchDetailsByJobCodeDataResponse.getStatus());
            }
        }

        txt_material_id.setText(failureReportFetchDetailsByJobCodeDataResponse.getMalt_id());
        txt_branch.setText(jobListFailureReportDataResponse.get(0).getBRCODE());
        edt_comp_device_id.setText(failureReportFetchDetailsByJobCodeDataResponse.getComp_device());
        txt_comp_device_name.setText(failureReportFetchDetailsByJobCodeDataResponse.getComp_device_name());
        txt_mechanic_id.setText(se_user_id);
        txt_mechanic_name.setText(se_user_name);
        txt_mechanic_phone.setText(se_user_mobile_no);
        txt_engineer_id.setText(jobListFailureReportDataResponse.get(0).getTech_code());
        txt_engineer_name.setText(jobListFailureReportDataResponse.get(0).getTech_name());
        txt_engineer_phone.setText(jobListFailureReportDataResponse.get(0).getTech_code());
        txt_route.setText(jobListFailureReportDataResponse.get(0).getSROUTE());
        txt_ser_mast_cust_name_add.setText(failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address());
        txt_install_address.setText(failureReportFetchDetailsByJobCodeDataResponse.getInstall_address());

        /*failureReportCreateTechRequest.setStatus(failureReportFetchDetailsByJobCodeDataResponse.getStatus());
        failureReportCreateTechRequest.setMatl_id(failureReportFetchDetailsByJobCodeDataResponse.getMalt_id());
        failureReportCreateTechRequest.setBr_code(jobListFailureReportDataResponse.get(0).getBRCODE());
        failureReportCreateTechRequest.setComp_device_no(failureReportFetchDetailsByJobCodeDataResponse.getComp_device());
        failureReportCreateTechRequest.setComp_device_name(failureReportFetchDetailsByJobCodeDataResponse.getComp_device_name());
        failureReportCreateTechRequest.setMech_code(se_user_id);
        failureReportCreateTechRequest.setMech_name(se_user_name);
        failureReportCreateTechRequest.setEng_code(jobListFailureReportDataResponse.get(0).getTech_code());
        failureReportCreateTechRequest.setEng_name(jobListFailureReportDataResponse.get(0).getTech_name());
        failureReportCreateTechRequest.setCustomer_address(failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address());
        failureReportCreateTechRequest.setIns_address(failureReportFetchDetailsByJobCodeDataResponse.getInstall_address());
        failureReportCreateTechRequest.setIns_address(failureReportFetchDetailsByJobCodeDataResponse.getInstall_address());
        failureReportCreateTechRequest.setRoute_code(jobListFailureReportDataResponse.get(0).getSROUTE());

//        failureReportCreateTechRequest.setComp_device_no(edt_comp_device_id.getText().toString().trim());

        Log.i(TAG, "setView: failureReportCreateTechRequest -> " + new Gson().toJson(failureReportCreateTechRequest));*/
    }

    private void setImageListView() {
        rv_upload_images_list.setVisibility(View.VISIBLE);
        rv_upload_images_list.setItemAnimator(new DefaultItemAnimator());
        PetCurrentImageList2Adapter petCurrentImageListAdapter = new PetCurrentImageList2Adapter(pet_imgList, new OnItemClickDataChangeListener() {
            @Override
            public void itemClickDataChangeListener(int position, String strParam, String strData) {
                if (strParam.equalsIgnoreCase("remove")) {
                    pet_imgList.remove(position);
                }
            }
        });
        rv_upload_images_list.setAdapter(petCurrentImageListAdapter);
    }

    private String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    private String getFilePathFromURI(Context context, Uri contentUri) {
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String path = context.getFilesDir() + "/" + "MyFirstApp/";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fullName = path + "mylog";
            File copyFile = new File(fullName);

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "copy: error -> " + e.getMessage());
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        int selPos = i - 1;

        Log.i(TAG, "onItemSelected: selPos -> " + selPos);

        /*if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_matl_return_type:
                    failureReportCreateTechRequest.setMatl_return_type("");
                    break;
                case R.id.spinner_department:
                    failureReportCreateTechRequest.setDepart_name("");
                    break;
                case R.id.spinner_service_type:
                    failureReportCreateTechRequest.setServ_type("");
                    break;
                case R.id.spinner_physical_cond:
                    failureReportCreateTechRequest.setPhys_cond("");
                    break;
                case R.id.spinner_current_status:
                    failureReportCreateTechRequest.setCurr_status("");
                    break;
                case R.id.spinner_reason_code:
                    failureReportCreateTechRequest.setReason_code("");
                    failureReportCreateTechRequest.setReason_name("");
                    break;
                case R.id.spinner_nature_of_failure:
                    failureReportCreateTechRequest.setNature_failure("");
                    break;
                case R.id.spinner_vvvf_trip_while:
                    failureReportCreateTechRequest.setVvvf_trip_while("");
                    break;
                case R.id.spinner_vvvf_trip_type:
                    failureReportCreateTechRequest.setVvvf_trip_type("");
                    break;
                case R.id.spinner_encoder_checked:
                    failureReportCreateTechRequest.setEncoder_checked("");
                    break;
                case R.id.spinner_load_inside_life:
                    failureReportCreateTechRequest.setLoad_inside_lift("");
                    break;
                case R.id.spinner_electric_supply:
                    failureReportCreateTechRequest.setElectric_supply("");
                    break;
                case R.id.spinner_battery_check_status:
                    failureReportCreateTechRequest.setBat_check_status("");
                    break;
                case R.id.spinner_battery_warranty_status:
                    failureReportCreateTechRequest.setBat_warranty_status("");
                    break;
                default:
                    break;
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.spinner_matl_return_type:
                    Log.i(TAG, "onItemSelected: spinner_matl_return_type: Matl_reture_type -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getMatl_reture_type().get(selPos)));
                    failureReportCreateTechRequest.setMatl_return_type(failureReportDropDownDataResponse.getData().getMatl_reture_type().get(selPos).getValue());
                    break;
                case R.id.spinner_department:
                    Log.i(TAG, "onItemSelected: spinner_department: Depart_name -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getDepart().get(selPos)));
                    failureReportCreateTechRequest.setDepart_name(failureReportDropDownDataResponse.getData().getDepart().get(selPos).getValue());
                    break;
                case R.id.spinner_service_type:
                    Log.i(TAG, "onItemSelected: spinner_service_type: Serv_type -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getServ_type().get(selPos)));
                    failureReportCreateTechRequest.setServ_type(failureReportDropDownDataResponse.getData().getServ_type().get(selPos).getValue());
                    break;
                case R.id.spinner_physical_cond:
                    Log.i(TAG, "onItemSelected: spinner_physical_cond: Phys_cond -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getPys_condition().get(selPos)));
                    failureReportCreateTechRequest.setPhys_cond(failureReportDropDownDataResponse.getData().getPys_condition().get(selPos).getValue());
                    break;
                case R.id.spinner_current_status:
                    Log.i(TAG, "onItemSelected: spinner_current_status: Curr_status -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getCuur_status().get(selPos)));
                    failureReportCreateTechRequest.setCurr_status(failureReportDropDownDataResponse.getData().getCuur_status().get(selPos).getValue());
                    break;
                case R.id.spinner_reason_code:
                    Log.i(TAG, "onItemSelected: spinner_reason_code: Reason_code - Reason_name -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getReasoncode().get(selPos)));
                    failureReportCreateTechRequest.setReason_code(failureReportDropDownDataResponse.getData().getReasoncode().get(selPos).getValue());
                    failureReportCreateTechRequest.setReason_name(failureReportDropDownDataResponse.getData().getReasoncode().get(selPos).getDisplay_name());
                    break;
                case R.id.spinner_nature_of_failure:
                    Log.i(TAG, "onItemSelected: spinner_nature_of_failure: Nature_failure -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getNatu_failure().get(selPos)));
                    failureReportCreateTechRequest.setNature_failure(failureReportDropDownDataResponse.getData().getNatu_failure().get(selPos).getValue());
                    break;
                case R.id.spinner_vvvf_trip_while:
                    Log.i(TAG, "onItemSelected: spinner_vvvf_trip_while: Vvvf_trip_while -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getVvvf_trip_while().get(selPos)));
                    failureReportCreateTechRequest.setVvvf_trip_while(failureReportDropDownDataResponse.getData().getVvvf_trip_while().get(selPos).getValue());
                    break;
                case R.id.spinner_vvvf_trip_type:
                    Log.i(TAG, "onItemSelected: spinner_vvvf_trip_type: Vvvf_trip_type -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getVvvf_trip_type().get(selPos)));
                    failureReportCreateTechRequest.setVvvf_trip_type(failureReportDropDownDataResponse.getData().getVvvf_trip_type().get(selPos).getValue());
                    break;
                case R.id.spinner_encoder_checked:
                    Log.i(TAG, "onItemSelected: spinner_encoder_checked: Encoder_checked -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getEncoder_checked().get(selPos)));
                    failureReportCreateTechRequest.setEncoder_checked(failureReportDropDownDataResponse.getData().getEncoder_checked().get(selPos).getValue());
                    break;
                case R.id.spinner_load_inside_life:
                    Log.i(TAG, "onItemSelected: spinner_load_inside_life: Load_inside_lift -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getLd_inside_lift().get(selPos)));
                    failureReportCreateTechRequest.setLoad_inside_lift(failureReportDropDownDataResponse.getData().getLd_inside_lift().get(selPos).getValue());
                    break;
                case R.id.spinner_electric_supply:
                    Log.i(TAG, "onItemSelected: spinner_electric_supply: Electric_supply -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getElectric_supply().get(selPos)));
                    failureReportCreateTechRequest.setElectric_supply(failureReportDropDownDataResponse.getData().getElectric_supply().get(selPos).getValue());
                    break;
                case R.id.spinner_battery_check_status:
                    Log.i(TAG, "onItemSelected: spinner_battery_check_status: Bat_check_status -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getBat_check_status().get(selPos)));
                    failureReportCreateTechRequest.setBat_check_status(failureReportDropDownDataResponse.getData().getBat_check_status().get(selPos).getValue());
                    break;
                case R.id.spinner_battery_warranty_status:
                    Log.i(TAG, "onItemSelected: spinner_battery_warranty_status: Bat_check_status -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getBat_warr_status().get(selPos)));
                    failureReportCreateTechRequest.setBat_warranty_status(failureReportDropDownDataResponse.getData().getBat_warr_status().get(selPos).getValue());
                    break;
                default:
                    break;
            }
        }
        Log.i(TAG, "onItemSelected: failureReportCreateTechRequest -> " + new Gson().toJson(failureReportCreateTechRequest));*/
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
            case R.id.img_search_comp_device: {

                /*if (CommonFunction.nullPointerValidator(edt_comp_device_id.getText().toString().toUpperCase().trim())) {
                    failureReportFetchDetailsByComIdRequest.setComp_device_no(edt_comp_device_id.getText().toString().toUpperCase().trim());
                    getFailureReportFetchDetailsByComId(failureReportFetchDetailsByComIdRequest);
                }*/
            }
            break;
            /*case R.id.txt_date: {
                strDateType = "txt_date";
                callDatePicker();
            }
            break;*/
            case R.id.txt_failure_date: {
                strDateType = "txt_failure_date";
                callDatePicker();
            }
            break;
            case R.id.btn_upload_image: {
                choosePetImage();
            }
            break;
            case R.id.btn_submit: {

//                validateFailureReportCreateTech();
//                Toasty.success(this, "Failure Report Submitted Successfully", Toasty.LENGTH_LONG).show();
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