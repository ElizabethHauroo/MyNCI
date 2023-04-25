package com.example.mynciapp.BookingModels;

import java.io.Serializable;

public class RoomBooking implements Serializable {

        private String size;
        private String roomNumber;
        private String description;


        public RoomBooking() {
        }

        public RoomBooking(String size, String roomNumber, String description) {
                this.size = size;
                this.roomNumber = roomNumber;
                this.description = description;
        }

        public String getSize() {
                return size;
        }

        public void setSize(String size) {
                this.size = size;
        }

        public String getRoomNumber() {
                return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
                this.roomNumber = roomNumber;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }


}
