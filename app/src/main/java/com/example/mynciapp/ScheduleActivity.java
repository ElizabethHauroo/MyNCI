package com.example.mynciapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;


import com.example.mynciapp.Adapter.MyViewPagerAdapter;
import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Common.NonSwipeViewPager;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;

    //@BindView(R.id.step_view)
    private StepView stepview;
    private NonSwipeViewPager viewpager;
    private Button previousBTN, nextBTN;

    //Event
    @OnClick(R.id.booking_next_btn)
    void nextClick(){
        Toast.makeText(this,"Clicked"+Common.currentRoom.getRoomId(), Toast.LENGTH_SHORT).show();
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Common.currentRoom = intent.getParcelableExtra(Common.KEY_SIZE_STORE);
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
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position ==0) {
                    previousBTN.setEnabled(false);
                }
                else{
                    previousBTN.setEnabled(true);
                }
                setColourButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setColourButton() {
        if(nextBTN.isEnabled()){
            nextBTN.setBackgroundResource(R.color.lime_green);
        }
        else{
            nextBTN.setBackgroundResource(R.color.battleship_grey);
        }
        if(previousBTN.isEnabled()){
            previousBTN.setBackgroundResource(R.color.lime_green);
        }
        else{
            previousBTN.setBackgroundResource(R.color.battleship_grey);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Room");
        stepList.add("Time");
        stepList.add("Purpose");
        stepList.add("Confirm");
        stepview.setSteps(stepList);
    }
}