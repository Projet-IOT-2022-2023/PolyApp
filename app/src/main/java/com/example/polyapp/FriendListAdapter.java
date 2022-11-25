package com.example.polyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polyapp.edt.EDTActivity;
import com.example.polyapp.edt.UserStruct;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private static List<UserStruct> mSensorList;
    private final LayoutInflater mInflater;
    public static final String EXTRA_MESSAGE = "com.example.myrecyclerview";
    // constructor which get the list have generated in your main class
    public FriendListAdapter(Context context, List<UserStruct> friendsList) {
        mInflater = LayoutInflater.from(context);
        this.mSensorList = friendsList;
    }

    // make the link between the layout design and your code through the adapter
    public static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView sensorItemView;
        public final TextView typeItemView;
        public final TextView vendorItemView;

        final FriendListAdapter mAdapter;

        public FriendViewHolder(View itemView, FriendListAdapter adapter) {
            super(itemView);
            sensorItemView = itemView.findViewById(R.id.sensor);
            typeItemView = itemView.findViewById(R.id.type);
            vendorItemView = itemView.findViewById(R.id.vendor);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition =  getAdapterPosition();
            // Use that to access the affected item in mWordList.
            UserStruct sensorClicked = mSensorList.get(mPosition);

            int resourceID = sensorClicked.getPromoID();

            Log.d("sensor position", String.valueOf(mPosition));

            Intent intent = new Intent(v.getContext(), EDTActivity.class);
            Bundle b = new Bundle();
            b.putInt("promoID", resourceID); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            v.getContext().startActivity(intent);

            //          Intent intent = new Intent (v.getContext(),MyDisplayFriend.class);// the trick : indicates v.getContext()...
            //String message = String.valueOf(sensorClicked.getType());
            //intent.putExtra(EXTRA_MESSAGE, message);
            //          v.getContext().startActivity(intent);// the trick : indicates v.getContext()...
        }


    }





    @Override // create each view of the the list
    public FriendListAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.activity_friend_list_adapter,parent, false);
        return new FriendViewHolder(mItemView,this);

    }

    @Override //populate the view
    public void onBindViewHolder( FriendListAdapter.FriendViewHolder holder, int position) {
        String mCurrent;

        /*
        for (UserStruct currentFriend : mSensorList ) {
            mCurrent = currentFriend.last_name;
            holder.sensorItemView.setText(mCurrent);
            mCurrent = currentFriend.first_name;
            holder.typeItemView.setText(mCurrent);
            mCurrent = currentFriend.getPromoName();
            holder.vendorItemView.setText(mCurrent);
        }*/

        mCurrent = mSensorList.get(position).last_name;
        holder.sensorItemView.setText(mCurrent);
        mCurrent =  mSensorList.get(position).first_name;
        holder.typeItemView.setText(mCurrent);
        mCurrent =  mSensorList.get(position).getPromoName();
        holder.vendorItemView.setText(mCurrent);

        /*
        mCurrent= mSensorList.get(position).getName();
        holder.sensorItemView.setText(mCurrent);
        mCurrent= "Type :"+mSensorList.get(position).getType();
        holder.typeItemView.setText(mCurrent);
        mCurrent= "Vendor :"+mSensorList.get(position).getVendor();
        holder.vendorItemView.setText(mCurrent);
        mCurrent= "Resolution :"+mSensorList.get(position).getResolution();
        holder.resolutionItemView.setText(mCurrent);
        mCurrent= "Power :"+mSensorList.get(position).getPower();
        holder.powerItemView.setText(mCurrent);
        mCurrent= "Version :"+mSensorList.get(position).getVersion();
        holder.versionItemView.setText(mCurrent);
        */


    }

    @Override //get the list size
    public int getItemCount() {
        return mSensorList.size();
    }
}