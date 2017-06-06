package com.example.synerzip.recircle_android.models.UserMessages;

import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.User;

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
public class OwnerProdRelatedMsg {
    private String user_prod_msg_id;

    private String user_product_id;

    private String user_id;

    private String user_msg;

    private String msg_type;

    private boolean is_read;

    private Date created_at;

    private User user;

    private UserProdOrderDetail user_prod_order_detail;

    private ArrayList<UserProdMsgPool> user_prod_msg_pools;

    private Product user_product;
}
