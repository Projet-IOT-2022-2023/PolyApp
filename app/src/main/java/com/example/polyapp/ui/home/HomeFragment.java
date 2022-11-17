package com.example.polyapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.polyapp.QRCodeActivity;
import com.example.polyapp.R;
import com.example.polyapp.databinding.FragmentHomeBinding;
import com.example.polyapp.edt.Specialities;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String[] list_s;
    private Spinner spin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_not_connected_gallery, container, false);
        Button button = (Button) view.findViewById(R.id.button_add_friend);
        spin = (Spinner) view.findViewById(R.id.spinner);

        Specialities s = new Specialities();
        list_s = new String[s.listSpecialities().length +1];
        list_s[0] = "Tous";
        for (int i=0; i<s.listSpecialities().length;i++) list_s[i+1] = s.listSpecialities()[i];

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list_s);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}