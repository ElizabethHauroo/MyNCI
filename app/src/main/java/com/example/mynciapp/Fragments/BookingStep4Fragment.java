package com.example.mynciapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Model.BookingInformation;
import com.example.mynciapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    @BindView(R.id.txt_booking_purpose_text)
    TextView txt_booking_purpose_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @OnClick(R.id.btn_confirm)
    void confirmBooking(){
        getUserInfo();


    }

    private void resetStaticData() {
        // reset every step so that we can make more than one booking.
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentRoom = null;
        Common.currentPurpose = null;
        Common.currentDate.add(Calendar.DATE,0); //current reset
    }





    /*
        private void getUserInfo() {
            if(user != null){
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String firstName = snapshot.child("firstname").getValue(String.class);
                            String lastName = snapshot.child("lastname").getValue(String.class);
                            String course = snapshot.child("course").getValue(String.class);
    
                            //create booking information
                            BookingInformation bookingInformation = new BookingInformation();
    
                            bookingInformation.setPurposeId(Common.currentPurpose.getPurposeId());
                            bookingInformation.setReason(Common.currentPurpose.getReason());
                            bookingInformation.setFirstname(firstName + " " + lastName);
                            bookingInformation.setCourse(course);
                            bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                                    .append(" on ")
                                    .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
                            bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));
    
                            //back to confirmBooking()
                        }
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("BookingStep4Fragment", "Error reading user data from database: " + error.getMessage());
                        Toast.makeText(getContext(), "Error reading user data from database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    */
private void getUserInfo() {
    if (user != null) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String firstName = snapshot.child("firstname").getValue(String.class);
                    String lastName = snapshot.child("lastname").getValue(String.class);
                    String course = snapshot.child("course").getValue(String.class);

                    //create booking information

                        BookingInformation bookingInformation = new BookingInformation();

                        bookingInformation.setPurposeId(Common.currentPurpose.getPurposeId());
                        bookingInformation.setReason(Common.currentPurpose.getReason());
                        bookingInformation.setFirstname(firstName);
                        bookingInformation.setLastname(lastName);
                        bookingInformation.setCourse(course);
                        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                                .append(" on ")
                                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
                        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));


                    //write data in FireBase
                    DocumentReference bookingDate = FirebaseFirestore.getInstance()
                            .collection("AllRooms")
                            .document(Common.size)
                            .collection("Rooms")
                            .document(Common.currentRoom.getRoomId())
                            .collection("Purpose")
                            .document(Common.currentPurpose.getPurposeId())
                            .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                            .document(String.valueOf(Common.currentTimeSlot));  //date simple format = dd_MM_yyyy must be the same in firebase!

                    //confirmBooking()
                    bookingDate.set(bookingInformation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    resetStaticData();
                                    getActivity().finish();
                                    Toast.makeText(getContext(), "Booking Complete!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BookingStep4Fragment", "Error reading user data from database: " + error.getMessage());
                Toast.makeText(getContext(), "Error reading user data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_purpose_text.setText(Common.currentPurpose.getReason());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" on ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));
    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if(instance==null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //applying format for the confirm page
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRM_BOOKING));


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step4, container, false);
        unbinder = ButterKnife.bind(this,itemView);
        return itemView;
    }
}
