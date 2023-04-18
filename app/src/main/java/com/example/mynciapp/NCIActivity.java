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
import java.util.Date;
import java.util.List;

public class NCIActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();

    private RecyclerView rvNciPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private FloatingActionButton floatingCreatePostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nciactivity);

        rvNciPosts = findViewById(R.id.rv_nci_posts);
        rvNciPosts.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        rvNciPosts.setAdapter(postAdapter);

        floatingCreatePostButton = findViewById(R.id.floating_nci_create_post_button);

        loadGeneralPosts();    // loading every General posts (by every user)

        floatingCreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePostDialog(); //same method as CourseActivity
            }
        });

    }

    // same method as CourseActivity
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
                    Toast.makeText(NCIActivity.this, "Please fill in the content and select at least one feed type.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

    }

    // same method as CourseActivity
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
                                    Toast.makeText(NCIActivity.this, "Post created successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NCIActivity.this, "Failed to create the post. Please try again.", Toast.LENGTH_SHORT).show();
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

    // loading every posts (by every user) that have checked the "general feed box when createv)
    private void loadGeneralPosts() {

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);

                    if (post.getGeneral()) {
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NCIActivity.this, "Failed to load posts. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}