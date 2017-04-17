package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProdImages implements Parcelable{
    private String user_prod_image_url;

    private String created_at;

    protected UserProdImages(Parcel in) {
        user_prod_image_url = in.readString();
        created_at = in.readString();
    }

    public static final Creator<UserProdImages> CREATOR = new Creator<UserProdImages>() {
        @Override
        public UserProdImages createFromParcel(Parcel in) {
            return new UserProdImages(in);
        }

        @Override
        public UserProdImages[] newArray(int size) {
            return new UserProdImages[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_prod_image_url);
        dest.writeString(created_at);
    }
}
