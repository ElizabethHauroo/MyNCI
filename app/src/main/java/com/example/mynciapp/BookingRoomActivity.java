package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mynciapp.BookingFragments.Fragment1Booking;
import com.example.mynciapp.BookingFragments.Fragment2Booking;
import com.example.mynciapp.BookingFragments.Fragment3Booking;
import com.example.mynciapp.BookingModels.BookingReason;
import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.BookingModels.TimeslotBooking;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;


public class BookingRoomActivity extends AppCompatActivity {

    private RoomBooking selectedRoom;
    private TimeslotBooking selectedTimeSlot;
    private BookingReason selectedBookingReason;
    private StepView stepView;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room);

        stepView = findViewById(R.id.step_view_booking);
        stepView.setStepsNumber(3);
        stepView.go(0, true);

        //--------------------------------


        setupStepView(); //headings for stepview steps
        showFragment1(); // when first loaded, start on frag1

        nav=findViewById(R.id.bottom_navigation_booking);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(BookingRoomActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.book_bottomnav:
                        startActivity(new Intent(BookingRoomActivity.this, AdminActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(BookingRoomActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }
                return true;
            }
        });


    } // onCreate


    private void showFragment1() {
        Fragment1Booking fragment1 = new Fragment1Booking();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment1).commit();
    }

    public void onRoomSelected(RoomBooking room) {
        selectedRoom = room;
        showFragment2();
    }

    private void showFragment2() {
        Fragment2Booking fragment2 = new Fragment2Booking();
        fragment2.setOnTimeslotSelectedListener(new Fragment2Booking.OnTimeslotSelectedListener() {
            @Override
            public void onTimeslotSelected(TimeslotBooking selectedTimeslot, BookingReason bookingReason) {
                onTimeSlotAndBookingReasonSelected(selectedTimeslot, bookingReason);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment2).addToBackStack(null).commit();
        stepView.go(1, true);
    }

    public void onTimeSlotAndBookingReasonSelected(TimeslotBooking timeSlot, BookingReason bookingReason) {
        selectedTimeSlot = timeSlot;
        selectedBookingReason = bookingReason;
        showFragment3();
    }
    private void showFragment3() {
        Fragment3Booking fragment3 = new Fragment3Booking();

        Bundle args = new Bundle();
        args.putSerializable("selectedTimeslot", selectedTimeSlot);
        args.putSerializable("bookingReason", selectedBookingReason);
        args.putSerializable("selectedRoom", selectedRoom);
        fragment3.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment3).addToBackStack(null).commit();
        stepView.go(2, true);
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

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Room");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

}