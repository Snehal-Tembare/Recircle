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
public class UserReqProdMsgPool implements Parcelable {

    private String user_prod_msg_pool_id;

    private String user_id;

    private String user_msg;

    private boolean is_read;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_prod_msg_pool_id);
        dest.writeString(this.user_id);
        dest.writeString(this.user_msg);
        dest.writeByte(this.is_read ? (byte) 1 : (byte) 0);
    }

    public UserReqProdMsgPool() {
    }

    protected UserReqProdMsgPool(Parcel in) {
        this.user_prod_msg_pool_id = in.readString();
        this.user_id = in.readString();
        this.user_msg = in.readString();
        this.is_read = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UserReqProdMsgPool> CREATOR = new Parcelable.Creator<UserReqProdMsgPool>() {
        @Override
        public UserReqProdMsgPool createFromParcel(Parcel source) {
            return new UserReqProdMsgPool(source);
        }

        @Override
        public UserReqProdMsgPool[] newArray(int size) {
            return new UserReqProdMsgPool[size];
        }
    };
}
