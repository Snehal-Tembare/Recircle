package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 4/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class User implements Parcelable {
    private String first_name;
    private String last_name;
    private String user_image_url;

    protected User(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        user_image_url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(user_image_url);
    }
}
