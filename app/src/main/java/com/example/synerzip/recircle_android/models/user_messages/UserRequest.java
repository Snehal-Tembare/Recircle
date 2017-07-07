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
public class UserRequest implements Parcelable {
    private String first_name;

    private String last_name;

    private String user_image_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.user_image_url);
    }

    public UserRequest() {
    }

    protected UserRequest(Parcel in) {
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.user_image_url = in.readString();
    }

    public static final Parcelable.Creator<UserRequest> CREATOR = new Parcelable.Creator<UserRequest>() {
        @Override
        public UserRequest createFromParcel(Parcel source) {
            return new UserRequest(source);
        }

        @Override
        public UserRequest[] newArray(int size) {
            return new UserRequest[size];
        }
    };
}
