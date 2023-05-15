package com.triton.johnson_tap_app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    static final int DATE_PICKER_ID = 1111;
    Button btn;
    DatePickerDialog datepicker;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn = (Button) findViewById(R.id.button);


        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }


}