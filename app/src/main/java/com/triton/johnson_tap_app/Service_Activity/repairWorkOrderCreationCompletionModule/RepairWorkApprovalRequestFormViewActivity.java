package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationCompletionModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.responsepojo.FailureReportDropDownDataResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestApprovalRequestListRpMechResponse;
import com.triton.johnson_tap_app.utils.ConnectionDetector;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairWorkApprovalRequestFormViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private String strDateType = "", TAG = RepairWorkApprovalRequestFormViewActivity.class.getSimpleName(), se_user_id = "",
            se_user_mobile_no = "", se_user_name = "", se_user_location = "", networkStatus;
    private TextView txt_jobid, txt_building_name, txt_branch_code, txt_route, txt_ext_by,
            txt_service_type, txt_mech_sub_id, txt_mech_sub_name, txt_zonal_eng_id, txt_zonal_eng_name,
            txt_assistant_name, /*txt_branch_office_no,*/
            txt_install_address, txt_nature_work_id, txt_nature_work_name,
            txt_work_process_id, txt_work_process_name, txt_other_process, txt_work_start_date, txt_work_expected_date,
            txt_tech_trained, txt_man_power, txt_mr_no, txt_mat_ava_site, txt_repair_toolkit, txt_first_aid_kit,
            txt_full_body_harness, txt_hard_hat, txt_safety_shoes, txt_hand_gloves, txt_chain_block_status, txt_chain_block_capacity,
            txt_webbing_belt, txt_ladder_required, txt_ms_pipe_required, txt_rebelling_clamp_required, txt_dShackle_required,
    /*txt_comp_date,*/ txt_bar_maint_dis_req, txt_repair_work_tech;
    private ImageView img_back;
    private Button btn_next;
    private ArrayList<String> serviceTypeList = new ArrayList<>();
    private Dialog dialog;
    private RepairWorkRequestApprovalRequestListRpMechResponse.Data repairWorkRequestApprovalRequestListRpMechDataResponse = new RepairWorkRequestApprovalRequestListRpMechResponse.Data();
    private FailureReportDropDownDataResponse failureReportDropDownDataResponse = new FailureReportDropDownDataResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_work_approval_request_form_view);

        context = RepairWorkApprovalRequestFormViewActivity.this;

        btn_next = findViewById(R.id.btn_next);
        img_back = findViewById(R.id.img_back);

        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);
        txt_branch_code = findViewById(R.id.txt_branch_code);
        txt_route = findViewById(R.id.txt_route);
        txt_ext_by = findViewById(R.id.txt_ext_by);
        txt_service_type = findViewById(R.id.txt_service_type);
        txt_mech_sub_id = findViewById(R.id.txt_mech_sub_id);
        txt_mech_sub_name = findViewById(R.id.txt_mech_sub_name);
        txt_zonal_eng_id = findViewById(R.id.txt_zonal_eng_id);
        txt_zonal_eng_name = findViewById(R.id.txt_zonal_eng_name);
        txt_assistant_name = findViewById(R.id.txt_assistant_name);
        /*txt_branch_office_no = findViewById(R.id.txt_branch_office_no);*/
        txt_install_address = findViewById(R.id.txt_install_address);
        txt_nature_work_id = findViewById(R.id.txt_nature_work_id);
        txt_nature_work_name = findViewById(R.id.txt_nature_work_name);
        txt_work_process_id = findViewById(R.id.txt_work_process_id);
        txt_work_process_name = findViewById(R.id.txt_work_process_name);
        txt_other_process = findViewById(R.id.txt_other_process);
        txt_work_start_date = findViewById(R.id.txt_work_start_date);
        txt_work_expected_date = findViewById(R.id.txt_work_expected_date);
        txt_tech_trained = findViewById(R.id.txt_tech_trained);
        txt_man_power = findViewById(R.id.txt_man_power);
        txt_mr_no = findViewById(R.id.txt_mr_no);
        txt_mat_ava_site = findViewById(R.id.txt_mat_ava_site);
        txt_repair_toolkit = findViewById(R.id.txt_repair_toolkit);
        txt_first_aid_kit = findViewById(R.id.txt_first_aid_kit);
        txt_full_body_harness = findViewById(R.id.txt_full_body_harness);
        txt_hard_hat = findViewById(R.id.txt_hard_hat);
        txt_safety_shoes = findViewById(R.id.txt_safety_shoes);
        txt_hand_gloves = findViewById(R.id.txt_hand_gloves);
        txt_chain_block_status = findViewById(R.id.txt_chain_block_status);
        txt_chain_block_capacity = findViewById(R.id.txt_chain_block_capacity);
        txt_webbing_belt = findViewById(R.id.txt_webbing_belt);
        txt_ladder_required = findViewById(R.id.txt_ladder_required);
        txt_ms_pipe_required = findViewById(R.id.txt_ms_pipe_required);
        txt_rebelling_clamp_required = findViewById(R.id.txt_rebelling_clamp_required);
        txt_dShackle_required = findViewById(R.id.txt_dShackle_required);
        /*txt_comp_date = findViewById(R.id.txt_comp_date);*/
        txt_bar_maint_dis_req = findViewById(R.id.txt_bar_maint_dis_req);
        txt_repair_work_tech = findViewById(R.id.txt_repair_work_tech);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("repairWorkRequestApprovalRequestListRpMechDataResponse")) {
                repairWorkRequestApprovalRequestListRpMechDataResponse = extras.getParcelable("repairWorkRequestApprovalRequestListRpMechDataResponse");
            }
        }
        Log.i(TAG, "onCreate: repairWorkRequestApprovalRequestListRpMechDataResponse -> " + new Gson().toJson(repairWorkRequestApprovalRequestListRpMechDataResponse));

        txt_jobid.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getJob_no());
        txt_building_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getCustomer_name());
        txt_branch_code.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getBr_code());
        txt_route.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getRoute_code());
        txt_ext_by.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getExecution_by());
        txt_service_type.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getService_type());
        txt_mech_sub_id.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getMech_id());
        txt_mech_sub_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getMech_name());
        txt_zonal_eng_id.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getZonal_eng_id());
        txt_zonal_eng_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getZonal_eng_name());
        txt_assistant_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getAssistant_name());
        /*txt_branch_office_no.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getBranch_office_no());*/
        txt_install_address.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getInstall_address());
        txt_nature_work_id.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getNature_of_work_id());
        txt_nature_work_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getNature_of_work_name());
        txt_work_process_id.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getNature_of_work_process_id());
        txt_work_process_name.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getNature_of_work_process_name());
        txt_other_process.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getOther_process());
        txt_work_start_date.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getWork_start_date());
        txt_work_expected_date.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getWork_expected_date());
        txt_tech_trained.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getTech_trained());
        txt_man_power.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getMan_power());
        txt_mr_no.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getMr_no());
        txt_mat_ava_site.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getMaterial_available_site());
        txt_repair_toolkit.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getRepair_toolkit());
        txt_first_aid_kit.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getFirst_aid_kit());
        txt_full_body_harness.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getFull_body_harness());
        txt_hard_hat.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getHard_hat());
        txt_safety_shoes.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getSafety_shoes());
        txt_hand_gloves.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getHand_gloves());
        txt_chain_block_status.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getChain_block_status());
        txt_chain_block_capacity.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getChain_block_capacity());
        txt_webbing_belt.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getWebbing_belt());
        txt_ladder_required.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getLadder_req());
        txt_ms_pipe_required.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getMs_pipe_req());
        txt_rebelling_clamp_required.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getRebelling_clamp());
        txt_dShackle_required.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getDshackle_req());
        /*txt_comp_date.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getCompleted_date());*/
        txt_bar_maint_dis_req.setText(repairWorkRequestApprovalRequestListRpMechDataResponse.getBarricate_main_req());
        txt_repair_work_tech.setText(String.format("%s - %s - %s", repairWorkRequestApprovalRequestListRpMechDataResponse.getRepair_work_mech_id(), repairWorkRequestApprovalRequestListRpMechDataResponse.getRepair_work_mech_name(), repairWorkRequestApprovalRequestListRpMechDataResponse.getRepair_work_mech_no()));

        btn_next.setOnClickListener(this);
        img_back.setOnClickListener(this);

        initLoadingDialog();

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        Log.i(TAG, "onCreate: networkStatus -> " + networkStatus);

        /*if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            NoInternetDialog();
        } else {
            getFailureReportDropDownData();
        }*/
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

                            if (failureReportDropDownDataResponse.getData().getServ_type() != null
                                    && !failureReportDropDownDataResponse.getData().getServ_type().isEmpty()) {
                                for (FailureReportDropDownDataResponse.Serv_type servType : failureReportDropDownDataResponse.getData().getServ_type()) {
                                    serviceTypeList.add(servType.getDisplay_name());
                                }

                                if (!serviceTypeList.isEmpty()) {
                                    for (int i = 0; i < failureReportDropDownDataResponse.getData().getServ_type().size(); i++) {
                                        if (failureReportDropDownDataResponse.getData().getServ_type().get(i).getValue().equalsIgnoreCase(repairWorkRequestApprovalRequestListRpMechDataResponse.getService_type())) {
                                            txt_service_type.setText(failureReportDropDownDataResponse.getData().getServ_type().get(i).getDisplay_name());
                                        }
                                    }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.btn_next: {
                Intent intent = new Intent(context, JobHazardAnalysisFormActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("repairWorkRequestApprovalRequestListRpMechDataResponse", repairWorkRequestApprovalRequestListRpMechDataResponse);
                context.startActivity(intent);
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}