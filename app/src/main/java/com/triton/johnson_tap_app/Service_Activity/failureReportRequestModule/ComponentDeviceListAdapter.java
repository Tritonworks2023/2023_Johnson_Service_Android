package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickFailureReportFetchDetailsByComIdResponseListener;
import com.triton.johnson_tap_app.responsepojo.FailureReportCompDeviceListResponse;

import java.util.List;

public class ComponentDeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = ComponentDeviceListAdapter.class.getSimpleName(), formattedDate = "";
    List<FailureReportCompDeviceListResponse.Data> failureReportCompDeviceListDataResponse;
    FailureReportCompDeviceListResponse.Data currentItem;
    OnItemClickFailureReportFetchDetailsByComIdResponseListener onItemClickFailureReportFetchDetailsByComIdResponseListener;

    public ComponentDeviceListAdapter(List<FailureReportCompDeviceListResponse.Data> failureReportCompDeviceListDataResponse, OnItemClickFailureReportFetchDetailsByComIdResponseListener onItemClickFailureReportFetchDetailsByComIdResponseListener) {

        this.failureReportCompDeviceListDataResponse = failureReportCompDeviceListDataResponse;
        this.onItemClickFailureReportFetchDetailsByComIdResponseListener = onItemClickFailureReportFetchDetailsByComIdResponseListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_component_device_list, parent, false);
        return new ComponentDeviceListAdapter.ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ComponentDeviceListAdapter.ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ComponentDeviceListAdapter.ViewHolderOne holder, int position) {

        currentItem = failureReportCompDeviceListDataResponse.get(position);

        holder.txt_comp_id.setText(currentItem.getST_PMH_PARTNO());
        holder.txt_comp_device_name.setText(currentItem.getST_PMH_PARTNAME());
        holder.txt_met_id.setText(nullPointer(currentItem.getST_PMH_BARCODEID()));

//        holder.itemView.setOnClickListener(v -> onItemClickDataChangeListener.itemClickDataChangeListener(position, "", ""));
        holder.itemView.setOnClickListener(v -> {
            onItemClickFailureReportFetchDetailsByComIdResponseListener.itemClickFailureReportFetchDetailsByComIdResponseListener(failureReportCompDeviceListDataResponse.get(position));
            Log.i(TAG, "initLayoutOne: currentItem -> " + new Gson().toJson(failureReportCompDeviceListDataResponse.get(position)));
        });
    }

    @Override
    public int getItemCount() {
        return failureReportCompDeviceListDataResponse.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filteredList(List<FailureReportCompDeviceListResponse.Data> failureReportCompDeviceListDataResponseFilteredList) {
        failureReportCompDeviceListDataResponse = failureReportCompDeviceListDataResponseFilteredList;
        notifyDataSetChanged();
    }

    private static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_comp_id, txt_comp_device_name, txt_met_id;

        public ViewHolderOne(View view) {
            super(view);

            txt_comp_id = view.findViewById(R.id.txt_comp_id);
            txt_comp_device_name = view.findViewById(R.id.txt_comp_device_name);
            txt_met_id = view.findViewById(R.id.txt_met_id);
        }
    }
}
