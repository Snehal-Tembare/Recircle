package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 15/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Setter
@Getter
public class RentItem implements Parcelable {
     private String user_product_id;
    private String date_on_order;
    private String order_from_date;
    private String order_to_date;
    private String user_msg;
    private int payment_total;
    private int payment_discount;
    private int service_fee;
    private int protection_plan_fee;
    private int final_payment;
    private int protection_plan;

    public RentItem(){}

    protected RentItem(Parcel in) {
        user_product_id = in.readString();
        date_on_order = in.readString();
        order_from_date = in.readString();
        order_to_date = in.readString();
        user_msg = in.readString();
        payment_total = in.readInt();
        payment_discount = in.readInt();
        service_fee = in.readInt();
        protection_plan_fee = in.readInt();
        final_payment = in.readInt();
        protection_plan = in.readInt();
    }

    public static final Creator<RentItem> CREATOR = new Creator<RentItem>() {
        @Override
        public RentItem createFromParcel(Parcel in) {
            return new RentItem(in);
        }

        @Override
        public RentItem[] newArray(int size) {
            return new RentItem[size];
        }
    };

    public RentItem(String user_product_id,
                    String date_on_order,
                    String order_from_date,
                    String order_to_date,
                    String user_msg,
                    int payment_total,
                    int payment_discount,
                    int service_fee,
                    int protection_plan_fee,
                    int final_payment,
                    int protection_plan) {
        this.user_product_id = user_product_id;
        this.date_on_order = date_on_order;
        this.order_from_date = order_from_date;
        this.order_to_date = order_to_date;
        this.user_msg = user_msg;
        this.payment_total = payment_total;
        this.payment_discount = payment_discount;
        this.service_fee = service_fee;
        this.protection_plan_fee = protection_plan_fee;
        this.final_payment = final_payment;
        this.protection_plan = protection_plan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_product_id);
        dest.writeString(date_on_order);
        dest.writeString(order_from_date);
        dest.writeString(order_to_date);
        dest.writeString(user_msg);
        dest.writeInt(payment_total);
        dest.writeInt(payment_discount);
        dest.writeInt(service_fee);
        dest.writeInt(protection_plan_fee);
        dest.writeInt(final_payment);
        dest.writeInt(protection_plan);
    }
}
