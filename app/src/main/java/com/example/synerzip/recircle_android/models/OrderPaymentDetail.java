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

public class OrderPaymentDetail implements Parcelable {
    private Integer payment_total;
    private Integer payment_discount;
    private Integer service_fee;
    private Integer protection_plan_fee;
    private Integer final_payment;
    private Boolean protection_plan;

    public OrderPaymentDetail(Parcel in) {
        payment_total = in.readInt();
        payment_discount = in.readInt();
        service_fee = in.readInt();
        final_payment = in.readInt();
        protection_plan_fee = in.readInt();
        protection_plan = in.readByte() != 0;

    }

    public static final Creator<OrderPaymentDetail> CREATOR = new Creator<OrderPaymentDetail>() {
        @Override
        public OrderPaymentDetail createFromParcel(Parcel in) {
            return new OrderPaymentDetail(in);
        }

        @Override
        public OrderPaymentDetail[] newArray(int size) {
            return new OrderPaymentDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(payment_total);
        dest.writeInt(payment_discount);
        dest.writeInt(service_fee);
        dest.writeInt(final_payment);
        dest.writeInt(protection_plan_fee);
        dest.writeByte((byte) (protection_plan ? 1 : 0));

    }
}
