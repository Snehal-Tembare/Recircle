package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 24/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProdMsgPool implements Parcelable {
    private String user_msg;

    protected UserProdMsgPool(Parcel in) {
        user_msg = in.readString();
    }

    public static final Creator<UserProdMsgPool> CREATOR = new Creator<UserProdMsgPool>() {
        @Override
        public UserProdMsgPool createFromParcel(Parcel in) {
            return new UserProdMsgPool(in);
        }

        @Override
        public UserProdMsgPool[] newArray(int size) {
            return new UserProdMsgPool[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_msg);
    }
}
