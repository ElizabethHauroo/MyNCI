package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class MapActivity extends AppCompatActivity {

    private Button first, second, third, forth;
    private ImageView imageBox;
    BottomNavigationView nav;

    private AutoCompleteTextView autoCompleteTextView;
    //private ImageView imageView;
    private MaterialSpinner spinner;
    private String[] map_room_names_for_search;
    private int[] map_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        autoCompleteTextView = findViewById(R.id.search_autoCompleteTxt);
        //imageView = findViewById(R.id.map_mainview);
        spinner = findViewById(R.id.AllRooms_spinner);

        map_room_names_for_search = getResources().getStringArray(R.array.map_room_names_for_search);
        String[] mapImagesString = getResources().getStringArray((R.array.map_images));
        map_images = new int[mapImagesString.length];
        for(int i = 0; i < mapImagesString.length; i++ ){
            map_images[i] = getResources().getIdentifier(mapImagesString[i], "drawable", getPackageName());
        }
        setupAutoCompleteTextView();
        setupSpinner();

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
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.add_bottomnav:
                        startActivity(new Intent(MapActivity.this, AddActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(MapActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }



                return true;
            }
        });

    }// --------------------- onCreate------------------------------ onCreate------------------------

    private void setupAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, map_room_names_for_search);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                updateImageView(selectedItem);
            }
        });
    }

    private void updateImageView(String selectedItem) {
        int index = -1;
        for (int i = 0; i < map_room_names_for_search.length; i++) {
            if (map_room_names_for_search[i].equals(selectedItem)) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            imageBox.setImageResource(map_images[index]);
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, map_room_names_for_search);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                updateImageView(map_room_names_for_search[position]);
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                // Don't do anything
            }
        });
    }



}