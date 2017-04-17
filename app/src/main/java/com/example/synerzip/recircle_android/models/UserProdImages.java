package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProdImages {
    private String user_prod_image_url;
    private String created_at;

    public UserProdImages(String user_prod_image_url, String created_at) {
        this.user_prod_image_url = user_prod_image_url;
        this.created_at = created_at;
    }
}
