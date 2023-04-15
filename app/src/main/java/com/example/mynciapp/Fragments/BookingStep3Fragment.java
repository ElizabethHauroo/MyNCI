package com.example.mynciapp.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mynciapp.Adapter.MyTimeSlotAdapter;
import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Common.SpacesItemDecoration;
import com.example.mynciapp.Interface.ITimeSlotLoadListener;
import com.example.mynciapp.Model.TimeSlot;
import com.example.mynciapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    //variable
    DocumentReference purposeDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;

    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    //Calendar selected_date;

    @BindView(R.id.recycler_timeslot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0); //today's date
            loadAvailableTimeSlotofPurpose(Common.currentPurpose.getPurposeId(),
                    simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailableTimeSlotofPurpose(String purposeId, String bookDate) {
       // dialog.show();

        // /AllRooms/Large/Rooms/BprMx0J4PcNJBS5TnZKb/Purpose/Dcp6atL6HdBRMIfh2mjj
        //creating a hardcoded booking
        purposeDoc = FirebaseFirestore.getInstance()
                .collection("AllRooms")
                .document(Common.size)
                .collection("Rooms")
                .document(Common.currentRoom.getRoomId())
                .collection("Purpose")
                .document(Common.currentPurpose.getPurposeId());

        //get info
        purposeDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllRooms")
                                .document(Common.size)
                                .collection("Rooms")
                                .document(Common.currentRoom.getRoomId())
                                .collection("Purpose")
                                .document(Common.currentPurpose.getPurposeId())
                                .collection(bookDate);  //date simple format = dd_MM_yyyy must be the same in firebase!
                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty()){ //if there are no bookings
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    }else{
                                        //the room is booked
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot document:task.getResult()){
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        }
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });

    }

    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if(instance==null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        //dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        dialog = new SpotsDialog(getContext(), R.style.SpotsDialog_Cancel);

        //selected_date = Calendar.getInstance();
        //selected_date.add(Calendar.DATE,0); //initialise current date
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step3, container, false);
        unbinder = ButterKnife.bind(this,itemView);

        initItemView();
        return itemView;
    }

    private void initItemView() {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        //calendar

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
                    loadAvailableTimeSlotofPurpose(Common.currentPurpose.getPurposeId(),
                            simpleDateFormat.format(selectedCalendar.getTime()));
                }
            }
        });

/*
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if(selected_date.getTimeInMillis() != date.getTimeInMillis()){
                    selected_date = date;
                    loadAvailableTimeSlotofPurpose((Common.currentPurpose.getPurposeId(),
                            simpleDateFormat.format(date.getTime)));
                }
            }
        });

 */

        // Redundant?
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2); // 2 days left




    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(), timeSlotList);
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }
}
