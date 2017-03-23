package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserInfo {
    private String first_name;

    private String user_image_url;

    private String email;

    private ArrayList<UserAddresses> user_addresses;

    private String last_name;
    
}
