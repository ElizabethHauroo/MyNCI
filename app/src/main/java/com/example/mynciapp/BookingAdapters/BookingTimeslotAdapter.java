package com.example.mynciapp.BookingAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.BookingModels.TimeslotBooking;
import com.example.mynciapp.R;

import java.util.List;

public class BookingTimeslotAdapter extends RecyclerView.Adapter<BookingTimeslotAdapter.BookingTimeslotViewHolder>  {

    private List<TimeslotBooking> timeslots;
    private int selectedPosition = -1;
    private OnTimeslotClickListener onTimeslotClickListener;


    public BookingTimeslotAdapter(List<TimeslotBooking> timeslots, OnTimeslotClickListener onTimeslotClickListener) {
        this.timeslots = timeslots;
        this.onTimeslotClickListener = onTimeslotClickListener;
    }

    @NonNull
    @Override
    public BookingTimeslotAdapter.BookingTimeslotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_time_slot, parent, false);
        return new BookingTimeslotViewHolder(view, onTimeslotClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull BookingTimeslotAdapter.BookingTimeslotViewHolder holder, int position) {
        TimeslotBooking timeslot = timeslots.get(holder.getAbsoluteAdapterPosition());
        holder.timeslotTimeText.setText(timeslot.getBookingTime());
        holder.timeslotStatusText.setText(timeslot.isBooked() ? "Already Booked" : " ");

        if(timeslot.isBooked()){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.platinum_grey));
           // holder.timeslotStatusText.setTextColor(R.color.black_olive);
            holder.itemView.setOnClickListener(null);
        }
        else {
            if (selectedPosition == holder.getAbsoluteAdapterPosition()) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_cerulean));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the selected position and notify the adapter
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAbsoluteAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                if ( onTimeslotClickListener != null) {
                    onTimeslotClickListener.onTimeslotClick(holder.getAbsoluteAdapterPosition());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return timeslots.size();
    }

    public class BookingTimeslotViewHolder extends RecyclerView.ViewHolder {

        TextView timeslotTimeText;
        TextView timeslotStatusText;
        OnTimeslotClickListener onTimeslotClickListener;

        public BookingTimeslotViewHolder(@NonNull View itemView, OnTimeslotClickListener onTimeslotClickListener) {
            super(itemView);
            timeslotTimeText = itemView.findViewById(R.id.txt_time_slot);
            timeslotStatusText = itemView.findViewById(R.id.txt_timeslot_description);

            this.onTimeslotClickListener = onTimeslotClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( onTimeslotClickListener != null) {
                        onTimeslotClickListener.onTimeslotClick(getAbsoluteAdapterPosition());
                    }
                }
            });
        }

    }

    public interface OnTimeslotClickListener {
        void onTimeslotClick(int position);
    }
}
