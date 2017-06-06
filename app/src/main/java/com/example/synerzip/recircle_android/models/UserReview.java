package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 24/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class UserReview implements Parcelable{
    private String prod_review_id;
    private String user_product_id;
    private String reviewer_id;
    private String user_prod_order_id;
    private Integer prod_rating;
    private String prod_review;
    private String created_at;

    protected UserReview(Parcel in) {
        prod_review_id = in.readString();
        user_product_id = in.readString();
        reviewer_id = in.readString();
        user_prod_order_id = in.readString();
        prod_review = in.readString();
        created_at = in.readString();
    }

    public static final Creator<UserReview> CREATOR = new Creator<UserReview>() {
        @Override
        public UserReview createFromParcel(Parcel in) {
            return new UserReview(in);
        }

        @Override
        public UserReview[] newArray(int size) {
            return new UserReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prod_review_id);
        dest.writeString(user_product_id);
        dest.writeString(reviewer_id);
        dest.writeString(user_prod_order_id);
        dest.writeString(prod_review);
        dest.writeString(created_at);
    }
}
