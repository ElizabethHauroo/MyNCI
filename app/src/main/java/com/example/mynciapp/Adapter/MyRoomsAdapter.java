package com.example.mynciapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
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
import com.example.mynciapp.Model.RoomSize;
import com.example.mynciapp.R;


import java.util.ArrayList;
import java.util.List;

public class MyRoomsAdapter extends RecyclerView.Adapter<MyRoomsAdapter.MyViewHolder> {

    Context context;
    List<RoomSize> sizeList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyRoomsAdapter(Context context, List<RoomSize> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_rooms, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_floor.setText(sizeList.get(position).getFloor());
        //holder.txt_roomNum.setText(sizeList.get(position).getRoomNum());
        holder.txt_roomNum.setText(sizeList.get(position).getRoom_num());

        if(!cardViewList.contains(holder.card_room)){
            cardViewList.add(holder.card_room);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set thw background colour of card not selected to white.
                for(CardView cardView:cardViewList){
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                }
                //set thw background colour of card selected to purple.
                holder.card_room.setCardBackgroundColor(context.getResources().getColor(R.color.mikado_yellow));

                //use the broadcast manager to tell ScheduleActivity to enable the "Next" Button
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_SIZE_STORE,sizeList.get(pos));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_roomNum, txt_floor;
        CardView card_room;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_room = (CardView)itemView.findViewById(R.id.card_room);
            txt_roomNum = (TextView)itemView.findViewById(R.id.roomNum);
            txt_floor = (TextView)itemView.findViewById(R.id.floorNum);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAbsoluteAdapterPosition());
        }
    }
}
