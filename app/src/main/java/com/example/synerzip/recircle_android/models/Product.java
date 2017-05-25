package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 17/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class Product {

    private String product_id;
    private String product_title;
    private AutoCompleteProductDetail product_detail;
    //var for internal logic
    private String product_manufacturer_id;
    private String product_manufacturer_name;
    //manufacturer name + product title
    private String product_manufacturer_title;

    private  ProductDetail productDetail;

    private ProductManufacturer product_manufacturer;

}
