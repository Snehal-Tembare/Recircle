package com.example.synerzip.recircle_android;

import android.app.Application;

/**
 * Created by Snehal Tembare on 11/7/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}