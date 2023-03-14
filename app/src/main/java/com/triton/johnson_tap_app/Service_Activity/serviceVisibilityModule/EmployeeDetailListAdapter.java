package com.triton.johnson_tap_app.Service_Activity.serviceVisibilityModule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.responsepojo.EmployeeDetailsListResponse;

import java.util.ArrayList;

public class EmployeeDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<EmployeeDetailsListResponse.Data> employeeDetailsDataListResponse;
    EmployeeDetailsListResponse.Data currentItem;
    OnItemClickDataChangeListener onItemClickDataChangeListener;

    public EmployeeDetailListAdapter(ArrayList<EmployeeDetailsListResponse.Data> employeeDetailsDataListResponse, OnItemClickDataChangeListener onItemClickDataChangeListener) {
        this.employeeDetailsDataListResponse = employeeDetailsDataListResponse;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_detail_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

        currentItem = employeeDetailsDataListResponse.get(position);

        holder.txt_emp_id.setText(currentItem.getOM_SM_EMPID());
        holder.txt_emp_name.setText(currentItem.getOM_SM_EMPNAME());

        holder.txt_imei_no.setText(currentItem.getOM_SM_IMEI());

        holder.itemView.setOnClickListener(v -> onItemClickDataChangeListener.itemClickDataChangeListener(position, "", ""));
    }

    @Override
    public int getItemCount() {
        return employeeDetailsDataListResponse.size();
    }

    private static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_emp_id, txt_emp_name, txt_imei_no;

        public ViewHolderOne(View view) {
            super(view);

            txt_emp_id = view.findViewById(R.id.txt_emp_id);
            txt_emp_name = view.findViewById(R.id.txt_emp_name);
            txt_imei_no = view.findViewById(R.id.txt_imei_no);
        }
    }
}
