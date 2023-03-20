package com.triton.johnson_tap_app.Service_Activity.safetyAuditModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.CreateSafetyAuditRequest;
import com.triton.johnson_tap_app.requestpojo.SafetyAuditCheckDataRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.JobListSafetyAuditResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafetyAuditFormActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static String formattedDate = "";
    private TextView txt_jobid, txt_building_name, txt_engineer_name, txt_conducted_on;
    private EditText edt_comments_a, edt_comments_b, edt_comments_c, edt_comments_d, edt_general;
    private String TAG = SafetyAuditFormActivity.class.getSimpleName(), strDateType = "", se_user_id = "",
            se_user_name = "", se_user_mobile_no = "", se_user_location = "", networkStatus = "", uploadImagePath = "";
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private ImageView iv_back;
    private CheckBox cb1A, cb1B, cb1C, cb1D, cb2A, cb2B, cb2C, cb2D, cb3A, cb3B, cb3C, cb3D, cb4A, cb4B, cb4C, cb4D,
            cb5A, cb5B, cb5C, cb5D, cb6A, cb6B, cb6C, cb6D, cb7A, cb7B, cb7C, cb7D, cb8A, cb8B, cb8C, cb8D,
            cb9A, cb9B, cb9C, cb9D, cb10A, cb10B, cb10C, cb10D, cb11A, cb11B, cb11C, cb11D, cb12A, cb12B, cb12C, cb12D,
            cb13A, cb13B, cb13C, cb13D, cb14A, cb14B, cb14C, cb14D, cb15A, cb15B, cb15C, cb15D, cb16A, cb16B, cb16C, cb16D,
            cb17A, cb17B, cb17C, cb17D, cb18A, cb18B, cb18C, cb18D, cb19A, cb19B, cb19C, cb19D, cb20A, cb20B, cb20C, cb20D,
            cb21A, cb21B, cb21C, cb21D, cb22A, cb22B, cb22C, cb22D, cb23A, cb23B, cb23C, cb23D, cb24A, cb24B, cb24C, cb24D,
            cb25A, cb25B, cb25C, cb25D, cb26A, cb26B, cb26C, cb26D, cb27A, cb27B, cb27C, cb27D, cb28A, cb28B, cb28C, cb28D,
            cb29A, cb29B, cb29C, cb29D, cb30A, cb30B, cb30C, cb30D, cb31A, cb31B, cb31C, cb31D, cb32A, cb32B, cb32C, cb32D,
            cb33A, cb33B, cb33C, cb33D, cb34A, cb34B, cb34C, cb34D, cb35A, cb35B, cb35C, cb35D, cb36A, cb36B, cb36C, cb36D,
            cb37A, cb37B, cb37C, cb37D, cb38A, cb38B, cb38C, cb38D, cb39A, cb39B, cb39C, cb39D, cb40A, cb40B, cb40C, cb40D;
    private JobListSafetyAuditResponse.Data jobListSafetyAuditDateResponse = new JobListSafetyAuditResponse.Data();
    private CreateSafetyAuditRequest createSafetyAuditRequest = new CreateSafetyAuditRequest();
    private SafetyAuditCheckDataRequest safetyAuditCheckDataRequest = new SafetyAuditCheckDataRequest();
    private Context context;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private Button btn_submit, clear_button, save_button;
    private SignaturePad signaturePad;
    private Bitmap signatureBitmap;
    private MultipartBody.Part signaturePart;
    private ProgressDialog progressDialog;
    private boolean checkDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_safety_audit_form);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        se_user_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        iv_back = findViewById(R.id.iv_back);

        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_engineer_name = findViewById(R.id.txt_engineer_name);
        txt_conducted_on = findViewById(R.id.txt_conducted_on);

        edt_comments_a = findViewById(R.id.edt_comments_a);
        edt_comments_b = findViewById(R.id.edt_comments_b);
        edt_comments_c = findViewById(R.id.edt_comments_c);
        edt_comments_d = findViewById(R.id.edt_comments_d);
        edt_general = findViewById(R.id.edt_general);

        btn_submit = findViewById(R.id.btn_submit);
        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);

        signaturePad = findViewById(R.id.signaturePad);

        cb1A = findViewById(R.id.cb1A);
        cb1B = findViewById(R.id.cb1B);
        cb1C = findViewById(R.id.cb1C);
        cb1D = findViewById(R.id.cb1D);
        cb2A = findViewById(R.id.cb2A);
        cb2B = findViewById(R.id.cb2B);
        cb2C = findViewById(R.id.cb2C);
        cb2D = findViewById(R.id.cb2D);
        cb3A = findViewById(R.id.cb3A);
        cb3B = findViewById(R.id.cb3B);
        cb3C = findViewById(R.id.cb3C);
        cb3D = findViewById(R.id.cb3D);
        cb4A = findViewById(R.id.cb4A);
        cb4B = findViewById(R.id.cb4B);
        cb4C = findViewById(R.id.cb4C);
        cb4D = findViewById(R.id.cb4D);
        cb5A = findViewById(R.id.cb5A);
        cb5B = findViewById(R.id.cb5B);
        cb5C = findViewById(R.id.cb5C);
        cb5D = findViewById(R.id.cb5D);
        cb6A = findViewById(R.id.cb6A);
        cb6B = findViewById(R.id.cb6B);
        cb6C = findViewById(R.id.cb6C);
        cb6D = findViewById(R.id.cb6D);
        cb7A = findViewById(R.id.cb7A);
        cb7B = findViewById(R.id.cb7B);
        cb7C = findViewById(R.id.cb7C);
        cb7D = findViewById(R.id.cb7D);
        cb8A = findViewById(R.id.cb8A);
        cb8B = findViewById(R.id.cb8B);
        cb8C = findViewById(R.id.cb8C);
        cb8D = findViewById(R.id.cb8D);
        cb9A = findViewById(R.id.cb9A);
        cb9B = findViewById(R.id.cb9B);
        cb9C = findViewById(R.id.cb9C);
        cb9D = findViewById(R.id.cb9D);
        cb10A = findViewById(R.id.cb10A);
        cb10B = findViewById(R.id.cb10B);
        cb10C = findViewById(R.id.cb10C);
        cb10D = findViewById(R.id.cb10D);
        cb11A = findViewById(R.id.cb11A);
        cb11B = findViewById(R.id.cb11B);
        cb11C = findViewById(R.id.cb11C);
        cb11D = findViewById(R.id.cb11D);
        cb12A = findViewById(R.id.cb12A);
        cb12B = findViewById(R.id.cb12B);
        cb12C = findViewById(R.id.cb12C);
        cb12D = findViewById(R.id.cb12D);
        cb13A = findViewById(R.id.cb13A);
        cb13B = findViewById(R.id.cb13B);
        cb13C = findViewById(R.id.cb13C);
        cb13D = findViewById(R.id.cb13D);
        cb14A = findViewById(R.id.cb14A);
        cb14B = findViewById(R.id.cb14B);
        cb14C = findViewById(R.id.cb14C);
        cb14D = findViewById(R.id.cb14D);
        cb15A = findViewById(R.id.cb15A);
        cb15B = findViewById(R.id.cb15B);
        cb15C = findViewById(R.id.cb15C);
        cb15D = findViewById(R.id.cb15D);
        cb16A = findViewById(R.id.cb16A);
        cb16B = findViewById(R.id.cb16B);
        cb16C = findViewById(R.id.cb16C);
        cb16D = findViewById(R.id.cb16D);
        cb17A = findViewById(R.id.cb17A);
        cb17B = findViewById(R.id.cb17B);
        cb17C = findViewById(R.id.cb17C);
        cb17D = findViewById(R.id.cb17D);
        cb18A = findViewById(R.id.cb18A);
        cb18B = findViewById(R.id.cb18B);
        cb18C = findViewById(R.id.cb18C);
        cb18D = findViewById(R.id.cb18D);
        cb19A = findViewById(R.id.cb19A);
        cb19B = findViewById(R.id.cb19B);
        cb19C = findViewById(R.id.cb19C);
        cb19D = findViewById(R.id.cb19D);
        cb20A = findViewById(R.id.cb20A);
        cb20B = findViewById(R.id.cb20B);
        cb20C = findViewById(R.id.cb20C);
        cb20D = findViewById(R.id.cb20D);
        cb21A = findViewById(R.id.cb21A);
        cb21B = findViewById(R.id.cb21B);
        cb21C = findViewById(R.id.cb21C);
        cb21D = findViewById(R.id.cb21D);
        cb22A = findViewById(R.id.cb22A);
        cb22B = findViewById(R.id.cb22B);
        cb22C = findViewById(R.id.cb22C);
        cb22D = findViewById(R.id.cb22D);
        cb23A = findViewById(R.id.cb23A);
        cb23B = findViewById(R.id.cb23B);
        cb23C = findViewById(R.id.cb23C);
        cb23D = findViewById(R.id.cb23D);
        cb24A = findViewById(R.id.cb24A);
        cb24B = findViewById(R.id.cb24B);
        cb24C = findViewById(R.id.cb24C);
        cb24D = findViewById(R.id.cb24D);
        cb25A = findViewById(R.id.cb25A);
        cb25B = findViewById(R.id.cb25B);
        cb25C = findViewById(R.id.cb25C);
        cb25D = findViewById(R.id.cb25D);
        cb26A = findViewById(R.id.cb26A);
        cb26B = findViewById(R.id.cb26B);
        cb26C = findViewById(R.id.cb26C);
        cb26D = findViewById(R.id.cb26D);
        cb27A = findViewById(R.id.cb27A);
        cb27B = findViewById(R.id.cb27B);
        cb27C = findViewById(R.id.cb27C);
        cb27D = findViewById(R.id.cb27D);
        cb28A = findViewById(R.id.cb28A);
        cb28B = findViewById(R.id.cb28B);
        cb28C = findViewById(R.id.cb28C);
        cb28D = findViewById(R.id.cb28D);
        cb29A = findViewById(R.id.cb29A);
        cb29B = findViewById(R.id.cb29B);
        cb29C = findViewById(R.id.cb29C);
        cb29D = findViewById(R.id.cb29D);
        cb30A = findViewById(R.id.cb30A);
        cb30B = findViewById(R.id.cb30B);
        cb30C = findViewById(R.id.cb30C);
        cb30D = findViewById(R.id.cb30D);
        cb31A = findViewById(R.id.cb31A);
        cb31B = findViewById(R.id.cb31B);
        cb31C = findViewById(R.id.cb31C);
        cb31D = findViewById(R.id.cb31D);
        cb32A = findViewById(R.id.cb32A);
        cb32B = findViewById(R.id.cb32B);
        cb32C = findViewById(R.id.cb32C);
        cb32D = findViewById(R.id.cb32D);
        cb33A = findViewById(R.id.cb33A);
        cb33B = findViewById(R.id.cb33B);
        cb33C = findViewById(R.id.cb33C);
        cb33D = findViewById(R.id.cb33D);
        cb34A = findViewById(R.id.cb34A);
        cb34B = findViewById(R.id.cb34B);
        cb34C = findViewById(R.id.cb34C);
        cb34D = findViewById(R.id.cb34D);
        cb35A = findViewById(R.id.cb35A);
        cb35B = findViewById(R.id.cb35B);
        cb35C = findViewById(R.id.cb35C);
        cb35D = findViewById(R.id.cb35D);
        cb36A = findViewById(R.id.cb36A);
        cb36B = findViewById(R.id.cb36B);
        cb36C = findViewById(R.id.cb36C);
        cb36D = findViewById(R.id.cb36D);
        cb37A = findViewById(R.id.cb37A);
        cb37B = findViewById(R.id.cb37B);
        cb37C = findViewById(R.id.cb37C);
        cb37D = findViewById(R.id.cb37D);
        cb38A = findViewById(R.id.cb38A);
        cb38B = findViewById(R.id.cb38B);
        cb38C = findViewById(R.id.cb38C);
        cb38D = findViewById(R.id.cb38D);
        cb39A = findViewById(R.id.cb39A);
        cb39B = findViewById(R.id.cb39B);
        cb39C = findViewById(R.id.cb39C);
        cb39D = findViewById(R.id.cb39D);
        cb40A = findViewById(R.id.cb40A);
        cb40B = findViewById(R.id.cb40B);
        cb40C = findViewById(R.id.cb40C);
        cb40D = findViewById(R.id.cb40D);

        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        txt_conducted_on.setOnClickListener(this);
        save_button.setOnClickListener(this);
        clear_button.setOnClickListener(this);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            public void onSigned() {
                save_button.setEnabled(true);
                clear_button.setEnabled(true);
            }

            public void onClear() {
                save_button.setEnabled(false);
                clear_button.setEnabled(false);
            }
        });

        cb1A.setOnCheckedChangeListener(this);
        cb1B.setOnCheckedChangeListener(this);
        cb1C.setOnCheckedChangeListener(this);
        cb1D.setOnCheckedChangeListener(this);
        cb2A.setOnCheckedChangeListener(this);
        cb2B.setOnCheckedChangeListener(this);
        cb2C.setOnCheckedChangeListener(this);
        cb2D.setOnCheckedChangeListener(this);
        cb3A.setOnCheckedChangeListener(this);
        cb3B.setOnCheckedChangeListener(this);
        cb3C.setOnCheckedChangeListener(this);
        cb3D.setOnCheckedChangeListener(this);
        cb4A.setOnCheckedChangeListener(this);
        cb4B.setOnCheckedChangeListener(this);
        cb4C.setOnCheckedChangeListener(this);
        cb4D.setOnCheckedChangeListener(this);
        cb5A.setOnCheckedChangeListener(this);
        cb5B.setOnCheckedChangeListener(this);
        cb5C.setOnCheckedChangeListener(this);
        cb5D.setOnCheckedChangeListener(this);
        cb6A.setOnCheckedChangeListener(this);
        cb6B.setOnCheckedChangeListener(this);
        cb6C.setOnCheckedChangeListener(this);
        cb6D.setOnCheckedChangeListener(this);
        cb7A.setOnCheckedChangeListener(this);
        cb7B.setOnCheckedChangeListener(this);
        cb7C.setOnCheckedChangeListener(this);
        cb7D.setOnCheckedChangeListener(this);
        cb8A.setOnCheckedChangeListener(this);
        cb8B.setOnCheckedChangeListener(this);
        cb8C.setOnCheckedChangeListener(this);
        cb8D.setOnCheckedChangeListener(this);
        cb9A.setOnCheckedChangeListener(this);
        cb9B.setOnCheckedChangeListener(this);
        cb9C.setOnCheckedChangeListener(this);
        cb9D.setOnCheckedChangeListener(this);
        cb10A.setOnCheckedChangeListener(this);
        cb10B.setOnCheckedChangeListener(this);
        cb10C.setOnCheckedChangeListener(this);
        cb10D.setOnCheckedChangeListener(this);
        cb11A.setOnCheckedChangeListener(this);
        cb11B.setOnCheckedChangeListener(this);
        cb11C.setOnCheckedChangeListener(this);
        cb11D.setOnCheckedChangeListener(this);
        cb12A.setOnCheckedChangeListener(this);
        cb12B.setOnCheckedChangeListener(this);
        cb12C.setOnCheckedChangeListener(this);
        cb12D.setOnCheckedChangeListener(this);
        cb13A.setOnCheckedChangeListener(this);
        cb13B.setOnCheckedChangeListener(this);
        cb13C.setOnCheckedChangeListener(this);
        cb13D.setOnCheckedChangeListener(this);
        cb14A.setOnCheckedChangeListener(this);
        cb14B.setOnCheckedChangeListener(this);
        cb14C.setOnCheckedChangeListener(this);
        cb14D.setOnCheckedChangeListener(this);
        cb15A.setOnCheckedChangeListener(this);
        cb15B.setOnCheckedChangeListener(this);
        cb15C.setOnCheckedChangeListener(this);
        cb15D.setOnCheckedChangeListener(this);
        cb16A.setOnCheckedChangeListener(this);
        cb16B.setOnCheckedChangeListener(this);
        cb16C.setOnCheckedChangeListener(this);
        cb16D.setOnCheckedChangeListener(this);
        cb17A.setOnCheckedChangeListener(this);
        cb17B.setOnCheckedChangeListener(this);
        cb17C.setOnCheckedChangeListener(this);
        cb17D.setOnCheckedChangeListener(this);
        cb18A.setOnCheckedChangeListener(this);
        cb18B.setOnCheckedChangeListener(this);
        cb18C.setOnCheckedChangeListener(this);
        cb18D.setOnCheckedChangeListener(this);
        cb19A.setOnCheckedChangeListener(this);
        cb19B.setOnCheckedChangeListener(this);
        cb19C.setOnCheckedChangeListener(this);
        cb19D.setOnCheckedChangeListener(this);
        cb20A.setOnCheckedChangeListener(this);
        cb20B.setOnCheckedChangeListener(this);
        cb20C.setOnCheckedChangeListener(this);
        cb20D.setOnCheckedChangeListener(this);
        cb21A.setOnCheckedChangeListener(this);
        cb21B.setOnCheckedChangeListener(this);
        cb21C.setOnCheckedChangeListener(this);
        cb21D.setOnCheckedChangeListener(this);
        cb22A.setOnCheckedChangeListener(this);
        cb22B.setOnCheckedChangeListener(this);
        cb22C.setOnCheckedChangeListener(this);
        cb22D.setOnCheckedChangeListener(this);
        cb23A.setOnCheckedChangeListener(this);
        cb23B.setOnCheckedChangeListener(this);
        cb23C.setOnCheckedChangeListener(this);
        cb23D.setOnCheckedChangeListener(this);
        cb24A.setOnCheckedChangeListener(this);
        cb24B.setOnCheckedChangeListener(this);
        cb24C.setOnCheckedChangeListener(this);
        cb24D.setOnCheckedChangeListener(this);
        cb25A.setOnCheckedChangeListener(this);
        cb25B.setOnCheckedChangeListener(this);
        cb25C.setOnCheckedChangeListener(this);
        cb25D.setOnCheckedChangeListener(this);
        cb26A.setOnCheckedChangeListener(this);
        cb26B.setOnCheckedChangeListener(this);
        cb26C.setOnCheckedChangeListener(this);
        cb26D.setOnCheckedChangeListener(this);
        cb27A.setOnCheckedChangeListener(this);
        cb27B.setOnCheckedChangeListener(this);
        cb27C.setOnCheckedChangeListener(this);
        cb27D.setOnCheckedChangeListener(this);
        cb28A.setOnCheckedChangeListener(this);
        cb28B.setOnCheckedChangeListener(this);
        cb28C.setOnCheckedChangeListener(this);
        cb28D.setOnCheckedChangeListener(this);
        cb29A.setOnCheckedChangeListener(this);
        cb29B.setOnCheckedChangeListener(this);
        cb29C.setOnCheckedChangeListener(this);
        cb29D.setOnCheckedChangeListener(this);
        cb30A.setOnCheckedChangeListener(this);
        cb30B.setOnCheckedChangeListener(this);
        cb30C.setOnCheckedChangeListener(this);
        cb30D.setOnCheckedChangeListener(this);
        cb31A.setOnCheckedChangeListener(this);
        cb31B.setOnCheckedChangeListener(this);
        cb31C.setOnCheckedChangeListener(this);
        cb31D.setOnCheckedChangeListener(this);
        cb32A.setOnCheckedChangeListener(this);
        cb32B.setOnCheckedChangeListener(this);
        cb32C.setOnCheckedChangeListener(this);
        cb32D.setOnCheckedChangeListener(this);
        cb33A.setOnCheckedChangeListener(this);
        cb33B.setOnCheckedChangeListener(this);
        cb33C.setOnCheckedChangeListener(this);
        cb33D.setOnCheckedChangeListener(this);
        cb34A.setOnCheckedChangeListener(this);
        cb34B.setOnCheckedChangeListener(this);
        cb34C.setOnCheckedChangeListener(this);
        cb34D.setOnCheckedChangeListener(this);
        cb35A.setOnCheckedChangeListener(this);
        cb35B.setOnCheckedChangeListener(this);
        cb35C.setOnCheckedChangeListener(this);
        cb35D.setOnCheckedChangeListener(this);
        cb36A.setOnCheckedChangeListener(this);
        cb36B.setOnCheckedChangeListener(this);
        cb36C.setOnCheckedChangeListener(this);
        cb36D.setOnCheckedChangeListener(this);
        cb37A.setOnCheckedChangeListener(this);
        cb37B.setOnCheckedChangeListener(this);
        cb37C.setOnCheckedChangeListener(this);
        cb37D.setOnCheckedChangeListener(this);
        cb38A.setOnCheckedChangeListener(this);
        cb38B.setOnCheckedChangeListener(this);
        cb38C.setOnCheckedChangeListener(this);
        cb38D.setOnCheckedChangeListener(this);
        cb39A.setOnCheckedChangeListener(this);
        cb39B.setOnCheckedChangeListener(this);
        cb39C.setOnCheckedChangeListener(this);
        cb39D.setOnCheckedChangeListener(this);
        cb40A.setOnCheckedChangeListener(this);
        cb40B.setOnCheckedChangeListener(this);
        cb40C.setOnCheckedChangeListener(this);
        cb40D.setOnCheckedChangeListener(this);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("jobListSafetyAuditDateResponse")) {
                jobListSafetyAuditDateResponse = extra.getParcelable("jobListSafetyAuditDateResponse");
            }
        }

        Log.i(TAG, "onCreate: jobListSafetyAuditDateResponse -> " + new Gson().toJson(jobListSafetyAuditDateResponse));

        createSafetyAuditRequest.setSubmitted_by_num(se_user_mobile_no);
        createSafetyAuditRequest.setSubmitted_by_name(se_user_name);
        createSafetyAuditRequest.setSubmitted_by_emp_code(se_user_id);
        createSafetyAuditRequest.setJob_id(jobListSafetyAuditDateResponse.getJOBNO());
        createSafetyAuditRequest.setBrcode(jobListSafetyAuditDateResponse.getBRCODE());
        createSafetyAuditRequest.setSite_name(jobListSafetyAuditDateResponse.getCUST_NAME());

        safetyAuditCheckDataRequest.setJob_id(jobListSafetyAuditDateResponse.getJOBNO());
        safetyAuditCheckDataRequest.setSubmitted_by_num(se_user_mobile_no);

        txt_jobid.setText(jobListSafetyAuditDateResponse.getJOBNO());
        txt_building_name.setText(jobListSafetyAuditDateResponse.getCUST_NAME());
        txt_engineer_name.setText(se_user_name);

        initLoadingDialog();
        getTodayDate();
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
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

        /*if (strDateType.equalsIgnoreCase("txt_conducted_on")) {
            txt_conducted_on.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_conducted_on.setText(dateTime);
        }*/
        txt_conducted_on.setText(dateTime);

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
            if (strDateType.equalsIgnoreCase("txt_conducted_on")) {
                createSafetyAuditRequest.setConducted_on(formattedDate);
                safetyAuditCheckDataRequest.setReport_date(formattedDate);

                getSafetyAuditCheckDate();
            } else if (strDateType.equalsIgnoreCase("txt_both")) {
                createSafetyAuditRequest.setSubmitted_by_on(formattedDate);
                createSafetyAuditRequest.setConducted_on(formattedDate);
                safetyAuditCheckDataRequest.setReport_date(formattedDate);

                getSafetyAuditCheckDate();
            }

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
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

    private void uploadDigitalSignatureImageRequest(File file) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait Image Upload ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);
        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(signaturePart);

        Log.i(TAG, "uploadDigitalSignatureImageRequest: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                Log.i(TAG, "uploadDigitalSignatureImageRequest: onResponse: FileUploadResponse -> " + new Gson().toJson(response.body()));
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (200 == response.body().getCode()) {
                        uploadImagePath = response.body().getData();
                        createSafetyAuditRequest.setEngineer_signature(uploadImagePath);
                        if (uploadImagePath != null) {
                            signaturePad.setEnabled(false);
                            save_button.setEnabled(false);
                            clear_button.setEnabled(false);
                            Toast.makeText(context, "Signature Saved", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ErrorMsgDialog("Please Try Again Later");
                    }
                } else {
                    ErrorMsgDialog("Something Went Wrong.. Try Again Later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "uploadDigitalSignatureImageRequest: onFailure: error -> " + t.getMessage());
                ErrorMsgDialog("Something Went Wrong.. Try Again Later");
                progressDialog.dismiss();
            }
        });
    }

    private void getSafetyAuditCheckDate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Log.i(TAG, "getSafetyAuditCheckDate: safetyAuditCheckDataRequest -> " + new Gson().toJson(safetyAuditCheckDataRequest));
        Call<SuccessResponse> call = apiInterface.getSafetyAuditCheckDate(getContentType(), safetyAuditCheckDataRequest);
        Log.i(TAG, "getSafetyAuditCheckDate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getSafetyAuditCheckDate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (response.body().getCode() == 200) {
                        checkDate = false;
                        ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
                    } else {
                        checkDate = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getSafetyAuditCheckDate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void validateCreateSafetyAuditRequest() {
        Log.i(TAG, "validateCreateSafetyAuditRequest: createSafetyAuditRequest (1) -> " + new Gson().toJson(createSafetyAuditRequest));

        createSafetyAuditRequest.setObservation_a(nullPointer(edt_comments_a.getText().toString().trim()));
        createSafetyAuditRequest.setObservation_b(nullPointer(edt_comments_b.getText().toString().trim()));
        createSafetyAuditRequest.setObservation_c(nullPointer(edt_comments_c.getText().toString().trim()));
        createSafetyAuditRequest.setObservation_d(nullPointer(edt_comments_d.getText().toString().trim()));
        createSafetyAuditRequest.setGeneral_comment(nullPointer(edt_general.getText().toString().trim()));

        /*if (!checkDate) {
            ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
        } else*/
        if (!nullPointerValidator(createSafetyAuditRequest.getSA01())) {
            ErrorMsgDialog("Please Select 1. Emergency and Safety equipment are functional.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA02())) {
            ErrorMsgDialog("Please Select 2. Capacity plate and safety instructions provided and clearly visible.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA03())) {
            ErrorMsgDialog("Please Select 3. All applicable service signage, safety stickers / warning signs are fixed.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA04())) {
            ErrorMsgDialog("Please Select 4. Access ladder to Machine room is safe and hand rail are provided.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA05())) {
            ErrorMsgDialog("Please Select 5. Machine room lighting are adequate and safe.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA06())) {
            ErrorMsgDialog("Please Select 6. All the Electrical Panels are covered, wired neatly and  Safe.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA07())) {
            ErrorMsgDialog("Please Select 7. Earthing terminal correctly fixed and grounding  available.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA08())) {
            ErrorMsgDialog("Please Select 8. Machine room is free from Trip or fall hazards and lockable Condition.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA09())) {
            ErrorMsgDialog("Please Select 9. Machine room is clean and ventilators provided.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA10())) {
            ErrorMsgDialog("Please Select 10. Machine room openings are covered and secured from falling objects.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA11())) {
            ErrorMsgDialog("Please Select 11. Controller and ARD are covered and locked.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA12())) {
            ErrorMsgDialog("Please Select 12. All applicable guards are in place and warning signs fixed.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA13())) {
            ErrorMsgDialog("Please Select 13. Brake condition and operation are safe and satisfactory.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA14())) {
            ErrorMsgDialog("Please Select 14. Manual Rescue procedure is displayed and accessories are available and functional condition.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA15())) {
            ErrorMsgDialog("Please Select 15. ARD is in working Condition. (If available)");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA16())) {
            ErrorMsgDialog("Please Select 16. Thimble rods / Eye bolts are secured with lock nuts and split pins.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA17())) {
            ErrorMsgDialog("Please Select 17. Shaft lighting up to pit are Adequate and safe, having good illumination.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA18())) {
            ErrorMsgDialog("Please Select 18. Low overhead identified, marked / protected.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA19())) {
            ErrorMsgDialog("Please Select 19. Landing door Mechanical locking and Unlocking mechanisms are in Working Condition at ALL levels.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA20())) {
            ErrorMsgDialog("Please Select 20. Main rope and Governor rope are in good condition ( No strand cut ) - Visual check.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA21())) {
            ErrorMsgDialog("Please Select 21. Filler weight anti jump, CWT arrester properly fixed.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA22())) {
            ErrorMsgDialog("Please Select 22. All the safety circuits are in function without bypass. Such as landing door, Car door, final limit, pit switches, safety gear switches, tension pulley switches etc.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA23())) {
            ErrorMsgDialog("Please Select 23. Car top light & fan is safe, protected and working.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA24())) {
            ErrorMsgDialog("Please Select 24. Car top barricades are in place and firmly fitted.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA25())) {
            ErrorMsgDialog("Please Select 25. Car top and Cwt Top - Applicable guards are in place.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA26())) {
            ErrorMsgDialog("Please Select 26. Car top earth wires are connected.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA27())) {
            ErrorMsgDialog("Please Select 27. All car top switches and inspection buttons are in good condition and working in order.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA28())) {
            ErrorMsgDialog("Please Select 28. Car Top is neat and Clean and meets '5 S'");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA29())) {
            ErrorMsgDialog("Please Select 29. Traveling Cables are fitted properly and Secured Safely.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA30())) {
            ErrorMsgDialog("Please Select 30. CWT screen correctly set to cover buffer and buffer clearance is acceptable.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA31())) {
            ErrorMsgDialog("Please Select 31. Pit switches are in place and functional - Verify.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA32())) {
            ErrorMsgDialog("Please Select 32. Pit ladder fitted correctly in it’s location and safe.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA33())) {
            ErrorMsgDialog("Please Select 33. Compensation Chains are fitted properly and Secured Safely.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA34())) {
            ErrorMsgDialog("Please Select 34. Pit is Clean and Clear from materials.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA35())) {
            ErrorMsgDialog("Please Select 35. All applicable tools are available and in good condition.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA36())) {
            ErrorMsgDialog("Please Select 36. Electrical safety practice (Switching ON and OFF Method) is followed properly.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA37())) {
            ErrorMsgDialog("Please Select 37. While working at Car top and machine room, mobile not in use.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA38())) {
            ErrorMsgDialog("Please Select 38. Door stopper and hand lamp available.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA39())) {
            ErrorMsgDialog("Please Select 39. Car Top Entry and Exit procedures are followed properly and verified.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSA40())) {
            ErrorMsgDialog("Please Select 40. Pit Entry and Exit procedures are followed properly and verified.");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getJob_id())) {
            ErrorMsgDialog("Please Select Job ID");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Select Submitted By Number");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSubmitted_by_name())) {
            ErrorMsgDialog("Please Select Submitted By Name");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSubmitted_by_emp_code())) {
            ErrorMsgDialog("Please Select Submitted By Emp Code");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Select Submitted By On");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getObservation_a())) {
            ErrorMsgDialog("Please Enter Observation A");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getObservation_b())) {
            ErrorMsgDialog("Please Enter Observation B");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getObservation_c())) {
            ErrorMsgDialog("Please Enter Observation C");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getObservation_d())) {
            ErrorMsgDialog("Please Enter Observation D");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getGeneral_comment())) {
            ErrorMsgDialog("Please Enter General Comments");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getEngineer_name())) {
            ErrorMsgDialog("Please Select Engineer Name");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getEngineer_signature())) {
            ErrorMsgDialog("Please Select Engineer Signature");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getConducted_on())) {
            ErrorMsgDialog("Please Select Conducted On");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getBrcode())) {
            ErrorMsgDialog("Please Select Branch Code");
        } else if (!nullPointerValidator(createSafetyAuditRequest.getSite_name())) {
            ErrorMsgDialog("Please Select Site Name");
        } else {
            Log.i(TAG, "validateCreateSafetyAuditRequest: createSafetyAuditRequest (2) -> " + new Gson().toJson(createSafetyAuditRequest));
            getSafetyAuditCreate();
        }
    }

    private void getSafetyAuditCreate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getSafetyAuditCreate(getContentType(), createSafetyAuditRequest);
        Log.i(TAG, "getSafetyAuditCreate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getSafetyAuditCreate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
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
                Log.e(TAG, "getSafetyAuditCreate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: {
                onBackPressed();
            }
            break;
            case R.id.txt_conducted_on: {
                strDateType = "txt_conducted_on";
                callDatePicker();
            }
            break;
            case R.id.save_button: {
                signatureBitmap = signaturePad.getSignatureBitmap();
                Log.i(TAG, "onClick: signatureBitmap" + signatureBitmap);
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
                    Log.e(TAG, "onClick: save_button: error -> " + e.getMessage());
                }

                signaturePart = MultipartBody.Part.createFormData("sampleFile", se_user_id + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

                Log.i(TAG, "onClick: save_button: Network -> " + networkStatus);

                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                    NoInternetDialog();
                } else {
                    uploadDigitalSignatureImageRequest(file);
                }
            }
            break;
            case R.id.clear_button: {
                signaturePad.clear();
            }
            break;
            case R.id.btn_submit: {
                validateCreateSafetyAuditRequest();
            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            switch (compoundButton.getId()) {
                case R.id.cb1A: {
                    cb1B.setChecked(false);
                    cb1C.setChecked(false);
                    cb1D.setChecked(false);
                    createSafetyAuditRequest.setSA01("A");
                }
                break;
                case R.id.cb1B: {
                    cb1A.setChecked(false);
                    cb1C.setChecked(false);
                    cb1D.setChecked(false);
                    createSafetyAuditRequest.setSA01("B");
                }
                break;
                case R.id.cb1C: {
                    cb1A.setChecked(false);
                    cb1B.setChecked(false);
                    cb1D.setChecked(false);
                    createSafetyAuditRequest.setSA01("C");
                }
                break;
                case R.id.cb1D: {
                    cb1A.setChecked(false);
                    cb1B.setChecked(false);
                    cb1C.setChecked(false);
                    createSafetyAuditRequest.setSA01("D");
                }
                break;
                case R.id.cb2A: {
                    cb2B.setChecked(false);
                    cb2C.setChecked(false);
                    cb2D.setChecked(false);
                    createSafetyAuditRequest.setSA02("A");
                }
                break;
                case R.id.cb2B: {
                    cb2A.setChecked(false);
                    cb2C.setChecked(false);
                    cb2D.setChecked(false);
                    createSafetyAuditRequest.setSA02("B");
                }
                break;
                case R.id.cb2C: {
                    cb2A.setChecked(false);
                    cb2B.setChecked(false);
                    cb2D.setChecked(false);
                    createSafetyAuditRequest.setSA02("C");
                }
                break;
                case R.id.cb2D: {
                    cb2A.setChecked(false);
                    cb2B.setChecked(false);
                    cb2C.setChecked(false);
                    createSafetyAuditRequest.setSA02("D");
                }
                break;
                case R.id.cb3A: {
                    cb3B.setChecked(false);
                    cb3C.setChecked(false);
                    cb3D.setChecked(false);
                    createSafetyAuditRequest.setSA03("A");
                }
                break;
                case R.id.cb3B: {
                    cb3A.setChecked(false);
                    cb3C.setChecked(false);
                    cb3D.setChecked(false);
                    createSafetyAuditRequest.setSA03("B");
                }
                break;
                case R.id.cb3C: {
                    cb3A.setChecked(false);
                    cb3B.setChecked(false);
                    cb3D.setChecked(false);
                    createSafetyAuditRequest.setSA03("C");
                }
                break;
                case R.id.cb3D: {
                    cb3A.setChecked(false);
                    cb3B.setChecked(false);
                    cb3C.setChecked(false);
                    createSafetyAuditRequest.setSA03("D");
                }
                break;
                case R.id.cb4A: {
                    cb4B.setChecked(false);
                    cb4C.setChecked(false);
                    cb4D.setChecked(false);
                    createSafetyAuditRequest.setSA04("A");
                }
                break;
                case R.id.cb4B: {
                    cb4A.setChecked(false);
                    cb4C.setChecked(false);
                    cb4D.setChecked(false);
                    createSafetyAuditRequest.setSA04("B");
                }
                break;
                case R.id.cb4C: {
                    cb4A.setChecked(false);
                    cb4B.setChecked(false);
                    cb4D.setChecked(false);
                    createSafetyAuditRequest.setSA04("C");
                }
                break;
                case R.id.cb4D: {
                    cb4A.setChecked(false);
                    cb4B.setChecked(false);
                    cb4C.setChecked(false);
                    createSafetyAuditRequest.setSA04("D");
                }
                break;
                case R.id.cb5A: {
                    cb5B.setChecked(false);
                    cb5C.setChecked(false);
                    cb5D.setChecked(false);
                    createSafetyAuditRequest.setSA05("A");
                }
                break;
                case R.id.cb5B: {
                    cb5A.setChecked(false);
                    cb5C.setChecked(false);
                    cb5D.setChecked(false);
                    createSafetyAuditRequest.setSA05("B");
                }
                break;
                case R.id.cb5C: {
                    cb5A.setChecked(false);
                    cb5B.setChecked(false);
                    cb5D.setChecked(false);
                    createSafetyAuditRequest.setSA05("C");
                }
                break;
                case R.id.cb5D: {
                    cb5A.setChecked(false);
                    cb5B.setChecked(false);
                    cb5C.setChecked(false);
                    createSafetyAuditRequest.setSA05("D");
                }
                break;
                case R.id.cb6A: {
                    cb6B.setChecked(false);
                    cb6C.setChecked(false);
                    cb6D.setChecked(false);
                    createSafetyAuditRequest.setSA06("A");
                }
                break;
                case R.id.cb6B: {
                    cb6A.setChecked(false);
                    cb6C.setChecked(false);
                    cb6D.setChecked(false);
                    createSafetyAuditRequest.setSA06("B");
                }
                break;
                case R.id.cb6C: {
                    cb6A.setChecked(false);
                    cb6B.setChecked(false);
                    cb6D.setChecked(false);
                    createSafetyAuditRequest.setSA06("C");
                }
                break;
                case R.id.cb6D: {
                    cb6A.setChecked(false);
                    cb6B.setChecked(false);
                    cb6C.setChecked(false);
                    createSafetyAuditRequest.setSA06("D");
                }
                break;
                case R.id.cb7A: {
                    cb7B.setChecked(false);
                    cb7C.setChecked(false);
                    cb7D.setChecked(false);
                    createSafetyAuditRequest.setSA07("A");
                }
                break;
                case R.id.cb7B: {
                    cb7A.setChecked(false);
                    cb7C.setChecked(false);
                    cb7D.setChecked(false);
                    createSafetyAuditRequest.setSA07("B");
                }
                break;
                case R.id.cb7C: {
                    cb7A.setChecked(false);
                    cb7B.setChecked(false);
                    cb7D.setChecked(false);
                    createSafetyAuditRequest.setSA07("C");
                }
                break;
                case R.id.cb7D: {
                    cb7A.setChecked(false);
                    cb7B.setChecked(false);
                    cb7C.setChecked(false);
                    createSafetyAuditRequest.setSA07("D");
                }
                break;
                case R.id.cb8A: {
                    cb8B.setChecked(false);
                    cb8C.setChecked(false);
                    cb8D.setChecked(false);
                    createSafetyAuditRequest.setSA08("A");
                }
                break;
                case R.id.cb8B: {
                    cb8A.setChecked(false);
                    cb8C.setChecked(false);
                    cb8D.setChecked(false);
                    createSafetyAuditRequest.setSA08("B");
                }
                break;
                case R.id.cb8C: {
                    cb8A.setChecked(false);
                    cb8B.setChecked(false);
                    cb8D.setChecked(false);
                    createSafetyAuditRequest.setSA08("C");
                }
                break;
                case R.id.cb8D: {
                    cb8A.setChecked(false);
                    cb8B.setChecked(false);
                    cb8C.setChecked(false);
                    createSafetyAuditRequest.setSA08("D");
                }
                break;
                case R.id.cb9A: {
                    cb9B.setChecked(false);
                    cb9C.setChecked(false);
                    cb9D.setChecked(false);
                    createSafetyAuditRequest.setSA09("A");
                }
                break;
                case R.id.cb9B: {
                    cb9A.setChecked(false);
                    cb9C.setChecked(false);
                    cb9D.setChecked(false);
                    createSafetyAuditRequest.setSA09("B");
                }
                break;
                case R.id.cb9C: {
                    cb9A.setChecked(false);
                    cb9B.setChecked(false);
                    cb9D.setChecked(false);
                    createSafetyAuditRequest.setSA09("C");
                }
                break;
                case R.id.cb9D: {
                    cb9A.setChecked(false);
                    cb9B.setChecked(false);
                    cb9C.setChecked(false);
                    createSafetyAuditRequest.setSA09("D");
                }
                break;
                case R.id.cb10A: {
                    cb10B.setChecked(false);
                    cb10C.setChecked(false);
                    cb10D.setChecked(false);
                    createSafetyAuditRequest.setSA10("A");
                }
                break;
                case R.id.cb10B: {
                    cb10A.setChecked(false);
                    cb10C.setChecked(false);
                    cb10D.setChecked(false);
                    createSafetyAuditRequest.setSA10("B");
                }
                break;
                case R.id.cb10C: {
                    cb10A.setChecked(false);
                    cb10B.setChecked(false);
                    cb10D.setChecked(false);
                    createSafetyAuditRequest.setSA10("C");
                }
                break;
                case R.id.cb10D: {
                    cb10A.setChecked(false);
                    cb10B.setChecked(false);
                    cb10C.setChecked(false);
                    createSafetyAuditRequest.setSA10("D");
                }
                break;
                case R.id.cb11A: {
                    cb11B.setChecked(false);
                    cb11C.setChecked(false);
                    cb11D.setChecked(false);
                    createSafetyAuditRequest.setSA11("A");
                }
                break;
                case R.id.cb11B: {
                    cb11A.setChecked(false);
                    cb11C.setChecked(false);
                    cb11D.setChecked(false);
                    createSafetyAuditRequest.setSA11("B");
                }
                break;
                case R.id.cb11C: {
                    cb11A.setChecked(false);
                    cb11B.setChecked(false);
                    cb11D.setChecked(false);
                    createSafetyAuditRequest.setSA11("C");
                }
                break;
                case R.id.cb11D: {
                    cb11A.setChecked(false);
                    cb11B.setChecked(false);
                    cb11C.setChecked(false);
                    createSafetyAuditRequest.setSA11("D");
                }
                break;
                case R.id.cb12A: {
                    cb12B.setChecked(false);
                    cb12C.setChecked(false);
                    cb12D.setChecked(false);
                    createSafetyAuditRequest.setSA12("A");
                }
                break;
                case R.id.cb12B: {
                    cb12A.setChecked(false);
                    cb12C.setChecked(false);
                    cb12D.setChecked(false);
                    createSafetyAuditRequest.setSA12("B");
                }
                break;
                case R.id.cb12C: {
                    cb12A.setChecked(false);
                    cb12B.setChecked(false);
                    cb12D.setChecked(false);
                    createSafetyAuditRequest.setSA12("C");
                }
                break;
                case R.id.cb12D: {
                    cb12A.setChecked(false);
                    cb12B.setChecked(false);
                    cb12C.setChecked(false);
                    createSafetyAuditRequest.setSA12("D");
                }
                break;
                case R.id.cb13A: {
                    cb13B.setChecked(false);
                    cb13C.setChecked(false);
                    cb13D.setChecked(false);
                    createSafetyAuditRequest.setSA13("A");
                }
                break;
                case R.id.cb13B: {
                    cb13A.setChecked(false);
                    cb13C.setChecked(false);
                    cb13D.setChecked(false);
                    createSafetyAuditRequest.setSA13("B");
                }
                break;
                case R.id.cb13C: {
                    cb13A.setChecked(false);
                    cb13B.setChecked(false);
                    cb13D.setChecked(false);
                    createSafetyAuditRequest.setSA13("C");
                }
                break;
                case R.id.cb13D: {
                    cb13A.setChecked(false);
                    cb13B.setChecked(false);
                    cb13C.setChecked(false);
                    createSafetyAuditRequest.setSA13("D");
                }
                break;
                case R.id.cb14A: {
                    cb14B.setChecked(false);
                    cb14C.setChecked(false);
                    cb14D.setChecked(false);
                    createSafetyAuditRequest.setSA14("A");
                }
                break;
                case R.id.cb14B: {
                    cb14A.setChecked(false);
                    cb14C.setChecked(false);
                    cb14D.setChecked(false);
                    createSafetyAuditRequest.setSA14("B");
                }
                break;
                case R.id.cb14C: {
                    cb14A.setChecked(false);
                    cb14B.setChecked(false);
                    cb14D.setChecked(false);
                    createSafetyAuditRequest.setSA14("C");
                }
                break;
                case R.id.cb14D: {
                    cb14A.setChecked(false);
                    cb14B.setChecked(false);
                    cb14C.setChecked(false);
                    createSafetyAuditRequest.setSA14("D");
                }
                break;
                case R.id.cb15A: {
                    cb15B.setChecked(false);
                    cb15C.setChecked(false);
                    cb15D.setChecked(false);
                    createSafetyAuditRequest.setSA15("A");
                }
                break;
                case R.id.cb15B: {
                    cb15A.setChecked(false);
                    cb15C.setChecked(false);
                    cb15D.setChecked(false);
                    createSafetyAuditRequest.setSA15("B");
                }
                break;
                case R.id.cb15C: {
                    cb15A.setChecked(false);
                    cb15B.setChecked(false);
                    cb15D.setChecked(false);
                    createSafetyAuditRequest.setSA15("C");
                }
                break;
                case R.id.cb15D: {
                    cb15A.setChecked(false);
                    cb15B.setChecked(false);
                    cb15C.setChecked(false);
                    createSafetyAuditRequest.setSA15("D");
                }
                break;
                case R.id.cb16A: {
                    cb16B.setChecked(false);
                    cb16C.setChecked(false);
                    cb16D.setChecked(false);
                    createSafetyAuditRequest.setSA16("A");
                }
                break;
                case R.id.cb16B: {
                    cb16A.setChecked(false);
                    cb16C.setChecked(false);
                    cb16D.setChecked(false);
                    createSafetyAuditRequest.setSA16("B");
                }
                break;
                case R.id.cb16C: {
                    cb16A.setChecked(false);
                    cb16B.setChecked(false);
                    cb16D.setChecked(false);
                    createSafetyAuditRequest.setSA16("C");
                }
                break;
                case R.id.cb16D: {
                    cb16A.setChecked(false);
                    cb16B.setChecked(false);
                    cb16C.setChecked(false);
                    createSafetyAuditRequest.setSA16("D");
                }
                break;
                case R.id.cb17A: {
                    cb17B.setChecked(false);
                    cb17C.setChecked(false);
                    cb17D.setChecked(false);
                    createSafetyAuditRequest.setSA17("A");
                }
                break;
                case R.id.cb17B: {
                    cb17A.setChecked(false);
                    cb17C.setChecked(false);
                    cb17D.setChecked(false);
                    createSafetyAuditRequest.setSA17("B");
                }
                break;
                case R.id.cb17C: {
                    cb17A.setChecked(false);
                    cb17B.setChecked(false);
                    cb17D.setChecked(false);
                    createSafetyAuditRequest.setSA17("C");
                }
                break;
                case R.id.cb17D: {
                    cb17A.setChecked(false);
                    cb17B.setChecked(false);
                    cb17C.setChecked(false);
                    createSafetyAuditRequest.setSA17("D");
                }
                break;
                case R.id.cb18A: {
                    cb18B.setChecked(false);
                    cb18C.setChecked(false);
                    cb18D.setChecked(false);
                    createSafetyAuditRequest.setSA18("A");
                }
                break;
                case R.id.cb18B: {
                    cb18A.setChecked(false);
                    cb18C.setChecked(false);
                    cb18D.setChecked(false);
                    createSafetyAuditRequest.setSA18("B");
                }
                break;
                case R.id.cb18C: {
                    cb18A.setChecked(false);
                    cb18B.setChecked(false);
                    cb18D.setChecked(false);
                    createSafetyAuditRequest.setSA18("C");
                }
                break;
                case R.id.cb18D: {
                    cb18A.setChecked(false);
                    cb18B.setChecked(false);
                    cb18C.setChecked(false);
                    createSafetyAuditRequest.setSA18("D");
                }
                break;
                case R.id.cb19A: {
                    cb19B.setChecked(false);
                    cb19C.setChecked(false);
                    cb19D.setChecked(false);
                    createSafetyAuditRequest.setSA19("A");
                }
                break;
                case R.id.cb19B: {
                    cb19A.setChecked(false);
                    cb19C.setChecked(false);
                    cb19D.setChecked(false);
                    createSafetyAuditRequest.setSA19("B");
                }
                break;
                case R.id.cb19C: {
                    cb19A.setChecked(false);
                    cb19B.setChecked(false);
                    cb19D.setChecked(false);
                    createSafetyAuditRequest.setSA19("C");
                }
                break;
                case R.id.cb19D: {
                    cb19A.setChecked(false);
                    cb19B.setChecked(false);
                    cb19C.setChecked(false);
                    createSafetyAuditRequest.setSA19("D");
                }
                break;
                case R.id.cb20A: {
                    cb20B.setChecked(false);
                    cb20C.setChecked(false);
                    cb20D.setChecked(false);
                    createSafetyAuditRequest.setSA20("A");
                }
                break;
                case R.id.cb20B: {
                    cb20A.setChecked(false);
                    cb20C.setChecked(false);
                    cb20D.setChecked(false);
                    createSafetyAuditRequest.setSA20("B");
                }
                break;
                case R.id.cb20C: {
                    cb20A.setChecked(false);
                    cb20B.setChecked(false);
                    cb20D.setChecked(false);
                    createSafetyAuditRequest.setSA20("C");
                }
                break;
                case R.id.cb20D: {
                    cb20A.setChecked(false);
                    cb20B.setChecked(false);
                    cb20C.setChecked(false);
                    createSafetyAuditRequest.setSA20("D");
                }
                break;
                case R.id.cb21A: {
                    cb21B.setChecked(false);
                    cb21C.setChecked(false);
                    cb21D.setChecked(false);
                    createSafetyAuditRequest.setSA21("A");
                }
                break;
                case R.id.cb21B: {
                    cb21A.setChecked(false);
                    cb21C.setChecked(false);
                    cb21D.setChecked(false);
                    createSafetyAuditRequest.setSA21("B");
                }
                break;
                case R.id.cb21C: {
                    cb21A.setChecked(false);
                    cb21B.setChecked(false);
                    cb21D.setChecked(false);
                    createSafetyAuditRequest.setSA21("C");
                }
                break;
                case R.id.cb21D: {
                    cb21A.setChecked(false);
                    cb21B.setChecked(false);
                    cb21C.setChecked(false);
                    createSafetyAuditRequest.setSA21("D");
                }
                break;
                case R.id.cb22A: {
                    cb22B.setChecked(false);
                    cb22C.setChecked(false);
                    cb22D.setChecked(false);
                    createSafetyAuditRequest.setSA22("A");
                }
                break;
                case R.id.cb22B: {
                    cb22A.setChecked(false);
                    cb22C.setChecked(false);
                    cb22D.setChecked(false);
                    createSafetyAuditRequest.setSA22("B");
                }
                break;
                case R.id.cb22C: {
                    cb22A.setChecked(false);
                    cb22B.setChecked(false);
                    cb22D.setChecked(false);
                    createSafetyAuditRequest.setSA22("C");
                }
                break;
                case R.id.cb22D: {
                    cb22A.setChecked(false);
                    cb22B.setChecked(false);
                    cb22C.setChecked(false);
                    createSafetyAuditRequest.setSA22("D");
                }
                break;
                case R.id.cb23A: {
                    cb23B.setChecked(false);
                    cb23C.setChecked(false);
                    cb23D.setChecked(false);
                    createSafetyAuditRequest.setSA23("A");
                }
                break;
                case R.id.cb23B: {
                    cb23A.setChecked(false);
                    cb23C.setChecked(false);
                    cb23D.setChecked(false);
                    createSafetyAuditRequest.setSA23("B");
                }
                break;
                case R.id.cb23C: {
                    cb23A.setChecked(false);
                    cb23B.setChecked(false);
                    cb23D.setChecked(false);
                    createSafetyAuditRequest.setSA23("C");
                }
                break;
                case R.id.cb23D: {
                    cb23A.setChecked(false);
                    cb23B.setChecked(false);
                    cb23C.setChecked(false);
                    createSafetyAuditRequest.setSA23("D");
                }
                break;
                case R.id.cb24A: {
                    cb24B.setChecked(false);
                    cb24C.setChecked(false);
                    cb24D.setChecked(false);
                    createSafetyAuditRequest.setSA24("A");
                }
                break;
                case R.id.cb24B: {
                    cb24A.setChecked(false);
                    cb24C.setChecked(false);
                    cb24D.setChecked(false);
                    createSafetyAuditRequest.setSA24("B");
                }
                break;
                case R.id.cb24C: {
                    cb24A.setChecked(false);
                    cb24B.setChecked(false);
                    cb24D.setChecked(false);
                    createSafetyAuditRequest.setSA24("C");
                }
                break;
                case R.id.cb24D: {
                    cb24A.setChecked(false);
                    cb24B.setChecked(false);
                    cb24C.setChecked(false);
                    createSafetyAuditRequest.setSA24("D");
                }
                break;
                case R.id.cb25A: {
                    cb25B.setChecked(false);
                    cb25C.setChecked(false);
                    cb25D.setChecked(false);
                    createSafetyAuditRequest.setSA25("A");
                }
                break;
                case R.id.cb25B: {
                    cb25A.setChecked(false);
                    cb25C.setChecked(false);
                    cb25D.setChecked(false);
                    createSafetyAuditRequest.setSA25("B");
                }
                break;
                case R.id.cb25C: {
                    cb25A.setChecked(false);
                    cb25B.setChecked(false);
                    cb25D.setChecked(false);
                    createSafetyAuditRequest.setSA25("C");
                }
                break;
                case R.id.cb25D: {
                    cb25A.setChecked(false);
                    cb25B.setChecked(false);
                    cb25C.setChecked(false);
                    createSafetyAuditRequest.setSA25("D");
                }
                break;
                case R.id.cb26A: {
                    cb26B.setChecked(false);
                    cb26C.setChecked(false);
                    cb26D.setChecked(false);
                    createSafetyAuditRequest.setSA26("A");
                }
                break;
                case R.id.cb26B: {
                    cb26A.setChecked(false);
                    cb26C.setChecked(false);
                    cb26D.setChecked(false);
                    createSafetyAuditRequest.setSA26("B");
                }
                break;
                case R.id.cb26C: {
                    cb26A.setChecked(false);
                    cb26B.setChecked(false);
                    cb26D.setChecked(false);
                    createSafetyAuditRequest.setSA26("C");
                }
                break;
                case R.id.cb26D: {
                    cb26A.setChecked(false);
                    cb26B.setChecked(false);
                    cb26C.setChecked(false);
                    createSafetyAuditRequest.setSA26("D");
                }
                break;
                case R.id.cb27A: {
                    cb27B.setChecked(false);
                    cb27C.setChecked(false);
                    cb27D.setChecked(false);
                    createSafetyAuditRequest.setSA27("A");
                }
                break;
                case R.id.cb27B: {
                    cb27A.setChecked(false);
                    cb27C.setChecked(false);
                    cb27D.setChecked(false);
                    createSafetyAuditRequest.setSA27("B");
                }
                break;
                case R.id.cb27C: {
                    cb27A.setChecked(false);
                    cb27B.setChecked(false);
                    cb27D.setChecked(false);
                    createSafetyAuditRequest.setSA27("C");
                }
                break;
                case R.id.cb27D: {
                    cb27A.setChecked(false);
                    cb27B.setChecked(false);
                    cb27C.setChecked(false);
                    createSafetyAuditRequest.setSA27("D");
                }
                break;
                case R.id.cb28A: {
                    cb28B.setChecked(false);
                    cb28C.setChecked(false);
                    cb28D.setChecked(false);
                    createSafetyAuditRequest.setSA28("A");
                }
                break;
                case R.id.cb28B: {
                    cb28A.setChecked(false);
                    cb28C.setChecked(false);
                    cb28D.setChecked(false);
                    createSafetyAuditRequest.setSA28("B");
                }
                break;
                case R.id.cb28C: {
                    cb28A.setChecked(false);
                    cb28B.setChecked(false);
                    cb28D.setChecked(false);
                    createSafetyAuditRequest.setSA28("C");
                }
                break;
                case R.id.cb28D: {
                    cb28A.setChecked(false);
                    cb28B.setChecked(false);
                    cb28C.setChecked(false);
                    createSafetyAuditRequest.setSA28("D");
                }
                break;
                case R.id.cb29A: {
                    cb29B.setChecked(false);
                    cb29C.setChecked(false);
                    cb29D.setChecked(false);
                    createSafetyAuditRequest.setSA29("A");
                }
                break;
                case R.id.cb29B: {
                    cb29A.setChecked(false);
                    cb29C.setChecked(false);
                    cb29D.setChecked(false);
                    createSafetyAuditRequest.setSA29("B");
                }
                break;
                case R.id.cb29C: {
                    cb29A.setChecked(false);
                    cb29B.setChecked(false);
                    cb29D.setChecked(false);
                    createSafetyAuditRequest.setSA29("C");
                }
                break;
                case R.id.cb29D: {
                    cb29A.setChecked(false);
                    cb29B.setChecked(false);
                    cb29C.setChecked(false);
                    createSafetyAuditRequest.setSA29("D");
                }
                break;
                case R.id.cb30A: {
                    cb30B.setChecked(false);
                    cb30C.setChecked(false);
                    cb30D.setChecked(false);
                    createSafetyAuditRequest.setSA30("A");
                }
                break;
                case R.id.cb30B: {
                    cb30A.setChecked(false);
                    cb30C.setChecked(false);
                    cb30D.setChecked(false);
                    createSafetyAuditRequest.setSA30("B");
                }
                break;
                case R.id.cb30C: {
                    cb30A.setChecked(false);
                    cb30B.setChecked(false);
                    cb30D.setChecked(false);
                    createSafetyAuditRequest.setSA30("C");
                }
                break;
                case R.id.cb30D: {
                    cb30A.setChecked(false);
                    cb30B.setChecked(false);
                    cb30C.setChecked(false);
                    createSafetyAuditRequest.setSA30("D");
                }
                break;
                case R.id.cb31A: {
                    cb31B.setChecked(false);
                    cb31C.setChecked(false);
                    cb31D.setChecked(false);
                    createSafetyAuditRequest.setSA31("A");
                }
                break;
                case R.id.cb31B: {
                    cb31A.setChecked(false);
                    cb31C.setChecked(false);
                    cb31D.setChecked(false);
                    createSafetyAuditRequest.setSA31("B");
                }
                break;
                case R.id.cb31C: {
                    cb31A.setChecked(false);
                    cb31B.setChecked(false);
                    cb31D.setChecked(false);
                    createSafetyAuditRequest.setSA31("C");
                }
                break;
                case R.id.cb31D: {
                    cb31A.setChecked(false);
                    cb31B.setChecked(false);
                    cb31C.setChecked(false);
                    createSafetyAuditRequest.setSA31("D");
                }
                break;
                case R.id.cb32A: {
                    cb32B.setChecked(false);
                    cb32C.setChecked(false);
                    cb32D.setChecked(false);
                    createSafetyAuditRequest.setSA32("A");
                }
                break;
                case R.id.cb32B: {
                    cb32A.setChecked(false);
                    cb32C.setChecked(false);
                    cb32D.setChecked(false);
                    createSafetyAuditRequest.setSA32("B");
                }
                break;
                case R.id.cb32C: {
                    cb32A.setChecked(false);
                    cb32B.setChecked(false);
                    cb32D.setChecked(false);
                    createSafetyAuditRequest.setSA32("C");
                }
                break;
                case R.id.cb32D: {
                    cb32A.setChecked(false);
                    cb32B.setChecked(false);
                    cb32C.setChecked(false);
                    createSafetyAuditRequest.setSA32("D");
                }
                break;
                case R.id.cb33A: {
                    cb33B.setChecked(false);
                    cb33C.setChecked(false);
                    cb33D.setChecked(false);
                    createSafetyAuditRequest.setSA33("A");
                }
                break;
                case R.id.cb33B: {
                    cb33A.setChecked(false);
                    cb33C.setChecked(false);
                    cb33D.setChecked(false);
                    createSafetyAuditRequest.setSA33("B");
                }
                break;
                case R.id.cb33C: {
                    cb33A.setChecked(false);
                    cb33B.setChecked(false);
                    cb33D.setChecked(false);
                    createSafetyAuditRequest.setSA33("C");
                }
                break;
                case R.id.cb33D: {
                    cb33A.setChecked(false);
                    cb33B.setChecked(false);
                    cb33C.setChecked(false);
                    createSafetyAuditRequest.setSA33("D");
                }
                break;
                case R.id.cb34A: {
                    cb34B.setChecked(false);
                    cb34C.setChecked(false);
                    cb34D.setChecked(false);
                    createSafetyAuditRequest.setSA34("A");
                }
                break;
                case R.id.cb34B: {
                    cb34A.setChecked(false);
                    cb34C.setChecked(false);
                    cb34D.setChecked(false);
                    createSafetyAuditRequest.setSA34("B");
                }
                break;
                case R.id.cb34C: {
                    cb34A.setChecked(false);
                    cb34B.setChecked(false);
                    cb34D.setChecked(false);
                    createSafetyAuditRequest.setSA34("C");
                }
                break;
                case R.id.cb34D: {
                    cb34A.setChecked(false);
                    cb34B.setChecked(false);
                    cb34C.setChecked(false);
                    createSafetyAuditRequest.setSA34("D");
                }
                break;
                case R.id.cb35A: {
                    cb35B.setChecked(false);
                    cb35C.setChecked(false);
                    cb35D.setChecked(false);
                    createSafetyAuditRequest.setSA35("A");
                }
                break;
                case R.id.cb35B: {
                    cb35A.setChecked(false);
                    cb35C.setChecked(false);
                    cb35D.setChecked(false);
                    createSafetyAuditRequest.setSA35("B");
                }
                break;
                case R.id.cb35C: {
                    cb35A.setChecked(false);
                    cb35B.setChecked(false);
                    cb35D.setChecked(false);
                    createSafetyAuditRequest.setSA35("C");
                }
                break;
                case R.id.cb35D: {
                    cb35A.setChecked(false);
                    cb35B.setChecked(false);
                    cb35C.setChecked(false);
                    createSafetyAuditRequest.setSA35("D");
                }
                break;
                case R.id.cb36A: {
                    cb36B.setChecked(false);
                    cb36C.setChecked(false);
                    cb36D.setChecked(false);
                    createSafetyAuditRequest.setSA36("A");
                }
                break;
                case R.id.cb36B: {
                    cb36A.setChecked(false);
                    cb36C.setChecked(false);
                    cb36D.setChecked(false);
                    createSafetyAuditRequest.setSA36("B");
                }
                break;
                case R.id.cb36C: {
                    cb36A.setChecked(false);
                    cb36B.setChecked(false);
                    cb36D.setChecked(false);
                    createSafetyAuditRequest.setSA36("C");
                }
                break;
                case R.id.cb36D: {
                    cb36A.setChecked(false);
                    cb36B.setChecked(false);
                    cb36C.setChecked(false);
                    createSafetyAuditRequest.setSA36("D");
                }
                break;
                case R.id.cb37A: {
                    cb37B.setChecked(false);
                    cb37C.setChecked(false);
                    cb37D.setChecked(false);
                    createSafetyAuditRequest.setSA37("A");
                }
                break;
                case R.id.cb37B: {
                    cb37A.setChecked(false);
                    cb37C.setChecked(false);
                    cb37D.setChecked(false);
                    createSafetyAuditRequest.setSA37("B");
                }
                break;
                case R.id.cb37C: {
                    cb37A.setChecked(false);
                    cb37B.setChecked(false);
                    cb37D.setChecked(false);
                    createSafetyAuditRequest.setSA37("C");
                }
                break;
                case R.id.cb37D: {
                    cb37A.setChecked(false);
                    cb37B.setChecked(false);
                    cb37C.setChecked(false);
                    createSafetyAuditRequest.setSA37("D");
                }
                break;
                case R.id.cb38A: {
                    cb38B.setChecked(false);
                    cb38C.setChecked(false);
                    cb38D.setChecked(false);
                    createSafetyAuditRequest.setSA38("A");
                }
                break;
                case R.id.cb38B: {
                    cb38A.setChecked(false);
                    cb38C.setChecked(false);
                    cb38D.setChecked(false);
                    createSafetyAuditRequest.setSA38("B");
                }
                break;
                case R.id.cb38C: {
                    cb38A.setChecked(false);
                    cb38B.setChecked(false);
                    cb38D.setChecked(false);
                    createSafetyAuditRequest.setSA38("C");
                }
                break;
                case R.id.cb38D: {
                    cb38A.setChecked(false);
                    cb38B.setChecked(false);
                    cb38C.setChecked(false);
                    createSafetyAuditRequest.setSA38("D");
                }
                break;
                case R.id.cb39A: {
                    cb39B.setChecked(false);
                    cb39C.setChecked(false);
                    cb39D.setChecked(false);
                    createSafetyAuditRequest.setSA39("A");
                }
                break;
                case R.id.cb39B: {
                    cb39A.setChecked(false);
                    cb39C.setChecked(false);
                    cb39D.setChecked(false);
                    createSafetyAuditRequest.setSA39("B");
                }
                break;
                case R.id.cb39C: {
                    cb39A.setChecked(false);
                    cb39B.setChecked(false);
                    cb39D.setChecked(false);
                    createSafetyAuditRequest.setSA39("C");
                }
                break;
                case R.id.cb39D: {
                    cb39A.setChecked(false);
                    cb39B.setChecked(false);
                    cb39C.setChecked(false);
                    createSafetyAuditRequest.setSA39("D");
                }
                break;
                case R.id.cb40A: {
                    cb40B.setChecked(false);
                    cb40C.setChecked(false);
                    cb40D.setChecked(false);
                    createSafetyAuditRequest.setSA40("A");
                }
                break;
                case R.id.cb40B: {
                    cb40A.setChecked(false);
                    cb40C.setChecked(false);
                    cb40D.setChecked(false);
                    createSafetyAuditRequest.setSA40("B");
                }
                break;
                case R.id.cb40C: {
                    cb40A.setChecked(false);
                    cb40B.setChecked(false);
                    cb40D.setChecked(false);
                    createSafetyAuditRequest.setSA40("C");
                }
                break;
                case R.id.cb40D: {
                    cb40A.setChecked(false);
                    cb40B.setChecked(false);
                    cb40C.setChecked(false);
                    createSafetyAuditRequest.setSA40("D");
                }
                break;
            }
        } else {
            switch (compoundButton.getId()) {
                case R.id.cb1A:
                case R.id.cb1B:
                case R.id.cb1C:
                case R.id.cb1D:
                    createSafetyAuditRequest.setSA01("");
                    break;
                case R.id.cb2A:
                case R.id.cb2B:
                case R.id.cb2C:
                case R.id.cb2D:
                    createSafetyAuditRequest.setSA02("");
                    break;
                case R.id.cb3A:
                case R.id.cb3B:
                case R.id.cb3C:
                case R.id.cb3D:
                    createSafetyAuditRequest.setSA03("");
                    break;
                case R.id.cb4A:
                case R.id.cb4B:
                case R.id.cb4C:
                case R.id.cb4D:
                    createSafetyAuditRequest.setSA04("");
                    break;
                case R.id.cb5A:
                case R.id.cb5B:
                case R.id.cb5C:
                case R.id.cb5D:
                    createSafetyAuditRequest.setSA05("");
                    break;
                case R.id.cb6A:
                case R.id.cb6B:
                case R.id.cb6C:
                case R.id.cb6D:
                    createSafetyAuditRequest.setSA06("");
                    break;
                case R.id.cb7A:
                case R.id.cb7B:
                case R.id.cb7C:
                case R.id.cb7D:
                    createSafetyAuditRequest.setSA07("");
                    break;
                case R.id.cb8A:
                case R.id.cb8B:
                case R.id.cb8C:
                case R.id.cb8D:
                    createSafetyAuditRequest.setSA08("");
                    break;
                case R.id.cb9A:
                case R.id.cb9B:
                case R.id.cb9C:
                case R.id.cb9D:
                    createSafetyAuditRequest.setSA09("");
                    break;
                case R.id.cb10A:
                case R.id.cb10B:
                case R.id.cb10C:
                case R.id.cb10D:
                    createSafetyAuditRequest.setSA10("");
                    break;
                case R.id.cb11A:
                case R.id.cb11B:
                case R.id.cb11C:
                case R.id.cb11D:
                    createSafetyAuditRequest.setSA11("");
                    break;
                case R.id.cb12A:
                case R.id.cb12B:
                case R.id.cb12C:
                case R.id.cb12D:
                    createSafetyAuditRequest.setSA12("");
                    break;
                case R.id.cb13A:
                case R.id.cb13B:
                case R.id.cb13C:
                case R.id.cb13D:
                    createSafetyAuditRequest.setSA13("");
                    break;
                case R.id.cb14A:
                case R.id.cb14B:
                case R.id.cb14C:
                case R.id.cb14D:
                    createSafetyAuditRequest.setSA14("");
                    break;
                case R.id.cb15A:
                case R.id.cb15B:
                case R.id.cb15C:
                case R.id.cb15D:
                    createSafetyAuditRequest.setSA15("");
                    break;
                case R.id.cb16A:
                case R.id.cb16B:
                case R.id.cb16C:
                case R.id.cb16D:
                    createSafetyAuditRequest.setSA16("");
                    break;
                case R.id.cb17A:
                case R.id.cb17B:
                case R.id.cb17C:
                case R.id.cb17D:
                    createSafetyAuditRequest.setSA17("");
                    break;
                case R.id.cb18A:
                case R.id.cb18B:
                case R.id.cb18C:
                case R.id.cb18D:
                    createSafetyAuditRequest.setSA18("");
                    break;
                case R.id.cb19A:
                case R.id.cb19B:
                case R.id.cb19C:
                case R.id.cb19D:
                    createSafetyAuditRequest.setSA19("");
                    break;
                case R.id.cb20A:
                case R.id.cb20B:
                case R.id.cb20C:
                case R.id.cb20D:
                    createSafetyAuditRequest.setSA20("");
                    break;
                case R.id.cb21A:
                case R.id.cb21B:
                case R.id.cb21C:
                case R.id.cb21D:
                    createSafetyAuditRequest.setSA21("");
                    break;
                case R.id.cb22A:
                case R.id.cb22B:
                case R.id.cb22C:
                case R.id.cb22D:
                    createSafetyAuditRequest.setSA22("");
                    break;
                case R.id.cb23A:
                case R.id.cb23B:
                case R.id.cb23C:
                case R.id.cb23D:
                    createSafetyAuditRequest.setSA23("");
                    break;
                case R.id.cb24A:
                case R.id.cb24B:
                case R.id.cb24C:
                case R.id.cb24D:
                    createSafetyAuditRequest.setSA24("");
                    break;
                case R.id.cb25A:
                case R.id.cb25B:
                case R.id.cb25C:
                case R.id.cb25D:
                    createSafetyAuditRequest.setSA25("");
                    break;
                case R.id.cb26A:
                case R.id.cb26B:
                case R.id.cb26C:
                case R.id.cb26D:
                    createSafetyAuditRequest.setSA26("");
                    break;
                case R.id.cb27A:
                case R.id.cb27B:
                case R.id.cb27C:
                case R.id.cb27D:
                    createSafetyAuditRequest.setSA27("");
                    break;
                case R.id.cb28A:
                case R.id.cb28B:
                case R.id.cb28C:
                case R.id.cb28D:
                    createSafetyAuditRequest.setSA28("");
                    break;
                case R.id.cb29A:
                case R.id.cb29B:
                case R.id.cb29C:
                case R.id.cb29D:
                    createSafetyAuditRequest.setSA29("");
                    break;
                case R.id.cb30A:
                case R.id.cb30B:
                case R.id.cb30C:
                case R.id.cb30D:
                    createSafetyAuditRequest.setSA30("");
                    break;
                case R.id.cb31A:
                case R.id.cb31B:
                case R.id.cb31C:
                case R.id.cb31D:
                    createSafetyAuditRequest.setSA31("");
                    break;
                case R.id.cb32A:
                case R.id.cb32B:
                case R.id.cb32C:
                case R.id.cb32D:
                    createSafetyAuditRequest.setSA32("");
                    break;
                case R.id.cb33A:
                case R.id.cb33B:
                case R.id.cb33C:
                case R.id.cb33D:
                    createSafetyAuditRequest.setSA33("");
                    break;
                case R.id.cb34A:
                case R.id.cb34B:
                case R.id.cb34C:
                case R.id.cb34D:
                    createSafetyAuditRequest.setSA34("");
                    break;
                case R.id.cb35A:
                case R.id.cb35B:
                case R.id.cb35C:
                case R.id.cb35D:
                    createSafetyAuditRequest.setSA35("");
                    break;
                case R.id.cb36A:
                case R.id.cb36B:
                case R.id.cb36C:
                case R.id.cb36D:
                    createSafetyAuditRequest.setSA36("");
                    break;
                case R.id.cb37A:
                case R.id.cb37B:
                case R.id.cb37C:
                case R.id.cb37D:
                    createSafetyAuditRequest.setSA37("");
                    break;
                case R.id.cb38A:
                case R.id.cb38B:
                case R.id.cb38C:
                case R.id.cb38D:
                    createSafetyAuditRequest.setSA38("");
                    break;
                case R.id.cb39A:
                case R.id.cb39B:
                case R.id.cb39C:
                case R.id.cb39D:
                    createSafetyAuditRequest.setSA39("");
                    break;
                case R.id.cb40A:
                case R.id.cb40B:
                case R.id.cb40C:
                case R.id.cb40D:
                    createSafetyAuditRequest.setSA40("");
                    break;
            }
        }
        Log.i(TAG, "onCheckedChanged: createSafetyAuditRequest - " + new Gson().toJson(createSafetyAuditRequest));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}