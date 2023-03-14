package com.triton.johnson_tap_app.Service_Activity.repairWorkApprovalRequestModule;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.RestUtils;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.requestpojo.Count_pasusedRequest;
import com.triton.johnson_tap_app.responsepojo.Count_pasusedResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class RepairWorkApprovalRequestFormActivity extends AppCompatActivity implements View.OnClickListener {
    private static String formattedDate = "";
    private Context context;
    private SharedPreferences sharedPreferences;
    private String TAG = RepairWorkApprovalRequestFormActivity.class.getSimpleName(), strDateType = "", se_user_mobile_no, se_user_name, se_id, check_id, service_title, message, paused_count, networkStatus = "";
    private ImageView img_back;
    private Spinner spinner_ext_by, spinner_tech_trained, spinner_mat_ava_site, spinner_repair_toolkit, spinner_first_aid_kit,
            spinner_full_body_harness, spinner_hard_hat, spinner_safety_shoes, spinner_hand_gloves, spinner_chain_block_status, spinner_chain_block_capacity,
            spinner_webbing_belt, spinner_ladder_required, spinner_ms_pipe_required, spinner_rebelling_clamp_required,
            spinner_dShackle_required, spinner_bar_maint_dis_req;
    private EditText edt_maj_con_area_if, edt_recom_is, edt_cust_name, edt_desig, edt_contact_no, edt_surv_conducted_by;
    private SignaturePad signaturePad;
    private LinearLayout buttons_container;
    private TextView txt_branch_code, txt_branch_name, txt_route, txt_job_num, txt_cust_name, txt_ser_type,
            txt_nature_work_id, txt_nature_work_name, txt_nature_work_process_id, txt_nature_work_process_name,
            txt_work_start_date, txt_work_expected_date, txt_comp_date;
    private Button btn_submit;
    private String[] yesNoNaArray, yesNoArray, executionArray, chainBlockStatusArray, chainBlockCapacityArray;
    private int day, month, year;
    private DatePickerDialog datePickerDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_approval_request_form);
        context = this;

        img_back = findViewById(R.id.img_back);

        txt_branch_code = findViewById(R.id.txt_branch_code);
        txt_branch_name = findViewById(R.id.txt_branch_name);
        txt_route = findViewById(R.id.txt_route);
        txt_job_num = findViewById(R.id.txt_job_num);
        txt_cust_name = findViewById(R.id.txt_cust_name);
        txt_ser_type = findViewById(R.id.txt_ser_type);
        txt_nature_work_id = findViewById(R.id.txt_nature_work_id);
        txt_nature_work_name = findViewById(R.id.txt_nature_work_name);
        txt_nature_work_process_id = findViewById(R.id.txt_nature_work_process_id);
        txt_nature_work_process_name = findViewById(R.id.txt_nature_work_process_name);
        txt_work_start_date = findViewById(R.id.txt_work_start_date);
        txt_work_expected_date = findViewById(R.id.txt_work_expected_date);
        txt_comp_date = findViewById(R.id.txt_comp_date);

        spinner_ext_by = findViewById(R.id.spinner_ext_by);
        spinner_tech_trained = findViewById(R.id.spinner_tech_trained);
        spinner_mat_ava_site = findViewById(R.id.spinner_mat_ava_site);
        spinner_repair_toolkit = findViewById(R.id.spinner_repair_toolkit);
        spinner_first_aid_kit = findViewById(R.id.spinner_first_aid_kit);
        spinner_full_body_harness = findViewById(R.id.spinner_full_body_harness);
        spinner_hard_hat = findViewById(R.id.spinner_hard_hat);
        spinner_safety_shoes = findViewById(R.id.spinner_safety_shoes);
        spinner_hand_gloves = findViewById(R.id.spinner_hand_gloves);
        spinner_chain_block_status = findViewById(R.id.spinner_chain_block_status);
        spinner_chain_block_capacity = findViewById(R.id.spinner_chain_block_capacity);
        spinner_webbing_belt = findViewById(R.id.spinner_webbing_belt);
        spinner_ladder_required = findViewById(R.id.spinner_ladder_required);
        spinner_ms_pipe_required = findViewById(R.id.spinner_ms_pipe_required);
        spinner_rebelling_clamp_required = findViewById(R.id.spinner_rebelling_clamp_required);
        spinner_dShackle_required = findViewById(R.id.spinner_dShackle_required);
        spinner_bar_maint_dis_req = findViewById(R.id.spinner_bar_maint_dis_req);

        edt_maj_con_area_if = findViewById(R.id.edt_maj_con_area_if);
        edt_recom_is = findViewById(R.id.edt_recom_is);
        edt_cust_name = findViewById(R.id.edt_cust_name);
        edt_desig = findViewById(R.id.edt_desig);
        edt_contact_no = findViewById(R.id.edt_contact_no);
//        edt_surv_conducted_by = findViewById(R.id.edt_surv_conducted_by);

        signaturePad = findViewById(R.id.signaturePad);

        buttons_container = findViewById(R.id.buttons_container);
        btn_submit = findViewById(R.id.btn_submit);

        yesNoNaArray = getResources().getStringArray(R.array.yes_no_na_array);
        yesNoArray = getResources().getStringArray(R.array.yes_no_array);
        executionArray = new String[]{"SELECT", "JLCPL", "SUBCON"};
        chainBlockStatusArray = new String[]{"SELECT", "CE - Certified", "NC - Not Certified", "NA - Not Applicable"};
        chainBlockCapacityArray = new String[]{"SELECT", "2 - 2 TON", "3 - 3 TON", "5 - 5 TON", "NA Not Applicable"};

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("_id", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "onCreate: service_title -> " + service_title);
        Log.i(TAG, "onCreate: se_user_mobile_no -> " + se_user_mobile_no);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
//            Count_paused();
        }

        ArrayAdapter<String> yesNoNaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoNaArray);
        yesNoNaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoArray);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> executionAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, executionArray);
        executionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> chainBlockStatusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, chainBlockStatusArray);
        chainBlockStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> chainBlockCapacityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, chainBlockCapacityArray);
        chainBlockCapacityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_ext_by.setAdapter(executionAdapter);

        spinner_chain_block_status.setAdapter(chainBlockStatusAdapter);

        spinner_chain_block_capacity.setAdapter(chainBlockCapacityAdapter);

        spinner_tech_trained.setAdapter(yesNoAdapter);
        spinner_mat_ava_site.setAdapter(yesNoAdapter);
        spinner_repair_toolkit.setAdapter(yesNoAdapter);
        spinner_first_aid_kit.setAdapter(yesNoAdapter);
        spinner_full_body_harness.setAdapter(yesNoAdapter);
        spinner_hard_hat.setAdapter(yesNoAdapter);
        spinner_safety_shoes.setAdapter(yesNoAdapter);
        spinner_hand_gloves.setAdapter(yesNoAdapter);

        spinner_webbing_belt.setAdapter(yesNoNaAdapter);
        spinner_ladder_required.setAdapter(yesNoNaAdapter);
        spinner_ms_pipe_required.setAdapter(yesNoNaAdapter);
        spinner_rebelling_clamp_required.setAdapter(yesNoNaAdapter);
        spinner_dShackle_required.setAdapter(yesNoNaAdapter);
        spinner_bar_maint_dis_req.setAdapter(yesNoNaAdapter);

        txt_work_start_date.setOnClickListener(this);
        txt_work_expected_date.setOnClickListener(this);
        txt_comp_date.setOnClickListener(this);
        img_back.setOnClickListener(this);
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

    private void Count_paused() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Count_pasusedResponse> call = apiInterface.Count_AuditstatuscountCall(RestUtils.getContentType(), count_pasuedRequest());

        Log.i(TAG, "Count_paused: URL --> " + call.request().url().toString());

        call.enqueue(new Callback<Count_pasusedResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<Count_pasusedResponse> call, @NonNull retrofit2.Response<Count_pasusedResponse> response) {

                Log.i(TAG, "Count_paused: onResponse: Count_pasusedResponse --> " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            paused_count = response.body().getData().getPaused_count();
                            Log.e("Count", "" + paused_count);
                            // pasused_count.setText( paused_count);
                        }
                    } else {
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Count_pasusedResponse> call, @NonNull Throwable t) {

                Log.e(TAG, "Count_paused: onFailure: error --> " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Count_pasusedRequest count_pasuedRequest() {
        Count_pasusedRequest count = new Count_pasusedRequest();
        count.setUser_mobile_no(se_user_mobile_no);
        count.setService_name(service_title);
        Log.w(TAG, "loginRequest " + new Gson().toJson(count));
        return count;
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

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

        if (strDateType.equalsIgnoreCase("txt_work_start_date")) {
            txt_work_start_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_work_expire_date")) {
            txt_work_expected_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_comp_date")) {
            txt_comp_date.setText(dateTime);
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

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: {
                onBackPressed();
//                Intent send = new Intent(context, ServicesActivity.class);
//                startActivity(send);
            }
            break;
            case R.id.txt_work_start_date: {
                strDateType = "txt_work_start_date";
                callDatePicker();
            }
            break;
            case R.id.txt_work_expected_date: {
                strDateType = "txt_work_expected_date";
                callDatePicker();
            }
            break;
            case R.id.txt_comp_date: {
                strDateType = "txt_comp_date";
                callDatePicker();
            }
            break;
        }
    }
}
