package com.example.polyapp.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.polyapp.QRCodeActivity;
import com.example.polyapp.R;
import com.example.polyapp.edt.DBManager;
import com.example.polyapp.edt.Specialities;
import com.example.polyapp.edt.UserManager;

public class HomeFragment extends Fragment {

    //private FragmentHomeBinding binding;
    private String[] list_s;
    private Spinner spin;
    private UserManager users;
    private DBManager db;
    private View view;
    private Button button;
    private TextView textViewStatus;
    private TextView userNameText;
    private TextView userFirstNameText;
    private TextView userPromoText;
    private String userName;
    private String userFirstName;
    private String userPromo;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 69) {
                        Intent t = result.getData();
                        Bundle extras = t.getExtras();
                        Log.d("myTag", extras.toString());


                        if (t != null) {
                            userName = extras.getString("name");
                            userFirstName = extras.getString("firstName");
                            userPromo = extras.getString("promo");
                            userNameText.setText(userName);
                            userFirstNameText.setText(userFirstName);
                            userPromoText.setText(userPromo);
                        }
                    }
                }
            }
    );

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = new DBManager(this.getContext());
        db.open();
        users = new UserManager(db);


/*
        if (users.getMainUser() != null)
        {
            view = inflater.inflate(R.layout.user_connected_gallery, container, false);
            userNameText = (TextView) view.findViewById(R.id.userName);
            userFirstNameText = (TextView) view.findViewById(R.id.userFirstName);
            userPromoText = (TextView) view.findViewById(R.id.userPromo);
            userNameText.setText(users.getMainUser().last_name);
            userFirstNameText.setText(users.getMainUser().first_name);
            userPromoText.setText(users.getMainUser().getPromoName());
        }

        else
        {
            view = inflater.inflate(R.layout.user_not_connected_gallery, container, false);
            button = (Button) view.findViewById(R.id.buttonValidate);
            userNameText = (TextView) view.findViewById(R.id.setUserName);
            userFirstNameText = (TextView) view.findViewById(R.id.setUserFirstName);
            spin = (Spinner) view.findViewById(R.id.setUserPromo);

            Specialities s = new Specialities();
            list_s = new String[s.listSpecialities().length +1];
            list_s[0] = "Tous";
            for (int i=0; i<s.listSpecialities().length;i++) list_s[i+1] = s.listSpecialities()[i];

            //Creating the ArrayAdapter instance having the country list

            ArrayAdapter aa = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item,list_s);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);


        }
*/

        view = inflater.inflate(R.layout.user_connected_gallery, container, false);

        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        button = (Button) view.findViewById(R.id.buttonValidate);
        userNameText = (TextView) view.findViewById(R.id.userName);
        userFirstNameText = (TextView) view.findViewById(R.id.userFirstName);
        userPromoText = (TextView) view.findViewById(R.id.userPromo);

        if (users.getMainUser() != null)
        {
            textViewStatus.setText("Vous êtes");
            userNameText.setText(users.getMainUser().last_name);
            userFirstNameText.setText(users.getMainUser().first_name);
            userPromoText.setText(users.getMainUser().getPromoName());
        }

        else
        {
            textViewStatus.setText("Veuillez saisir vos informations");
            userNameText.setText("");
            userFirstNameText.setText("");
            userPromoText.setText("");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //userName = userNameText.getText().toString();
                    //userFirstName = userFirstNameText.getText().toString();
                    //userPromo = spin.getSelectedItem().toString();
                    //users.createUserByPromoName(userFirstName, userName, userPromo);
                    //Log.d("myTag", users.getAllUsers().toString());

                Intent t = new Intent(getActivity(), HomeEditActivity.class);
                activityLauncher.launch(t);
                //startActivity(t);
            }
        });




        //view = inflater.inflate(R.layout.user_not_connected_gallery, container, false);
        //view = inflater.inflate(R.layout.user_connected_gallery, container, false);


        //view = inflater.inflate(R.layout.user_not_connected_gallery, container, false);
        //view = inflater.inflate(R.layout.user_connected_gallery, container, false);
        //Log.d("AAAAAAAAA", users.getUserById(0).toString());
        //Log.d("Name", users.getMainUser().first_name);
        //Log.d("Last name", users.getMainUser().last_name);
        //Log.d("Promo name", users.getMainUser().getPromoName());



        //View view = inflater.inflate(R.layout.user_not_connected_gallery, container, false);

/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameText.getText().toString();
                userFirstName = userFirstNameText.getText().toString();
                userPromo = spin.getSelectedItem().toString();
                users.createUserByPromoName(userFirstName, userName, userPromo);
                //Log.d("myTag", users.getAllUsers().toString());
            }
        });*/
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}