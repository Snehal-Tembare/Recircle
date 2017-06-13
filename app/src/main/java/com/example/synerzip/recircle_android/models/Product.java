package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 17/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class Product implements Parcelable {

    private String product_id;
    private String product_title;

    //var for internal logic
    private String product_manufacturer_id;
    private String product_manufacturer_name;
    //manufacturer name + product title
    private String product_manufacturer_title;

    private ProductDetail product_detail;
    private ProductManufacturer product_manufacturer;

    public Product(){}

     Product(Parcel in) {
        product_id = in.readString();
        product_title = in.readString();
        product_manufacturer_id = in.readString();
        product_manufacturer_name = in.readString();
        product_manufacturer_title = in.readString();
        product_detail = in.readParcelable(ProductDetail.class.getClassLoader());
        product_manufacturer = in.readParcelable(ProductManufacturer.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_id);
        dest.writeString(product_title);
        dest.writeString(product_manufacturer_id);
        dest.writeString(product_manufacturer_name);
        dest.writeString(product_manufacturer_title);
        dest.writeParcelable(product_detail, flags);
        dest.writeParcelable(product_manufacturer, flags);
    }
}
