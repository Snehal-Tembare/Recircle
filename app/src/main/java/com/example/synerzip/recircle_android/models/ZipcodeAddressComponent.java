package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 13/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
class ZipcodeAddressComponent {
    private String long_name;
    private String short_name;
    private ArrayList<String> types;
}
