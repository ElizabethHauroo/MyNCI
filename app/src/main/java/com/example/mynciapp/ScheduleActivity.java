package com.example.mynciapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.example.mynciapp.Adapter.MyViewPagerAdapter;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ScheduleActivity extends AppCompatActivity {

    //@BindView(R.id.step_view)
    private StepView stepview;
    private ViewPager viewpager;
    private Button previousBTN, nextBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(ScheduleActivity.this);

        stepview = (StepView) findViewById(R.id.step_view);
        viewpager = (ViewPager) findViewById(R.id.view_pager);

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
            nextBTN.setBackgroundResource(R.color.blue_cerulean);
        }
        else{
            nextBTN.setBackgroundResource(R.color.battleship_grey);
        }
        if(previousBTN.isEnabled()){
            previousBTN.setBackgroundResource(R.color.blue_cerulean);
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