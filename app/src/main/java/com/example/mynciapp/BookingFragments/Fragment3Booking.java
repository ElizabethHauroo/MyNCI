package com.example.mynciapp.BookingFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynciapp.BookingModels.BookingReason;
import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.BookingModels.TimeslotBooking;
import com.example.mynciapp.HomeActivity;
import com.example.mynciapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fragment3Booking extends Fragment {

    private TextView frag3UserNameTxt;
    private TextView frag3BookingTimeTxt;
    private TextView frag3BookingReasonTxt;
    private TextView frag3RoomSize;
    private TextView frag3RoomNumber;
    private Button frag3BtnConfirm;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private TimeslotBooking selectedTimeslot;
    private BookingReason bookingReason;
    private RoomBooking selectedRoom;
    private String selectedDateString;
    private String selectedTimeslotString;


    public Fragment3Booking() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_frag_3, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();


        frag3UserNameTxt = view.findViewById(R.id.frag3_user_name_txt);
        frag3BookingTimeTxt = view.findViewById(R.id.frag3_booking_time_txt);
        frag3BookingReasonTxt = view.findViewById(R.id.frag3_booking_reason_txt);
        frag3RoomSize = view.findViewById(R.id.frag3_roomSize);
        frag3RoomNumber = view.findViewById(R.id.frag3_roomNumber);
        frag3BtnConfirm = view.findViewById(R.id.frag3_btn_confirm);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String firstName = snapshot.child("firstname").getValue(String.class);
                final String lastName = snapshot.child("lastname").getValue(String.class);
                String fullName = firstName + " "+lastName;
                Log.d("Fragment3Booking", "Retrieved full name: " + fullName);

                if (getArguments() != null && getArguments().containsKey("selectedTimeslot")) {
                    initializeData();

                    //String timeSlotFormatted = String.format("%s on %s", selectedTimeslot.getBookingTime(), selectedTimeslot.getBookingDate());
                    String timeSlotFormatted = String.format("%s \n%s", selectedTimeslot.getBookingTime(), selectedDateString);
                    String bookingReasonText = bookingReason.name();

                    // Display the gathered information
                    frag3UserNameTxt.setText(fullName);
                    frag3BookingTimeTxt.setText(timeSlotFormatted);
                    frag3BookingReasonTxt.setText(bookingReasonText);
                    frag3RoomSize.setText(selectedRoom.getSize());
                    frag3RoomNumber.setText(selectedRoom.getRoomNumber());
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        frag3BtnConfirm.setOnClickListener(v -> {
            if (selectedTimeslot != null && bookingReason != null && selectedRoom != null) {
                String bookingID = UUID.randomUUID().toString();

                String roomID = UUID.randomUUID().toString();
                Integer slotNumber = selectedTimeslot.getSlotNumber();

                if (roomID != null ) {
                    if (firestore != null) {
                        Map<String, Object> bookingData = new HashMap<>();
                        bookingData.put("bookingID", bookingID);
                        bookingData.put("currentUserName", frag3UserNameTxt.getText().toString());
                        bookingData.put("room_number", frag3RoomNumber.getText().toString());
                        //bookingData.put("timeslot", selectedTimeslot.getBookingTime());
                        bookingData.put("BookingReason", bookingReason.name());
                        bookingData.put("roomSize", frag3RoomSize.getText().toString());
                        bookingData.put("bookingDate", selectedDateString);
                        //bookingData.put("bookingTime", selectedTimeslotString);
                        //bookingData.put("bookingdate", selectedTimeslot.getBookingDate());
                        bookingData.put("bookingTime", selectedTimeslot.getBookingTime());
                        bookingData.put("userID", currentUserID);

                        firestore.collection("RoomBookings").add(bookingData)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Failed to confirm booking. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                        saveTimeslotToFirebase();
                    } else {
                        Toast.makeText(getActivity(), "Error: Firestore is not initialized. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: Room ID or Slot Number is missing. Please try again.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return view;


    } // onCreate

    private void initializeData() {
        selectedDateString = getArguments().getString("selectedDateString");
        selectedTimeslotString = getArguments().getString("selectedTimeslotString");
        selectedTimeslot = (TimeslotBooking) getArguments().getSerializable("selectedTimeslot");
        bookingReason = (BookingReason) getArguments().getSerializable("bookingReason");
        selectedRoom = (RoomBooking) getArguments().getSerializable("selectedRoom");
    }

    private void saveTimeslotToFirebase() {
        TimeslotBooking selectedTimeslot = (TimeslotBooking) getArguments().getSerializable("selectedTimeslot");
        selectedTimeslot.setBooked(true);
        selectedTimeslot.setBookingReason(bookingReason);


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("BookedTimeslots").document(selectedTimeslot.getTimeslotID())
                .set(selectedTimeslot)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Fragment3Booking", "Timeslot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fragment3Booking", "Error writing timeslot", e);
                    }
                });
    }




}
