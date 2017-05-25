package com.example.synerzip.recircle_android.models;

/**
 * Created by Prajakta Patil on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ChangePwdRequest {
    private String old_password;
    private String new_password;

    /**
     * constructor for change password
     * @param old_password
     * @param new_password
     */
    public ChangePwdRequest(String old_password,
                            String new_password){
        this.old_password=old_password;
        this.new_password=new_password;
    }
}
