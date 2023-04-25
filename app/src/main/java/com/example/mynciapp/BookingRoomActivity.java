package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStateManagerControl;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mynciapp.BookingFragments.Fragment1Booking;
import com.example.mynciapp.BookingFragments.Fragment2Booking;
import com.example.mynciapp.BookingFragments.Fragment3Booking;
import com.example.mynciapp.BookingModels.BookingReason;
import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.BookingModels.TimeslotBooking;
import com.example.mynciapp.Common.NonSwipeViewPager;
import com.shuhart.stepview.StepView;


public class BookingRoomActivity extends AppCompatActivity {

    private RoomBooking selectedRoom;
    private TimeslotBooking selectedTimeSlot;
    private BookingReason selectedBookingReason;
   // private NonSwipeViewPager viewPager;
    private StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room);

        //viewPager = findViewById(R.id.view_pager_booking);
        stepView = findViewById(R.id.step_view_booking);
        stepView.setStepsNumber(3);
        stepView.go(0, true);
        //setupViewPager();

        showFragment1();



    } // onCreate


    private void showFragment1() {
        Fragment1Booking fragment1 = new Fragment1Booking();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment1).commit();
    }

    public void onRoomSelected(RoomBooking room) {
        selectedRoom = room;
        showFragment2();
        /* Navigate to the next fragment (Step 2)
        Fragment2Booking fragment2= new Fragment2Booking();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment2).addToBackStack(null).commit(); */
        //viewPager.setCurrentItem(1);
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

}