package com.example.synerzip.recircle_android.models.user_messages;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserReqProduct {

    private String product_id;

    private String user_product_id;

    private UserReqProductDetails product;
}
