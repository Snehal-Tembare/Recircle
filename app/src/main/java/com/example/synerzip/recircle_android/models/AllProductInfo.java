package com.example.synerzip.recircle_android.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class AllProductInfo implements Serializable {
    private ArrayList<PopularProducts> popularProducts;

    private ArrayList<ProductDetails> productDetails;
}
