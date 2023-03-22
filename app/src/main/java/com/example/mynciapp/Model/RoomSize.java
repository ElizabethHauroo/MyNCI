package com.example.mynciapp.Model;

public class RoomSize {

    private String roomNum, floor;

    public RoomSize() {
    }

    public RoomSize(String roomNum, String floor) {
        this.roomNum = roomNum;
        this.floor = floor;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
