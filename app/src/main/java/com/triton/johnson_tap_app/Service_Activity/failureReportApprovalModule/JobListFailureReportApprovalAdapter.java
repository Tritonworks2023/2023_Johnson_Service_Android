package com.triton.johnson_tap_app.Service_Activity.failureReportApprovalModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.responsepojo.FailureReportRequestListByEngCodeResponse;

import java.util.List;

public class JobListFailureReportApprovalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String service_title, TAG = JobListFailureReportApprovalAdapter.class.getSimpleName();
    List<FailureReportRequestListByEngCodeResponse.Data> failureReportRequestListByEngCodeDataResponseList;
    FailureReportRequestListByEngCodeResponse.Data currentItem;
    SharedPreferences sharedPreferences;
    OnItemClickDataChangeListener onItemClickDataChangeListener;

    public JobListFailureReportApprovalAdapter(Context applicationContext, List<FailureReportRequestListByEngCodeResponse.Data> failureReportRequestListByEngCodeDataResponseList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

        this.context = applicationContext;
        this.failureReportRequestListByEngCodeDataResponseList = failureReportRequestListByEngCodeDataResponseList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "JobListFailureReportApprovalAdapter: service_title --> " + service_title);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job_list_failure_report_approvel, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = failureReportRequestListByEngCodeDataResponseList.get(position);

        holder.txt_jobid.setText(currentItem.getJob_id());
        holder.txt_building_number.setText(currentItem.getCustomer_address());
        holder.txt_submitted_by.setText(currentItem.getSubmitted_by_name());
        holder.txt_submitted_on.setText(currentItem.getSubmitted_by_on());
        holder.txt_status.setText(currentItem.getApp_status());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickDataChangeListener.itemClickDataChangeListener(position, "", "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return failureReportRequestListByEngCodeDataResponseList.size();
    }

    public void filterList(List<FailureReportRequestListByEngCodeResponse.Data> filterlist) {
        failureReportRequestListByEngCodeDataResponseList = filterlist;
        notifyDataSetChanged();
    }

    private class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_jobid, txt_building_number, txt_submitted_by, txt_submitted_on, txt_status;

        public ViewHolderOne(View view) {
            super(view);

            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_building_number = view.findViewById(R.id.txt_building_number);
            txt_submitted_by = view.findViewById(R.id.txt_submitted_by);
            txt_submitted_on = view.findViewById(R.id.txt_submitted_on);
            txt_status = view.findViewById(R.id.txt_status);
        }
    }
}