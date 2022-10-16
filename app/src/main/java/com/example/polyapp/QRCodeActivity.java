package com.example.polyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodeActivity extends AppCompatActivity implements ScanResultReceiver {

    private TextView formatTxt, contentTxt, name, surname, age;
    private String data;
    private String[] dataSeparated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        formatTxt = (TextView)findViewById(R.id.textView2);
        contentTxt = (TextView)findViewById(R.id.textView3);
        name = (TextView)findViewById(R.id.textView4);
        surname = (TextView)findViewById(R.id.textView5);
        age = (TextView)findViewById(R.id.textView6);
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
        age.setText("AGE: " + dataSeparated[2]);
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}