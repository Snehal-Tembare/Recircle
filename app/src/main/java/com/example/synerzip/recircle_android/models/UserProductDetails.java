package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProductDetails implements Parcelable {
    private String user_product_id;
    private int price_per_day;
    private String user_prod_desc;
    private String created_at;
    private int product_avg_rating;
    private ArrayList<UserProdReview> user_prod_reviews;
    private ArrayList<UserProdImages> user_prod_images;
    private String product_manufacturer_name;
    private String product_title;

    protected UserProductDetails(Parcel in) {
        user_product_id = in.readString();
        price_per_day = in.readInt();
        user_prod_desc = in.readString();
        created_at = in.readString();
        product_avg_rating = in.readInt();
        user_prod_reviews = in.createTypedArrayList(UserProdReview.CREATOR);
        user_prod_images = in.createTypedArrayList(UserProdImages.CREATOR);
        product_manufacturer_name = in.readString();
        product_title = in.readString();
    }

    public static final Creator<UserProductDetails> CREATOR = new Creator<UserProductDetails>() {
        @Override
        public UserProductDetails createFromParcel(Parcel in) {
            return new UserProductDetails(in);
        }

        @Override
        public UserProductDetails[] newArray(int size) {
            return new UserProductDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_product_id);
        dest.writeInt(price_per_day);
        dest.writeString(user_prod_desc);
        dest.writeString(created_at);
        dest.writeInt(product_avg_rating);
        dest.writeTypedList(user_prod_reviews);
        dest.writeTypedList(user_prod_images);
        dest.writeString(product_manufacturer_name);
        dest.writeString(product_title);
    }
}
