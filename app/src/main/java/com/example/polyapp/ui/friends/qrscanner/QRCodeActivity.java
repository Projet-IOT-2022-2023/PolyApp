package com.example.polyapp.ui.friends.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polyapp.R;
import com.example.polyapp.edt.DBManager;
import com.example.polyapp.edt.UserManager;

public class QRCodeActivity extends AppCompatActivity implements ScanResultReceiver {

    private TextView txt, formatTxt, contentTxt, name, firstName, cat;
    private Button btn;
    private Button buttonConfirmFriend;
    private Button buttonCancelFriend;
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
        buttonCancelFriend = (Button)findViewById(R.id.buttonCancelFriend);

        txt.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        buttonConfirmFriend.setVisibility(View.INVISIBLE);
        buttonCancelFriend.setVisibility(View.INVISIBLE);
    }
/*
    public String convertToString(String str) {
        String correctedStr = "";
        if(str.equals("A1"))
            correctedStr = "A1";
        else if(str.equals("A1 STI2D"))
            correctedStr = "A1 STI2D";
        else if(str.equals("A2"))
            correctedStr = "A2";
        else if(str.equals("A2 STI2D"))
            correctedStr = "A2 STI2D";
        else if(str.equals("A3 GC"))
            correctedStr = "A3 GC";
        else if(str.equals("A3 ICM"))
            correctedStr = "A3 ICM";
        else if(str.equals("A3 PROD"))
            correctedStr = "A3 PROD";
        else if(str.equals("A3 SB"))
            correctedStr = "A3 SB";
        else if(str.equals("A3 TEAM"))
            correctedStr = "A3 TEAM";
        else if(str.equals("A3 GI FISA"))
            correctedStr = "A3 GI FISA";
        else if(str.equals("A3 GI FISE"))
            correctedStr = "A3 GI FISE";
        else if(str.equals("A4 GC"))
            correctedStr = "A4 GC";
        else if(str.equals("A4 GPSE"))
            correctedStr = "A4 GPSE";
        else if(str.equals("A4 ICM"))
            correctedStr = "A4 ICM";
        else if(str.equals("A4 PROD"))
            correctedStr = "A4 PROD";
        else if(str.equals("A4 SB"))
            correctedStr = "A4 SB";
        else if(str.equals("A4 TEAM"))
            correctedStr = "A4 TEAM";
        else if(str.equals("A4 GI FISA"))
            correctedStr = "A4 GI FISA";
        else if(str.equals("A4 GI FISE"))
            correctedStr = "A4 GI FISE";
        else if(str.equals("A5 GC"))
            correctedStr = "A5 GC";
        else if(str.equals("A5 GPSE"))
            correctedStr = "A5 GPSE";
        else if(str.equals("A5 ICM"))
            correctedStr = "A5 ICM";
        else if(str.equals("A5 PROD"))
            correctedStr = "A5 PROD";
        else if(str.equals("A5 SB"))
            correctedStr = "A5 SB";
        else if(str.equals("A5 TEAM"))
            correctedStr = "A5 TEAM";
        else if(str.equals("A5 GI FISA"))
            correctedStr = "A5 GI FISA";
        else if(str.equals("A5 GI FISE"))
            correctedStr = "A5 GI FISE";
        else if(str.equals("DU IoT"))
            correctedStr = "DU IoT";
        else if(str.equals("Master AESM"))
            correctedStr = "Master AESM";

        return correctedStr;
    }*/

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
        //users.createUserByPromoName(dataSeparated[0], dataSeparated[1], dataSeparated[2]);
        //users.createUserByPromoName("Polaris", "Limbo", "A3 GPSE");
        String name = (String) dataSeparated[0];
        String fname = (String) dataSeparated[1];
        String promo = (String) dataSeparated[2];
        users.createUserByPromoName(name, fname, promo);
        QRCodeActivity.super.onBackPressed();
    }

    public void cancelFriend(View view){
        QRCodeActivity.super.onBackPressed();
    }

    @Override
    public void scanResultData(String codeFormat, String codeContent){
        // display it on screen
        data = codeContent;
        dataSeparated = data.split(";");
        //formatTxt.setText("FORMAT: " + codeFormat);
        //contentTxt.setText("CONTENT: " + codeContent);
        name.setText("Nom : " + dataSeparated[0]);
        firstName.setText("Prénom : " + dataSeparated[1]);
        cat.setText("Promo : " + dataSeparated[2]);
        txt.setVisibility(View.VISIBLE);
        btn.setVisibility(View.INVISIBLE);
        img.setVisibility(View.VISIBLE);
        buttonConfirmFriend.setVisibility(View.VISIBLE);
        buttonCancelFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}