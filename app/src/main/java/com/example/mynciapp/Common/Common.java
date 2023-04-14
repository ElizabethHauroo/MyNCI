package com.example.mynciapp.Common;

import com.example.mynciapp.Model.Purpose;
import com.example.mynciapp.Model.RoomSize;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT" ;
    public static final String KEY_SIZE_STORE = "SIZE_SAVE" ;
    public static final String KEY_PURPOSE_LOAD_DONE = "PURPOSE_LOAD_DONE" ;
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT" ;
    public static final String KEY_STEP = "STEP" ;
    public static final String KEY_PURPOSE_SELECTED = "PURPOSE_SELECTED";
    public static final int TIME_SLOT_TOTAL = 9; //20 if half hour slots.. tbd
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static RoomSize currentRoom;
    public static int step = 0; // At the start of the entire process, the first step is 0
    public static String size="";
    public static Purpose currentPurpose;
    public static int currentTimeSlot=-1;
    public static Calendar currentDate=Calendar.getInstance();
    public static User currentUser;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

    public static String convertTimeSlotToString(int slot) {
        switch(slot) {
            case 0:
                return "9:00 - 10:00";
            case 1:
                return "10:00 - 11:00";
            case 2:
                return "11:00 - 12:00";
            case 3:
                return "12:00 - 13:00";
            case 4:
                return "13:00 - 14:00";
            case 5:
                return "14:00 - 15:00";
            case 6:
                return "15:00 - 16:00";
            case 7:
                return "16:00 - 17:00";
            case 8:
                return "17:00 - 18:00";
            default:
                return "Closed";


        }


    }
}
