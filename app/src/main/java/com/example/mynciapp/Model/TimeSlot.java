package com.example.mynciapp.Model;

public class TimeSlot {

    private Long slot;
    private boolean available;

    public TimeSlot() {
    }

    public TimeSlot(Long slot, boolean available) {
        this.slot = slot;
        this.available = available;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
