package com.example.mynciapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Interface.IRecyclerItemSelectedListener;
import com.example.mynciapp.Model.Purpose;
import com.example.mynciapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyPurposeAdapter extends RecyclerView.Adapter<MyPurposeAdapter.MyViewHolder> {

    Context context;
    List<Purpose> purposeList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyPurposeAdapter(Context context, List<Purpose> purposeList) {
        this.context = context;
        this.purposeList = purposeList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_purpose, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_reason.setText(purposeList.get(position).getReason());
        holder.txt_purposeDescription.setText(purposeList.get(position).getDescription());
        holder.ratingBar.setRating((float)purposeList.get(position).getRating());

        if(!cardViewList.contains(holder.card_purpose)){
            cardViewList.add(holder.card_purpose);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background for non-selected items
                for(CardView cardview : cardViewList){
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
                //set background for selected item
                holder.card_purpose.setCardBackgroundColor(context.getResources().getColor(R.color.mikado_yellow));

                //use the broadcast manager to tell ScheduleActivity to enable the "Next" Button
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_PURPOSE_SELECTED,purposeList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return purposeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_reason, txt_purposeDescription;
        RatingBar ratingBar;
        CardView card_purpose;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_reason = (TextView)itemView.findViewById(R.id.purposeString);
            txt_purposeDescription = (TextView)itemView.findViewById(R.id.purposeDescription);
            ratingBar = (RatingBar)itemView.findViewById(R.id.purposeRating);
            card_purpose = (CardView)itemView.findViewById(R.id.card_purpose);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAbsoluteAdapterPosition());
        }
    }
}
