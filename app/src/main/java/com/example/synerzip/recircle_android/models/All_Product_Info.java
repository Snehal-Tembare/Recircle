package com.example.synerzip.recircle_android.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter

public class All_Product_Info implements Serializable {
    private List<PopularProducts> popularProducts;

    private List<ProductDetails> productDetails;
}
