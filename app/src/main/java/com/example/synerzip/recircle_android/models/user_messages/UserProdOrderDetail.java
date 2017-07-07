package com.example.synerzip.recircle_android.models.user_messages;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserProdOrderDetail implements Parcelable {
    private String status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
    }

    public UserProdOrderDetail() {
    }

    protected UserProdOrderDetail(Parcel in) {
        this.status = in.readString();
    }

    public static final Parcelable.Creator<UserProdOrderDetail> CREATOR = new Parcelable.Creator<UserProdOrderDetail>() {
        @Override
        public UserProdOrderDetail createFromParcel(Parcel source) {
            return new UserProdOrderDetail(source);
        }

        @Override
        public UserProdOrderDetail[] newArray(int size) {
            return new UserProdOrderDetail[size];
        }
    };
}
