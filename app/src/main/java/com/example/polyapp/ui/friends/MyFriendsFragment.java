package com.example.polyapp.ui.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.polyapp.MainActivity;
import com.example.polyapp.ui.friends.qrscanner.QRCodeActivity;
import com.example.polyapp.R;

public class MyFriendsFragment extends Fragment {

    //private UserConnectedGalleryBinding binding;
    private Button buttonAddFriend;
    private Button buttonMyFriends;
    private View view;

    public MyFriendsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Read DB here to handle which window should be open
        if (((MainActivity)getActivity()).getMainUserRegist())
        {
            view = inflater.inflate(R.layout.fragment_my_friends, container, false);
            buttonAddFriend = (Button) view.findViewById(R.id.buttonAddFriend);
            buttonMyFriends = (Button) view.findViewById(R.id.buttonMyFriends);

            buttonAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), QRCodeActivity.class);
                    startActivity(in);
                }
            });

            buttonMyFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), MyFriendsActivity.class);
                    startActivity(in);
                }
            });
        }

        else
        {
            view = inflater.inflate(R.layout.fragment_home_unregistered, container, false);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

}