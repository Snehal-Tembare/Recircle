package com.example.synerzip.recircle_android.models.rentals;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProdMsg;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserRequests implements Parcelable{

    private String user_prod_order_id;
    private String request_id;
    private String renter_id;
    private String date_on_order;
    private String order_from_date;
    private String order_to_date;
    private String status;
    private String updated_at;
    private OrderPaymentDetail order_payment_detail;
    private User user;
    private UserProdMsg user_prod_msg;

    private String user_product_id;
    private ArrayList<UserProdImages> user_prod_images;
    private String product_id;
    private Integer price_per_day;
    private Integer product_avg_rating;
    private Product product;
    private UserProdCancelOrder user_prod_cancel_order;

    protected UserRequests(Parcel in) {
        user_prod_order_id=in.readString();
        request_id=in.readString();
        renter_id=in.readString();
        date_on_order=in.readString();
        order_from_date=in.readString();
        order_to_date=in.readString();
        status=in.readString();
        updated_at=in.readString();
        order_payment_detail=in.readParcelable(OrderPaymentDetail.class.getClassLoader());
        user=in.readParcelable(User.class.getClassLoader());
        user_prod_msg=in.readParcelable(UserProdMsg.class.getClassLoader());
        user_product_id=in.readString();
        product_id=in.readString();
        price_per_day=in.readInt();
        product_avg_rating=in.readInt();
        product=in.readParcelable(Product.class.getClassLoader());
        user_prod_cancel_order=in.readParcelable(UserProdCancelOrder.class.getClassLoader());
    }

    public static final Creator<UserRequests> CREATOR = new Creator<UserRequests>() {
        @Override
        public UserRequests createFromParcel(Parcel in) {
            return new UserRequests(in);
        }

        @Override
        public UserRequests[] newArray(int size) {
            return new UserRequests[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_prod_order_id);
        dest.writeString(request_id);
        dest.writeString(renter_id);
        dest.writeString(date_on_order);
        dest.writeString(order_from_date);
        dest.writeString(order_to_date);
        dest.writeString(status);
        dest.writeString(status);
        dest.writeParcelable(order_payment_detail,flags);
        dest.writeString(user_product_id);
        dest.writeString(product_id);
        dest.writeParcelable(user_prod_cancel_order,flags);
        dest.writeParcelable(user_prod_msg,flags);
        dest.writeInt(price_per_day);
        dest.writeInt(product_avg_rating);
    }
}
