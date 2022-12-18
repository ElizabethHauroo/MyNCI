package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MapActivity extends AppCompatActivity {

    private Button first, second, third, forth;
    private ImageView imageBox;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        first=(Button) findViewById(R.id.first_floor_map);
        second=(Button) findViewById(R.id.second_floor_map);
        third=(Button) findViewById(R.id.third_floor_map);
        forth=(Button) findViewById(R.id.forth_floor_map);
        imageBox = (ImageView) findViewById(R.id.map_mainview);

        nav=findViewById(R.id.bottom_navigation_map);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBox.setImageResource(R.drawable.sample1);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBox.setImageResource(R.drawable.sample2);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBox.setImageResource(R.drawable.sample3);
            }
        });
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBox.setImageResource(R.drawable.sample4);
            }
        });
        //BOTTOM NAVIGATION
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(MapActivity.this, HomeActivity.class));
                        break;
                    case R.id.add_bottomnav:
                        Toast.makeText(MapActivity.this, "Add", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.profile_bottomnav:
                        Toast.makeText(MapActivity.this, "Profile", Toast.LENGTH_LONG).show();
                        break;

                    default:
                }



                return true;
            }
        });

    }// onCreate
}