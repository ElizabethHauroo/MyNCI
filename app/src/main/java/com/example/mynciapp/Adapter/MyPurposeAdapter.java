package com.example.mynciapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Model.Purpose;
import com.example.mynciapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class MyPurposeAdapter extends RecyclerView.Adapter<MyPurposeAdapter.MyViewHolder> {

    Context context;
    List<Purpose> purposeList;

    public MyPurposeAdapter(Context context, List<Purpose> purposeList) {
        this.context = context;
        this.purposeList = purposeList;
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
    }

    @Override
    public int getItemCount() {
        return purposeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_reason, txt_purposeDescription;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_reason = (TextView)itemView.findViewById(R.id.purposeString);
            txt_purposeDescription = (TextView)itemView.findViewById(R.id.purposeDescription);
            ratingBar = (RatingBar)itemView.findViewById(R.id.purposeRating);
        }
    }
}
