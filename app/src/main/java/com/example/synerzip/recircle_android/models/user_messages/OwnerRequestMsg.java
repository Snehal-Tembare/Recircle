package com.example.synerzip.recircle_android.models.user_messages;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class OwnerRequestMsg {
    private String user_prod_msg_id;

    private String user_product_id;

    private String user_id;

    private String user_msg;

    private String msg_type;

    private Date created_at;

    private boolean is_read;

    private UserRequest user;

    private UserReqProdOrderDetail user_prod_order_detail;

    private ArrayList<UserReqProdMsgPool> user_prod_msg_pools;

    private UserReqProduct user_product;
}
