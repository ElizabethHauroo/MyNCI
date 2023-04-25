package com.example.mynciapp.BookingModels;

import java.io.Serializable;

public class TimeslotBooking implements Serializable {

    private String bookingDate;
    private String bookingTime;
    private int slotNumber;
    private boolean isBooked;
    private BookingReason bookingReason;

    public TimeslotBooking() {
    }

    public TimeslotBooking(String bookingDate, String bookingTime, int slotNumber, boolean isBooked) {
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.slotNumber = slotNumber;
        this.isBooked = isBooked;
    }

    public TimeslotBooking(String bookingDate, String bookingTime, int slotNumber, boolean isBooked, BookingReason bookingReason) {
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.slotNumber = slotNumber;
        this.isBooked = isBooked;
        this.bookingReason = bookingReason;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public BookingReason getBookingReason() {
        return bookingReason;
    }

    public void setBookingReason(BookingReason bookingReason) {
        this.bookingReason = bookingReason;
    }
}
