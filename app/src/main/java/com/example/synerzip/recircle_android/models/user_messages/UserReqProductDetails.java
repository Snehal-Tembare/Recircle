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

class UserReqProductDetails implements Parcelable {

    private String product_title;

    private String product_manufacturer_id;

    private ProductReqManufacturer product_manufacturer;

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

    public UserReqProductDetails() {
    }

    protected UserReqProductDetails(Parcel in) {
        this.product_title = in.readString();
        this.product_manufacturer_id = in.readString();
        this.product_manufacturer = in.readParcelable(ProductReqManufacturer.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserReqProductDetails> CREATOR = new Parcelable.Creator<UserReqProductDetails>() {
        @Override
        public UserReqProductDetails createFromParcel(Parcel source) {
            return new UserReqProductDetails(source);
        }

        @Override
        public UserReqProductDetails[] newArray(int size) {
            return new UserReqProductDetails[size];
        }
    };
}
