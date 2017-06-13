package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserAddress implements Parcelable {

    private String user_address_id;

    private String street;

    private String city;

    private String state;

    private int zip;

    /**
     * constructor for UserAddress
     * @param user_address_id
     * @param street
     * @param city
     * @param state
     * @param zip
     */
    public UserAddress(String user_address_id, String street, String city, String state, int zip) {
        this.user_address_id = user_address_id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    protected UserAddress(Parcel in) {
        user_address_id = in.readString();
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readInt();
    }

    public static final Creator<UserAddress> CREATOR = new Creator<UserAddress>() {
        @Override
        public UserAddress createFromParcel(Parcel in) {
            return new UserAddress(in);
        }

        @Override
        public UserAddress[] newArray(int size) {
            return new UserAddress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_address_id);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(zip);
    }
}
