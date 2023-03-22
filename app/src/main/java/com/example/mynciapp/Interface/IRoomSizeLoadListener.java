package com.example.mynciapp.Interface;

import com.example.mynciapp.Model.RoomSize;

import java.util.List;

public interface IRoomSizeLoadListener {
    void onRoomSizeLoadSuccess(List<RoomSize> sizeList);
    void onRoomSizeLoadFailed(String message);
}
