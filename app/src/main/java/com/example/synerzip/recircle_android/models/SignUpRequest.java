package com.example.synerzip.recircle_android.models;

/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class SignUpRequest {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private long user_mob_no;
    private int otp;

    /**
     * constructor for sign up request
     *
     * @param first_name
     * @param last_name
     * @param email
     * @param password
     * @param user_mob_no
     * @param otp
     */
    public SignUpRequest(String first_name,
                         String last_name,
                         String email,
                         String password,
                         long user_mob_no,
                         int otp) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.user_mob_no = user_mob_no;
        this.otp = otp;
    }
}
