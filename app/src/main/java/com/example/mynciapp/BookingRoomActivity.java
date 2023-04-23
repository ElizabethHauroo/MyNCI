package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

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
    private NonSwipeViewPager viewPager;
    private StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room);

        viewPager = findViewById(R.id.view_pager_booking);
        stepView = findViewById(R.id.step_view_booking);
        setupViewPager();




    } // onCreate

    private void setupViewPager() {
        Fragment1Booking fragment1 = new Fragment1Booking();
        Fragment2Booking fragment2 = new Fragment2Booking();
        Fragment3Booking fragment3 = new Fragment3Booking();

        fragment2.setOnTimeslotSelectedListener(new Fragment2Booking.OnTimeslotSelectedListener() {
            @Override
            public void onTimeslotSelected(TimeslotBooking selectedTimeslot, BookingReason bookingReason) {
                onTimeSlotAndBookingReasonSelected(selectedTimeslot, bookingReason);
            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                stepView.go(position, true);
            }
        });

        stepView.setStepsNumber(3);
        stepView.go(0, true);
    }


    public void onRoomSelected(RoomBooking room) {
        selectedRoom = room;
        /* Navigate to the next fragment (Step 2)
        Fragment2Booking fragment2= new Fragment2Booking();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment2).addToBackStack(null).commit(); */
        viewPager.setCurrentItem(1);

    }

    public void onTimeSlotAndBookingReasonSelected(TimeslotBooking timeSlot, BookingReason bookingReason) {
        selectedTimeSlot = timeSlot;
        selectedBookingReason = bookingReason;

        Fragment3Booking fragment3 = new Fragment3Booking();

        Bundle args = new Bundle();
        args.putSerializable("selectedTimeslot", selectedTimeSlot);
        args.putSerializable("bookingReason", selectedBookingReason);
        fragment3.setArguments(args);

        /* Navigate to the next fragment (Step 3)
        getSupportFragmentManager().beginTransaction().replace(R.id.for_booking_frags, fragment3).addToBackStack(null).commit(); */
        viewPager.setCurrentItem(2);
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