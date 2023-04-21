package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;


import com.example.mynciapp.Adapter.MyProfileBookingsAdapter;
import com.example.mynciapp.Common.SpacesItemDecoration;
import com.example.mynciapp.Model.BookingInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView mFullNameTextView, mCourseTextView, mUsernameTextView;

    private CircleImageView profilePicture;
    private StorageReference userProfileImageRef;


    private RecyclerView rvBookings;
    private MyProfileBookingsAdapter bookingAdapter;
    private List<BookingInformation> userBookings;

    BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String currentUserID = mAuth.getCurrentUser().getUid();

        mFullNameTextView = findViewById(R.id.fullname_txt_profile_page);
        mUsernameTextView = findViewById(R.id.username_txt_profile_page);
        mCourseTextView = findViewById(R.id.course_txt_profile_page);

        profilePicture = findViewById(R.id.profile_my_profile_picture);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        userProfileImageRef = storage.getReference().child("Profile Images").child(currentUserID + ".jpg");



        rvBookings = findViewById(R.id.rv_mybookings);
        rvBookings.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rvBookings.setLayoutManager(gridLayoutManager);
        rvBookings.addItemDecoration(new SpacesItemDecoration(3));


        userBookings = new ArrayList<>();
        bookingAdapter = new MyProfileBookingsAdapter(userBookings, this);
        rvBookings.setAdapter(bookingAdapter);

        nav = findViewById(R.id.bottom_navigation_profile);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.add_bottomnav:
                        startActivity(new Intent(ProfileActivity.this, AddActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }

                return true;
            }
        });

        //Get Current User Info for Header
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String firstName = snapshot.child("firstname").getValue(String.class);
                final String lastName = snapshot.child("lastname").getValue(String.class);
                String fullName = firstName + " "+lastName;
                mFullNameTextView.setText(fullName);
                final String course = snapshot.child("course").getValue(String.class);
                mCourseTextView.setText(course);
                final String usernameRaw = snapshot.child("username").getValue(String.class);
                String username = "@"+usernameRaw;
                mUsernameTextView.setText(username);

                if (snapshot.hasChild("profileimage")) {
                    String profileImageUrl = snapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.defaultprofile).into(profilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadUserBookings();




    }

    private void loadUserBookings() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            CollectionReference userBookingRef = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(currentUserId)
                    .collection("MyBookings");

            userBookingRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    BookingInformation booking = document.toObject(BookingInformation.class);
                                    userBookings.add(booking);
                                    Log.d("ProfileActivity", "Booking added: " + booking.getTime()); // Add this line
                                }
                                bookingAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("ProfilePage", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

}