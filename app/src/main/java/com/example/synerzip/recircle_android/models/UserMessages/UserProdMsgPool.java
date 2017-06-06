package com.example.synerzip.recircle_android.models.UserMessages;

import lombok.Getter;
import lombok.Setter;
import retrofit2.http.GET;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class UserProdMsgPool {
    private String user_prod_msg_pool_id;

    private String user_id;

    private String user_msg;

    private boolean is_read;
}
