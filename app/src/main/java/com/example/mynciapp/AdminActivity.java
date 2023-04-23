package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Switch parkingSwitch;
    private DatabaseReference parkingRef;
    private Button admin_logout_btn;

    @Override
    public void onBackPressed() {
        // leaving this blank to disable the back button
        // this means that once admin is logged in, the only page they will ever see is AdminActivity
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth=FirebaseAuth.getInstance();
        parkingSwitch = findViewById(R.id.parking_switch);

        admin_logout_btn = findViewById(R.id.admin_logout_btn);
        admin_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(AdminActivity.this, MainActivity.class)); //send back to login page
            }
        });

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