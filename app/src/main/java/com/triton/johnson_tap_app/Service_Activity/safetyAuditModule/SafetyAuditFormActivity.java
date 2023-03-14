package com.triton.johnson_tap_app.Service_Activity.safetyAuditModule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.triton.johnson_tap_app.R;

public class SafetyAuditFormActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_jobid, txt_building_name;
    private String strJobNo = "", strBuildName = "", TAG = SafetyAuditFormActivity.class.getSimpleName();
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_safety_audit_form);

        iv_back = findViewById(R.id.iv_back);

        txt_jobid = findViewById(R.id.txt_jobid);
        txt_building_name = findViewById(R.id.txt_building_name);

        iv_back.setOnClickListener(this);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            if (extra.containsKey("job_no")) {
                strJobNo = extra.getString("job_no");
            }
            if (extra.containsKey("build_name")) {
                strBuildName = extra.getString("build_name");
            }
        }

        Log.i(TAG, "onCreate: strJobNo -> " + strJobNo + " strBuildName -> " + strBuildName);

        txt_jobid.setText(strJobNo);
        txt_building_name.setText(strBuildName);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: {
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