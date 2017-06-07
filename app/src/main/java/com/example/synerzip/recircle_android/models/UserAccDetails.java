package com.example.synerzip.recircle_android.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 23/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter
public class UserAccDetails {

    private String user_acc_details_id;

    private int acc_number;

    private String acc_holder_name;

    private int routing_number;

    private String DOB;

    private int ssn;

    private String business_type;

    /**
     * constructor for UserAccDetails
     * @param user_acc_details_id
     * @param acc_number
     * @param acc_holder_name
     * @param routing_number
     * @param DOB
     * @param ssn
     * @param business_type
     */
    public UserAccDetails(String user_acc_details_id, int acc_number,
                          String acc_holder_name, int routing_number,
                          String DOB, int ssn, String business_type) {
        this.user_acc_details_id = user_acc_details_id;
        this.acc_number = acc_number;
        this.acc_holder_name = acc_holder_name;
        this.routing_number = routing_number;
        this.DOB = DOB;
        this.ssn = ssn;
        this.business_type = business_type;
    }
}
