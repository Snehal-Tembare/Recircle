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

    private Date DOB;

    private int ssn;

    private String business_type;
}
