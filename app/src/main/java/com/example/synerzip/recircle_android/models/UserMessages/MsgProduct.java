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
public class MsgProduct implements Parcelable {

    private String product_title;
    private String product_manufacturer_id;
    private MsgProdManufacturer product_manufacturer;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_title);
        dest.writeString(this.product_manufacturer_id);
        dest.writeParcelable(this.product_manufacturer, flags);
    }

    public MsgProduct() {
    }

    protected MsgProduct(Parcel in) {
        this.product_title = in.readString();
        this.product_manufacturer_id = in.readString();
        this.product_manufacturer = in.readParcelable(MsgProdManufacturer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MsgProduct> CREATOR = new Parcelable.Creator<MsgProduct>() {
        @Override
        public MsgProduct createFromParcel(Parcel source) {
            return new MsgProduct(source);
        }

        @Override
        public MsgProduct[] newArray(int size) {
            return new MsgProduct[size];
        }
    };
}