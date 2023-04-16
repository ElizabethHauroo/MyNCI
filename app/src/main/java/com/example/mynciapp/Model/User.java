package com.example.mynciapp.Model;

public class User {

    private String firstname, lastname, course;

    public User() {
    }

    public User(String firstname, String lastname, String course) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.course = course;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
