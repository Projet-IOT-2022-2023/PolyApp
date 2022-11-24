package com.example.polyapp.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.polyapp.R;
import com.example.polyapp.edt.Specialities;

public class HomeEditActivity extends AppCompatActivity {

    private String[] list_s;
    private Spinner spin;
    private Button buttonClear;
    private Button buttonValidate;
    private TextView userNameText;
    private TextView userFirstNameText;
    private TextView userPromoText;
    private String userName;
    private String userFirstName;
    private String userPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_edit);

        buttonClear = (Button) this.findViewById(R.id.buttonClear);
        buttonValidate = (Button) this.findViewById(R.id.buttonEditData);
        userNameText = (TextView) this.findViewById(R.id.setUserName);
        userFirstNameText = (TextView)this.findViewById(R.id.setUserFirstName);
        spin = (Spinner) this.findViewById(R.id.setUserPromo);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Validate();
            }
        });


        Specialities s = new Specialities();
        list_s = new String[s.listSpecialities().length +1];
        list_s[0] = "Tous";
        for (int i=0; i<s.listSpecialities().length;i++) list_s[i+1] = s.listSpecialities()[i];

        //Creating the ArrayAdapter instance having the country list

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list_s);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    public void Clear() {
        userNameText.setText("");
        userFirstNameText.setText("");
    }

    public void Validate() {
        userName = userNameText.getText().toString();
        userFirstName = userFirstNameText.getText().toString();
        userPromo = spin.getSelectedItem().toString();
        Intent t = new Intent();
        Bundle extras = new Bundle();
        extras.putString("name", userName);
        extras.putString("firstName", userFirstName);
        extras.putString("promo", userPromo);
        t.putExtras(extras);
        setResult(69, t);

        HomeEditActivity.super.onBackPressed();
    }


}