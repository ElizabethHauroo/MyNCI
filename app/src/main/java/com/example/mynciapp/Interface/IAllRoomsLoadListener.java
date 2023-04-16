package com.example.mynciapp.Interface;

import java.util.List;

public interface IAllRoomsLoadListener {

    void onAllRoomsLoadSuccess(List<String> roomSizeList);
    void onAllRoomsLoadFailed(String message);
}
