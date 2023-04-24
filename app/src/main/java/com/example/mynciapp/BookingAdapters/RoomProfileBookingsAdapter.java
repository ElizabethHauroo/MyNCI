package com.example.mynciapp.BookingAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.BookingModels.RoomBookingInformation;
import com.example.mynciapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RoomProfileBookingsAdapter extends RecyclerView.Adapter<RoomProfileBookingsAdapter.ViewHolder> {

    private List<RoomBookingInformation> profileBookingList;
    private Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    public RoomProfileBookingsAdapter(List<RoomBookingInformation> profileBookingList, Context context) {
        this.profileBookingList = profileBookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomProfileBookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomProfileBookingsAdapter.ViewHolder holder, int position) {
        RoomBookingInformation booking = profileBookingList.get(position);

        holder.my_bookings_purpose.setText(booking.getBookingReason());
        holder.my_bookings_roomNum.setText(booking.getRoom_number());
        holder.my_bookings_date.setText(booking.getBookingDate());
        holder.my_bookings_time.setText(booking.getBookingTime());


    }

    @Override
    public int getItemCount() {
        return profileBookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView my_bookings_purpose, my_bookings_roomNum, my_bookings_date, my_bookings_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            my_bookings_purpose = itemView.findViewById(R.id.my_bookings_purpose);
            my_bookings_roomNum = itemView.findViewById(R.id.my_bookings_roomNum);
            my_bookings_date = itemView.findViewById(R.id.my_bookings_date);
            my_bookings_time = itemView.findViewById(R.id.my_bookings_time);
        /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup(getAbsoluteAdapterPosition());
                }
            });

         */
        }
    }
}
