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

    public void scanNow(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scan_fragment,scanFragment);
        fragmentTransaction.commit();
    }

    public void confirmFriend(View view){
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