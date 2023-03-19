package com.triton.johnson_tap_app.Service_Activity.safetyAuditModule;

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
import com.triton.johnson_tap_app.responsepojo.JobListSafetyAuditResponse;

import java.util.List;

public class JobListSafetyAuditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String service_title, TAG = JobListSafetyAuditAdapter.class.getSimpleName();
    List<JobListSafetyAuditResponse.Data> jobListSafetyAuditDataResponseList;
    JobListSafetyAuditResponse.Data currentItem;
    SharedPreferences sharedPreferences;
    OnItemClickDataChangeListener onItemClickDataChangeListener;

    public JobListSafetyAuditAdapter(Context applicationContext, List<JobListSafetyAuditResponse.Data> jobListSafetyAuditDataResponseList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

        this.context = applicationContext;
        this.jobListSafetyAuditDataResponseList = jobListSafetyAuditDataResponseList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "JobListSafetyAuditAdapter: service_title --> " + service_title);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job_safety_audit, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = jobListSafetyAuditDataResponseList.get(position);

        holder.txt_jobid.setText(currentItem.getJOBNO());
        holder.txt_building_number.setText(currentItem.getCUST_NAME());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickDataChangeListener.itemClickDataChangeListener(position, "", "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobListSafetyAuditDataResponseList.size();
    }

    public void filterList(List<JobListSafetyAuditResponse.Data> filterlist) {
        jobListSafetyAuditDataResponseList = filterlist;
        notifyDataSetChanged();
    }

    private class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_jobid, txt_building_number;

        public ViewHolderOne(View view) {
            super(view);

            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_building_number = view.findViewById(R.id.txt_building_number);
        }
    }
}
