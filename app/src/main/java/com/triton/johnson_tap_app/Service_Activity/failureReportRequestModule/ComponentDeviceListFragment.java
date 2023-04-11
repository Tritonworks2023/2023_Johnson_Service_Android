package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

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
import com.triton.johnson_tap_app.interfaces.OnItemClickFailureReportFetchDetailsByComIdResponseListener;
import com.triton.johnson_tap_app.requestpojo.FailureReportCompDeviceListRequest;
import com.triton.johnson_tap_app.responsepojo.FailureReportCompDeviceListResponse;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComponentDeviceListFragment extends Fragment implements OnItemClickFailureReportFetchDetailsByComIdResponseListener {

    private ImageView img_back;
    private EditText edt_search;
    private RecyclerView rv_component_device_list;
    private TextView txt_no_records;
    private Context context;
    private Dialog dialog;
    private String TAG = ComponentDeviceListFragment.class.getSimpleName(), message = "", jobId = "";
    private ArrayList<FailureReportCompDeviceListResponse.Data> failureReportCompDeviceListDataResponseList;
    private OnItemClickFailureReportFetchDetailsByComIdResponseListener onItemClickFailureReportFetchDetailsByComIdResponseListener;
    private ComponentDeviceListAdapter componentDeviceListAdapter;

    public ComponentDeviceListFragment() {
        // Required empty public constructor
    }

    public ComponentDeviceListFragment(String jobId, OnItemClickFailureReportFetchDetailsByComIdResponseListener onItemClickFailureReportFetchDetailsByComIdResponseListener) {
        this.jobId = jobId;
        this.onItemClickFailureReportFetchDetailsByComIdResponseListener = onItemClickFailureReportFetchDetailsByComIdResponseListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_component_device_list, container, false);
        context = getContext();

        img_back = view.findViewById(R.id.img_back);
        edt_search = view.findViewById(R.id.edt_search);
        rv_component_device_list = view.findViewById(R.id.rv_component_device_list);
        txt_no_records = view.findViewById(R.id.txt_no_records);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ComponentDeviceListFragment.this).commit();
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
                    rv_component_device_list.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, failureReportCompDeviceListDataResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edt_search.getText().toString();

                rv_component_device_list.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, failureReportCompDeviceListDataResponseList);
            }
        });

        getFailureReportCompDeviceList(jobId);

        return view;
    }

    private void filter(String search, List<FailureReportCompDeviceListResponse.Data> failureReportCompDeviceListResponseList) {

        List<FailureReportCompDeviceListResponse.Data> filterList = new ArrayList<>();
        try {
            for (FailureReportCompDeviceListResponse.Data item : failureReportCompDeviceListResponseList) {
                if (item.getST_PMH_PARTNAME().contains(search) ||
                        item.getST_PMH_PARTNAME().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: Part Name -> " + item.getST_PMH_PARTNAME().contains(search.toLowerCase()));
                    filterList.add(item);
                }
                if (item.getST_PMH_PARTNO().contains(search) ||
                        item.getST_PMH_PARTNO().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: Part ID -> " + item.getST_PMH_PARTNO().contains(search.toLowerCase()));
                    filterList.add(item);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "filter: error -> " + e.getMessage());
        }

        if (filterList.isEmpty()) {
            //Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_component_device_list.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            componentDeviceListAdapter.filteredList(filterList);
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

    private void setView(List<FailureReportCompDeviceListResponse.Data> failureReportCompDeviceListDataResponse) {
        rv_component_device_list.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_component_device_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_component_device_list.setItemAnimator(new DefaultItemAnimator());
        componentDeviceListAdapter = new ComponentDeviceListAdapter(failureReportCompDeviceListDataResponse, this);
        rv_component_device_list.setAdapter(componentDeviceListAdapter);
    }

    private FailureReportCompDeviceListRequest jobListRequest(String strSearch) {

        FailureReportCompDeviceListRequest job = new FailureReportCompDeviceListRequest();

        if (strSearch != null && !strSearch.isEmpty()) {
            job.setJob_id(strSearch);
            Log.i(TAG, "jobListRequest: FailureReportCompDeviceListRequest --> " + new Gson().toJson(job));
            return job;
        } else {
            return null;
        }
    }

    private void getFailureReportCompDeviceList(String strSearch) {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        FailureReportCompDeviceListRequest jobIdRequest = jobListRequest(strSearch);

        if (jobIdRequest != null) {

            Call<FailureReportCompDeviceListResponse> call = apiInterface.getFailureReportCompDeviceList(RestUtils.getContentType(), jobIdRequest);
            Log.i(TAG, "getFailureReportCompDeviceList: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<FailureReportCompDeviceListResponse>() {
                @Override
                public void onResponse(@NonNull Call<FailureReportCompDeviceListResponse> call, @NonNull Response<FailureReportCompDeviceListResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getFailureReportCompDeviceList: onResponse: JobListRopeMaintenanceResponse -> " + new Gson().toJson(response.body()));
                    failureReportCompDeviceListDataResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                failureReportCompDeviceListDataResponseList = response.body().getData();

                                if (failureReportCompDeviceListDataResponseList.isEmpty()) {

                                    rv_component_device_list.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                    edt_search.setEnabled(false);
                                } else {
//                                    edt_search.setEnabled(true);
                                    setView(failureReportCompDeviceListDataResponseList);
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
                public void onFailure(@NonNull Call<FailureReportCompDeviceListResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getFailureReportCompDeviceList: onFailure: error --> " + t.getMessage());
                    rv_component_device_list.setVisibility(View.GONE);
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
    public void itemClickFailureReportFetchDetailsByComIdResponseListener(FailureReportCompDeviceListResponse.Data failureReportCompDeviceListDataResponseSelected) {
        FailureReportCompDeviceListResponse.Data failureReportCompDeviceListDataResponse = new FailureReportCompDeviceListResponse.Data();
        failureReportCompDeviceListDataResponse = failureReportCompDeviceListDataResponseSelected;

        Log.i(TAG, "itemClickFailureReportFetchDetailsByComIdResponseListener: failureReportCompDeviceListDataResponseSelected --> " + new Gson().toJson(failureReportCompDeviceListDataResponse));
        onItemClickFailureReportFetchDetailsByComIdResponseListener.itemClickFailureReportFetchDetailsByComIdResponseListener(failureReportCompDeviceListDataResponse);

    }
}