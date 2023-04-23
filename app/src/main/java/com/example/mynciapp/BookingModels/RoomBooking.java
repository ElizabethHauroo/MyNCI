package com.example.mynciapp.BookingModels;

public class RoomBooking {

        private String size;
        private int roomNumber;
        private String description;
        private String roomID;

        public RoomBooking() {
        }

        public RoomBooking(String size, int roomNumber, String description, String roomID) {
                this.size = size;
                this.roomNumber = roomNumber;
                this.description = description;
                this.roomID = roomID;
        }

        public String getSize() {
                return size;
        }

        public void setSize(String size) {
                this.size = size;
        }

        public int getRoomNumber() {
                return roomNumber;
        }

        public void setRoomNumber(int roomNumber) {
                this.roomNumber = roomNumber;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getRoomID() {
                return roomID;
        }

        public void setRoomID(String roomID) {
                this.roomID = roomID;
        }
}
