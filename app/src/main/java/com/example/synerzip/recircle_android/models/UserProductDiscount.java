package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 5/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProductDiscount implements Parcelable {
    private String userProdDiscId;
    private Integer discountForDays;
    private Integer percentage;
    private Boolean isActive;

    protected UserProductDiscount(Parcel in) {
        userProdDiscId = in.readString();
    }

    public static final Creator<UserProductDiscount> CREATOR = new Creator<UserProductDiscount>() {
        @Override
        public UserProductDiscount createFromParcel(Parcel in) {
            return new UserProductDiscount(in);
        }

        @Override
        public UserProductDiscount[] newArray(int size) {
            return new UserProductDiscount[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userProdDiscId);
    }
}
