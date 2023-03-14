package com.triton.johnson_tap_app.Service_Activity.repairWorkModule;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.triton.johnson_tap_app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JobHazardAnalysisFormActivity extends AppCompatActivity implements View.OnClickListener {

    private static String formattedDate = "";
    String[] yesNoNaArray;
    private ImageView img_back;
    private TextView txt_date, txt_mech_date, txt_eng_date;
    private EditText edt_job_num, edt_branch_name, edt_site_name, edt_nature_work, edt_remark,
            edt_mech_name, edt_mech_emp_num, edt_eng_name, edt_eng_emp_num;
    private Spinner spin_team_aware_stand_repair, spin_comp_conduct, spin_rel_ppes, spin_prop_spec_tools, spin_mech_differ_level,
            spin_barr_main_display, spin_discon_elect_cic, spin_cert_hoist_tool, spin_access_mach_room, spin_light_adeq_machine_room,
            spin_floor_free_tripping, spin_oil_lub_closed, spin_speed_gov_func, spin_host_available, spin_stop_switch_easily_access_iden_car,
            spin_stop_switch_func_ver_car, spin_car_top_insp_func_prop_verif, spin_light_adeq_car_top, spin_car_top_barri_angle_install,
            spin_car_top_phy_dam_cab_wire_work, spin_tools_mater_repair, spin_mech_safety_gear, spin_light_adeq_hoist_way,
            spin_covers_fascia_install, spin_hoist_screen_protect_insta, spin_stop_switch_easily_access_iden_pit, spin_stop_switch_func_ver_pit,
            spin_light_adeq_pit, spin_pit_lad_install_access, spin_cwt_scr_guard_blo_buf_top, spin_dup_shaft_pit_not_done,
            spin_free_wat_oil_mat_trip_hazard;
    private String strDateType = "", TAG = JobHazardAnalysisFormActivity.class.getSimpleName();
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_hazard_analysis_form);

        context = JobHazardAnalysisFormActivity.this;

        img_back = findViewById(R.id.img_back);

        txt_date = findViewById(R.id.txt_date);
        txt_mech_date = findViewById(R.id.txt_mech_date);
        txt_eng_date = findViewById(R.id.txt_eng_date);

        edt_job_num = findViewById(R.id.edt_job_num);
        edt_branch_name = findViewById(R.id.edt_branch_name);
        edt_site_name = findViewById(R.id.edt_site_name);
        edt_nature_work = findViewById(R.id.edt_nature_work);
        edt_remark = findViewById(R.id.edt_remark);
        edt_mech_name = findViewById(R.id.edt_mech_name);
        edt_mech_emp_num = findViewById(R.id.edt_mech_emp_num);
        edt_eng_name = findViewById(R.id.edt_eng_name);
        edt_eng_emp_num = findViewById(R.id.edt_eng_emp_num);

        spin_team_aware_stand_repair = findViewById(R.id.spin_team_aware_stand_repair);
        spin_comp_conduct = findViewById(R.id.spin_comp_conduct);
        spin_rel_ppes = findViewById(R.id.spin_rel_ppes);
        spin_prop_spec_tools = findViewById(R.id.spin_prop_spec_tools);
        spin_mech_differ_level = findViewById(R.id.spin_mech_differ_level);
        spin_barr_main_display = findViewById(R.id.spin_barr_main_display);
        spin_discon_elect_cic = findViewById(R.id.spin_discon_elect_cic);
        spin_cert_hoist_tool = findViewById(R.id.spin_cert_hoist_tool);
        spin_access_mach_room = findViewById(R.id.spin_access_mach_room);
        spin_light_adeq_machine_room = findViewById(R.id.spin_light_adeq_machine_room);
        spin_floor_free_tripping = findViewById(R.id.spin_floor_free_tripping);
        spin_oil_lub_closed = findViewById(R.id.spin_oil_lub_closed);
        spin_speed_gov_func = findViewById(R.id.spin_speed_gov_func);
        spin_host_available = findViewById(R.id.spin_host_available);
        spin_stop_switch_easily_access_iden_car = findViewById(R.id.spin_stop_switch_easily_access_iden_car);
        spin_stop_switch_func_ver_car = findViewById(R.id.spin_stop_switch_func_ver_car);
        spin_car_top_insp_func_prop_verif = findViewById(R.id.spin_car_top_insp_func_prop_verif);
        spin_light_adeq_car_top = findViewById(R.id.spin_light_adeq_car_top);
        spin_car_top_barri_angle_install = findViewById(R.id.spin_car_top_barri_angle_install);
        spin_car_top_phy_dam_cab_wire_work = findViewById(R.id.spin_car_top_phy_dam_cab_wire_work);
        spin_tools_mater_repair = findViewById(R.id.spin_tools_mater_repair);
        spin_mech_safety_gear = findViewById(R.id.spin_mech_safety_gear);
        spin_light_adeq_hoist_way = findViewById(R.id.spin_light_adeq_hoist_way);
        spin_covers_fascia_install = findViewById(R.id.spin_covers_fascia_install);
        spin_hoist_screen_protect_insta = findViewById(R.id.spin_hoist_screen_protect_insta);
        spin_stop_switch_easily_access_iden_pit = findViewById(R.id.spin_stop_switch_easily_access_iden_pit);
        spin_stop_switch_func_ver_pit = findViewById(R.id.spin_stop_switch_func_ver_pit);
        spin_light_adeq_pit = findViewById(R.id.spin_light_adeq_pit);
        spin_pit_lad_install_access = findViewById(R.id.spin_pit_lad_install_access);
        spin_cwt_scr_guard_blo_buf_top = findViewById(R.id.spin_cwt_scr_guard_blo_buf_top);
        spin_dup_shaft_pit_not_done = findViewById(R.id.spin_dup_shaft_pit_not_done);
        spin_free_wat_oil_mat_trip_hazard = findViewById(R.id.spin_free_wat_oil_mat_trip_hazard);

        yesNoNaArray = getResources().getStringArray(R.array.yes_no_na_array);

        ArrayAdapter<String> yesNoNaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, yesNoNaArray);
        yesNoNaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        img_back.setOnClickListener(this);
        txt_date.setOnClickListener(this);
        txt_mech_date.setOnClickListener(this);
        txt_eng_date.setOnClickListener(this);

        spin_team_aware_stand_repair.setAdapter(yesNoNaAdapter);
        spin_comp_conduct.setAdapter(yesNoNaAdapter);
        spin_rel_ppes.setAdapter(yesNoNaAdapter);
        spin_prop_spec_tools.setAdapter(yesNoNaAdapter);
        spin_mech_differ_level.setAdapter(yesNoNaAdapter);
        spin_barr_main_display.setAdapter(yesNoNaAdapter);
        spin_discon_elect_cic.setAdapter(yesNoNaAdapter);
        spin_cert_hoist_tool.setAdapter(yesNoNaAdapter);
        spin_access_mach_room.setAdapter(yesNoNaAdapter);
        spin_light_adeq_machine_room.setAdapter(yesNoNaAdapter);
        spin_floor_free_tripping.setAdapter(yesNoNaAdapter);
        spin_oil_lub_closed.setAdapter(yesNoNaAdapter);
        spin_speed_gov_func.setAdapter(yesNoNaAdapter);
        spin_host_available.setAdapter(yesNoNaAdapter);
        spin_stop_switch_easily_access_iden_car.setAdapter(yesNoNaAdapter);
        spin_stop_switch_func_ver_car.setAdapter(yesNoNaAdapter);
        spin_car_top_insp_func_prop_verif.setAdapter(yesNoNaAdapter);
        spin_light_adeq_car_top.setAdapter(yesNoNaAdapter);
        spin_car_top_barri_angle_install.setAdapter(yesNoNaAdapter);
        spin_car_top_phy_dam_cab_wire_work.setAdapter(yesNoNaAdapter);
        spin_tools_mater_repair.setAdapter(yesNoNaAdapter);
        spin_mech_safety_gear.setAdapter(yesNoNaAdapter);
        spin_light_adeq_hoist_way.setAdapter(yesNoNaAdapter);
        spin_covers_fascia_install.setAdapter(yesNoNaAdapter);
        spin_hoist_screen_protect_insta.setAdapter(yesNoNaAdapter);
        spin_stop_switch_easily_access_iden_pit.setAdapter(yesNoNaAdapter);
        spin_stop_switch_func_ver_pit.setAdapter(yesNoNaAdapter);
        spin_light_adeq_pit.setAdapter(yesNoNaAdapter);
        spin_pit_lad_install_access.setAdapter(yesNoNaAdapter);
        spin_cwt_scr_guard_blo_buf_top.setAdapter(yesNoNaAdapter);
        spin_dup_shaft_pit_not_done.setAdapter(yesNoNaAdapter);
        spin_free_wat_oil_mat_trip_hazard.setAdapter(yesNoNaAdapter);

        getTodayDate();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_date) {
            strDateType = "txt_date";
            callDatePicker();
        } else if (view.getId() == R.id.txt_mech_date) {
            strDateType = "txt_mech_date";
            callDatePicker();
        } else if (view.getId() == R.id.txt_eng_date) {
            strDateType = "txt_eng_date";
            callDatePicker();
        } else if (view.getId() == R.id.img_back) {
            onBackPressed();
        }
    }

    private void callDatePicker() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        setDate(dayOfMonth, monthOfYear, year1), year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void getTodayDate() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        strDateType = "txt_both";
        setDate(day, month, year);
    }

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

        if (strDateType.equalsIgnoreCase("txt_date")) {
            txt_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_mech_date")) {
            txt_mech_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_eng_date")) {
            txt_eng_date.setText(dateTime);
        } else if (strDateType.equalsIgnoreCase("txt_both")) {
            txt_date.setText(dateTime);
            txt_mech_date.setText(dateTime);
            txt_eng_date.setText(dateTime);
        }

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
            formattedDate = str;

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}