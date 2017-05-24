package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserRentings implements Parcelable {

    private String user_prod_order_id;
    private String date_on_order;
    private String order_from_date;
    private String order_to_date;
    private String status;
    private String updated_at;
    private OrderPaymentDetail order_payment_detail;
    private Object userReview;
    private User user;
    private UserProdMsg user_prod_msg;
    private String user_product_id;
    private String product_id;
    private Integer price_per_day;
    private Integer product_avg_rating;
    private Product product;


    protected UserRentings(Parcel in) {
        user_prod_order_id = in.readString();
        date_on_order = in.readString();
        order_from_date = in.readString();
        order_to_date = in.readString();
        status = in.readString();
        updated_at = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        user_prod_msg = in.readParcelable(UserProdMsg.class.getClassLoader());
        user_product_id = in.readString();
        product_id = in.readString();
    }

    public static final Creator<UserRentings> CREATOR = new Creator<UserRentings>() {
        @Override
        public UserRentings createFromParcel(Parcel in) {
            return new UserRentings(in);
        }

        @Override
        public UserRentings[] newArray(int size) {
            return new UserRentings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_prod_order_id);
        dest.writeString(date_on_order);
        dest.writeString(order_from_date);
        dest.writeString(order_to_date);
        dest.writeString(status);
        dest.writeString(updated_at);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(user_prod_msg, flags);
        dest.writeString(user_product_id);
        dest.writeString(product_id);
    }
}
