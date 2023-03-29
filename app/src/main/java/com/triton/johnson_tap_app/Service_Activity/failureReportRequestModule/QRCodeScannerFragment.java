package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.triton.johnson_tap_app.R;

public class QRCodeScannerFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener, View.OnClickListener {

    private QRCodeReaderView qrCoderView;
    private String TAG = QRCodeScannerFragment.class.getSimpleName();
    private TextView txt_qr_code;
    private ImageView img_back;

    public QRCodeScannerFragment() {
        // Required empty public constructor
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
        qrCoderView.setOnQRCodeReadListener(this);
        qrCoderView.setQRDecodingEnabled(true);
        qrCoderView.setAutofocusInterval(2000L);
        qrCoderView.setTorchEnabled(true);
        qrCoderView.setBackCamera();

        img_back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.i(TAG, "onQRCodeRead: text -> " + text + " points.length -> " + points.length);

        for (PointF pnt : points) {
            Log.i(TAG, "onQRCodeRead: points -> " + pnt);
        }

        txt_qr_code.setText(text);
        qrCoderView.stopCamera();
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