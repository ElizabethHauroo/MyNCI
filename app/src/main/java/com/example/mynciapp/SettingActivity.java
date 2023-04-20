package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button update_courseBTN, update_pictureBTN, change_passwordBTN;
    TextView settings_fullname_txt, settings_studentNumber_txt, settings_course_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Bottom Navigation
        nav=findViewById(R.id.bottom_navigation_setting);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.add_bottomnav:
                        startActivity(new Intent(SettingActivity.this, AddActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }



                return true;
            }
        });

        settings_fullname_txt=findViewById(R.id.settings_fullname_txt);
        settings_studentNumber_txt=findViewById(R.id.settings_studentNumber_txt);
        settings_course_txt=findViewById(R.id.settings_course_txt);

        update_courseBTN = findViewById(R.id.settings_update_courseBTN);
        update_pictureBTN = findViewById(R.id.settings_update_pictureBTN);
        change_passwordBTN = findViewById(R.id.settings_change_passwordBTN);


    }
}