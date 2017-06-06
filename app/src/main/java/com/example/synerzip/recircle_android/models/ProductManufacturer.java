package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 24/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

class ProductManufacturer implements Parcelable {
    private String product_manufacturer_name;

    ProductManufacturer(Parcel in){
        product_manufacturer_name=in.readString();
    }

    public static final Creator<ProductManufacturer> CREATOR = new Creator<ProductManufacturer>() {
        @Override
        public ProductManufacturer createFromParcel(Parcel in) {
            return new ProductManufacturer(in);
        }

        @Override
        public ProductManufacturer[] newArray(int size) {
            return new ProductManufacturer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_manufacturer_name);

    }
}
