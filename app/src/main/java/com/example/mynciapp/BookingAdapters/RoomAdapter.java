package com.example.mynciapp.BookingAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.BookingModels.RoomBooking;
import com.example.mynciapp.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{

    private List<RoomBooking> rooms;
    private OnRoomClickListener onRoomClickListener;


    public RoomAdapter(List<RoomBooking> rooms, OnRoomClickListener onRoomClickListener) {
        this.rooms = rooms;
        this.onRoomClickListener = onRoomClickListener;
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view, onRoomClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
        RoomBooking room = rooms.get(position);
        holder.roomDescription.setText(room.getDescription());
        holder.roomSize.setText(room.getSize());
        holder.roomNumber.setText(String.valueOf(room.getRoomNumber()));

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView roomDescription, roomSize, roomNumber;
        OnRoomClickListener onRoomClickListener;

        public RoomViewHolder(@NonNull View itemView, OnRoomClickListener onRoomClickListener) {
            super(itemView);
            roomDescription = itemView.findViewById(R.id.roomDescription_room_item);
            roomSize = itemView.findViewById(R.id.roomSize_room_item);
            roomNumber = itemView.findViewById(R.id.roomNumber_room_item);

            this.onRoomClickListener = onRoomClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRoomClickListener.onRoomClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnRoomClickListener {
        void onRoomClick(int position);
    }
}
