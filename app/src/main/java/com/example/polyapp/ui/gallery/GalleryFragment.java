package com.example.polyapp.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.polyapp.QRCodeActivity;
import com.example.polyapp.R;

public class GalleryFragment extends Fragment {

    //private UserConnectedGalleryBinding binding;

    public GalleryFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Read DB here to handle which window should be open

        View view = inflater.inflate(R.layout.user_connected_gallery, container, false);
        Button button = (Button) view.findViewById(R.id.buttonValidate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), QRCodeActivity.class);
                startActivity(in);
            }
        });
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

}