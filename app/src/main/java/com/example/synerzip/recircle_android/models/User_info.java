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

public class User_info {
    private String first_name;

    private String user_image_url;

    private String email;

    private ArrayList<User_addresses> user_addresses;

    private String last_name;
    
}
