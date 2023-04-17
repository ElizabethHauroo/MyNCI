package com.example.mynciapp.Model;

public class Post {

    private String post_id;
    private String post_authorId;
    private String post_authorName;
    private String post_courseCode;
    private String post_content;
    private long post_timestamp;
    public Boolean isCourse, isGeneral;
    private int post_upvote_Count;
    private int post_downvote_Count;

    public Post() {
    }

    public Post(String post_id, String post_authorId, String post_authorName, String post_courseCode, String post_content, long post_timestamp, Boolean isCourse, Boolean isGeneral, int post_upvote_Count, int post_downvote_Count) {
        this.post_id = post_id;
        this.post_authorId = post_authorId;
        this.post_authorName = post_authorName;
        this.post_courseCode = post_courseCode;
        this.post_content = post_content;
        this.post_timestamp = post_timestamp;
        this.isCourse = isCourse;
        this.isGeneral = isGeneral;
        this.post_upvote_Count = post_upvote_Count;
        this.post_downvote_Count = post_downvote_Count;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_authorId() {
        return post_authorId;
    }

    public void setPost_authorId(String post_authorId) {
        this.post_authorId = post_authorId;
    }

    public String getPost_authorName() {
        return post_authorName;
    }

    public void setPost_authorName(String post_authorName) {
        this.post_authorName = post_authorName;
    }

    public String getPost_courseCode() {
        return post_courseCode;
    }

    public void setPost_courseCode(String post_courseCode) {
        this.post_courseCode = post_courseCode;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public long getPost_timestamp() {
        return post_timestamp;
    }

    public void setPost_timestamp(long post_timestamp) {
        this.post_timestamp = post_timestamp;
    }

    public Boolean getCourse() {
        return isCourse;
    }

    public void setCourse(Boolean course) {
        isCourse = course;
    }

    public Boolean getGeneral() {
        return isGeneral;
    }

    public void setGeneral(Boolean general) {
        isGeneral = general;
    }

    public int getPost_upvote_Count() {
        return post_upvote_Count;
    }

    public void setPost_upvote_Count(int post_upvote_Count) {
        this.post_upvote_Count = post_upvote_Count;
    }

    public int getPost_downvote_Count() {
        return post_downvote_Count;
    }

    public void setPost_downvote_Count(int post_downvote_Count) {
        this.post_downvote_Count = post_downvote_Count;
    }
}
