package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 20/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class EditProduct implements Parcelable {

    private String user_product_id;
    private Integer price_per_day;
    private String user_prod_desc;
    private String user_product_zipcode;
    private Integer fromAustin;
    private Integer min_rental_days;
    private ArrayList<Discounts> user_prod_discounts;
    private ArrayList<UserProdImages> user_prod_images;
    private ArrayList<UserProductUnAvailability> user_prod_unavailability;

    public EditProduct() {
    }

    public EditProduct(Parcel in) {
        user_product_id = in.readString();
        price_per_day = in.readInt();
        user_product_zipcode = in.readString();
        user_prod_discounts = in.createTypedArrayList(Discounts.CREATOR);
        user_prod_images = in.createTypedArrayList(UserProdImages.CREATOR);
        user_prod_unavailability = in.createTypedArrayList(UserProductUnAvailability.CREATOR);
    }

    public static final Creator<EditProduct> CREATOR = new Creator<EditProduct>() {
        @Override
        public EditProduct createFromParcel(Parcel in) {
            return new EditProduct(in);
        }

        @Override
        public EditProduct[] newArray(int size) {
            return new EditProduct[size];
        }
    };

    public EditProduct(String user_product_id,
                       Integer price_per_day,
                       String user_prod_desc,
                       String user_product_zipcode,
                       Integer fromAustin,
                       Integer min_rental_days,
                       ArrayList<Discounts> user_prod_discounts,
                       ArrayList<UserProdImages> user_prod_images,
                       ArrayList<UserProductUnAvailability> user_prod_unavailability) {
        this.user_product_id = user_product_id;
        this.price_per_day = price_per_day;
        this.user_prod_desc = user_prod_desc;
        this.user_product_zipcode = user_product_zipcode;
        this.fromAustin = fromAustin;
        this.min_rental_days = min_rental_days;
        this.user_prod_discounts = user_prod_discounts;
        this.user_prod_images = user_prod_images;
        this.user_prod_unavailability = user_prod_unavailability;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_product_id);
        dest.writeInt(price_per_day);
        dest.writeString(user_product_zipcode);
        dest.writeTypedList(user_prod_discounts);
        dest.writeTypedList(user_prod_images);
        dest.writeTypedList(user_prod_unavailability);
    }
}
