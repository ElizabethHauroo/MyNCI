package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mynciapp.BookingAdapters.AdminBookingsAdapter;
import com.example.mynciapp.BookingModels.AdminBookingInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Switch parkingSwitch;
    private DatabaseReference parkingRef;
    private Button admin_logout_btn;

    private RecyclerView rvAdminBookings;
    private AdminBookingsAdapter adminBookingsAdapter;
    private List<AdminBookingInformation> adminBookingList;
    private FirebaseFirestore db;

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

        rvAdminBookings = findViewById(R.id.rv_admin_bookings);
        adminBookingList = new ArrayList<>();
        adminBookingsAdapter = new AdminBookingsAdapter(adminBookingList, this);
        rvAdminBookings.setLayoutManager(new LinearLayoutManager(this));
        rvAdminBookings.setAdapter(adminBookingsAdapter);
        db = FirebaseFirestore.getInstance();

        loadTodaysBookings();

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

    private void loadTodaysBookings() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String todayDate = sdf.format(new Date());

        db.collection("RoomBookings")
                .whereEqualTo("bookingDate", todayDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<AdminBookingInformation> todayBookingsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AdminBookingInformation booking = document.toObject(AdminBookingInformation.class);
                                todayBookingsList.add(booking);
                            }
                            setupRecyclerView(todayBookingsList);
                        } else {
                            Log.d("AdminActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupRecyclerView(List<AdminBookingInformation> todayBookingsList) {
        RecyclerView recyclerView = findViewById(R.id.rv_admin_bookings);
        AdminBookingsAdapter adapter = new AdminBookingsAdapter(todayBookingsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}