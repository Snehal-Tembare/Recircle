package com.example.synerzip.recircle_android.models.user_messages;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 15/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserAskQueResponse {
    private String user_prod_msg_id;
    private String user_msg;

    /**
     * constructor for UserAskQueResponse
     * @param user_prod_msg_id
     * @param user_msg
     */
    public UserAskQueResponse(String user_prod_msg_id, String user_msg) {
        this.user_prod_msg_id = user_prod_msg_id;
        this.user_msg = user_msg;
    }
}
