package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class Discounts implements Parcelable {
    private int percentage;
    private int discount_for_days;
    private int isActive;

    /**
     * constructor fo Discounts
     *
     * @param percentage
     * @param discount_for_days
     * @param isActive
     */
    public Discounts(int percentage,
                     int discount_for_days,
                     int isActive) {
        this.percentage = percentage;
        this.discount_for_days = discount_for_days;
        this.isActive = isActive;
    }

    /**
     * creator object for discounts
     */
    public static final Creator<Discounts> CREATOR = new Creator<Discounts>() {
        @Override
        public Discounts createFromParcel(Parcel in) {
            return new Discounts(in);
        }

        @Override
        public Discounts[] newArray(int size) {
            return new Discounts[size];
        }
    };

    /**
     * constructor for user
     *
     * @param in
     */
    public Discounts(Parcel in) {
        percentage = in.readInt();
        discount_for_days = in.readInt();
        isActive = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(percentage);
        dest.writeInt(discount_for_days);
        dest.writeInt(isActive);
    }
}
