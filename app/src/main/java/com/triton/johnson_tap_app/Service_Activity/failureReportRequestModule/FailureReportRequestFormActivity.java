package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImage;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.Adapter.PetCurrentImageList2Adapter;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.interfaces.OnItemClickFailureReportFetchDetailsByComIdResponseListener;
import com.triton.johnson_tap_app.requestpojo.FailureReportCreateTechRequest;
import com.triton.johnson_tap_app.requestpojo.FailureReportFetchDetailsByComIdRequest;
import com.triton.johnson_tap_app.requestpojo.FindFlrRbwNumberRequest;
import com.triton.johnson_tap_app.requestpojo.JobIdRequest;
import com.triton.johnson_tap_app.requestpojo.PetAppointmentCreateRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportCompDeviceListResponse;
import com.triton.johnson_tap_app.responsepojo.FailureReportDropDownDataResponse;
import com.triton.johnson_tap_app.responsepojo.FailureReportFetchDetailsByComIdResponse;
import com.triton.johnson_tap_app.responsepojo.FailureReportFetchDetailsByJobCodeResponse;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.JobListFailureReportResponse;
import com.triton.johnson_tap_app.responsepojo.NextSeqNoResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;
import com.triton.johnson_tap_app.utils.ConnectionDetector;
import com.triton.johnson_tap_app.utils.RestUtils;

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
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureReportRequestFormActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, OnItemClickFailureReportFetchDetailsByComIdResponseListener {

    private static String formattedDate = "", formattedDate2 = "", formattedDate3 = "";
    int PERMISSION_CLINIC = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private RequestBody rbJobId, rbSeqNo;
    private Context context;
    private LinearLayout ll_eng_privilege;
    private ImageView img_back, img_search_comp;
    private Button btn_upload_image, btn_submit;
    private RecyclerView rv_upload_images_list;
    private TextView txt_job_id, txt_building_name, /*txt_date,*/
            txt_department, txt_service_type,
            txt_failure_date, txt_installed_date, txt_bc_qr_code,
            txt_status, txt_branch, txt_comp_device_name, txt_mechanic_id,
            txt_mechanic_name, /*txt_mechanic_phone,*/
            txt_engineer_id, txt_engineer_name, /*txt_engineer_phone,*/
            txt_ser_mast_cust_name_add, txt_install_address, txt_route, txt_menu_name;
    private EditText edt_model_make, edt_rating, edt_serial_no,
            edt_observation, edt_supply_vol,
            edt_tech_exp_comment, /*edt_curlss_no, edt_prvlss_no,*/
            edt_vvf_remark,
            edt_vvf_item, edt_electric_volt;
    private Spinner spinner_matl_return_type, /*spinner_department, spinner_service_type,*/
            spinner_physical_cond,
            spinner_current_status, spinner_nature_of_failure, spinner_vvvf_trip_while, spinner_vvvf_trip_type,
            spinner_encoder_checked, spinner_load_inside_life, spinner_electric_supply, spinner_battery_check_status,
            spinner_battery_warranty_status/*, spinner_reason_code*/;
    private List<JobListFailureReportResponse.Data> jobListFailureReportDataResponse = new ArrayList<>();
    private FailureReportFetchDetailsByJobCodeResponse.Data failureReportFetchDetailsByJobCodeDataResponse = new FailureReportFetchDetailsByJobCodeResponse.Data();
    private FailureReportDropDownDataResponse failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();
    private FailureReportFetchDetailsByComIdRequest failureReportFetchDetailsByComIdRequest = new FailureReportFetchDetailsByComIdRequest();
    private FailureReportFetchDetailsByComIdResponse failureReportFetchDetailsByComIdResponse = new FailureReportFetchDetailsByComIdResponse();
    private FailureReportCreateTechRequest failureReportCreateTechRequest = new FailureReportCreateTechRequest();
    private FindFlrRbwNumberRequest findFlrRbwNumberRequest = new FindFlrRbwNumberRequest();
    private ArrayList<FailureReportCreateTechRequest.File_image> failureReportCreateTechFileImageRequestList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private String TAG = FailureReportRequestFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", emp_Type = "", networkStatus = "", uploadImagePath = "",
            strScanType = "", strScanData = "", message = "", userid = "", str_title = "", strSeqNo = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private boolean checkDate = false;
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
        setContentView(R.layout.activity_failure_report_request_form);

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
        img_search_comp = findViewById(R.id.img_search_comp);

        btn_upload_image = findViewById(R.id.btn_upload_image);
        btn_submit = findViewById(R.id.btn_submit);

        rv_upload_images_list = findViewById(R.id.rv_upload_images_list);

        txt_job_id = findViewById(R.id.txt_job_id);
        txt_menu_name = findViewById(R.id.txt_menu_name);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_department = findViewById(R.id.txt_department);
        txt_service_type = findViewById(R.id.txt_service_type);
        /*txt_date = findViewById(R.id.txt_date);*/
        txt_failure_date = findViewById(R.id.txt_failure_date);
        txt_installed_date = findViewById(R.id.txt_installed_date);
        txt_bc_qr_code = findViewById(R.id.txt_bc_qr_code);
        txt_status = findViewById(R.id.txt_status);
        txt_branch = findViewById(R.id.txt_branch);
        txt_comp_device_name = findViewById(R.id.txt_comp_device_name);
        txt_mechanic_id = findViewById(R.id.txt_mechanic_id);
        txt_mechanic_name = findViewById(R.id.txt_mechanic_name);
        /*txt_mechanic_phone = findViewById(R.id.txt_mechanic_phone);*/
        txt_engineer_id = findViewById(R.id.txt_engineer_id);
        txt_engineer_name = findViewById(R.id.txt_engineer_name);
        /*txt_engineer_phone = findViewById(R.id.txt_engineer_phone);*/
        txt_ser_mast_cust_name_add = findViewById(R.id.txt_ser_mast_cust_name_add);
        txt_install_address = findViewById(R.id.txt_install_address);
        txt_route = findViewById(R.id.txt_route);

        edt_model_make = findViewById(R.id.edt_model_make);
        edt_rating = findViewById(R.id.edt_rating);
        edt_serial_no = findViewById(R.id.edt_serial_no);
        edt_observation = findViewById(R.id.edt_observation);
        edt_supply_vol = findViewById(R.id.edt_supply_vol);
        edt_tech_exp_comment = findViewById(R.id.edt_tech_exp_comment);
        /*edt_curlss_no = findViewById(R.id.edt_curlss_no);
        edt_prvlss_no = findViewById(R.id.edt_prvlss_no);*/
        edt_vvf_remark = findViewById(R.id.edt_vvf_remark);
        edt_vvf_item = findViewById(R.id.edt_vvf_item);
        edt_electric_volt = findViewById(R.id.edt_electric_volt);

        spinner_matl_return_type = findViewById(R.id.spinner_matl_return_type);
        /*spinner_department = findViewById(R.id.spinner_department);
        spinner_service_type = findViewById(R.id.spinner_service_type);*/
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
        /*spinner_reason_code = findViewById(R.id.spinner_reason_code);*/

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("failureReportFetchDetailsByJobCodeDataResponse")) {
                failureReportFetchDetailsByJobCodeDataResponse = extra.getParcelable("failureReportFetchDetailsByJobCodeDataResponse");
            }
            if (extra.containsKey("strScanType")) {
                strScanType = extra.getString("strScanType");
            }
            if (extra.containsKey("strScanData")) {
                strScanData = extra.getString("strScanData");
            }
            if (extra.containsKey("str_title")) {
                str_title = extra.getString("str_title");
            }
        }

        Log.i(TAG, "onCreate: failureReportFetchDetailsByJobCodeDataResponse -> " + new Gson().toJson(failureReportFetchDetailsByJobCodeDataResponse));

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
        /*spinner_department.setAdapter(departmentAdapter);
        spinner_service_type.setAdapter(serviceTypeAdapter);*/
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
        /*spinner_reason_code.setAdapter(reasonCodeAdapter);*/

        spinner_matl_return_type.setOnItemSelectedListener(this);
        /*spinner_department.setOnItemSelectedListener(this);
        spinner_service_type.setOnItemSelectedListener(this);*/
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
        /*spinner_reason_code.setOnItemSelectedListener(this);*/

        img_back.setOnClickListener(this);
        img_search_comp.setOnClickListener(this);
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
            getNextSeqNo();
            getFailureReportDropDownData();
        }

        String[] separated = failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address().split(",");

        Log.i(TAG, "onCreate: Job_id -> " + failureReportFetchDetailsByJobCodeDataResponse.getJob_id());

        rbJobId = RequestBody.create(MediaType.parse("multipart/form-data"), failureReportFetchDetailsByJobCodeDataResponse.getJob_id());

        txt_menu_name.setText(str_title);
        txt_job_id.setText(CommonFunction.nullPointer(failureReportFetchDetailsByJobCodeDataResponse.getJob_id()));
        txt_building_name.setText(CommonFunction.nullPointer(separated[0]));

        if (CommonFunction.nullPointer(strScanType).equalsIgnoreCase("qr_code_scan")) {
            txt_bc_qr_code.setText(CommonFunction.nullPointer(strScanData));
            failureReportCreateTechRequest.setQr_bar_code(strScanData);
        } else if (CommonFunction.nullPointer(strScanType).equalsIgnoreCase("bar_code_scan")) {
            txt_bc_qr_code.setText(CommonFunction.nullPointer(strScanData));
            failureReportCreateTechRequest.setBar_code_job_no(strScanData);
        } else {
            txt_bc_qr_code.setText("NO CODE");
            failureReportCreateTechRequest.setBar_code_job_no("NO CODE");
        }

        findFlrRbwNumberRequest.setBr_code(se_user_location);

        failureReportCreateTechRequest.setJob_id(failureReportFetchDetailsByJobCodeDataResponse.getJob_id());
        failureReportCreateTechRequest.setSubmitted_by_emp_code(se_user_id);
        failureReportCreateTechRequest.setSubmitted_by_num(se_user_mobile_no);
        failureReportCreateTechRequest.setSubmitted_by_name(se_user_name);
        failureReportCreateTechRequest.setDepart_name(failureReportFetchDetailsByJobCodeDataResponse.getDepartment());
        failureReportCreateTechRequest.setServ_type(failureReportFetchDetailsByJobCodeDataResponse.getServ_type());

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
            if (strDateType.equalsIgnoreCase("txt_failure_date")) {

                failureReportCreateTechRequest.setFailure_date(formattedDate);

            } else if (strDateType.equalsIgnoreCase("txt_both")) {

                failureReportCreateTechRequest.setFailure_date(formattedDate);
                failureReportCreateTechRequest.setSubmitted_by_on(formattedDate);
            }

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
            failureReportCreateTechRequest.setInst_date(formattedDate2);

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

        txt_branch.setText(jobListFailureReportDataResponse.get(0).getBRCODE());
        if (nullPointerValidator(failureReportFetchDetailsByJobCodeDataResponse.getMalt_id())) {
            txt_comp_device_name.setText(String.format("%s - %s - %s", failureReportFetchDetailsByJobCodeDataResponse.getComp_device(), failureReportFetchDetailsByJobCodeDataResponse.getComp_device_name(), failureReportFetchDetailsByJobCodeDataResponse.getMalt_id()));
        } else {
            txt_comp_device_name.setText(String.format("%s - %s", failureReportFetchDetailsByJobCodeDataResponse.getComp_device(), failureReportFetchDetailsByJobCodeDataResponse.getComp_device_name()));
        }
        txt_mechanic_id.setText(se_user_id);
        txt_mechanic_name.setText(se_user_name);
        /*txt_mechanic_phone.setText(se_user_mobile_no);*/
        txt_engineer_id.setText(jobListFailureReportDataResponse.get(0).getTech_code());
        txt_engineer_name.setText(jobListFailureReportDataResponse.get(0).getTech_name());
        /*txt_engineer_phone.setText(jobListFailureReportDataResponse.get(0).getTech_code());*/
        txt_route.setText(jobListFailureReportDataResponse.get(0).getSROUTE());
        txt_ser_mast_cust_name_add.setText(failureReportFetchDetailsByJobCodeDataResponse.getCustomer_address());
        txt_install_address.setText(failureReportFetchDetailsByJobCodeDataResponse.getInstall_address());

        failureReportCreateTechRequest.setStatus(failureReportFetchDetailsByJobCodeDataResponse.getStatus());
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

        failureReportCreateTechRequest.setDepart_name(jobListFailureReportDataResponse.get(0).getDEPRT());
        failureReportCreateTechRequest.setServ_type(jobListFailureReportDataResponse.get(0).getSERV_TYPE());

        if (!departmentList.isEmpty() && failureReportDropDownDataResponse != null
                && !failureReportDropDownDataResponse.getData().getDepart().isEmpty()) {
            for (int i = 0; i < failureReportDropDownDataResponse.getData().getDepart().size(); i++) {
                if (failureReportDropDownDataResponse.getData().getDepart().get(i).getValue().equalsIgnoreCase(jobListFailureReportDataResponse.get(0).getDEPRT())) {
                    txt_department.setText(failureReportDropDownDataResponse.getData().getDepart().get(i).getDisplay_name());
                    /*spinner_department.setSelection(i + 1);*/
                }
            }
        }

        if (!serviceTypeList.isEmpty() && failureReportDropDownDataResponse != null
                && !failureReportDropDownDataResponse.getData().getServ_type().isEmpty()) {
            for (int i = 0; i < failureReportDropDownDataResponse.getData().getServ_type().size(); i++) {
                if (failureReportDropDownDataResponse.getData().getServ_type().get(i).getValue().equalsIgnoreCase(jobListFailureReportDataResponse.get(0).getSERV_TYPE())) {
                    txt_service_type.setText(failureReportDropDownDataResponse.getData().getServ_type().get(i).getDisplay_name());
                    /*spinner_service_type.setSelection(i + 1);*/
                }
            }
        }

        Log.i(TAG, "setView: failureReportCreateTechRequest -> " + new Gson().toJson(failureReportCreateTechRequest));
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

    private void validateFailureReportCreateTech() {

        Log.i(TAG, "validateFailureReportCreateTech: failureReportCreateTechRequest -> " + new Gson().toJson(failureReportCreateTechRequest));

        failureReportCreateTechRequest.setSeq_no(nullPointer(strSeqNo));
        failureReportCreateTechRequest.setModel_make(nullPointer(edt_model_make.getText().toString().trim()));
        failureReportCreateTechRequest.setRating(nullPointer(edt_rating.getText().toString().trim()));
        failureReportCreateTechRequest.setSerial_no(nullPointer(edt_serial_no.getText().toString().trim()));
        failureReportCreateTechRequest.setObservation(nullPointer(edt_observation.getText().toString().trim()));
        failureReportCreateTechRequest.setSupply_vol(nullPointer(edt_supply_vol.getText().toString().trim()));
        failureReportCreateTechRequest.setTech_comment(nullPointer(edt_tech_exp_comment.getText().toString().trim()));
        /*failureReportCreateTechRequest.setCurlss_no(nullPointer(edt_curlss_no.getText().toString().trim()));
        failureReportCreateTechRequest.setPrvlss_no(nullPointer(edt_prvlss_no.getText().toString().trim()));*/

        if (!nullPointerValidator(failureReportCreateTechRequest.getJob_id())) {
            ErrorMsgDialog("Please Select Job ID");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getFailure_date())) {
            ErrorMsgDialog("Please Select Failure Date");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSeq_no())) {
            ErrorMsgDialog("Please Select Seq No");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getMatl_return_type())) {
            ErrorMsgDialog("Please Select Material Return Type");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getStatus())) {
            ErrorMsgDialog("Please Select Status");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getBr_code())) {
            ErrorMsgDialog("Please Select Branch Code");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getComp_device_no()) || failureReportCreateTechRequest.getComp_device_no().equalsIgnoreCase("No NUM")) {
            ErrorMsgDialog("Please Select Comp/Device ID");
        } else /*if (!nullPointerValidator(failureReportCreateTechRequest.getMatl_id()) || failureReportCreateTechRequest.getMatl_id().equalsIgnoreCase("NA")) {
            ErrorMsgDialog("Please Select Material ID");
        } else */if (!nullPointerValidator(failureReportCreateTechRequest.getComp_device_name()) || failureReportCreateTechRequest.getComp_device_name().equalsIgnoreCase("NO NAME")) {
            ErrorMsgDialog("Please Select Comp/Device Name");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getDepart_name())) {
            ErrorMsgDialog("Please Select Department Name");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getServ_type())) {
            ErrorMsgDialog("Please Select Service Type");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getModel_make())) {
            ErrorMsgDialog("Please Enter Model & Make");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getRating())) {
            ErrorMsgDialog("Please Enter Rating");
        } else if (failureReportCreateTechRequest.getRating().length() <= 1) {
            ErrorMsgDialog("Please Enter Valid Rating.\n(Note: Accepts Only Above One Digit value)");
        } else /*if (!nullPointerValidator(failureReportCreateTechRequest.getSerial_no())) {
            ErrorMsgDialog("Please Enter Serial No.");
        } else */if (!nullPointerValidator(failureReportCreateTechRequest.getObservation())) {
            ErrorMsgDialog("Please Enter Observation");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSupply_vol())) {
            ErrorMsgDialog("Please Enter Supply Vol");
        } else if (failureReportCreateTechRequest.getSupply_vol().length() <= 1) {
            ErrorMsgDialog("Please Enter Valid Supply Vol.\n(Note: Accepts Only Above One Digit value)");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getInst_date())) {
            ErrorMsgDialog("Please Select Installed Date");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getPhys_cond())) {
            ErrorMsgDialog("Please Select Physical Cond");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getCurr_status())) {
            ErrorMsgDialog("Please Select Current Status");
        } else /*if (!nullPointerValidator(failureReportCreateTechRequest.getTech_comment())) {
            ErrorMsgDialog("Please Enter Technician Expect comment");
        } else */if (!nullPointerValidator(failureReportCreateTechRequest.getMech_code())) {
            ErrorMsgDialog("Please Enter Mechanic ID");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getMech_name())) {
            ErrorMsgDialog("Please Enter Mechanic Name");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getEng_code())) {
            ErrorMsgDialog("Please Enter Engineer ID");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getEng_name())) {
            ErrorMsgDialog("Please Enter Engineer Name");
        } /*else if (!nullPointerValidator(failureReportCreateTechRequest.getReason_code())) {
            ErrorMsgDialog("Please Select Reason Code");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getReason_name())) {
            ErrorMsgDialog("Please Select Reason Code");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getRoute_code())) {
            ErrorMsgDialog("Please Enter Route Code");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getCurlss_no())) {
            ErrorMsgDialog("Please Enter Curlss No.");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getPrvlss_no())) {
            ErrorMsgDialog("Please Enter Prvlss No.");
        }*/ else if (!nullPointerValidator(failureReportCreateTechRequest.getCustomer_address())) {
            ErrorMsgDialog("Please Enter Customer Address");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getIns_address())) {
            ErrorMsgDialog("Please Enter Installation Address");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSubmitted_by_emp_code())) {
            ErrorMsgDialog("Please Enter Submitted By Emp Code");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSubmitted_by_name())) {
            ErrorMsgDialog("Please Enter Submitted By Name");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Enter Submitted By Num");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Enter Submitted By On");
        } else if (!nullPointerValidator(failureReportCreateTechRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Enter Submitted By On");
        } /*else if (pet_imgList == null || pet_imgList.isEmpty()) {
            ErrorMsgDialog("Please Upload At Least One Image");
        }*/ else {

            if (pet_imgList != null && !pet_imgList.isEmpty()) {
                Log.i(TAG, "validateCreateFailureReportRequest: count -> " + pet_imgList.size() + " pet_imgList -> " + new Gson().toJson(pet_imgList));

                failureReportCreateTechFileImageRequestList = new ArrayList<>();

                for (PetAppointmentCreateRequest.PetImgBean image : pet_imgList) {
                    failureReportCreateTechFileImageRequestList.add(new FailureReportCreateTechRequest.File_image(image.getPet_img()));
                }

                if (!failureReportCreateTechFileImageRequestList.isEmpty()) {
                    failureReportCreateTechRequest.setFile_image(failureReportCreateTechFileImageRequestList);
                }
            }

            getFailureReportCreateTech(failureReportCreateTechRequest);
        }
        Log.i(TAG, "validateCreateFailureReportRequest: failureReportCreateTechRequest (2) -> " + new Gson().toJson(failureReportCreateTechRequest));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = null;
                    if (result != null) {
                        resultUri = result.getUriContent();
                    }

                    if (resultUri != null) {
                        Log.i(TAG, "onActivityResult: resultUri -> " + resultUri);

                        String filename = getFileName(resultUri);
                        Log.i(TAG, "onActivityResult: filename -> " + filename);

                        String filePath = getFilePathFromURI(this, resultUri);

                        assert filePath != null;

                        File file = new File(filePath); // initialize file here

                        long length = file.length() / 1024; // Size in KB

                        Log.i(TAG, "onActivityResult: length -> " + length);

                        if (length > 2000) {

                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("File Size")
                                    .setContentText("Please choose file size less than 2 MB ")
                                    .setConfirmText("Ok")
                                    .show();
                        } else {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());

                            filePart = MultipartBody.Part.createFormData("sampleFile", userid + currentDateandTime + filename, RequestBody.create(MediaType.parse("image/*"), file));
                            getFailureReportUpload();
                        }
                    } else {
                        Toasty.warning(this, "Image Error!!Please upload Some other image", Toasty.LENGTH_LONG).show();
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: error -> " + e.getMessage());
        }
    }

    private JobIdRequest jobListRequest(String strSearch) {

        JobIdRequest job = new JobIdRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setJob_id(strSearch);
            Log.i(TAG, "jobListRequest: JobIdRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void getFetchDataJobId(String strSearch) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        JobIdRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<JobListFailureReportResponse> call = apiInterface.getFailureReportFetchDataJobId(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFetchDataJobId: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<JobListFailureReportResponse>() {
                @Override
                public void onResponse(@NonNull Call<JobListFailureReportResponse> call, @NonNull Response<JobListFailureReportResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFetchDataJobId: onResponse: JobListFailureReportDataResponse -> " + new Gson().toJson(response.body()));
                    jobListFailureReportDataResponse = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();
                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                jobListFailureReportDataResponse = response.body().getData();
                                if (jobListFailureReportDataResponse.isEmpty()) {
                                    ErrorMsgDialog("No Records Found");
                                } else {
                                    setView(failureReportFetchDetailsByJobCodeDataResponse, jobListFailureReportDataResponse);
                                }
                            }
                        } else {
                            ErrorMsgDialog(message);
                        }
                    } else {
                        ErrorMsgDialog("");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JobListFailureReportResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFetchDataJobId: onFailure: error --> " + t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    private void getFailureReportFetchDetailsByComId(FailureReportFetchDetailsByComIdRequest failureReportFetchDetailsByComIdRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }
        Log.i(TAG, "getFailureReportFetchDetailsByComId: failureReportFetchDetailsByComIdRequest -> " + new Gson().toJson(failureReportFetchDetailsByComIdRequest));
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<FailureReportFetchDetailsByComIdResponse> call = apiInterface.getFailureReportFetchDetailsByComId(RestUtils.getContentType(), failureReportFetchDetailsByComIdRequest);
        Log.i(TAG, "getFailureReportFetchDetailsByComId: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FailureReportFetchDetailsByComIdResponse>() {
            @Override
            public void onResponse(@NonNull Call<FailureReportFetchDetailsByComIdResponse> call, @NonNull Response<FailureReportFetchDetailsByComIdResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getFailureReportFetchDetailsByComId: onResponse: FailureReportFetchDetailsByComIdResponse -> " + new Gson().toJson(response.body()));
                failureReportFetchDetailsByComIdResponse.setData(new ArrayList<>());
                /*if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            failureReportFetchDetailsByComIdResponse.setData(response.body().getData());
                            if (failureReportFetchDetailsByComIdResponse.getData().isEmpty()) {
                                ErrorMsgDialog("No Records Found");
                            } else {
                                txt_comp_device_name.setText(failureReportFetchDetailsByComIdResponse.getData().get(0).getComp_device_name());
                                txt_material_id.setText(failureReportFetchDetailsByComIdResponse.getData().get(0).getMalt_id());
                                failureReportCreateTechRequest.setComp_device_name(failureReportFetchDetailsByComIdResponse.getData().get(0).getComp_device_name());
                                failureReportCreateTechRequest.setMatl_id("" + failureReportFetchDetailsByComIdResponse.getData().get(0).getMalt_id());
                            }
                        }
                    } else {
                        ErrorMsgDialog(message);
                    }
                } else {
                    ErrorMsgDialog("");
                }*/
            }

            @Override
            public void onFailure(@NonNull Call<FailureReportFetchDetailsByComIdResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getFailureReportFetchDetailsByComId: onFailure: error --> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

                        if (failureReportDropDownDataResponse.getData() != null) {

                            if (failureReportDropDownDataResponse.getData().getMatl_reture_type() != null
                                    && !failureReportDropDownDataResponse.getData().getMatl_reture_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Matl_reture_type matlReturnType : failureReportDropDownDataResponse.getData().getMatl_reture_type()) {
                                    matlReturnTypeList.add(matlReturnType.getDisplay_name());
                                }

                                if (!matlReturnTypeList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getMatl_reture_type().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getMatl_reture_type().get(i).getValue().equalsIgnoreCase("P")) {
                                            spinner_matl_return_type.setSelection(i + 1);
                                        }
                                    }
                                }
                            }

                            if (failureReportDropDownDataResponse.getData().getDepart() != null
                                    && !failureReportDropDownDataResponse.getData().getDepart().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Depart depart : failureReportDropDownDataResponse.getData().getDepart()) {
                                    departmentList.add(depart.getDisplay_name());
                                }

                                if (!departmentList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getDepart().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getDepart().get(i).getValue().equalsIgnoreCase(failureReportFetchDetailsByJobCodeDataResponse.getDepartment())) {
                                            txt_department.setText(failureReportDropDownDataResponse.getData().getDepart().get(i).getDisplay_name());
                                            /*spinner_department.setSelection(i + 1);*/
                                        }
                                    }
                                }
                            }

                            if (failureReportDropDownDataResponse.getData().getServ_type() != null
                                    && !failureReportDropDownDataResponse.getData().getServ_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Serv_type servType : failureReportDropDownDataResponse.getData().getServ_type()) {
                                    serviceTypeList.add(servType.getDisplay_name());
                                }

                                if (!serviceTypeList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getServ_type().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getServ_type().get(i).getValue().equalsIgnoreCase(failureReportFetchDetailsByJobCodeDataResponse.getServ_type())) {
                                            txt_service_type.setText(failureReportDropDownDataResponse.getData().getServ_type().get(i).getDisplay_name());
                                            /*spinner_service_type.setSelection(i + 1);*/
                                        }
                                    }
                                }
                            }

                            if (failureReportDropDownDataResponse.getData().getPys_condition() != null
                                    && !failureReportDropDownDataResponse.getData().getPys_condition().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Pys_condition pysCondition : failureReportDropDownDataResponse.getData().getPys_condition()) {
                                    physicalCondList.add(pysCondition.getDisplay_name());
                                }

                                if (!physicalCondList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getPys_condition().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getPys_condition().get(i).getValue().equalsIgnoreCase("O")) {
                                            spinner_physical_cond.setSelection(i + 1);
                                        }
                                    }
                                }
                            }

                            if (failureReportDropDownDataResponse.getData().getCuur_status() != null
                                    && !failureReportDropDownDataResponse.getData().getCuur_status().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Cuur_status cuurStatus : failureReportDropDownDataResponse.getData().getCuur_status()) {
                                    currentStatusList.add(cuurStatus.getDisplay_name());
                                }

                                if (!currentStatusList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getCuur_status().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getCuur_status().get(i).getValue().equalsIgnoreCase("S")) {
                                            spinner_current_status.setSelection(i + 1);
                                        }
                                    }
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
                            if (failureReportDropDownDataResponse.getData().getReasoncode() != null
                                    && !failureReportDropDownDataResponse.getData().getReasoncode().isEmpty()) {
                                for (FailureReportDropDownDataResponse.ReasonCode reasonCode : failureReportDropDownDataResponse.getData().getReasoncode()) {
                                    reasonCodeList.add(reasonCode.getDisplay_name());
                                }
                            }

                            if (CommonFunction.nullPointerValidator(failureReportFetchDetailsByJobCodeDataResponse.getJob_id())) {
                                getFetchDataJobId(failureReportFetchDetailsByJobCodeDataResponse.getJob_id());
                            } else {
                                Toast.makeText(context, "Enter valid Job Id", Toast.LENGTH_SHORT).show();
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

    private void getNextSeqNo() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<NextSeqNoResponse> call = apiInterface.getNextSeqNo(RestUtils.getContentType());
        Log.i(TAG, "getNextSeqNo: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<NextSeqNoResponse>() {
            @Override
            public void onResponse(@NonNull Call<NextSeqNoResponse> call, @NonNull Response<NextSeqNoResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getNextSeqNo: onResponse: NextSeqNoResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().getCode() == 200) {

                        rbSeqNo = RequestBody.create(MediaType.parse("multipart/form-data"), response.body().getData().get(0).getNEXTVAL());
                        strSeqNo = response.body().getData().get(0).getNEXTVAL();
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NextSeqNoResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getNextSeqNo: onFailure: error --> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFailureReportUpload() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);
//        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(filePart);
        Call<FileUploadResponse> call = apiInterface.getFailureReportUpload(rbJobId, rbSeqNo, filePart);

        Log.i(TAG, "getFailureReportUpload: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                Log.i(TAG, "getFailureReportUpload: onResponse: FileUploadResponse -> " + new Gson().toJson(response.body()));

                dialog.dismiss();
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        uploadImagePath = response.body().getData();
                        PetAppointmentCreateRequest.PetImgBean petImgBean = new PetAppointmentCreateRequest.PetImgBean();
                        petImgBean.setPet_img(uploadImagePath);
                        pet_imgList.add(petImgBean);
                        if (uploadImagePath != null) {
                            setImageListView();
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
                dialog.dismiss();
                Log.i(TAG, "getFailureReportUpload: onFailure: error -> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void getFailureReportCreateTech(FailureReportCreateTechRequest failureReportCreateTechRequest) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        Log.i(TAG, "getFailureReportCreateTech: failureReportCreateTechRequest -> " + new Gson().toJson(failureReportCreateTechRequest));
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getFailureReportCreateTech(RestUtils.getContentType(), failureReportCreateTechRequest);
        Log.i(TAG, "getFailureReportCreateTech: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getFailureReportCreateTech: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getFailureReportCreateTech: onFailure: error --> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void itemClickFailureReportFetchDetailsByComIdResponseListener(FailureReportCompDeviceListResponse.Data failureReportCompDeviceListDataResponse) {
        Log.i(TAG, "itemClickFailureReportFetchDetailsByComIdResponseListener: failureReportCompDeviceListDataResponse -> " + new Gson().toJson(failureReportCompDeviceListDataResponse));

        if (nullPointerValidator(failureReportCompDeviceListDataResponse.getST_PMH_BARCODEID())) {
            txt_comp_device_name.setText(String.format("%s - %s - %s", failureReportCompDeviceListDataResponse.getST_PMH_PARTNO(), failureReportCompDeviceListDataResponse.getST_PMH_PARTNAME(), failureReportCompDeviceListDataResponse.getST_PMH_BARCODEID()));
        } else {
            txt_comp_device_name.setText(String.format("%s - %s", failureReportCompDeviceListDataResponse.getST_PMH_PARTNO(), failureReportCompDeviceListDataResponse.getST_PMH_PARTNAME()));
        }
        failureReportCreateTechRequest.setComp_device_no(failureReportCompDeviceListDataResponse.getST_PMH_PARTNO());
        failureReportCreateTechRequest.setComp_device_name(failureReportCompDeviceListDataResponse.getST_PMH_PARTNAME());
        failureReportCreateTechRequest.setMatl_id(failureReportCompDeviceListDataResponse.getST_PMH_BARCODEID());

        onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        int selPos = i - 1;

        Log.i(TAG, "onItemSelected: selPos -> " + selPos);

        if (item.equalsIgnoreCase("select")) {
            switch (adapterView.getId()) {
                case R.id.spinner_matl_return_type:
                    failureReportCreateTechRequest.setMatl_return_type("");
                    break;
                /*case R.id.spinner_department:
                    failureReportCreateTechRequest.setDepart_name("");
                    break;
                case R.id.spinner_service_type:
                    failureReportCreateTechRequest.setServ_type("");
                    break;*/
                case R.id.spinner_physical_cond:
                    failureReportCreateTechRequest.setPhys_cond("");
                    break;
                case R.id.spinner_current_status:
                    failureReportCreateTechRequest.setCurr_status("");
                    break;
                /*case R.id.spinner_reason_code:
                    failureReportCreateTechRequest.setReason_code("");
                    failureReportCreateTechRequest.setReason_name("");
                    break;*/
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
                /*case R.id.spinner_department:
                    Log.i(TAG, "onItemSelected: spinner_department: Depart_name -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getDepart().get(selPos)));
                    failureReportCreateTechRequest.setDepart_name(failureReportDropDownDataResponse.getData().getDepart().get(selPos).getValue());
                    break;
                case R.id.spinner_service_type:
                    Log.i(TAG, "onItemSelected: spinner_service_type: Serv_type -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getServ_type().get(selPos)));
                    failureReportCreateTechRequest.setServ_type(failureReportDropDownDataResponse.getData().getServ_type().get(selPos).getValue());
                    break;*/
                case R.id.spinner_physical_cond:
                    Log.i(TAG, "onItemSelected: spinner_physical_cond: Phys_cond -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getPys_condition().get(selPos)));
                    failureReportCreateTechRequest.setPhys_cond(failureReportDropDownDataResponse.getData().getPys_condition().get(selPos).getValue());
                    break;
                case R.id.spinner_current_status:
                    Log.i(TAG, "onItemSelected: spinner_current_status: Curr_status -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getCuur_status().get(selPos)));
                    failureReportCreateTechRequest.setCurr_status(failureReportDropDownDataResponse.getData().getCuur_status().get(selPos).getValue());
                    break;
                /*case R.id.spinner_reason_code:
                    Log.i(TAG, "onItemSelected: spinner_reason_code: Reason_code - Reason_name -> " + new Gson().toJson(failureReportDropDownDataResponse.getData().getReasoncode().get(selPos)));
                    failureReportCreateTechRequest.setReason_code(failureReportDropDownDataResponse.getData().getReasoncode().get(selPos).getValue());
                    failureReportCreateTechRequest.setReason_name(failureReportDropDownDataResponse.getData().getReasoncode().get(selPos).getDisplay_name());
                    break;*/
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
        Log.i(TAG, "onItemSelected: failureReportCreateTechRequest -> " + new Gson().toJson(failureReportCreateTechRequest));
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
            case R.id.img_search_comp: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("ComponentDeviceListFragment")
                        .add(android.R.id.content, new ComponentDeviceListFragment(failureReportFetchDetailsByJobCodeDataResponse.getJob_id(), this), "ComponentDeviceListFragment")
                        .commit();
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

                validateFailureReportCreateTech();
//                Toasty.success(this, "Failure Report Submitted Successfully", Toasty.LENGTH_LONG).show();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
        /*super.onBackPressed();
        finish();*/
    }
}