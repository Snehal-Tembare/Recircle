package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProductInfo {
    private String product_id;

    private String avai_to_date;

    private String avai_from_date;

    private String product_avg_rating;

    private String created_at;

    private ArrayList<User_Prod_Images> user_prod_images;

    private String user_prod_desc;

    private String price_per_day;

    private String user_product_id;
}
