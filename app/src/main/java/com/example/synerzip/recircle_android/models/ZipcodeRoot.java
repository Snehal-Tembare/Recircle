package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 13/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
@Getter
@Setter
public class ZipcodeRoot {
    private ArrayList<ZipcodeResult> results;
    private String status;
}
