package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnScannerDataListener;

public class BarCodeScannerFragment extends Fragment implements View.OnClickListener {

    private String TAG = BarCodeScannerFragment.class.getSimpleName();
    private TextView txt_bar_code;
    private ImageView img_back;
    private OnScannerDataListener onScannerDataListener;

    public BarCodeScannerFragment() {
        // Required empty public constructor
    }

    public BarCodeScannerFragment(OnScannerDataListener onScannerDataListener) {
        this.onScannerDataListener = onScannerDataListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_code_scanner, container, false);

        img_back = view.findViewById(R.id.img_back);

        txt_bar_code = view.findViewById(R.id.txt_bar_code);
        img_back.setOnClickListener(this);

        return view;
    }

    private void ErrorMsgDialog(String strMsg) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        txt_Message.setText(strMsg);

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);

        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            break;
        }
    }

}