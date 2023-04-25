package com.example.mynciapp.BookingModels;

public class AdminBookingInformation {

    private String bookingID;
    private String room_number;
    private String bookingDate;
    private String bookingTime;
    private String BookingReason;
    private String currentUserName;

    public AdminBookingInformation() {
    }

    public AdminBookingInformation(String bookingID, String room_number, String bookingDate, String bookingTime, String bookingReason, String currentUserName) {
        this.bookingID = bookingID;
        this.room_number = room_number;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        BookingReason = bookingReason;
        this.currentUserName = currentUserName;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
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

    public String getBookingReason() {
        return BookingReason;
    }

    public void setBookingReason(String bookingReason) {
        BookingReason = bookingReason;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }
}
