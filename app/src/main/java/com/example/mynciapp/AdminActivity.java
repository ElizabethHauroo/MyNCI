package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    private Switch parkingSwitch;
    private TextView parkingStatus;
    private DatabaseReference parkingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        parkingSwitch = findViewById(R.id.parking_switch);
        parkingStatus = findViewById(R.id.bookingTitle_admin);

        parkingRef = FirebaseDatabase.getInstance().getReference("parking");

        // grab the parking state from firebase
        parkingRef.child("isFull").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isFull = dataSnapshot.getValue(Boolean.class);
                if (isFull != null && isFull) {
                    parkingSwitch.setChecked(true);
                } else {
                    parkingSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // when the switch is toggled, set it to FULL
        parkingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                parkingRef.child("isFull").setValue(isChecked);
            }
        });






    } //create method



}