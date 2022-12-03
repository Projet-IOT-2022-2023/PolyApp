package com.example.polyapp.ui.friends.qrscanner;

import com.example.polyapp.ui.friends.qrscanner.NoScanResultException;

public interface ScanResultReceiver {

    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}