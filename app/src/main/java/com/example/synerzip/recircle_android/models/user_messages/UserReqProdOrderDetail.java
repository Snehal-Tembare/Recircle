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
class UserReqProdOrderDetail implements Parcelable {

    private String status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
    }

    public UserReqProdOrderDetail() {
    }

    protected UserReqProdOrderDetail(Parcel in) {
        this.status = in.readString();
    }

    public static final Parcelable.Creator<UserReqProdOrderDetail> CREATOR = new Parcelable.Creator<UserReqProdOrderDetail>() {
        @Override
        public UserReqProdOrderDetail createFromParcel(Parcel source) {
            return new UserReqProdOrderDetail(source);
        }

        @Override
        public UserReqProdOrderDetail[] newArray(int size) {
            return new UserReqProdOrderDetail[size];
        }
    };
}
