package com.example.synerzip.recircle_android.models.rentals;

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

public class OrderDetails implements Parcelable{

    private ArrayList<UserRequests> userRequests;
    private ArrayList<UserRentings> userRentings;

    protected OrderDetails(Parcel in) {
        userRequests=in.readParcelable(UserRequests.class.getClassLoader());
        userRentings=in.readParcelable(UserRentings.class.getClassLoader());
    }

    public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(userRequests);
        dest.writeList(userRentings);
    }
}
