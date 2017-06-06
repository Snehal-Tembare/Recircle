package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class UserProdMsg implements Parcelable{
    private String user_msg;
    private String user_prod_msg_id;
    private ArrayList<UserProdMsgPool> user_prod_msg_pools;

    protected UserProdMsg(Parcel in) {
        user_msg = in.readString();
        user_prod_msg_id = in.readString();
        user_prod_msg_pools = in.createTypedArrayList(UserProdMsgPool.CREATOR);
    }

    public static final Creator<UserProdMsg> CREATOR = new Creator<UserProdMsg>() {
        @Override
        public UserProdMsg createFromParcel(Parcel in) {
            return new UserProdMsg(in);
        }

        @Override
        public UserProdMsg[] newArray(int size) {
            return new UserProdMsg[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_msg);
        dest.writeString(user_prod_msg_id);
        dest.writeTypedList(user_prod_msg_pools);
    }
}
