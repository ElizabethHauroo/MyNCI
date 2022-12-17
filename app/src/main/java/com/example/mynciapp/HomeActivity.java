package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    BottomNavigationView nav;

    private CardView map, schedule, nci, course;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth=FirebaseAuth.getInstance();

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MyNCI");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close); //for the hamburger menu
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView =navigationView.inflateHeaderView(R.layout.navigation_header); //adding the header to the sidebar

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
                startActivity(new Intent(HomeActivity.this, ScheduleActivity.class));
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
                        Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.add_bottomnav:
                        Toast.makeText(HomeActivity.this, "Add", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.profile_bottomnav:
                        Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_LONG).show();
                        break;

                    default:
                }



                return true;
            }
        });





    }//onCreate

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
                Toast.makeText(this, "This is Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "This is Settings", Toast.LENGTH_SHORT).show();
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