package com.example.synerzip.recircle_android.models.UserMessages;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 11/7/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class MsgProdManufacturer implements Parcelable {

    private String product_manufacturer_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_manufacturer_name);
    }

    public MsgProdManufacturer() {
    }

    protected MsgProdManufacturer(Parcel in) {
        this.product_manufacturer_name = in.readString();
    }

    public static final Parcelable.Creator<MsgProdManufacturer> CREATOR = new Parcelable.Creator<MsgProdManufacturer>() {
        @Override
        public MsgProdManufacturer createFromParcel(Parcel source) {
            return new MsgProdManufacturer(source);
        }

        @Override
        public MsgProdManufacturer[] newArray(int size) {
            return new MsgProdManufacturer[size];
        }
    };
}
