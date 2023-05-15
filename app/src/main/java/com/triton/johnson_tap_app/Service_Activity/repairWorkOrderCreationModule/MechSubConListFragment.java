package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickRepairWorkRequestMachIdListResponseListener;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestMechIdListRequest;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechIdListResponse;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechSubConListFragment extends Fragment implements OnItemClickRepairWorkRequestMachIdListResponseListener {

    private ImageView img_back;
    private EditText edt_search;
    private RecyclerView rv_mech_sub_con_list;
    private TextView txt_no_records;
    private Context context;
    private Dialog dialog;
    private String TAG = MechSubConListFragment.class.getSimpleName(), BR_CODE = "", CONTYPE = "", message = "";
    private ArrayList<RepairWorkRequestMechIdListResponse.Data> repairWorkRequestMechIdListDataResponseList;
    private OnItemClickRepairWorkRequestMachIdListResponseListener onItemClickRepairWorkRequestMachIdListResponseListener;
    private MechSubConListAdapter mechSubConListAdapter;

    public MechSubConListFragment(String BR_CODE, String CONTYPE, OnItemClickRepairWorkRequestMachIdListResponseListener onItemClickRepairWorkRequestMachIdListResponseListener) {
        this.BR_CODE = BR_CODE;
        this.CONTYPE = CONTYPE;
        this.onItemClickRepairWorkRequestMachIdListResponseListener = onItemClickRepairWorkRequestMachIdListResponseListener;
    }

    public MechSubConListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mech_sub_con_list, container, false);
        context = getContext();

        img_back = view.findViewById(R.id.img_back);
        edt_search = view.findViewById(R.id.edt_search);
        rv_mech_sub_con_list = view.findViewById(R.id.rv_mech_sub_con_list);
        txt_no_records = view.findViewById(R.id.txt_no_records);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(MechSubConListFragment.this).commit();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String Search = edt_search.getText().toString();

                Log.i(TAG, "onTextChanged: edtSearch -> " + Search);

                if (Search.equals("")) {
                    rv_mech_sub_con_list.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, repairWorkRequestMechIdListDataResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edt_search.getText().toString();

                rv_mech_sub_con_list.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, repairWorkRequestMechIdListDataResponseList);
            }
        });

        getRepairWorkRequestMechIdList();
        return view;
    }

    private void filter(String search, ArrayList<RepairWorkRequestMechIdListResponse.Data> repairWorkRequestMechIdListResponseList) {

        ArrayList<RepairWorkRequestMechIdListResponse.Data> filterList = new ArrayList<>();
        try {
            for (RepairWorkRequestMechIdListResponse.Data item : repairWorkRequestMechIdListResponseList) {
                /*if (item.getEMPCODE().contains(search) ||
                        item.getEMPCODE().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: Part Name -> " + item.getEMPCODE().contains(search.toLowerCase()));
                    filterList.add(item);
                }*/
                if (item.getEMPNAME().contains(search) ||
                        item.getEMPNAME().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: Part ID -> " + item.getEMPNAME().contains(search.toLowerCase()));
                    filterList.add(item);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "filter: error -> " + e.getMessage());
        }

        if (filterList.isEmpty()) {
            //Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_mech_sub_con_list.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            mechSubConListAdapter.filteredList(filterList);
        }
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
            }
        });
    }

    private void showErrorAlert(String message) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.remarks_popup, null);

        EditText edt_Remarks = mView.findViewById(R.id.edt_remarks);
        Button btn_Submit = mView.findViewById(R.id.btn_submit);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txt_Message = mView.findViewById(R.id.txt_message);
        btn_Submit.setText("OK");
        edt_Remarks.setVisibility(View.GONE);
        txt_Message.setVisibility(View.VISIBLE);

        mBuilder.setView(mView);
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        txt_Message.setText(message);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }

    private void setView(ArrayList<RepairWorkRequestMechIdListResponse.Data> repairWorkRequestMechIdListDataResponse) {
        rv_mech_sub_con_list.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_mech_sub_con_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_mech_sub_con_list.setItemAnimator(new DefaultItemAnimator());
        mechSubConListAdapter = new MechSubConListAdapter(repairWorkRequestMechIdListDataResponse, this);
        rv_mech_sub_con_list.setAdapter(mechSubConListAdapter);
    }

    private RepairWorkRequestMechIdListRequest jobListRequest() {

        RepairWorkRequestMechIdListRequest job = new RepairWorkRequestMechIdListRequest();

        if (nullPointerValidator(BR_CODE) && nullPointerValidator(CONTYPE)) {
            job.setBR_CODE(BR_CODE);
            job.setCONTYPE(CONTYPE);
            Log.i(TAG, "jobListRequest: RepairWorkRequestMechIdListRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void getRepairWorkRequestMechIdList() {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        RepairWorkRequestMechIdListRequest jobIdRequest = jobListRequest();

        if (jobIdRequest != null) {

            Call<RepairWorkRequestMechIdListResponse> call = apiInterface.getRepairWorkRequestMechIdList(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getRepairWorkRequestMechIdList: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkRequestMechIdListResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkRequestMechIdListResponse> call, @NonNull Response<RepairWorkRequestMechIdListResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getRepairWorkRequestMechIdList: onResponse: RepairWorkRequestMechIdListResponse -> " + new Gson().toJson(response.body()));
                    repairWorkRequestMechIdListDataResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkRequestMechIdListDataResponseList = response.body().getData();

                                if (repairWorkRequestMechIdListDataResponseList.isEmpty()) {

                                    rv_mech_sub_con_list.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                    edt_search.setEnabled(false);
                                } else {
//                                    edt_search.setEnabled(true);
                                    setView(repairWorkRequestMechIdListDataResponseList);
                                }
                            } else {
                                showErrorAlert(message);
                            }
                        } else {
                            showErrorAlert("");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepairWorkRequestMechIdListResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getRepairWorkRequestMechIdList: onFailure: error --> " + t.getMessage());
                    rv_mech_sub_con_list.setVisibility(View.GONE);
                    showErrorAlert(t.getMessage());
                    txt_no_records.setVisibility(View.VISIBLE);
                    txt_no_records.setText("Something Went Wrong.. Try Again Later");
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void itemClickRepairWorkRequestMachIdListResponseListener(RepairWorkRequestMechIdListResponse.Data repairWorkRequestMechIdListDataResponseSelected) {
        RepairWorkRequestMechIdListResponse.Data repairWorkRequestMechIdListDataResponse = new RepairWorkRequestMechIdListResponse.Data();
        repairWorkRequestMechIdListDataResponse = repairWorkRequestMechIdListDataResponseSelected;

        Log.i(TAG, "itemClickRepairWorkRequestMachIdListResponseListener: repairWorkRequestMechIdListDataResponseSelected -> " + new Gson().toJson(repairWorkRequestMechIdListDataResponseSelected));
        onItemClickRepairWorkRequestMachIdListResponseListener.itemClickRepairWorkRequestMachIdListResponseListener(repairWorkRequestMechIdListDataResponse);
    }
}