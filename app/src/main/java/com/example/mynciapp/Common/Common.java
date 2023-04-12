package com.example.mynciapp.Common;

import com.example.mynciapp.Model.Purpose;
import com.example.mynciapp.Model.RoomSize;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT" ;
    public static final String KEY_SIZE_STORE = "SIZE_SAVE" ;
    public static final String KEY_PURPOSE_LOAD_DONE = "PURPOSE_LOAD_DONE" ;
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT" ;
    public static final String KEY_STEP = "STEP" ;
    public static final String KEY_PURPOSE_SELECTED = "PURPOSE_SELECTED";
    public static RoomSize currentRoom;
    public static int step = 0; // At the start of the entire process, the first step is 0
    public static String size="";
    public static Purpose currentPurpose;
}
