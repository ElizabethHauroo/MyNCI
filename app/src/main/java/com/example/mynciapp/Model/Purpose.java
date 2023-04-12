package com.example.mynciapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Purpose implements Parcelable {

    //here the variables should match exactly what is in the database collection
    private String reason, username, Description, purposeId;
    private Long rating;

    public Purpose() {
    }

    protected Purpose(Parcel in) {
        reason = in.readString();
        username = in.readString();
        Description = in.readString();
        purposeId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<Purpose> CREATOR = new Creator<Purpose>() {
        @Override
        public Purpose createFromParcel(Parcel in) {
            return new Purpose(in);
        }

        @Override
        public Purpose[] newArray(int size) {
            return new Purpose[size];
        }
    };

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reason);
        parcel.writeString(username);
        parcel.writeString(Description);
        parcel.writeString(purposeId);
        if (rating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(rating);
        }
    }
}
