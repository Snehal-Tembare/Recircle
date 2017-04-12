package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
@Getter
@Setter

public class Discounts {
    public int percentage;
    public int discount_for_days;
    public boolean isActive;

    /**
     * constructor fo Discounts
     *
     * @param percentage
     * @param discount_for_days
     * @param isActive
     */
    public Discounts(int percentage,
                     int discount_for_days,
                     boolean isActive) {
        this.percentage = percentage;
        this.discount_for_days = discount_for_days;
        this.isActive = isActive;
    }
}
