package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserAddress {

    private String user_address_id;

    private String street;

    private String city;

    private String state;

    private int zip;

    /**
     * constructor for UserAddress
     * @param user_address_id
     * @param street
     * @param city
     * @param state
     * @param zip
     */
    public UserAddress(String user_address_id, String street, String city, String state, int zip) {
        this.user_address_id = user_address_id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
