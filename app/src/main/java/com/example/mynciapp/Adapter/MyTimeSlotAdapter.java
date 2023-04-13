package com.example.mynciapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Model.TimeSlot;
import com.example.mynciapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());

        if(timeSlotList.size() == 0){  //if  slot is available show :
            holder.txt_timeslot_description.setText("Available");
            holder.txt_timeslot_description.setTextColor(context.getResources().getColor(R.color.lime_green));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(R.color.black_olive));
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.soft_green));

        }
        else{ //if  slot is not available show :

            for(TimeSlot slotValue:timeSlotList){
                int slot = Integer.parseInt(slotValue.getSlot().toString());

                if(slot == position){
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.platinum_grey));
                    holder.txt_timeslot_description.setText("Unavailable");
                    holder.txt_timeslot_description.setTextColor(context.getResources().getColor(R.color.imperial_red));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(R.color.black_olive));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_time_slot, txt_timeslot_description;
        CardView card_time_slot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_timeslot_description = (TextView)itemView.findViewById(R.id.txt_timeslot_description);
        }


    }
}
