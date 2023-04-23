package com.example.mynciapp.BookingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;



public class Fragment2Booking extends Fragment {

    private RadioGroup bookingReasonRadioGroup;
    private RadioButton meetingRadioButton;
    private RadioButton studyRadioButton;
    private MaterialCalendarView calendarView;
    private RecyclerView timeslotRecyclerView;
    private Button previousButton;
    private Button nextButton;

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

        return view;

    }
}
