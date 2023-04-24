package com.example.mynciapp.BookingFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.BookingAdapters.BookingTimeslotAdapter;
import com.example.mynciapp.BookingModels.BookingReason;
import com.example.mynciapp.BookingModels.TimeslotBooking;
import com.example.mynciapp.BookingRoomActivity;
import com.example.mynciapp.Common.Common;
import com.example.mynciapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Fragment2Booking extends Fragment {

    private RadioGroup bookingReasonRadioGroup;
    private RadioButton meetingRadioButton;
    private RadioButton studyRadioButton;
    private MaterialCalendarView calendarView;
    private RecyclerView timeslotRecyclerView;
    private Button previousButton;
    private Button nextButton;
    private TimeslotBooking selectedTimeslot;
    private OnTimeslotSelectedListener onTimeslotSelectedListener;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.booking_frag_2, container, false);

        bookingReasonRadioGroup = view.findViewById(R.id.bookingReasonRadioGroup);
        meetingRadioButton = view.findViewById(R.id.meetingRadioButton_frag1);
        studyRadioButton = view.findViewById(R.id.studyRadioButton_frag1);
        calendarView = view.findViewById(R.id.frag2_calendarView);
        timeslotRecyclerView = view.findViewById(R.id.frag2_rv_timeslots);
        previousButton = view.findViewById(R.id.frag2_previousBTN);
        nextButton = view.findViewById(R.id.frag2_nextBTN);

        bookingReasonRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (selectedTimeslot != null) {
                    updateNextButtonState(bookingReasonRadioGroup.getCheckedRadioButtonId() != -1);
                }
            }
        });

        previousButton.setEnabled(true);
        previousButton.setBackgroundColor(getResources().getColor(R.color.royal_purple));
        previousButton.setTextColor(Color.WHITE);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment1Booking fragment1Booking = new Fragment1Booking();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.for_booking_frags, fragment1Booking)
                        .addToBackStack(null)
                        .commit();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimeslotSelectedListener != null && selectedTimeslot != null) {
                    BookingReason bookingReason = meetingRadioButton.isChecked() ? BookingReason.MEETING : BookingReason.STUDY;
                    onTimeslotSelectedListener.onTimeslotSelected(selectedTimeslot, bookingReason);

                    Fragment3Booking fragment3Booking = new Fragment3Booking();

                    Bundle args = new Bundle();
                    args.putSerializable("selectedTimeslot", selectedTimeslot);
                    args.putSerializable("bookingReason", bookingReason);
                    args.putSerializable("selectedRoom", ((BookingRoomActivity) getActivity()).getSelectedRoom());
                    fragment3Booking.setArguments(args);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.for_booking_frags, fragment3Booking)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });


        updateNextButtonState(false);
        initCalendarView();


        return view;

    }

    private void initCalendarView() {
        // RecyclerView configuration
        timeslotRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        timeslotRecyclerView.setLayoutManager(gridLayoutManager);

        List<TimeslotBooking> dailyTimeslots = generateDailyTimeslots();
        BookingTimeslotAdapter timeslotAdapter = new BookingTimeslotAdapter(dailyTimeslots, new BookingTimeslotAdapter.OnTimeslotClickListener() {
            @Override
            public void onTimeslotClick(int position) {
                selectedTimeslot = dailyTimeslots.get(position);
                updateNextButtonState(bookingReasonRadioGroup.getCheckedRadioButtonId() != -1);
            }
        });
        timeslotRecyclerView.setAdapter(timeslotAdapter);

        // Calendar configuration
        int calendarDayHeight = (int) getResources().getDimension(R.dimen.calendar_day_height);
        int calendarHeight = calendarDayHeight * 7; // Show one week only

        // Set the calendar view to today's date
        Calendar calendar = Calendar.getInstance();
        calendarView.setSelectedDate(calendar);

        calendarView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calendarHeight));

        calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(date.getYear(), date.getMonth(), date.getDay());

                if (Common.bookingDate.getTimeInMillis() != selectedCalendar.getTimeInMillis()) {
                    Common.bookingDate = selectedCalendar;
                    String selectedDate = simpleDateFormat.format(selectedCalendar.getTime());
                    updateTimeslotsForSelectedDate(selectedDate);
                }

            }
        });
    }

    private void updateNextButtonState(boolean timeslotSelected) {
        Context context = getContext();
        if (timeslotSelected) {
            nextButton.setEnabled(true);
            nextButton.setBackgroundColor(getResources().getColor(R.color.royal_purple));
            nextButton.setTextColor(Color.WHITE);
        } else {
            nextButton.setEnabled(false);
            nextButton.setBackgroundColor(getResources().getColor(R.color.platinum_grey));
            nextButton.setTextColor(ContextCompat.getColor(context, R.color.battleship_grey));
        }
        Log.d("Fragment2Booking", "onNextButtonClicked called");
    }

    private List<TimeslotBooking> generateDailyTimeslots() {
                List<TimeslotBooking> timeslots = new ArrayList<>();
                String[] timeList = {"09:00 - 10:00", "11:00 - 12:00", "12:00 - 13:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00"};

                for (int i = 0; i < timeList.length; i++) {
                    TimeslotBooking timeslot = new TimeslotBooking();
                    timeslot.setBookingTime(timeList[i]);
                    timeslot.setSlotNumber(i);
                    timeslot.setBooked(false); // You can set this value based on your booking data
                    timeslots.add(timeslot);
                }

                return timeslots;
            }

    private void updateTimeslotsForSelectedDate(String selectedDate) {
        List<TimeslotBooking> dailyTimeslots = generateDailyTimeslots();

        BookingTimeslotAdapter timeslotAdapter = new BookingTimeslotAdapter(dailyTimeslots, new BookingTimeslotAdapter.OnTimeslotClickListener() {
            @Override
            public void onTimeslotClick(int position) {
                // Update the state of the next button when a time slot is clicked
                updateNextButtonState(true);
            }
        });
        timeslotRecyclerView.setAdapter(timeslotAdapter);
    }

    public interface OnTimeslotSelectedListener {
        void onTimeslotSelected(TimeslotBooking selectedTimeslot, BookingReason bookingReason);
    }

    public void setOnTimeslotSelectedListener(OnTimeslotSelectedListener listener) {
        this.onTimeslotSelectedListener = listener;
    }


}
