package com.example.synerzip.recircle_android.models;

import com.example.synerzip.recircle_android.ui.ListAnItemActivity;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class ListAnItemRequest {
    public String product_id;
    public int price_per_day;
    public int min_rental_day;
    public String user_prod_desc;
    public ArrayList<Discounts> user_prod_discounts;
    public ArrayList<UserProdImages> user_prod_images;
    public ArrayList<String> user_prod_unavailability;
    public int user_product_zipcode;

    public ListAnItemRequest(String product_id,
                             int price_per_day,
                             int min_rental_day,
                             String user_prod_desc,
                             ArrayList<Discounts> user_prod_discounts,
                             ArrayList<UserProdImages> user_prod_images,
                             ArrayList<String> user_prod_unavailability,
                             int user_product_zipcode){
        this.product_id=product_id;
        this.price_per_day=price_per_day;
        this.min_rental_day=min_rental_day;
        this.user_prod_desc=user_prod_desc;
        this.user_prod_discounts=user_prod_discounts;
        this.user_prod_images=user_prod_images;
        this.user_prod_unavailability=user_prod_unavailability;
        this.user_product_zipcode=user_product_zipcode;

    }
}
