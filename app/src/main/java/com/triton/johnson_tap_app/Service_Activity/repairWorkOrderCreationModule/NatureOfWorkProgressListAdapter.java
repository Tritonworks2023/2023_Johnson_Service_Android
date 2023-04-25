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
import com.triton.johnson_tap_app.interfaces.OnItemClickRepairWorkRequestNatureOfWorkResponseListener;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestNatureOfWorkResponse;

import java.util.ArrayList;

public class NatureOfWorkProgressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = NatureOfWorkProgressListAdapter.class.getSimpleName(), formattedDate = "";
    ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> repairWorkRequestNatureOfWorkDataResponseList;
    RepairWorkRequestNatureOfWorkResponse.Data currentItem;
    OnItemClickRepairWorkRequestNatureOfWorkResponseListener onItemClickRepairWorkRequestNatureOfWorkResponseListener;

    public NatureOfWorkProgressListAdapter(ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> repairWorkRequestNatureOfWorkDataResponseList, OnItemClickRepairWorkRequestNatureOfWorkResponseListener onItemClickRepairWorkRequestNatureOfWorkResponseListener) {

        this.repairWorkRequestNatureOfWorkDataResponseList = repairWorkRequestNatureOfWorkDataResponseList;
        this.onItemClickRepairWorkRequestNatureOfWorkResponseListener = onItemClickRepairWorkRequestNatureOfWorkResponseListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nature_of_work_progress_list, parent, false);
        return new NatureOfWorkProgressListAdapter.ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((NatureOfWorkProgressListAdapter.ViewHolderOne) holder, position);
    }

    private void initLayoutOne(NatureOfWorkProgressListAdapter.ViewHolderOne holder, int position) {

        currentItem = repairWorkRequestNatureOfWorkDataResponseList.get(position);

        holder.txt_now_id.setText(currentItem.getP_ID());
        holder.txt_now_name.setText(currentItem.getP_NAME());
        holder.txt_wp_id.setText(currentItem.getR_ID());
        holder.txt_wp_name.setText(currentItem.getR_NAME());

        holder.itemView.setOnClickListener(v -> {
            onItemClickRepairWorkRequestNatureOfWorkResponseListener.itemClickRepairWorkRequestNatureOfWorkResponseListener(repairWorkRequestNatureOfWorkDataResponseList.get(position));
            Log.i(TAG, "initLayoutOne: currentItem -> " + new Gson().toJson(repairWorkRequestNatureOfWorkDataResponseList.get(position)));
        });
    }

    @Override
    public int getItemCount() {
        return repairWorkRequestNatureOfWorkDataResponseList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filteredList(ArrayList<RepairWorkRequestNatureOfWorkResponse.Data> repairWorkRequestNatureOfWorkDataResponseFilteredList) {
        repairWorkRequestNatureOfWorkDataResponseList = repairWorkRequestNatureOfWorkDataResponseFilteredList;
        notifyDataSetChanged();
    }

    private static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_now_id, txt_now_name, txt_wp_id, txt_wp_name;

        public ViewHolderOne(View view) {
            super(view);

            txt_now_id = view.findViewById(R.id.txt_now_id);
            txt_now_name = view.findViewById(R.id.txt_now_name);
            txt_wp_id = view.findViewById(R.id.txt_wp_id);
            txt_wp_name = view.findViewById(R.id.txt_wp_name);
        }
    }
}
