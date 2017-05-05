package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserProductUnAvailability implements Parcelable {

    private String unavai_from_date;

    private String unavai_to_date;

    /**
     * creator object for userProductUnavailability
     */
    public static final Creator<UserProductUnAvailability> CREATOR = new Creator<UserProductUnAvailability>() {
        @Override
        public UserProductUnAvailability createFromParcel(Parcel in) {
            return new UserProductUnAvailability(in);
        }

        @Override
        public UserProductUnAvailability[] newArray(int size) {
            return new UserProductUnAvailability[size];
        }
    };

    /**
     * constructor for userProductUnavailability
     *
     * @param in
     */
    public UserProductUnAvailability(Parcel in) {
        unavai_from_date = in.readString();
        unavai_to_date = in.readString();

    }

    public UserProductUnAvailability(String unavai_from_date, String unavai_to_date) {
        this.unavai_from_date = unavai_from_date;
        this.unavai_to_date = unavai_to_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(unavai_from_date);
        dest.writeString(unavai_to_date);

    }
}
