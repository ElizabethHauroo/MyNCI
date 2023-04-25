package com.example.mynciapp.BookingAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.BookingModels.AdminBookingInformation;
import com.example.mynciapp.R;

import java.util.List;

public class AdminBookingsAdapter extends RecyclerView.Adapter<AdminBookingsAdapter.ViewHolder>{

    private List<AdminBookingInformation> adminBookingList;
    private Context context;

    public AdminBookingsAdapter(List<AdminBookingInformation> adminBookingList, Context context) {
        this.adminBookingList = adminBookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminBookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_booking_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBookingsAdapter.ViewHolder holder, int position) {
        AdminBookingInformation booking = adminBookingList.get(position);

        holder.roomAdminBookingTxt.setText(booking.getRoom_number());
        holder.timeslotAdminBookingTxt.setText(booking.getBookingTime());
        holder.reasonAdminBookingTxt.setText(booking.getBookingReason());
        holder.nameUserAdminBookingTxt.setText(booking.getCurrentUserName());

    }

    @Override
    public int getItemCount() {
        return adminBookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView roomAdminBookingTxt;
        TextView timeslotAdminBookingTxt;
        TextView reasonAdminBookingTxt;
        TextView nameUserAdminBookingTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomAdminBookingTxt = itemView.findViewById(R.id.room_admin_booking_txt);
            timeslotAdminBookingTxt = itemView.findViewById(R.id.timeslot_admin_booking_txt);
            reasonAdminBookingTxt = itemView.findViewById(R.id.reason_admin_booking_txt);
            nameUserAdminBookingTxt = itemView.findViewById(R.id.name_user_admin_booking_txt);
        }
    }
}
