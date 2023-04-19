package com.example.mynciapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynciapp.Model.Post;
import com.example.mynciapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;
    private OnPostClickListener onPostClickListener;
    /*
    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }*/

    public PostAdapter(Context context, List<Post> postList, OnPostClickListener onPostClickListener) {
        this.context = context;
        this.postList = postList;
        this.onPostClickListener = onPostClickListener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostClickListener.onPostClick(post);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView post_author_name_textView, post_course_textView, post_content_textView, post_date_time_textView;
        public ImageButton post_like_btn, post_dislike_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_author_name_textView = itemView.findViewById(R.id.post_author_name_textView);
            post_course_textView = itemView.findViewById(R.id.post_course_textView);
            post_content_textView = itemView.findViewById(R.id.post_content_textView);
            post_date_time_textView = itemView.findViewById(R.id.post_date_time_textView);
            post_like_btn = itemView.findViewById(R.id.post_like_btn);
            post_dislike_btn = itemView.findViewById(R.id.post_dislike_btn);
        }
    }

    //for clicking on a post
    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

}
