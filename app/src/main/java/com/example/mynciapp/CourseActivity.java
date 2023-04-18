package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynciapp.Adapter.PostAdapter;
import com.example.mynciapp.Model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.text.ParseException;


public class CourseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();

    private RecyclerView rvMyCoursePosts;
    private FloatingActionButton floatingCreatePostButton;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        rvMyCoursePosts = findViewById(R.id.rv_myCourse_posts);
        floatingCreatePostButton = findViewById(R.id.floating_create_post_button);
        rvMyCoursePosts.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        rvMyCoursePosts.setAdapter(postAdapter);

        loadFilteredPosts();

        floatingCreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePostDialog();
            }
        });
    }  // ---------------- ON CREATE -----------------------------

    private void showCreatePostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.create_post_dialog, null);
        builder.setView(view);

        final EditText createContentEditText = view.findViewById(R.id.create_content_edit_text);
        final CheckBox generalFeedCheckbox = view.findViewById(R.id.create_general_feed_checkbox);
        final CheckBox courseFeedCheckbox = view.findViewById(R.id.create_course_feed_checkbox);
        Button createPostBtn = view.findViewById(R.id.create_post_btn);

        final AlertDialog dialog = builder.create();
        createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = createContentEditText.getText().toString().trim();
                boolean isGeneral = generalFeedCheckbox.isChecked();
                boolean isCourse = courseFeedCheckbox.isChecked();

                if (!TextUtils.isEmpty(content) && (isGeneral || isCourse)) {
                    createNewPost(content, isGeneral, isCourse);
                    dialog.dismiss();
                } else {
                    Toast.makeText(CourseActivity.this, "Please fill in the content and select at least one feed type.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void createNewPost(String content, boolean isGeneral, boolean isCourse) {
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        final String firstName = snapshot.child("firstname").getValue(String.class);
                        final String lastName = snapshot.child("lastname").getValue(String.class);
                        final String course = snapshot.child("course").getValue(String.class);
                        final String fullName = firstName + " "+ lastName;

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  |  dd MMM yyyy");
                        String currentDateandTime = sdf.format(new Date());

                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
                        String postId = postsRef.push().getKey();

                        Post createdPost = new Post(postId, userId, fullName, course, content, currentDateandTime, isCourse, isGeneral, 0, 0);


                        postsRef.child(postId).setValue(createdPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CourseActivity.this, "Post created successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CourseActivity.this, "Failed to create the post. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } //if logged in



    }


    private void loadFilteredPosts() {
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String courseCode = dataSnapshot.child("course").getValue(String.class);

                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
                postsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);

                            if ((post.getCourse() && post.getPost_courseCode().equals(courseCode)) ||
                                    (post.getCourse() && post.getGeneral() && post.getPost_courseCode().equals(courseCode))) {
                                postList.add(post);
                            }
                        }
                        // Sort the posts by time that they were posted (newest to oldest)
                        Collections.sort(postList, new Comparator<Post>() {
                            @Override
                            public int compare(Post post1, Post post2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  |  dd MMM yyyy");
                                try {
                                    Date date1 = sdf.parse(post1.getPost_timestamp());
                                    Date date2 = sdf.parse(post2.getPost_timestamp());
                                    return date2.compareTo(date1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CourseActivity.this, "Failed to load posts. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }




}