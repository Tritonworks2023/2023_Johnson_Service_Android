package com.triton.johnson_tap_app.Service_Activity.repairWorkOrderCreationCompletionModule;

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
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestApprovalRequestListRpMechResponse;
import com.triton.johnson_tap_app.responsepojo.RepairWorkRequestMechResponse;

import java.util.List;

public class RepairWorkOrderCreationCompletionJobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String service_title, TAG = RepairWorkOrderCreationCompletionJobListAdapter.class.getSimpleName();
    List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> repairWorkRequestApprovalRequestListRpMechResponseList;
    RepairWorkRequestApprovalRequestListRpMechResponse.Data currentItem;
    SharedPreferences sharedPreferences;
    OnItemClickDataChangeListener onItemClickDataChangeListener;

    public RepairWorkOrderCreationCompletionJobListAdapter(Context applicationContext, List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> repairWorkRequestApprovalRequestListRpMechResponseList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

        this.context = applicationContext;
        this.repairWorkRequestApprovalRequestListRpMechResponseList = repairWorkRequestApprovalRequestListRpMechResponseList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "RepairWorkOrderCreationCompletionJobListAdapter: service_title --> " + service_title);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_repair_work_order_creation_completion_job_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = repairWorkRequestApprovalRequestListRpMechResponseList.get(position);

        holder.txt_jobid.setText(currentItem.getJob_no());
        holder.txt_building_number.setText(currentItem.getCustomer_name());
        holder.txt_machine_type.setText(currentItem.getSubmitted_by_on());
        holder.txt_date_of_app.setText(currentItem.getRequest_on());
        holder.txt_status.setText(currentItem.getStatus());
        holder.txt_controllerType.setText(currentItem.getSubmitted_by_emp_code());
        holder.txt_installed_date.setText(currentItem.getSubmitted_by_name());
        holder.txt_survey_on.setText(currentItem.getZonal_eng_id());
        holder.txt_date.setText(currentItem.getZonal_eng_name());
        holder.txt_route.setText(currentItem.getRoute_code());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickDataChangeListener.itemClickDataChangeListener(position, "", "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return repairWorkRequestApprovalRequestListRpMechResponseList.size();
    }

    public void filterList(List<RepairWorkRequestApprovalRequestListRpMechResponse.Data> filterlist) {
        repairWorkRequestApprovalRequestListRpMechResponseList = filterlist;
        notifyDataSetChanged();
    }

    private class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_jobid, txt_building_number, txt_machine_type, txt_date_of_app, txt_status, txt_controllerType,
                txt_installed_date, txt_survey_on, txt_date, txt_route;

        public ViewHolderOne(View view) {
            super(view);

            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_building_number = view.findViewById(R.id.txt_building_number);
            txt_machine_type = view.findViewById(R.id.txt_machine_type);
            txt_date_of_app = view.findViewById(R.id.txt_date_of_app);
            txt_status = view.findViewById(R.id.txt_status);
            txt_controllerType = view.findViewById(R.id.txt_controllerType);
            txt_installed_date = view.findViewById(R.id.txt_installed_date);
            txt_survey_on = view.findViewById(R.id.txt_survey_on);
            txt_date = view.findViewById(R.id.txt_date);
            txt_route = view.findViewById(R.id.txt_route);
        }
    }
}
