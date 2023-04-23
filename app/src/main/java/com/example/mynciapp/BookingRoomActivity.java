package com.example.mynciapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.mynciapp.BookingFragments.Fragment1Booking;
import com.example.mynciapp.BookingFragments.Fragment2Booking;
import com.example.mynciapp.BookingFragments.Fragment3Booking;
import com.example.mynciapp.BookingModels.BookingReason;
import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.BookingModels.TimeslotBooking;


public class BookingRoomActivity extends AppCompatActivity {

    private RoomBooking selectedRoom;
    private TimeslotBooking selectedTimeSlot;
    private BookingReason selectedBookingReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room);

        Fragment1Booking fragment1= new Fragment1Booking();
        getSupportFragmentManager().beginTransaction().add(R.id.for_booking_frags, fragment1).commit();



    } // onCreate

    public void onRoomSelected(RoomBooking room) {
        selectedRoom = room;
        // Navigate to the next fragment (Step 2)
        Fragment2Booking fragment2= new Fragment2Booking();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment2).addToBackStack(null).commit();

    }

    public void onTimeSlotAndBookingReasonSelected(TimeslotBooking timeSlot, BookingReason bookingReason) {
        selectedTimeSlot = timeSlot;
        selectedBookingReason = bookingReason;
        // Navigate to the next fragment (Step 3)
        Fragment3Booking fragment3= new Fragment3Booking();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment3).addToBackStack(null).commit();
    }
    public RoomBooking getSelectedRoom() {
        return selectedRoom;
    }

    public TimeslotBooking getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public BookingReason getSelectedBookingReason() {
        return selectedBookingReason;
    }

}