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
import com.triton.johnson_tap_app.interfaces.OnItemClickRepairWorkRequestNatureOfWorkResponseListener;
import com.triton.johnson_tap_app.requestpojo.RepairWorkRequestMechIdListRequest;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechIdListResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestNatureOfWorkResponse;
import com.triton.johnson_tap_app.utils.RestUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NatureOfWorkProgressListFragment extends Fragment implements OnItemClickRepairWorkRequestNatureOfWorkResponseListener{

    private ImageView img_back;
    private EditText edt_search;
    private RecyclerView rv_now_wp_list;
    private TextView txt_no_records;
    private Context context;
    private Dialog dialog;
    private String TAG = MechSubConListFragment.class.getSimpleName(), message = "";
    private ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> repairWorkRequestNatureOfWorkDataResponseList;
    private OnItemClickRepairWorkRequestNatureOfWorkResponseListener onItemClickRepairWorkRequestNatureOfWorkResponseListener;
    private NatureOfWorkProgressListAdapter natureOfWorkProgressListAdapter;

    public NatureOfWorkProgressListFragment(OnItemClickRepairWorkRequestNatureOfWorkResponseListener onItemClickRepairWorkRequestNatureOfWorkResponseListener) {
        this.onItemClickRepairWorkRequestNatureOfWorkResponseListener = onItemClickRepairWorkRequestNatureOfWorkResponseListener;
    }

    public NatureOfWorkProgressListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nature_of_work_progress_list, container, false);
        context = getContext();

        img_back = view.findViewById(R.id.img_back);
        edt_search = view.findViewById(R.id.edt_search);
        rv_now_wp_list = view.findViewById(R.id.rv_now_wp_list);
        txt_no_records = view.findViewById(R.id.txt_no_records);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(NatureOfWorkProgressListFragment.this).commit();
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
                    rv_now_wp_list.setVisibility(View.VISIBLE);
                } else {
                    filter(Search, repairWorkRequestNatureOfWorkDataResponseList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String Search = edt_search.getText().toString();

                rv_now_wp_list.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);

                filter(Search, repairWorkRequestNatureOfWorkDataResponseList);
            }
        });

        getRepairWorkRequestMechIdList();

        return view;
    }

    private void filter(String search, ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> repairWorkRequestNatureOfWorkDataResponseList) {

        ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> filterList = new ArrayList<>();
        try {
            for (RepairWorkRequestNatureOfWorkResponse.Data item : repairWorkRequestNatureOfWorkDataResponseList) {
                if (item.getP_NAME().contains(search) ||
                        item.getP_NAME().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: Part Name -> " + item.getP_NAME().contains(search.toLowerCase()));
                    filterList.add(item);
                }
                if (item.getR_NAME().contains(search) ||
                        item.getR_NAME().toLowerCase().contains(search.toLowerCase())) {
                    Log.i(TAG, "filter: Part ID -> " + item.getR_NAME().contains(search.toLowerCase()));
                    filterList.add(item);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "filter: error -> " + e.getMessage());
        }

        if (filterList.isEmpty()) {
            //Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_now_wp_list.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Data Found");
        } else {
            natureOfWorkProgressListAdapter.filteredList(filterList);
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

    private void setView(ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> repairWorkRequestNatureOfWorkDataResponse) {
        rv_now_wp_list.setVisibility(View.VISIBLE);
        txt_no_records.setVisibility(View.GONE);
        rv_now_wp_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_now_wp_list.setItemAnimator(new DefaultItemAnimator());
        natureOfWorkProgressListAdapter = new NatureOfWorkProgressListAdapter(repairWorkRequestNatureOfWorkDataResponse, this);
        rv_now_wp_list.setAdapter(natureOfWorkProgressListAdapter);
    }

    private void getRepairWorkRequestMechIdList() {

        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

            Call<RepairWorkRequestNatureOfWorkResponse> call = apiInterface.getRepairWorkRequestNatureOfWork(RestUtils.getContentType());
            Log.i(TAG, "getRepairWorkRequestMechIdList: URL -> " + call.request().url().toString());

            call.enqueue(new Callback<RepairWorkRequestNatureOfWorkResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepairWorkRequestNatureOfWorkResponse> call, @NonNull Response<RepairWorkRequestNatureOfWorkResponse> response) {
                    dialog.dismiss();
                    Log.i(TAG, "getRepairWorkRequestMechIdList: onResponse: RepairWorkRequestMechIdListResponse -> " + new Gson().toJson(response.body()));
                    repairWorkRequestNatureOfWorkDataResponseList = new ArrayList<>();
                    if (response.body() != null) {
                        message = response.body().getMessage();

                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                repairWorkRequestNatureOfWorkDataResponseList = response.body().getData();

                                if (repairWorkRequestNatureOfWorkDataResponseList.isEmpty()) {

                                    rv_now_wp_list.setVisibility(View.GONE);
                                    txt_no_records.setVisibility(View.VISIBLE);
                                    txt_no_records.setText("No Records Found");
//                                    edt_search.setEnabled(false);
                                } else {
//                                    edt_search.setEnabled(true);
                                    setView(repairWorkRequestNatureOfWorkDataResponseList);
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
                public void onFailure(@NonNull Call<RepairWorkRequestNatureOfWorkResponse> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "getRepairWorkRequestMechIdList: onFailure: error --> " + t.getMessage());
                    rv_now_wp_list.setVisibility(View.GONE);
                    showErrorAlert(t.getMessage());
                    txt_no_records.setVisibility(View.VISIBLE);
                    txt_no_records.setText("Something Went Wrong.. Try Again Later");
                }
            });
    }

    @Override
    public void itemClickRepairWorkRequestNatureOfWorkResponseListener(RepairWorkRequestNatureOfWorkResponse.Data repairWorkRequestNatureOfWorkDataResponseSelected) {
        RepairWorkRequestNatureOfWorkResponse.Data repairWorkRequestNatureOfWorkDataResponse = new RepairWorkRequestNatureOfWorkResponse.Data();
        repairWorkRequestNatureOfWorkDataResponse = repairWorkRequestNatureOfWorkDataResponseSelected;

        Log.i(TAG, "itemClickRepairWorkRequestNatureOfWorkResponseListener: repairWorkRequestNatureOfWorkDataResponse -> " + new Gson().toJson(repairWorkRequestNatureOfWorkDataResponse));
        onItemClickRepairWorkRequestNatureOfWorkResponseListener.itemClickRepairWorkRequestNatureOfWorkResponseListener(repairWorkRequestNatureOfWorkDataResponse);
    }
}