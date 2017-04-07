package com.example.synerzip.recircle_android.models;

/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */

public class LogInRequest {
    public String user_name;
    public String password;

    /**
     * constructor for log in request
     *
     * @param user_name
     * @param password
     */
    public LogInRequest(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }
}
