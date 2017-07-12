package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class ProductInfo implements Parcelable {

    private String product_manufacturer_id;

    private String product_manufacturer_name;

    private String product_category_id;

    private String product_category_name;

    private String product_category_description;

    private String product_title;

    private String product_description;

    private String product_price;

    private UserProdImages product_image_url;

    private String product_model_id;

    protected ProductInfo(Parcel in) {
        product_manufacturer_name = in.readString();
        product_manufacturer_id = in.readString();
//        product_image_url = in.readParcelable(UserProdImages.class.getClassLoader());
        product_model_id = in.readString();
        product_title = in.readString();
        product_category_name = in.readString();
        product_price = in.readString();
        product_category_description = in.readString();
        product_category_id = in.readString();
        product_description = in.readString();
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel in) {
            return new ProductInfo(in);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_manufacturer_name);
        dest.writeString(product_manufacturer_id);
//        dest.writeParcelable(product_image_url, flags);
        dest.writeString(product_model_id);
        dest.writeString(product_title);
        dest.writeString(product_category_name);
        dest.writeString(product_price);
        dest.writeString(product_category_description);
        dest.writeString(product_category_id);
        dest.writeString(product_description);
    }
}
