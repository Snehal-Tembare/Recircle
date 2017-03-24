package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 10/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class SearchProduct implements Parcelable {
    private ArrayList<Products> products;

    protected SearchProduct(Parcel in) {
        this.products = new ArrayList<Products>();

        in.readTypedList(products, Products.CREATOR);
    }

    public static final Creator<SearchProduct> CREATOR = new Creator<SearchProduct>() {
        @Override
        public SearchProduct createFromParcel(Parcel in) {
            return new SearchProduct(in);
        }

        @Override
        public SearchProduct[] newArray(int size) {
            return new SearchProduct[size];
        }
    };

    public SearchProduct(Products products,ArrayList<Products> productsArrayList) {
        this.products = productsArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(products);
    }
}
