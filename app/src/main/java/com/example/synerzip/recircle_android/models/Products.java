package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 10/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class Products implements Parcelable{

    private UserInfo user_info;

    private ProductInfo product_info;

    private UserProductInfo user_product_info;

    protected Products(Parcel in) {
        user_info = in.readParcelable(UserInfo.class.getClassLoader());
        product_info = in.readParcelable(ProductInfo.class.getClassLoader());
        user_product_info = in.readParcelable(UserProductInfo.class.getClassLoader());
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user_info, flags);
        dest.writeParcelable(product_info, flags);
        dest.writeParcelable(user_product_info, flags);
    }
}
