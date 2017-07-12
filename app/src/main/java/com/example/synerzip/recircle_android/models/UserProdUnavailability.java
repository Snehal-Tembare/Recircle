package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 12/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProdUnavailability implements Parcelable {

    private String unavai_from_date;
    private String unavai_to_date;

    protected UserProdUnavailability(Parcel in) {
        unavai_from_date = in.readString();
        unavai_to_date = in.readString();
    }

    public static final Creator<UserProdUnavailability> CREATOR = new Creator<UserProdUnavailability>() {
        @Override
        public UserProdUnavailability createFromParcel(Parcel in) {
            return new UserProdUnavailability(in);
        }

        @Override
        public UserProdUnavailability[] newArray(int size) {
            return new UserProdUnavailability[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(unavai_from_date);
        dest.writeString(unavai_to_date);
    }
}
