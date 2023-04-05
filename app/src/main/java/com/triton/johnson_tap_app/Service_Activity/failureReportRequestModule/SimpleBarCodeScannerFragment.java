package com.triton.johnson_tap_app.Service_Activity.failureReportRequestModule;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.barcode.Barcode;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.interfaces.OnScannerDataListener;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class SimpleBarCodeScannerFragment extends Fragment implements BarcodeReader.BarcodeReaderListener {
    private String TAG = SimpleBarCodeScannerFragment.class.getSimpleName();
    private OnScannerDataListener onScannerDataListener;
    private BarcodeReader barcode_fragment;

    public SimpleBarCodeScannerFragment() {

    }

    public SimpleBarCodeScannerFragment(OnScannerDataListener onScannerDataListener) {
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

        barcode_fragment = (BarcodeReader) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);

        return view;
    }

    @Override
    public void onScanned(Barcode barcode) {
        Log.i(TAG, "onScanned: " + barcode.displayValue);
        barcode_fragment.playBeep();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.i(TAG, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(TAG, "onScanError: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}