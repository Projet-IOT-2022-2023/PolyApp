package com.example.polyapp;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanFragment extends Fragment {

    private String codeFormat,codeContent;

    private final String noResultErrorMsg = "No scan data received!";

    private final ActivityResultLauncher<ScanOptions> fragmentLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();
                if(result.getContents() == null) {
                    parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
                } else {
                    codeContent = result.getContents();
                    codeFormat = result.getFormatName();
                    parentActivity.scanResultData(codeFormat,codeContent);
                }
            });

    public ScanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan a QR Code");
        options.setCameraId(0);
        fragmentLauncher.launch(options);
    }
}