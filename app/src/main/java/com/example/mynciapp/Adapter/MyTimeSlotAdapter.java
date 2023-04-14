package com.example.mynciapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Interface.IRecyclerItemSelectedListener;
import com.example.mynciapp.Model.TimeSlot;
import com.example.mynciapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;


    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
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

                    // setting a tag for all booked slots, then we can set the rest of the available slots to be clickable
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);

                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.platinum_grey));
                    holder.txt_timeslot_description.setText("Unavailable");
                    holder.txt_timeslot_description.setTextColor(context.getResources().getColor(R.color.imperial_red));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(R.color.black_olive));
                }
            }
        }

        // Add all cards to the list
        if(!cardViewList.contains(holder.card_time_slot)){
            cardViewList.add(holder.card_time_slot);
        }
        //check if time slot is free

            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void onItemSelectedListener(View view, int pos) {
                    for(CardView cardView:cardViewList){
                        if(cardView.getTag() == null){
                            cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                        }
                    }
                    // Selected Card (touched)
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.mikado_yellow));
                    //now that a card is selected, send broadcast to enable "next" button
                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                    intent.putExtra(Common.KEY_TIME_SLOT, pos); // i or position? final?
                    intent.putExtra(Common.KEY_STEP,3); //go to step 4 (index 3)
                    localBroadcastManager.sendBroadcast(intent);
                }
            });

        /*holder.setiRecyclerItemSelectedListener(((view, pos) -> {

            }));
        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView:cardViewList){
                    if(cardView.getTag() == null){
                        cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                    }
                }
                // Selected Card (touched)
                holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.mikado_yellow));
                //now that a card is selected, send broadcast to enable "next" button
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_TIME_SLOT, pos); // i or position? final?
                intent.putExtra(Common.KEY_STEP,3); //go to step 4 (index 3)
                localBroadcastManager.sendBroadcast(intent);
            }
        });  */
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_timeslot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_timeslot_description = (TextView)itemView.findViewById(R.id.txt_timeslot_description);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAbsoluteAdapterPosition());
        }
    }
}
