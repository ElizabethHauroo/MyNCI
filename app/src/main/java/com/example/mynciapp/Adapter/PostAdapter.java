package com.example.mynciapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Model.Post;
import com.example.mynciapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;
    private OnPostClickListener onPostClickListener;
    private String currentUserId;

    public PostAdapter(Context context, List<Post> postList, OnPostClickListener onPostClickListener, String currentUserId) {
        this.context = context;
        this.postList = postList;
        this.onPostClickListener = onPostClickListener;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = postList.get(position);
        holder.post_author_name_textView.setText(post.getPost_authorName());
        holder.post_course_textView.setText(post.getPost_courseCode());
        holder.post_content_textView.setText(post.getPost_content());
        holder.post_date_time_textView.setText(post.getPost_timestamp());
        holder.post_like_count.setText(String.valueOf(post.getLikes()));

        List<String> likedBy = post.getLikedBy();
        if (likedBy != null && likedBy.contains(currentUserId)) {
            holder.post_like_btn.setImageResource(R.drawable.star_selected);
        } else {
            holder.post_like_btn.setImageResource(R.drawable.star);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostClickListener.onPostClick(post);
            }
        });

        holder.post_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeButtonClick(post, holder.post_like_btn, holder.post_like_count);
            }
        });

        // get user data from Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(post.getPost_authorId());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("profileimage")) {
                    String profileImageUrl = dataSnapshot.child("profileimage").getValue(String.class);
                    // Use Picasso to load the image
                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.grey_profile).into(holder.post_author_image);
                } else {
                    // No profile image, load placeholder
                    Picasso.get().load(R.drawable.grey_profile).into(holder.post_author_image);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error...
            }
        });


    }


    private void onLikeButtonClick(Post post, ImageButton post_like_btn, TextView post_like_count) {

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(post.getPost_id());
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getLikedBy() == null) {
                    p.setLikedBy(new ArrayList<>());
                }

                if (p.getLikedBy().contains(currentUserId)) {
                    // Unlike
                    p.setLikes(p.getLikes() - 1);
                    p.getLikedBy().remove(currentUserId);
                } else {
                    // Like
                    p.setLikes(p.getLikes() + 1);
                    p.getLikedBy().add(currentUserId);
                }
                // Save post back to the database
                mutableData.setValue(p);
                return Transaction.success(mutableData);

            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Toast.makeText(context, "Failed to update like count: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (dataSnapshot != null) {
                        // Update the UI
                        Post updatedPost = dataSnapshot.getValue(Post.class);
                        if (updatedPost != null) {
                            post_like_count.setText(String.valueOf(updatedPost.getLikes()));
                            List<String> updatedLikedBy = updatedPost.getLikedBy();
                            if (updatedLikedBy != null && updatedLikedBy.contains(currentUserId)) {
                                post_like_btn.setImageResource(R.drawable.star_selected);
                            } else {
                                post_like_btn.setImageResource(R.drawable.star);
                            }
                        }
                    } else {
                        Toast.makeText(context, "Failed to update like count: dataSnapshot is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView post_author_name_textView, post_course_textView, post_content_textView, post_date_time_textView;
        public ImageButton post_like_btn;
        public TextView post_like_count;
        //public ImageButton post_dislike_btn;
        public CircleImageView post_author_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_author_name_textView = itemView.findViewById(R.id.post_author_name_textView);
            post_course_textView = itemView.findViewById(R.id.post_course_textView);
            post_content_textView = itemView.findViewById(R.id.post_content_textView);
            post_date_time_textView = itemView.findViewById(R.id.post_date_time_textView);
            post_like_btn = itemView.findViewById(R.id.post_like_btn);
            post_like_count = itemView.findViewById(R.id.post_like_count);
            //post_dislike_btn = itemView.findViewById(R.id.post_dislike_btn);
            post_author_image = itemView.findViewById(R.id.post_profile_image);
        }
    }

    //for clicking on a post
    public interface OnPostClickListener {

        void onPostClick(Post post);
    }

}
