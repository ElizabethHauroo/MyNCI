package com.example.mynciapp;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.example.mynciapp.Common.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    BottomNavigationView nav;

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;

    private CardView map, schedule, nci, course;

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference UsersRef;

    private TextView parkingStatus;
    private DatabaseReference parkingRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users"); // creating the users node, where we will be sotring all the user's information - unique uid


        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MyNCI");




        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close); //for the hamburger menu
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parkingStatus = findViewById(R.id.parking_status_home);
        parkingRef = FirebaseDatabase.getInstance().getReference("parking");



        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView =navigationView.inflateHeaderView(R.layout.navigation_header); //adding the header to the sidebar
        NavProfileImage=(CircleImageView)navView.findViewById(R.id.nav_profileImage); //we have to parse Nav View here
        NavProfileUserName = (TextView)navView.findViewById(R.id.nav_profilename);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("firstname")&&snapshot.hasChild("lastname")){
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String fullname = firstname + " "+ lastname;
                    NavProfileUserName.setText(fullname);
                }

                if(snapshot.hasChild("profileimage")){
                    String image = snapshot.child("profileimage").getValue().toString();
                    //let's display them on the navigation view
                    Picasso.get().load(image).placeholder(R.drawable.defaultprofile).into(NavProfileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nav=findViewById(R.id.bottom_navigation);

        map=findViewById(R.id.map_tile);
        schedule=findViewById(R.id.book_tile);
        nci=findViewById(R.id.nci_tile);
        course=findViewById(R.id.course_tile);


        // All Tiles Click Listeners

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, BookingRoomActivity.class));
            }
        });
        nci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NCIActivity.class));
            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CourseActivity.class));
            }
        });

        ParkingToggle();




        // Navigation Side + Bottom
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.add_bottomnav:
                        startActivity(new Intent(HomeActivity.this, AdminActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }



                return true;
            }
        });





    }//onCreate

    private void ParkingToggle() {

        // Read parking state from the database and set the UI accordingly
        parkingRef.child("isFull").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isFull = dataSnapshot.getValue(Boolean.class);
                if (isFull != null && isFull) {
                    parkingStatus.setText("Parking is currently FULL");
                    parkingStatus.setTextColor(getResources().getColor(R.color.orange_pumpkin));
                } else {
                    parkingStatus.setText("Parking Spaces are Available");
                    parkingStatus.setTextColor(getResources().getColor(R.color.blue_cerulean));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log any errors or handle them as needed
                Log.w(TAG, "Failed to read parking status:", databaseError.toException());
            }
        });
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser ==null){
            SendUserToLogin();
        }else{
            CheckUSerExistence();
        }
    }


    private void CheckUSerExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(current_user_id)){
                    //if this does not exist, then we need to add it to database. So we have to send them to setup
                    SendUserToSetup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendUserToSetup() {
        Intent intent=new Intent(HomeActivity.this,SetupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //CODE BLOCK FOR THE SIDE BAR NAVIGATION - SWITCH BETWEEN THE RELEVANT PAGES
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLogin();
                break;


        }
    }

    private void SendUserToLogin() {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

}