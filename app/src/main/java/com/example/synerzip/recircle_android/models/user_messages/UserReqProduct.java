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

public class UserReqProduct implements Parcelable {

    private String product_id;

    private String user_product_id;

    private UserReqProductDetails product;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.user_product_id);
        dest.writeParcelable(this.product, flags);
    }

    public UserReqProduct() {
    }

    protected UserReqProduct(Parcel in) {
        this.product_id = in.readString();
        this.user_product_id = in.readString();
        this.product = in.readParcelable(UserReqProductDetails.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserReqProduct> CREATOR = new Parcelable.Creator<UserReqProduct>() {
        @Override
        public UserReqProduct createFromParcel(Parcel source) {
            return new UserReqProduct(source);
        }

        @Override
        public UserReqProduct[] newArray(int size) {
            return new UserReqProduct[size];
        }
    };
}
