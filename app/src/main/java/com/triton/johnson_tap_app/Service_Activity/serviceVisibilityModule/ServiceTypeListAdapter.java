package com.triton.johnson_tap_app.Service_Activity.serviceVisibilityModule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.responsepojo.ServiceTypeLocalListResponse;

import java.util.List;

public class ServiceTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String status, TAG = ServiceTypeListAdapter.class.getSimpleName();
    List<ServiceTypeLocalListResponse> breedTypedataBeanList;
    ServiceTypeLocalListResponse currentItem;
    OnItemClickDataChangeListener onItemClickDataChangeListener;

    public ServiceTypeListAdapter(Context applicationContext, List<ServiceTypeLocalListResponse> breedTypedataBeanList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

        this.context = applicationContext;
        this.breedTypedataBeanList = breedTypedataBeanList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;

        Log.i(TAG, "ServiceTypeListAdapter: status --> " + status);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_type_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = breedTypedataBeanList.get(position);

        holder.txt_jobId.setText(currentItem.getS_code());
        holder.txt_jobName.setText(currentItem.getS_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickDataChangeListener.itemClickDataChangeListener(position, "", "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return breedTypedataBeanList.size();
    }

    public void filterrList(List<ServiceTypeLocalListResponse> filterlist) {
        breedTypedataBeanList = filterlist;
        notifyDataSetChanged();
    }

    private class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_jobId, txt_jobName;

        public ViewHolderOne(View view) {
            super(view);

            txt_jobId = view.findViewById(R.id.txt_jobId);
            txt_jobName = view.findViewById(R.id.txt_jobName);
        }
    }
}
