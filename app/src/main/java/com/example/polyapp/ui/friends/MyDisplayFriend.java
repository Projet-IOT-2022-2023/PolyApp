package com.example.polyapp.ui.friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.polyapp.R;

public class MyDisplayFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_display_friend);
        Intent intent = getIntent();
        //String message = intent.getStringExtra(MyFriendsActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.friendName);
        //textView.setText(message);
    }
}