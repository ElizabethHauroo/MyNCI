package com.example.mynciapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomSize implements Parcelable {

    //here the names should match exactly what is in the database
    private String room_num, floor, roomId;

    public RoomSize() {
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    protected RoomSize(Parcel in) {
        room_num = in.readString();
        floor = in.readString();
        roomId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(room_num);
        dest.writeString(floor);
        dest.writeString(roomId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoomSize> CREATOR = new Creator<RoomSize>() {
        @Override
        public RoomSize createFromParcel(Parcel in) {
            return new RoomSize(in);
        }

        @Override
        public RoomSize[] newArray(int size) {
            return new RoomSize[size];
        }
    };
}
