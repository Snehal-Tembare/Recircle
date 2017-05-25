package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserDetailsRequest {
    private String first_name;
    private String last_name;
    private String email;
    private String user_image_url;
    private UserAddress userAddress;
    private UserAccDetails user_acc_details;

}
