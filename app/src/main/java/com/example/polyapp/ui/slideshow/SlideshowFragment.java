package com.example.polyapp.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.polyapp.MainActivity;
import com.example.polyapp.R;
import com.example.polyapp.bluetooth.BluetoothActivity;
//import com.example.polyapp.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    //private FragmentSlideshowBinding binding;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (((MainActivity)getActivity()).getMainUserRegist())
        {
            view = inflater.inflate(R.layout.fragment_slideshow, container, false);

            Button button = (Button) view.findViewById(R.id.buttonAddFriend);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), BluetoothActivity.class);
                    startActivity(in);
                }
            });
        }

        else
            view = inflater.inflate(R.layout.fragment_not_registered, container, false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}