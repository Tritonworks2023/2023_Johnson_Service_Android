package com.triton.johnson_tap_app.Service_Activity.repairWorkApprovalRequestModule;

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
import com.triton.johnson_tap_app.responsepojo.NewJobListRepairWorkApprovalResponse;

import java.util.ArrayList;
import java.util.List;

public class NewJobListRepairWorkApprovalRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String status, service_title, TAG = NewJobListRepairWorkApprovalRequestAdapter.class.getSimpleName();
    List<NewJobListRepairWorkApprovalResponse.DataBean> breedTypedataBeanList;
    PetBreedTypeSelectListener petBreedTypeSelectListener;
    NewJobListRepairWorkApprovalResponse.DataBean currentItem;
    SharedPreferences sharedPreferences;
    OnItemClickDataChangeListener onItemClickDataChangeListener;
    private ArrayList<String> arliJobid;
    private ArrayList<String> arliCustname;
    private ArrayList<String> arliAuditdate;

    public NewJobListRepairWorkApprovalRequestAdapter(Context applicationContext, List<NewJobListRepairWorkApprovalResponse.DataBean> breedTypedataBeanList, OnItemClickDataChangeListener onItemClickDataChangeListener) {

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_new_job_repair_work_approval_request, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = breedTypedataBeanList.get(position);

        holder.txt_jobid.setText(currentItem.getJob_no());
        holder.txt_building_number.setText(currentItem.getBuilding_name());
        holder.txt_machine_type.setText(currentItem.getMachine_type());
        holder.txt_address.setText(currentItem.getAddress());
        holder.txt_controllerType.setText(currentItem.getController_type());
        holder.txt_installed_date.setText(currentItem.getInstalled_on());
        holder.txt_survey_on.setText(currentItem.getSurveyed_on());
        holder.txt_date.setText(currentItem.getDate());

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

    public void filterrList(List<NewJobListRepairWorkApprovalResponse.DataBean> filterlist) {
        breedTypedataBeanList = filterlist;
        notifyDataSetChanged();
    }

    private class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_jobid, txt_building_number, txt_machine_type, txt_address, txt_controllerType, txt_installed_date, txt_survey_on, txt_date;

        public ViewHolderOne(View view) {
            super(view);

            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_building_number = view.findViewById(R.id.txt_building_number);
            txt_machine_type = view.findViewById(R.id.txt_machine_type);
            txt_address = view.findViewById(R.id.txt_address);
            txt_controllerType = view.findViewById(R.id.txt_controllerType);
            txt_installed_date = view.findViewById(R.id.txt_installed_date);
            txt_survey_on = view.findViewById(R.id.txt_survey_on);
            txt_date = view.findViewById(R.id.txt_date);
        }
    }
}
