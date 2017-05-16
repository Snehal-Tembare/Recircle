package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserInfo implements Parcelable{

    private String user_id;

    private String first_name;

    private String user_image_url;

    private String email;

    private ArrayList<UserAddresses> user_addresses;

    private String last_name;

    protected UserInfo(Parcel in) {
        user_id = in.readString();
        first_name = in.readString();
        user_image_url = in.readString();
        email = in.readString();
        last_name = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(first_name);
        dest.writeString(user_image_url);
        dest.writeString(email);
        dest.writeString(last_name);
    }
}
