package com.example.mynciapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.mynciapp.BookingModels.AdminBookingInformation;

public class AdminBookingInformationTest {
    private AdminBookingInformation bookingInfo;

    @Before
    public void setUp() {
        bookingInfo = new AdminBookingInformation("123456", "Room A", "2023-05-10", "2:00 PM - 4:00 PM", "Meeting", "John Smith");
    }

    @Test
    public void testGetBookingID() {
        assertEquals("123456", bookingInfo.getBookingID());
    }

    @Test
    public void testSetBookingID() {
        bookingInfo.setBookingID("654321");
        assertEquals("654321", bookingInfo.getBookingID());
    }

    @Test
    public void testGetRoom_number() {
        assertEquals("Room A", bookingInfo.getRoom_number());
    }

    @Test
    public void testSetRoom_number() {
        bookingInfo.setRoom_number("Room B");
        assertEquals("Room B", bookingInfo.getRoom_number());
    }

    @Test
    public void testGetBookingDate() {
        assertEquals("2023-05-10", bookingInfo.getBookingDate());
    }

    @Test
    public void testSetBookingDate() {
        bookingInfo.setBookingDate("2023-05-11");
        assertEquals("2023-05-11", bookingInfo.getBookingDate());
    }

    @Test
    public void testGetBookingTime() {
        assertEquals("2:00 PM - 4:00 PM", bookingInfo.getBookingTime());
    }

    @Test
    public void testSetBookingTime() {
        bookingInfo.setBookingTime("4:00 PM - 6:00 PM");
        assertEquals("4:00 PM - 6:00 PM", bookingInfo.getBookingTime());
    }

    @Test
    public void testGetBookingReason() {
        assertEquals("Meeting", bookingInfo.getBookingReason());
    }

    @Test
    public void testSetBookingReason() {
        bookingInfo.setBookingReason("Study");
        assertEquals("Study", bookingInfo.getBookingReason());
    }

    @Test
    public void testGetCurrentUserName() {
        assertEquals("John Smith", bookingInfo.getCurrentUserName());
    }

    @Test
    public void testSetCurrentUserName() {
        bookingInfo.setCurrentUserName("Jane Doe");
        assertEquals("Jane Doe", bookingInfo.getCurrentUserName());
    }
}

