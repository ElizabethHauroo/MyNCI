package com.example.mynciapp;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
//import android.widget.Toolbar;

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


    private TextView parkingStatus;
    private DatabaseReference parkingRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
        currentUserID = mAuth.getCurrentUser().getUid();
        } else {
            // Handle the case where the user is null
        }
        //UsersRef = FirebaseDatabase.getInstance().getReference().child("Users"); // creating the users node, where we will be sotring all the user's information - unique uid

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (database != null) {
            DatabaseReference UsersRef = database.getReference().child("Users");
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

        } else {

        }


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
                    case R.id.book_bottomnav:
                        startActivity(new Intent(HomeActivity.this, BookingRoomActivity.class));
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
            case R.id.nav_moodle:
                goToMoodle();
                break;
            case R.id.nav_teams:
                goToTeams();
                break;
            case R.id.nav_timetable:
                goToTimetable();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLogin();
                break;


        }
    }



    private void goToMoodle() {
        String packageName = "com.moodle.moodlemobile";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        try {
            if (intent != null) {
                startActivity(intent);
            } else {
                String url = "https://mymoodle.ncirl.ie/";
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            }
        } catch (ActivityNotFoundException e) {
            String url = "https://mymoodle.ncirl.ie/";
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(webIntent);
        }
    }

    private void goToTeams() {
        String packageName = "com.microsoft.teams";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        try {
            if (intent != null) {
                startActivity(intent);
            } else {
                String url = "https://teams.microsoft.com/";
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            }
        } catch (ActivityNotFoundException e) {
            String url = "https://teams.microsoft.com/";
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(webIntent);
        }
    }

    private void goToTimetable() {
        String url = "https://scientia-opentt-ncirl.azurewebsites.net/";
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webIntent);
    }

    private void SendUserToLogin() {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

}