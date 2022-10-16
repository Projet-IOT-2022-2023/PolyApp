package com.example.polyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodeActivity extends AppCompatActivity implements ScanResultReceiver {

    private TextView txt, formatTxt, contentTxt, name, surname, cat;
    private String data;
    private String[] dataSeparated;
    private RemoteViews remoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        txt = (TextView)findViewById(R.id.textView10);
        formatTxt = (TextView)findViewById(R.id.textView2);
        contentTxt = (TextView)findViewById(R.id.textView3);
        name = (TextView)findViewById(R.id.textView4);
        surname = (TextView)findViewById(R.id.textView5);
        cat = (TextView)findViewById(R.id.textView6);

        txt.setVisibility(View.INVISIBLE);
    }

    public void scanNow(View view){
        // add fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scan_fragment,scanFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void scanResultData(String codeFormat, String codeContent){
        // display it on screen
        data = codeContent;
        dataSeparated = data.split(";");
        formatTxt.setText("FORMAT: " + codeFormat);
        contentTxt.setText("CONTENT: " + codeContent);
        name.setText("NAME: " + dataSeparated[0]);
        surname.setText("SURNAME: " + dataSeparated[1]);
        cat.setText("AGE: " + dataSeparated[2]);
        txt.setVisibility(View.VISIBLE);
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}