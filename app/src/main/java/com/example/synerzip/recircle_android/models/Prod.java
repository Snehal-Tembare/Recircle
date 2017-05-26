package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 25/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class Prod implements Parcelable {

    private String product_title;
    private String product_manufacturer_id;

    private  ProductDetail product_detail;

    private ProductManufacturer product_manufacturer;

    protected Prod(Parcel in) {
        product_title = in.readString();
        product_manufacturer_id = in.readString();
        product_detail = in.readParcelable(ProductDetail.class.getClassLoader());
        product_manufacturer = in.readParcelable(ProductManufacturer.class.getClassLoader());
    }

    public static final Creator<Prod> CREATOR = new Creator<Prod>() {
        @Override
        public Prod createFromParcel(Parcel in) {
            return new Prod(in);
        }

        @Override
        public Prod[] newArray(int size) {
            return new Prod[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_title);
        dest.writeString(product_manufacturer_id);
        dest.writeParcelable(product_detail, flags);
        dest.writeParcelable(product_manufacturer, flags);
    }
}
