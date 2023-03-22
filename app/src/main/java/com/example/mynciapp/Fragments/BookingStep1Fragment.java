package com.example.mynciapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Interface.IAllRoomsLoadListener;
import com.example.mynciapp.Interface.IRoomSizeLoadListener;
import com.example.mynciapp.Model.RoomSize;
import com.example.mynciapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;




public class BookingStep1Fragment extends Fragment implements IAllRoomsLoadListener, IRoomSizeLoadListener {

    //variable
    CollectionReference allRoomsRef;
    CollectionReference branchRef;

    IAllRoomsLoadListener iAllRoomsLoadListener;
    IRoomSizeLoadListener iRoomSizeLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_rooms)
    RecyclerView recycler_rooms;

    Unbinder unbinder;

    AlertDialog dialog;


    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if(instance==null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allRoomsRef = FirebaseFirestore.getInstance().collection("AllRooms");
        iAllRoomsLoadListener = this;
        iRoomSizeLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();

    }//OnCreate

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);



        View itemView = inflater.inflate(R.layout.fragment_booking_step1, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        loadAllRooms();
        return itemView;

    }//onCreateView

    private void loadAllRooms() {
        allRoomsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    list.add("Please select a Room Size (all?-Frag1) ");
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllRoomsLoadListener.onAllRoomsLoadSuccess(list);
                } //ifStatement
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllRoomsLoadListener.onAllRoomsLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllRoomsLoadSuccess(List<String> roomSizeList) {
        spinner.setItems(roomSizeList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0){
                    loadRoomsofSelectedSize(item.toString());
                }
            }
        });

    }

    private void loadRoomsofSelectedSize(String selectedSize) {

    }

    @Override
    public void onAllRoomsLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoomSizeLoadSuccess(List<RoomSize> sizeList) {

    }

    @Override
    public void onRoomSizeLoadFailed(String message) {

    }
}
