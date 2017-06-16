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

class ProductReqManufacturer implements Parcelable {

    private String product_manufacturer_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_manufacturer_name);
    }

    public ProductReqManufacturer() {
    }

    protected ProductReqManufacturer(Parcel in) {
        this.product_manufacturer_name = in.readString();
    }

    public static final Parcelable.Creator<ProductReqManufacturer> CREATOR = new Parcelable.Creator<ProductReqManufacturer>() {
        @Override
        public ProductReqManufacturer createFromParcel(Parcel source) {
            return new ProductReqManufacturer(source);
        }

        @Override
        public ProductReqManufacturer[] newArray(int size) {
            return new ProductReqManufacturer[size];
        }
    };
}
