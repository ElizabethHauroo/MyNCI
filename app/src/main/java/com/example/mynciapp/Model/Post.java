package com.example.mynciapp.Model;

import java.util.List;

public class Post {

    private String post_id;
    private String post_authorId;
    private String post_authorName;
    private String post_courseCode;
    private String post_content;
    private String post_timestamp;
    public Boolean isCourse, isGeneral;
    private int likes;
    private List<String> likedBy;


    public Post() {
    }

    public Post(String post_id, String post_authorId, String post_authorName, String post_courseCode, String post_content, String post_timestamp, Boolean isCourse, Boolean isGeneral, int likes, List<String> likedBy) {
        this.post_id = post_id;
        this.post_authorId = post_authorId;
        this.post_authorName = post_authorName;
        this.post_courseCode = post_courseCode;
        this.post_content = post_content;
        this.post_timestamp = post_timestamp;
        this.isCourse = isCourse;
        this.isGeneral = isGeneral;
        this.likes = likes;
        this.likedBy = likedBy;
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

    public String getPost_timestamp() {
        return post_timestamp;
    }

    public void setPost_timestamp(String post_timestamp) {
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }
}
