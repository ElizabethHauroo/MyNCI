package com.example.mynciapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;


import com.example.mynciapp.Adapter.MyViewPagerAdapter;
import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Common.NonSwipeViewPager;
import com.example.mynciapp.Model.Purpose;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class ScheduleActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    SpotsDialog dialog;
    CollectionReference purposeRef;


    BottomNavigationView nav;

    //@BindView(R.id.step_view)
    private StepView stepview;
    private NonSwipeViewPager viewpager;
    private Button previousBTN, nextBTN;

    //Event
    @OnClick(R.id.booking_previous_btn)
    void previousStep(){
        if(Common.step ==3 || Common.step > 0 ){
            Common.step--;
            viewpager.setCurrentItem(Common.step);

            if(Common.step < 3){
                nextBTN.setEnabled(true);
                setColourButton();
            }
        }
    }
    @OnClick(R.id.booking_next_btn)
    void nextClick(){
        //Toast.makeText(this,"Clicked"+Common.currentRoom.getRoomId(), Toast.LENGTH_SHORT).show();
        if(Common.step < 3 || Common.step == 0){
            Common.step++; //increase by one if the next button is clicked (as we move through the booking steps.
            if(Common.step == 1 ){ // after room number, chose purpose for booking (step 2)
                if(Common.currentRoom !=null){
                    loadPurposeByRoom(Common.currentRoom.getRoomId());
                }
            }
            else if(Common.step == 2){ //select timeslot
                if(Common.currentPurpose !=null){
                    loadTimeslotofPurpose(Common.currentPurpose.getPurposeId());
                }
            }
            else if(Common.step == 3){ //Confirm Step
                if(Common.currentTimeSlot != -1){
                    confirmBooking();
                }
            }
            viewpager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {
        //send broadcast to frag4
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeslotofPurpose(String purposeId) {
        // send broadcast to frag3 (step3)
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);

    }

    private void loadPurposeByRoom(String roomId) {
       // dialog.show();

        // select all purposes of Room
        //    /AllRooms/Large/Rooms/BprMx0J4PcNJBS5TnZKb/Purpose
        if(!TextUtils.isEmpty(Common.size)){
            purposeRef = FirebaseFirestore.getInstance()
                    .collection("AllRooms")
                    .document(Common.size)
                    .collection("Rooms")
                    .document(roomId)
                    .collection("Purpose");

            purposeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Purpose> purposes = new ArrayList<>();
                    for (QueryDocumentSnapshot purposeSnapshot:task.getResult()){
                        Purpose purpose = purposeSnapshot.toObject(Purpose.class);
                        purpose.setPurposeId(purposeSnapshot.getId()); //getting the id of that "purpose" (reason for booking) for that room

                        purposes.add(purpose);
                    }
                    //send broadcast to step 2 (frag2)
                    Intent intent = new Intent(Common.KEY_PURPOSE_LOAD_DONE);
                    intent.putParcelableArrayListExtra(Common.KEY_PURPOSE_LOAD_DONE, purposes);
                    localBroadcastManager.sendBroadcast(intent);

                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);

            if(step ==1){
                Common.currentRoom = intent.getParcelableExtra(Common.KEY_SIZE_STORE);
            }
            else if(step ==2){
                Common.currentPurpose = intent.getParcelableExtra(Common.KEY_PURPOSE_SELECTED);
            }
            else if(step ==3){
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT,-1);
            }

            nextBTN.setEnabled(true);
            setColourButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(ScheduleActivity.this);

        //dialog = new SpotsDialog.Builder().setContext(this).build();
        //dialog = new SpotsDialog(this).build();
        dialog = new SpotsDialog(this);

        nav=findViewById(R.id.bottom_navigation_schedule);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(ScheduleActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.book_bottomnav:
                        startActivity(new Intent(ScheduleActivity.this, AddActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(ScheduleActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }

                return true;
            }
        });



        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        stepview = (StepView) findViewById(R.id.step_view);
        viewpager = (NonSwipeViewPager) findViewById(R.id.view_pager);

        //Buttons
        previousBTN = (Button) findViewById(R.id.booking_previous_btn);
        nextBTN = (Button) findViewById(R.id.booking_next_btn);
        
        
        setupStepView();
        setColourButton();

        //View
        viewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewpager.setOffscreenPageLimit(4); //because we have fours fragments (steps in booking)
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //Show Steps
                stepview.go(position, true);
                if (position ==0) {
                    previousBTN.setEnabled(false);
                }
                else{
                    previousBTN.setEnabled(true);
                }
                //set disable Next button
                nextBTN.setEnabled(false);
                setColourButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setColourButton() {
        if(nextBTN.isEnabled()){
            nextBTN.setBackgroundResource(R.color.royal_purple);
            nextBTN.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        else{
            nextBTN.setBackgroundResource(R.color.platinum_grey);
            nextBTN.setTextColor(ContextCompat.getColor(this, R.color.battleship_grey));
        }
        if(previousBTN.isEnabled()){
            previousBTN.setBackgroundResource(R.color.royal_purple);
            previousBTN.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        else{
            previousBTN.setBackgroundResource(R.color.platinum_grey);
            previousBTN.setTextColor(ContextCompat.getColor(this, R.color.battleship_grey));
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Room");
        stepList.add("Purpose");
        stepList.add("Time");
        stepList.add("Confirm");
        stepview.setSteps(stepList);
    }
}