package com.example.synerzip.recircle_android.models.rentals;

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
    private Double payment_discount;
    private Double service_fee;
    private Double protection_plan_fee;
    private Double pre_auth_fee;
    private Double final_payment;
    private Boolean protection_plan;

    public OrderPaymentDetail(Parcel in) {
        payment_total = in.readInt();
        payment_discount = in.readDouble();
        service_fee = in.readDouble();
        final_payment = in.readDouble();
        protection_plan_fee = in.readDouble();
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
        dest.writeDouble(payment_discount);
        dest.writeDouble(service_fee);
        dest.writeDouble(final_payment);
        dest.writeDouble(protection_plan_fee);
        dest.writeByte((byte) (protection_plan ? 1 : 0));

    }
}
