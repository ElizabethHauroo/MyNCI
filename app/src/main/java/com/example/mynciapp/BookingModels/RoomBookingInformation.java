package com.example.mynciapp.BookingModels;

public class RoomBookingInformation {

    private String BookingReason, bookingDate, bookingID, bookingTime, currentUserName, roomSize, room_number, timeslot, userID;

    public RoomBookingInformation() {
    }

    public RoomBookingInformation(String bookingReason, String bookingDate, String bookingID, String bookingTime, String currentUserName, String roomSize, String room_number, String timeslot, String userID) {
        BookingReason = bookingReason;
        this.bookingDate = bookingDate;
        this.bookingID = bookingID;
        this.bookingTime = bookingTime;
        this.currentUserName = currentUserName;
        this.roomSize = roomSize;
        this.room_number = room_number;
        this.timeslot = timeslot;
        this.userID = userID;
    }

    public RoomBookingInformation(String testUser, String room1, String s, String s1) {
    }

    public String getBookingReason() {
        return BookingReason;
    }

    public void setBookingReason(String bookingReason) {
        BookingReason = bookingReason;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
