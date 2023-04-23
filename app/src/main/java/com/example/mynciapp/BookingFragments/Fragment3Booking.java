package com.example.mynciapp.BookingFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mynciapp.BookingModels.BookingReason;
import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.BookingModels.TimeslotBooking;
import com.example.mynciapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fragment3Booking extends Fragment {

    private TextView frag3UserNameTxt;
    private TextView frag3BookingTimeTxt;
    private TextView frag3BookingReasonTxt;
    private TextView frag3RoomSize;
    private TextView frag3RoomNumber;
    private Button frag3BtnConfirm;
    private FirebaseAuth mAuth;

    public Fragment3Booking() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booking_frag_3, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        getCurrentUserInfo();

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

                if (getArguments() != null) {
                    TimeslotBooking selectedTimeslot = (TimeslotBooking) getArguments().getSerializable("selectedTimeslot");
                    BookingReason bookingReason = (BookingReason) getArguments().getSerializable("bookingReason");
                    RoomBooking selectedRoom = (RoomBooking) getArguments().getSerializable("selectedRoom");
                    String timeSlotFormatted = String.format("%s on %s", selectedTimeslot.getBookingTime(), selectedTimeslot.getBookingDate());
                    String bookingReasonText = bookingReason.name();

                    Log.d("Fragment3Booking", "Selected timeslot: " + selectedTimeslot);
                    Log.d("Fragment3Booking", "Selected booking reason: " + bookingReason);
                    Log.d("Fragment3Booking", "Selected room: " + selectedRoom);

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




    }

    private void getCurrentUserInfo() {

    }

}
