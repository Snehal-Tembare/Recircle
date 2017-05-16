package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class AllProductInfo implements Parcelable {

    private ArrayList<Products> popularProducts;

    private ArrayList<Products> productDetails;

    public AllProductInfo(Parcel in) {
        popularProducts = in.createTypedArrayList(Products.CREATOR);
        productDetails = in.createTypedArrayList(Products.CREATOR);
    }

    public static final Creator<AllProductInfo> CREATOR = new Creator<AllProductInfo>() {
        @Override
        public AllProductInfo createFromParcel(Parcel in) {
            return new AllProductInfo(in);
        }

        @Override
        public AllProductInfo[] newArray(int size) {
            return new AllProductInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(popularProducts);
        dest.writeTypedList(productDetails);
    }
}
