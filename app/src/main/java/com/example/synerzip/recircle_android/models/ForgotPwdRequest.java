package com.example.synerzip.recircle_android.models;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */

public class ForgotPwdRequest {
    public int otp;
    public String user_name;
    public String new_password;
    public ForgotPwdRequest(int otp,
                            String user_name,
                            String new_password){
        this.otp=otp;
        this.user_name=user_name;
        this.new_password=new_password;
    }
}
