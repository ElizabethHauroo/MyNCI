package com.example.mynciapp.Model;

public class RoomSize {

    //here the names should match exactly what is in the database
    private String room_num, floor;

    public RoomSize() {
    }

    public RoomSize(String roomNum, String floor) {
        this.room_num = roomNum;
        this.floor = floor;
    }



    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
