package com.example.synerzip.recircle_android.models.rentals;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserRequest implements Parcelable{
    private String user_msg;
    private String action;

    public UserRequest(){}

    public UserRequest(Parcel in) {
        user_msg = in.readString();
        action = in.readString();
    }

    public static final Creator<UserRequest> CREATOR = new Creator<UserRequest>() {
        @Override
        public UserRequest createFromParcel(Parcel in) {
            return new UserRequest(in);
        }

        @Override
        public UserRequest[] newArray(int size) {
            return new UserRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_msg);
        dest.writeString(action);
    }
}
