package com.example.synerzip.recircle_android.models.UserMessages;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 20/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter
public class CancelUserProdRequest {

    private String user_prod_order_id;

    private String user_msg;

    /**
     * constructor for CancelUserProdRequest
     * @param user_prod_order_id
     * @param user_msg
     */
    public CancelUserProdRequest(String user_prod_order_id, String user_msg) {
        this.user_prod_order_id = user_prod_order_id;
        this.user_msg = user_msg;
    }
}
