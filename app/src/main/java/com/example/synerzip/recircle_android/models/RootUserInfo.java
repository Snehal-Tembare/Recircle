package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter
public class RootUserInfo implements Parcelable {
    private String user_id;

    private String first_name;

    private String last_name;

    private String email;

    private String user_image_url;

    private boolean notification_flag;

    private long user_mob_no;

    private boolean mobile_verified;

    private UserAddress userAddress;

    private UserAccDetails user_acc_details;

    public RootUserInfo(String user_id, String first_name, String last_name,
                        String email, String user_image_url, boolean notification_flag,
                        long user_mob_no, boolean mobile_verified, UserAddress userAddress,
                        UserAccDetails user_acc_details) {

        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.user_image_url = user_image_url;
        this.notification_flag = notification_flag;
        this.user_mob_no = user_mob_no;
        this.mobile_verified = mobile_verified;
        this.userAddress = userAddress;
        this.user_acc_details = user_acc_details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * creator object for user
     */
    public static final Creator<RootUserInfo> CREATOR = new Creator<RootUserInfo>() {
        @Override
        public RootUserInfo createFromParcel(Parcel in) {
            return new RootUserInfo(in);
        }

        @Override
        public RootUserInfo[] newArray(int size) {
            return new RootUserInfo[size];
        }
    };

    /**
     * constructor for RootUserInfo
     *
     * @param in
     */
    public RootUserInfo(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        user_image_url = in.readString();
        user_id = in.readString();
        email = in.readString();
        user_mob_no = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(user_image_url);
        dest.writeString(email);
        dest.writeLong(user_mob_no);
        dest.writeString(user_id);
    }


}
