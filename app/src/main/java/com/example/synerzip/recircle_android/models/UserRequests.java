package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserRequests implements Parcelable{

    protected UserRequests(Parcel in) {
    }

    public static final Creator<UserRequests> CREATOR = new Creator<UserRequests>() {
        @Override
        public UserRequests createFromParcel(Parcel in) {
            return new UserRequests(in);
        }

        @Override
        public UserRequests[] newArray(int size) {
            return new UserRequests[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
