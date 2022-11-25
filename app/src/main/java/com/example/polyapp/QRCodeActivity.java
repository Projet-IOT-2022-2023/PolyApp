package com.example.polyapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polyapp.edt.DBManager;
import com.example.polyapp.edt.UserManager;
import com.example.polyapp.ui.home.HomeEditActivity;

public class QRCodeActivity extends AppCompatActivity implements ScanResultReceiver {

    private TextView txt, formatTxt, contentTxt, name, firstName, cat;
    private Button btn;
    private Button buttonConfirmFriend;
    private ImageView img;
    private String data;
    private String[] dataSeparated;
    private DBManager db;
    private UserManager users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBManager(this);
        db.open();
        users = new UserManager(db);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        txt = (TextView)findViewById(R.id.textView10);
        formatTxt = (TextView)findViewById(R.id.textView2);
        contentTxt = (TextView)findViewById(R.id.textView3);
        name = (TextView)findViewById(R.id.textView4);
        firstName = (TextView)findViewById(R.id.textView5);
        cat = (TextView)findViewById(R.id.textView6);
        img = (ImageView)findViewById(R.id.imageView3);

        btn = (Button)findViewById(R.id.buttonQRScan);
        buttonConfirmFriend = (Button)findViewById(R.id.buttonConfirmFriend);

        txt.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        buttonConfirmFriend.setVisibility(View.INVISIBLE);

    }

    public void scanNow(View view){
        // add fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scan_fragment,scanFragment);
        fragmentTransaction.commit();
    }

    public void confirmFriend(View view){
        // add fragment
        users.createUserByPromoName(dataSeparated[0], dataSeparated[1], dataSeparated[2]);
        QRCodeActivity.super.onBackPressed();
    }

    @Override
    public void scanResultData(String codeFormat, String codeContent){
        // display it on screen
        data = codeContent;
        dataSeparated = data.split(";");
        formatTxt.setText("FORMAT: " + codeFormat);
        contentTxt.setText("CONTENT: " + codeContent);
        name.setText("Nom : " + dataSeparated[0]);
        firstName.setText("Prénom : " + dataSeparated[1]);
        cat.setText("Promo : " + dataSeparated[2]);
        txt.setVisibility(View.VISIBLE);
        btn.setVisibility(View.INVISIBLE);
        img.setVisibility(View.VISIBLE);
        buttonConfirmFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}