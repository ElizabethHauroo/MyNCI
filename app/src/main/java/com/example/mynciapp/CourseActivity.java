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

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();


        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        String postId = postsRef.push().getKey(); //giving each post a postID

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String authorId = currentUser.getUid();
        String authorName = currentUser.getDisplayName(); // retrieve author name from currentUser
        String courseCode = ""; // retrieve course code from currentUser

        Post post = new Post(postId, authorId, authorName, courseCode, content, System.currentTimeMillis(), isGeneral, isCourse, 0, 0);
        postsRef.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void loadFilteredPosts() {

        // Get Current User details: UserID and Course to be able to filter list
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                final String courseCode = dataSnapshot.child("course").getValue(String.class);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);

                    if (post.getCourse() && post.getPost_courseCode().equals(courseCode) || (post.getGeneral() && post.getPost_authorId().equals(currentUserID))) {
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CourseActivity.this, "Failed to load posts. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}