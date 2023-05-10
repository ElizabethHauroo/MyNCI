package com.example.mynciapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.mynciapp.Model.User;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User("John", "Doe", "Computer Science");
    }

    @Test
    public void testGetFirstname() {
        assertEquals("John", user.getFirstname());
    }

    @Test
    public void testSetFirstname() {
        user.setFirstname("Jane");
        assertEquals("Jane", user.getFirstname());
    }

    @Test
    public void testGetLastname() {
        assertEquals("Doe", user.getLastname());
    }

    @Test
    public void testSetLastname() {
        user.setLastname("Smith");
        assertEquals("Smith", user.getLastname());
    }

    @Test
    public void testGetCourse() {
        assertEquals("Computer Science", user.getCourse());
    }

    @Test
    public void testSetCourse() {
        user.setCourse("Information Technology");
        assertEquals("Information Technology", user.getCourse());
    }
}

