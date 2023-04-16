package com.example.mynciapp.Interface;

import com.example.mynciapp.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {

    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);

    void onTimeSlotLoadFailed(String message);

    void onTimeSlotLoadEmpty();
}
