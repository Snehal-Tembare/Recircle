package com.example.synerzip.recircle_android.models.rentals;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 13/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class CancelOrder implements Parcelable {
    private String user_prod_order_id;
    private String user_msg;

    public CancelOrder() {
    }

    protected CancelOrder(Parcel in) {
        user_prod_order_id = in.readString();
        user_msg = in.readString();
    }

    public static final Creator<CancelOrder> CREATOR = new Creator<CancelOrder>() {
        @Override
        public CancelOrder createFromParcel(Parcel in) {
            return new CancelOrder(in);
        }

        @Override
        public CancelOrder[] newArray(int size) {
            return new CancelOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_prod_order_id);
        dest.writeString(user_msg);
    }
}
