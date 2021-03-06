package com.example.synerzip.recircle_android.models;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ForgotPwdRequest {
    private int otp;
    private String user_name;
    private String new_password;

    /**
     * constructor fo ForgotPwdRequest
     *
     * @param otp
     * @param user_name
     * @param new_password
     */
    public ForgotPwdRequest(int otp,
                            String user_name,
                            String new_password) {
        this.otp = otp;
        this.user_name = user_name;
        this.new_password = new_password;
    }
}
