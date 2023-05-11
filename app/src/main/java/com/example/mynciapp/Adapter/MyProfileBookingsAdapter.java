package com.example.mynciapp.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Model.BookingInformation;
import com.example.mynciapp.ProfileActivity;
import com.example.mynciapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyProfileBookingsAdapter extends RecyclerView.Adapter<MyProfileBookingsAdapter.ViewHolder>{

    private List<BookingInformation> profileBookingList;
   // List<CardView> cardViewList;
    private Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();


    public MyProfileBookingsAdapter(List<BookingInformation> profileBookingList, Context context) {
        this.profileBookingList = profileBookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileBookingsAdapter.ViewHolder holder, int position) {

        BookingInformation booking = profileBookingList.get(position);

        holder.my_bookings_purpose.setText(booking.getReason());
        holder.my_bookings_roomNum.setText(booking.getRoom_num());
        holder.my_bookings_date.setText(booking.getDate());
        holder.my_bookings_time.setText(booking.getTime());


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



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup(getAbsoluteAdapterPosition());
                }
            });


        }
    }

    private void showPopup(int position) {

        final BookingInformation selectedBooking = profileBookingList.get(position);

        // Create a Dialog and set the custom layout
        final Dialog popupDialog = new Dialog(context);
        popupDialog.setContentView(R.layout.pop_up_booking_card);

        TextView roomNum_popup, date_popup, time_popup, purpose_popup;
        Button close_popup, delete_popup;

        roomNum_popup = popupDialog.findViewById(R.id.roomNum_popup);
        date_popup = popupDialog.findViewById(R.id.date_popup);
        time_popup = popupDialog.findViewById(R.id.time_popup);
        purpose_popup = popupDialog.findViewById(R.id.purpose_popup);
        close_popup = popupDialog.findViewById(R.id.popup_close_btn);
        delete_popup = popupDialog.findViewById(R.id.popup_delete_btn);

        // Set onClickListeners for buttons
        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });

        delete_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show delete confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to Cancel this booking?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteBooking(profileBookingList.get(position));
                                popupDialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });


                builder.create().show();

            }
        });
        // Show the popup dialog
        popupDialog.show();

    }


    private void deleteBooking(final BookingInformation selectedBooking){

        if (selectedBooking.getSize() == null) {
            Toast.makeText(context, "Error: Booking size is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBooking.getRoom_num() == null) {
            Toast.makeText(context, "Error: Booking room number is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBooking.getPurposeId() == null) {
            Toast.makeText(context, "Error: Booking purpose ID is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Common.simpleDateFormat.format(selectedBooking.getTimestamp().toDate()) == null) {
            Toast.makeText(context, "Error: Booking date is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (String.valueOf(selectedBooking.getSlot()) == null) {
            Toast.makeText(context, "Error: Booking slot is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Deleting from Users MyBooking Collection
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUserId)
                .collection("MyBookings")
                .document(selectedBooking.getBookingID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error cancelling booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
