package com.example.polyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.polyapp.edt.DBManager;
import com.example.polyapp.edt.UserManager;
import com.example.polyapp.edt.UserStruct;

import java.util.List;

public class MyFriendsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView; // the class RecyclerView is Android defined
    private FriendListAdapter mFriendListAdapter; // the class SensorListAdapter  must be written by you
    public static final String EXTRA_MESSAGE = "com.example.polyapp";
    private DBManager db;
    private UserManager users;
    private List<UserStruct> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        db = new DBManager(this);
        db.open();
        users = new UserManager(db);
        friendsList = users.getFriends();
        //  get the DB instead List<Sensor> sensorList  =      mSensorManager.getSensorList(Sensor.TYPE_ALL);
        // List<UserStruct> friendList =
        // use byte array for custom types
        for (UserStruct currentFriend : friendsList ) {
            Log.d("sensor",currentFriend.last_name);
            Log.d("sensor",currentFriend.first_name);
            Log.d("sensor",currentFriend.getPromoName());
        }
        // Create an adapter and supply the data to be displayed.
        mFriendListAdapter = new FriendListAdapter(this, friendsList); //constructor of your SensorListAdapter class
        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.IDrecyclerView);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mFriendListAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void myFunction(View view){ // when we click on the "List of sensors"
        //Log.d("sensor","myfunction");
        Intent intent = new Intent(this,MyDisplayFriend.class); // from this context to the new activity
        //String message = "Hello";
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}