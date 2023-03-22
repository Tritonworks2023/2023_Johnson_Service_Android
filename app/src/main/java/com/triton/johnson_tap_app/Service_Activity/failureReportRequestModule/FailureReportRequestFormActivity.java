package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.triton.johnson_tap_app.R;

public class FailureReportRequestFormActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView img_back;
    private TextView txt_job_id, txt_building_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_request_form);

        context = this;

        img_back = findViewById(R.id.img_back);

        txt_job_id = findViewById(R.id.txt_job_id);
        txt_building_name = findViewById(R.id.txt_building_name);

        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}