package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserProductUnAvailability {

    private String unavai_from_date;

    private String unavai_to_date;

    /**
     * constructor for UserProductUnAvailability
     *
     * @param unavai_from_date
     * @param unavai_to_date
     */
    public UserProductUnAvailability(String unavai_from_date, String unavai_to_date) {
        this.unavai_from_date = unavai_from_date;
        this.unavai_to_date = unavai_to_date;
    }
}
