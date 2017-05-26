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
class ProductDetail implements Parcelable  {
    private String product_image_url;

    ProductDetail(Parcel in) {
        product_image_url = in.readString();
    }

    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_image_url);
    }
}
