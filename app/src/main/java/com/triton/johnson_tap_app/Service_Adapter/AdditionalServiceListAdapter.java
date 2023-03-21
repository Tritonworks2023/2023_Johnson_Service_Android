package com.triton.johnson_tap_app.Service_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.johnson_tap_app.PetBreedTypeSelectListener;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.Service_Activity.elevatorSurveyModule.NewJobElevatorSurveyActivity;
import com.triton.johnson_tap_app.Service_Activity.escalatorSurveyModule.NewJobEscalatorSurveyActivity;
import com.triton.johnson_tap_app.Service_Activity.failureReportModule.JobFailureReportActivity;
import com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule.FailureReportRequestScannerActivity;
import com.triton.johnson_tap_app.Service_Activity.repairWorkApprovalRequestModule.NewJobRepairWorkApprovalRequestActivity;
import com.triton.johnson_tap_app.Service_Activity.repairWorkModule.JobHazardAnalysisFormActivity;
import com.triton.johnson_tap_app.Service_Activity.ropeMaintenanceModule.JobRopeMaintenanceActivity;
import com.triton.johnson_tap_app.Service_Activity.safetyAuditModule.SafetyAuditJobActivity;
import com.triton.johnson_tap_app.Service_Activity.serviceVisibilityModule.ServiceVisibilityJobActivity;
import com.triton.johnson_tap_app.responsepojo.ServiceResponse;

import java.util.List;

public class AdditionalServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceResponse.DataBean currentItem;
    SharedPreferences sharedPreferences;
    private String TAG = AdditionalServiceListAdapter.class.getSimpleName();
    private Context context;
    private List<ServiceResponse.DataBean> breedTypedataBeanList;
    private PetBreedTypeSelectListener petBreedTypeSelectListener;

    public AdditionalServiceListAdapter(Context context, List<ServiceResponse.DataBean> breedTypedataBeanList, PetBreedTypeSelectListener petBreedTypeSelectListener) {
        this.context = context;
        this.breedTypedataBeanList = breedTypedataBeanList;
        this.petBreedTypeSelectListener = petBreedTypeSelectListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_additional_services_item, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("LogNotTimber")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = breedTypedataBeanList.get(position);

        if (currentItem.getService_name() != null) {

            Log.e("Service Title", currentItem.getService_name().toString());

            holder.text_title.setText(currentItem.getService_name());
            holder.last_used_time.setText(currentItem.getLast_used_time());
            holder.upload_count.setText(currentItem.getUploaded_count());
            holder.paused_count.setText(currentItem.getPaused_count());
            holder.jobs_count.setText(currentItem.getJob_count());
        }

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                breedTypedataBeanList.get(position).getService_name();

                String s = breedTypedataBeanList.get(position).getService_name();
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("service_title", s);
                editor.apply();

                Log.i(TAG, "onClick: Service Title -> " + s);

                if (s.equals("Escalator Survey Form")) {
                    Intent n_act = new Intent(context, NewJobEscalatorSurveyActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Elevator Survey Form")) {
                    Intent n_act = new Intent(context, NewJobElevatorSurveyActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Service Visibility")) {
                    Intent n_act = new Intent(context, ServiceVisibilityJobActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Failure Report")) {
                    /*Intent n_act = new Intent(context, FailureReportFormActivity.class);*/
                    Intent n_act = new Intent(context, JobFailureReportActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Failure Report2")) {
                    /*Intent n_act = new Intent(context, FailureReportFormActivity.class);*/
                    Intent n_act = new Intent(context, FailureReportRequestScannerActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Repair Work Approval Request")) {
                    Intent n_act = new Intent(context, NewJobRepairWorkApprovalRequestActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Repair Work Approval")) {
                    Intent n_act = new Intent(context, JobHazardAnalysisFormActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Rope Maintenance")) {
                    Intent n_act = new Intent(context, JobRopeMaintenanceActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else if (s.equals("Safety Audit")) {
                    Intent n_act = new Intent(context, SafetyAuditJobActivity.class);
                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // n_act.putExtra("service_title", s);
                    context.startActivity(n_act);
                } else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return breedTypedataBeanList.size();
    }

    public void filterList(List<ServiceResponse.DataBean> breedTypedataBeanListFiltered) {
        breedTypedataBeanList = breedTypedataBeanListFiltered;
        Log.i(TAG, "filterList: breedTypedataBeanList -> " + new Gson().toJson(breedTypedataBeanList));

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView text_title, last_used_time, upload_count, paused_count, jobs_count;
        public LinearLayout ll_root;

        public ViewHolderOne(View itemView) {
            super(itemView);

            text_title = itemView.findViewById(R.id.text_title);
            last_used_time = itemView.findViewById(R.id.last_used_time);
            upload_count = itemView.findViewById(R.id.upload_count);
            paused_count = itemView.findViewById(R.id.paused_count);
            jobs_count = itemView.findViewById(R.id.jobs_count);
            ll_root = itemView.findViewById(R.id.lin_services_item);

        }
    }
}
