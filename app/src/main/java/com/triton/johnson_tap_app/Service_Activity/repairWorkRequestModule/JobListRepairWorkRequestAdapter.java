package com.triton.johnson_tap_app.Service_Activity.repairWorkRequestModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointer;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.responsepojo.JobListRepairWorkRequestResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobListRepairWorkRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = JobListRepairWorkRequestAdapter.class.getSimpleName(), formattedDate = "";
    List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList;
    JobListRepairWorkRequestResponse.Data currentItem;
    OnItemClickDataChangeListener onItemClickDataChangeListener;

    public JobListRepairWorkRequestAdapter(List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

        this.jobListRepairWorkRequestResponseList = jobListRepairWorkRequestResponseList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job_list_repair_work_request, parent, false);
        return new JobListRepairWorkRequestAdapter.ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((JobListRepairWorkRequestAdapter.ViewHolderOne) holder, position);
    }

    private void initLayoutOne(JobListRepairWorkRequestAdapter.ViewHolderOne holder, int position) {

        currentItem = jobListRepairWorkRequestResponseList.get(position);

        holder.txt_jobid.setText(currentItem.getJOBNO());
        holder.txt_building_number.setText(currentItem.getCUST_NAME());

        holder.txt_address.setText(String.format("%s, %s, %s, %s, %s", nullPointer(currentItem.getINST_ADD()), nullPointer(currentItem.getINST_ADD1()), nullPointer(currentItem.getINST_ADD3()), nullPointer(currentItem.getLANDMARK()), nullPointer(currentItem.getPINCODE())));

        setDate(holder, currentItem.getINST_ON());

        holder.itemView.setOnClickListener(v -> onItemClickDataChangeListener.itemClickDataChangeListener(position, "", ""));
    }

    @Override
    public int getItemCount() {
        return jobListRepairWorkRequestResponseList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filteredList(List<JobListRepairWorkRequestResponse.Data> jobListRepairWorkRequestResponseFilteredList) {
        jobListRepairWorkRequestResponseList = jobListRepairWorkRequestResponseFilteredList;
        notifyDataSetChanged();
    }

    private void setDate(JobListRepairWorkRequestAdapter.ViewHolderOne holder, String inputDateTime) {

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "dd-MMM-yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDateTime);
            str = outputFormat.format(date);
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/

            holder.txt_date.setText(formattedDate);
            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_jobid, txt_building_number, txt_address, txt_date;

        public ViewHolderOne(View view) {
            super(view);

            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_building_number = view.findViewById(R.id.txt_building_number);
            txt_address = view.findViewById(R.id.txt_address);
            txt_date = view.findViewById(R.id.txt_date);
        }
    }
}
