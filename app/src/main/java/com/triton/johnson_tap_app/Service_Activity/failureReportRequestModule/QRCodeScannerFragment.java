package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import static com.triton.johnson_tap_app.utils.CommonFunction.isValidQRCode;
import static com.triton.johnson_tap_app.utils.CommonFunction.nullPointerValidator;

import android.app.AlertDialog;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnScannerDataListener;

public class QRCodeScannerFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener, View.OnClickListener {

    private QRCodeReaderView qrCoderView;
    private String TAG = QRCodeScannerFragment.class.getSimpleName();
    private TextView txt_qr_code;
    private ImageView img_back;
    private OnScannerDataListener onScannerDataListener;

    public QRCodeScannerFragment() {
        // Required empty public constructor
    }

    public QRCodeScannerFragment(OnScannerDataListener onScannerDataListener) {
        this.onScannerDataListener = onScannerDataListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code_scanner, container, false);

        img_back = view.findViewById(R.id.img_back);

        txt_qr_code = view.findViewById(R.id.txt_qr_code);

        qrCoderView = view.findViewById(R.id.qrCoderView);

        img_back.setOnClickListener(this);

        qrCoderView.setOnQRCodeReadListener(this);
        qrCoderView.setQRDecodingEnabled(true);
        qrCoderView.setAutofocusInterval(2000L);
        qrCoderView.setTorchEnabled(true);
        qrCoderView.setBackCamera();

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
                qrCoderView.startCamera();
            }
        });
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.i(TAG, "onQRCodeRead: text -> " + text);

        qrCoderView.stopCamera();
        if (nullPointerValidator(text) && isValidQRCode(text)) {
            txt_qr_code.setText(text);
            onScannerDataListener.scannerDataListener("qr_scanner", text);
            img_back.performClick();
        } else {
            ErrorMsgDialog("Invalid QR Code.\nTry Again");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                getActivity().onBackPressed();
//                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            break;
        }
    }

}