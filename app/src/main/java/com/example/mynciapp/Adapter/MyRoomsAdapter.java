package com.example.mynciapp.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Model.RoomSize;
import com.example.mynciapp.R;

import java.util.List;

public class MyRoomsAdapter extends RecyclerView.Adapter<MyRoomsAdapter.MyViewHolder> {

    Context context;
    List<RoomSize> sizeList;

    public MyRoomsAdapter(Context context, List<RoomSize> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
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
        holder.txt_roomNum.setText(sizeList.get(position).getRoomNum());
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_roomNum, txt_floor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_roomNum = (TextView)itemView.findViewById(R.id.roomNum);
            txt_floor = (TextView)itemView.findViewById(R.id.floorNum);

        }
    }
}
