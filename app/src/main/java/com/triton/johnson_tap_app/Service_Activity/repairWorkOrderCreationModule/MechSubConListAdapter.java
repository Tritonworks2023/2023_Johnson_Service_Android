package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationModule;

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
import com.triton.johnson_tap_app.interfaces.OnItemClickRepairWorkRequestMachIdListResponseListener;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechIdListResponse;

import java.util.ArrayList;

public class MechSubConListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = MechSubConListAdapter.class.getSimpleName(), formattedDate = "";
    ArrayList<RepairWorkRequestMechIdListResponse.Data> repairWorkRequestMechIdListDataResponseList;
    RepairWorkRequestMechIdListResponse.Data currentItem;
    OnItemClickRepairWorkRequestMachIdListResponseListener onItemClickRepairWorkRequestMachIdListResponseListener;

    public MechSubConListAdapter(ArrayList<RepairWorkRequestMechIdListResponse.Data> repairWorkRequestMechIdListDataResponseList, OnItemClickRepairWorkRequestMachIdListResponseListener onItemClickRepairWorkRequestMachIdListResponseListener) {

        this.repairWorkRequestMechIdListDataResponseList = repairWorkRequestMechIdListDataResponseList;
        this.onItemClickRepairWorkRequestMachIdListResponseListener = onItemClickRepairWorkRequestMachIdListResponseListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mech_sub_con_list, parent, false);
        return new MechSubConListAdapter.ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((MechSubConListAdapter.ViewHolderOne) holder, position);
    }

    private void initLayoutOne(MechSubConListAdapter.ViewHolderOne holder, int position) {

        currentItem = repairWorkRequestMechIdListDataResponseList.get(position);

        holder.txt_emp_code.setText(currentItem.getEMPCODE());
        holder.txt_emp_name.setText(currentItem.getEMPNAME());

        holder.itemView.setOnClickListener(v -> {
            onItemClickRepairWorkRequestMachIdListResponseListener.itemClickRepairWorkRequestMachIdListResponseListener(repairWorkRequestMechIdListDataResponseList.get(position));
            Log.i(TAG, "initLayoutOne: currentItem -> " + new Gson().toJson(repairWorkRequestMechIdListDataResponseList.get(position)));
        });
    }

    @Override
    public int getItemCount() {
        return repairWorkRequestMechIdListDataResponseList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filteredList(ArrayList<RepairWorkRequestMechIdListResponse.Data> repairWorkRequestMechIdListDataResponseFilteredList) {
        repairWorkRequestMechIdListDataResponseList = repairWorkRequestMechIdListDataResponseFilteredList;
        notifyDataSetChanged();
    }

    private static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_emp_code, txt_emp_name;

        public ViewHolderOne(View view) {
            super(view);

            txt_emp_code = view.findViewById(R.id.txt_emp_code);
            txt_emp_name = view.findViewById(R.id.txt_emp_name);
        }
    }
}
