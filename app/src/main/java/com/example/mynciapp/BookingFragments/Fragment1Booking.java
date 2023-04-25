package com.example.mynciapp.BookingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.BookingAdapters.RoomAdapter;
import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.BookingRoomActivity;
import com.example.mynciapp.Common.SpacesItemDecoration;
import com.example.mynciapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment1Booking extends Fragment {

    private RecyclerView roomsRecyclerView;
    private Button nextButton;
    private RoomBooking selectedRoom;
    private FirebaseFirestore firestore;
    private RoomAdapter roomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_frag_1, container, false);

        roomsRecyclerView = view.findViewById(R.id.roomsRecyclerView_frag1);
        nextButton = view.findViewById(R.id.nextBTN_frag1);
        firestore = FirebaseFirestore.getInstance();

        initView();
        fetchRooms();

        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRoom != null) {
                    ((BookingRoomActivity) getActivity()).onRoomSelected(selectedRoom);
                }
            }
        });

        return view;
    }//onCreate

    private void initView() {

        roomsRecyclerView.setHasFixedSize(true);
        roomsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        roomsRecyclerView.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void fetchRooms() {
        firestore.collection("BookableRooms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<RoomBooking> rooms = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        RoomBooking room = document.toObject(RoomBooking.class);
                        rooms.add(room);
                    }
                    setupRecyclerView(rooms);
                } else {
                    // Handle any errors here
                }
            }
        });
    }

    private void setupRecyclerView(List<RoomBooking> rooms) {
        RoomAdapter.OnRoomClickListener roomClickListener = new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(int position) {

                selectedRoom = rooms.get(position);
                nextButton.setEnabled(true);
            }
        };

        roomAdapter = new RoomAdapter(rooms, roomClickListener);
        roomsRecyclerView.setAdapter(roomAdapter);
    }

}
