package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class User implements Parcelable {
    private String first_name;
    private String last_name;
    private String user_image_url;
    private String message;
    private String user_id;
    private String token;
    private String email;
    private long user_mob_no;

    //user profile
    private String user_avg_rating;
    private ArrayList<UserProductDetails> userProductDetails;

    /**
     * creator object for user
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * constructor for user
     *
     * @param in
     */
    public User(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        user_image_url = in.readString();
        message = in.readString();
        user_id = in.readString();
        token = in.readString();
        email = in.readString();
        user_mob_no = in.readLong();
        user_avg_rating = in.readString();
        userProductDetails = in.createTypedArrayList(UserProductDetails.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(user_image_url);
        dest.writeString(message);
        dest.writeString(email);
        dest.writeString(token);
        dest.writeString(user_id);
        dest.writeLong(user_mob_no);
        dest.writeString(user_avg_rating);
        dest.writeTypedList(userProductDetails);
    }
}
