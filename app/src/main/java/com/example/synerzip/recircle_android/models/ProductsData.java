package com.example.synerzip.recircle_android.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by synerzip on 17/3/17.
 */
@Getter
@Setter
public class ProductsData {
    private String product_manufacturer_id;
    private ArrayList<Product> products;
    private String product_manufacturer_name;
}
