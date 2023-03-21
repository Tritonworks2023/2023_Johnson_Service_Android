package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.triton.johnson_tap_app.R;

public class FailureReportRequestScannerActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView img_back;
    private Button btn_qr_scan, btn_barcode_scan, btn_submit;
    private EditText edt_qr_scan, edt_barcode_number, edt_job_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_failure_report_request_scanner);

        context = this;

        img_back = findViewById(R.id.img_back);

        btn_qr_scan = findViewById(R.id.btn_qr_scan);
        btn_barcode_scan = findViewById(R.id.btn_barcode_scan);
        btn_submit = findViewById(R.id.btn_submit);

        edt_qr_scan = findViewById(R.id.edt_qr_scan);
        edt_barcode_number = findViewById(R.id.edt_barcode_number);
        edt_job_num = findViewById(R.id.edt_job_num);

        img_back.setOnClickListener(this);

        btn_qr_scan.setOnClickListener(this);
        btn_barcode_scan.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                onBackPressed();
            }
            break;
            case R.id.btn_qr_scan: {

            }
            break;
            case R.id.btn_barcode_scan: {

            }
            break;
            case R.id.btn_submit: {
                Intent intent = new Intent(context, FailureReportRequestFormActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
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