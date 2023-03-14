package com.triton.johnson_tap_app.Service_Activity.ropeMaintenanceModule;

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

import com.triton.johnson_tap_app.PetBreedTypeSelectListener;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.responsepojo.JobListRopeMaintenanceResponse;

import java.util.ArrayList;
import java.util.List;

public class JobListRopeMaintenanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String status, service_title, TAG = JobListRopeMaintenanceAdapter.class.getSimpleName();
    List<JobListRopeMaintenanceResponse.Data> breedTypedataBeanList;
    PetBreedTypeSelectListener petBreedTypeSelectListener;
    JobListRopeMaintenanceResponse.Data currentItem;
    SharedPreferences sharedPreferences;
    OnItemClickDataChangeListener onItemClickDataChangeListener;
    private ArrayList<String> arliJobid;
    private ArrayList<String> arliCustname;
    private ArrayList<String> arliAuditdate;

    public JobListRopeMaintenanceAdapter(Context applicationContext, List<JobListRopeMaintenanceResponse.Data> breedTypedataBeanList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

        this.context = applicationContext;
        this.breedTypedataBeanList = breedTypedataBeanList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;

        Log.e("Status", "" + status);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        service_title = sharedPreferences.getString("service_title", "Services");

        Log.i(TAG, "NewJobListEscalatorSurveyAdapter: service_title --> " + service_title);
        Log.i(TAG, "NewJobListEscalatorSurveyAdapter: status --> " + status);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job_rope_maintenance, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = breedTypedataBeanList.get(position);

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
        return breedTypedataBeanList.size();
    }

    public void filterrList(List<JobListRopeMaintenanceResponse.Data> filterlist) {
        breedTypedataBeanList = filterlist;
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
