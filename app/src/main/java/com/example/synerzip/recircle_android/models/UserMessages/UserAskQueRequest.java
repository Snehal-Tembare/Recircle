package com.example.synerzip.recircle_android.models.UserMessages;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 6/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserAskQueRequest {
    private String user_product_id;
    private String user_msg;

    /**
     * constructor for UserAskQueRequest
     * @param user_product_id
     * @param user_msg
     */
    public UserAskQueRequest(String user_product_id, String user_msg) {
        this.user_product_id = user_product_id;
        this.user_msg = user_msg;
    }
}
