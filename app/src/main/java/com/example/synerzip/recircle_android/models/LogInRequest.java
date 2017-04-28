package com.example.synerzip.recircle_android.models;

/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class LogInRequest {
    private String user_name;
    private String password;

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
