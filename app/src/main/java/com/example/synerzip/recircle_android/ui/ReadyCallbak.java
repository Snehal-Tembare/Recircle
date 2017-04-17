package com.example.synerzip.recircle_android.ui;

import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;

import java.util.ArrayList;

/**
 * Created by Snehal Tembare on 28/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public interface ReadyCallbak{

    void searchProductResult(SearchProduct sd);

    void allItemsResult(ArrayList<ProductsData> productsDataList);

}