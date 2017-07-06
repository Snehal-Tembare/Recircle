package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 7/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO product_title parameter is not completed
@Getter
@Setter
public class ListAnItemRequest {
    private String product_id;
    private String product_title;
    private int price_per_day;
    private int min_rental_day;
    private String user_prod_desc;
    private ArrayList<Discounts> user_prod_discounts;
    private ArrayList<UserProdImages> user_prod_images;
    private ArrayList<UserProductUnAvailability> user_prod_unavailability;
    private long user_product_zipcode;
    private int fromAustin;

    /**
     * constructor for ListAnItemRequest
     *
     * @param product_id
     * @param product_title
     * @param price_per_day
     * @param min_rental_day
     * @param user_prod_desc
     * @param user_prod_discounts
     * @param user_prod_images
     * @param user_prod_unavailability
     * @param user_product_zipcode
     * @param fromAustin
     */
    public ListAnItemRequest(String product_id,
                             String product_title,
                             int price_per_day,
                             int min_rental_day,
                             String user_prod_desc,
                             ArrayList<Discounts> user_prod_discounts,
                             ArrayList<UserProdImages> user_prod_images,
                             ArrayList<UserProductUnAvailability> user_prod_unavailability,
                             long user_product_zipcode,
                             int fromAustin) {
        this.product_title = product_title;
        this.product_id = product_id;
        this.price_per_day = price_per_day;
        this.min_rental_day = min_rental_day;
        this.user_prod_desc = user_prod_desc;
        this.user_prod_discounts = user_prod_discounts;
        this.user_prod_images = user_prod_images;
        this.user_prod_unavailability = user_prod_unavailability;
        this.user_product_zipcode = user_product_zipcode;
        this.fromAustin = fromAustin;
    }
}
