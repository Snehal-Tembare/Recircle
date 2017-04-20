package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class Discounts {
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
}
