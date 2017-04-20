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
public class ZipcodeResult {
    private ArrayList<ZipcodeAddressComponent> address_components;
    private String formatted_address;
    private String place_id;
    private ArrayList<String> types;
}
