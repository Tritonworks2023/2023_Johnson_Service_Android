package com.triton.johnson_tap_app.Service_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnItemClickCheckBoxFeedbackGroupDetails;
import com.triton.johnson_tap_app.responsepojo.Feedback_GroupResponse;

import java.util.ArrayList;
import java.util.List;

public class Feedback_GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Feedback_GroupResponse.DataBean currentItem;
    String data = "", jobid, service_title, str_FeedBackGroup;
    ArrayList<String> myData = new ArrayList<>();
    boolean isStringExists = false;
    SharedPreferences sharedPreferences;
    String pre_check;
    private String TAG = "Feedback_GroupAdapter";
    private List<Feedback_GroupResponse.DataBean> dataBeanList;
    private Context context;
    private int size;
    private OnItemClickCheckBoxFeedbackGroupDetails onItemClickCheckBoxFeedbackGroupDetails;
    //   private UserTypeSelectListener1 userTypeSelectListener;

    public Feedback_GroupAdapter(Context thiscontext, List<Feedback_GroupResponse.DataBean> dataBeanList, ArrayList<String> mydata, OnItemClickCheckBoxFeedbackGroupDetails onItemClickCheckBoxFeedbackGroupDetails) {
        this.context = thiscontext;
        this.dataBeanList = dataBeanList;
        this.myData = mydata;
        this.onItemClickCheckBoxFeedbackGroupDetails = onItemClickCheckBoxFeedbackGroupDetails;
//        this.str_FeedBackGroup = feedback_group;
        //  this.userTypeSelectListener = userTypeSelectListener;

        /*CommonUtil.dbUtil = new DbUtil(context);
        CommonUtil.dbUtil.open();
        CommonUtil.dbHelper = new DbHelper(context);*/

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(thiscontext);
        jobid = sharedPreferences.getString("job_id", "default value");
        service_title = sharedPreferences.getString("service_title", "default value");
        Log.e("JobID", "" + jobid);
        Log.e("Name", "" + service_title);
        Log.e("My Feedback Group", "" + myData);

    }

    public void filterList(List<Feedback_GroupResponse.DataBean> filterllist) {
        dataBeanList = filterllist;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_card, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);
    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = dataBeanList.get(position);

        if (currentItem.getTitle() != null) {
            holder.name.setText(currentItem.getTitle());
            holder.code.setText(currentItem.getCodes());
        }

        String feedback = holder.code.getText().toString();
        String myfeedback = myData.toString();

        Log.e("Data Get 1", "" + myfeedback);

        isStringExists = myfeedback.contains(feedback);
        Log.e("isChecked", "" + isStringExists);

        if (isStringExists) {
            Log.e("Nish", "inside" + holder.code.getText().toString());
            //holder.chkSelected.setSelected(true);
            // breedTypedataBeanList.get(position).setSelected(true);
            holder.chx_usertypes.setChecked(true);
        }

        if (currentItem.isSelected()) {
            holder.chx_usertypes.setChecked(true);
        }

        /*holder.chx_usertypes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                String data = "";
                dataBeanList.get(position).setSelected(true);
                data = data + "\n" + dataBeanList.get(position).getCodes();
                Log.i(TAG, "onClick: data -> " + data);

                if (CommonUtil.dbUtil.hasFeedbackgroup(jobid, service_title, data, "2")) {
                    CommonUtil.dbUtil.deleteFeedbackgroup(jobid, service_title, data, "2");
                    Cursor cur = CommonUtil.dbUtil.getFeedbackgroup(jobid, service_title, "2");
                    Log.i(TAG, "onClick: cur.getCount -> " + cur.getCount());

                    Cursor curs = CommonUtil.dbUtil.getFeedbackDesc(jobid, service_title, "3");
                    Log.i(TAG, "onClick: curs.getCount -> " + curs.getCount());

                    CommonUtil.dbUtil.deleteFeedbackDescTable(jobid, service_title, "3");

                    Cursor cursor = CommonUtil.dbUtil.getFeedbackDesc(jobid, service_title, "3");
                    Log.i(TAG, "onClick: cursor.getCount -> " + cursor.getCount());
                } else {
                    CommonUtil.dbUtil.addFeedbackgroup(jobid, service_title, data, "2");
                    Cursor cur = CommonUtil.dbUtil.getFeedbackgroup(jobid, service_title, "2");
                    Log.i(TAG, "onClick: cur.getCount -> " + cur.getCount());
                }
            }
        });*/

        holder.chx_usertypes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String data = "";
                dataBeanList.get(position).setSelected(true);
                data = data + "\n" + dataBeanList.get(position).getCodes();
                Log.i(TAG, "onClick: data -> " + data);

                onItemClickCheckBoxFeedbackGroupDetails.itemClickCheckBoxFeedbackGroupDetails(position, dataBeanList.get(position));

                /*if (CommonUtil.dbUtil.hasFeedbackgroup(jobid, service_title, data, "2")) {
                    CommonUtil.dbUtil.deleteFeedbackgroup(jobid, service_title, data, "2");
                    Cursor cur = CommonUtil.dbUtil.getFeedbackgroup(jobid, service_title, "2");
                    Log.i(TAG, "onClick: cur.getCount -> " + cur.getCount());

                    Cursor curs = CommonUtil.dbUtil.getFeedbackDesc(jobid, service_title, "3");
                    Log.i(TAG, "onClick: curs.getCount -> " + curs.getCount());

                    CommonUtil.dbUtil.deleteFeedbackDescTable(jobid, service_title, "3");

                    Cursor cursor = CommonUtil.dbUtil.getFeedbackDesc(jobid, service_title, "3");
                    Log.i(TAG, "onClick: cursor.getCount -> " + cursor.getCount());
                } else {
                    CommonUtil.dbUtil.addFeedbackgroup(jobid, service_title, data, "2");
                    Cursor cur = CommonUtil.dbUtil.getFeedbackgroup(jobid, service_title, "2");
                    Log.i(TAG, "onClick: cur.getCount -> " + cur.getCount());
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView name, code;
        CardView cv_root;
        LinearLayout ll_usertypes;
        CheckBox chx_usertypes;

        public ViewHolderOne(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            code = itemView.findViewById(R.id.codes);
            ll_usertypes = itemView.findViewById(R.id.ll_usertypes);
            chx_usertypes = itemView.findViewById(R.id.chkSelected);
        }
    }
}
