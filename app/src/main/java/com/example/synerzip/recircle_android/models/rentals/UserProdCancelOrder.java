package com.example.synerzip.recircle_android.models.rentals;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.synerzip.recircle_android.models.User;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 8/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProdCancelOrder implements Parcelable {
    private String cancel_by;
    private String user_msg;
    private String created_at;
    private User user;

    protected UserProdCancelOrder(Parcel in) {
        cancel_by = in.readString();
        user_msg = in.readString();
        created_at = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<UserProdCancelOrder> CREATOR = new Creator<UserProdCancelOrder>() {
        @Override
        public UserProdCancelOrder createFromParcel(Parcel in) {
            return new UserProdCancelOrder(in);
        }

        @Override
        public UserProdCancelOrder[] newArray(int size) {
            return new UserProdCancelOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cancel_by);
        dest.writeString(user_msg);
        dest.writeString(created_at);
        dest.writeParcelable(user, flags);
    }
}
