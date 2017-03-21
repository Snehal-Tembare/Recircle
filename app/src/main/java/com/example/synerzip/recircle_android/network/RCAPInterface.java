package com.example.synerzip.recircle_android.network;

import com.example.synerzip.recircle_android.models.All_Product_Info;
import com.example.synerzip.recircle_android.models.RootObject;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Snehal Tembare on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public interface RCAPInterface {

    /**
     * Get product details
     */

    @GET(RCWebConstants.RC_GET_PRODUCT_DETAILS)
    Call<All_Product_Info> getProductDetails();


    @GET(RCWebConstants.RC_SEARCH_PRODUCT)
    Call<SearchProduct> searchProduct(@Query("productId") String productId);

    @GET(RCWebConstants.RC_SEARCH_PRODUCT)
    Call<SearchProduct> searchProductByManufactId(@Query("manufacturerId") String manufacturerId);


    @GET(RCWebConstants.RC_SEARCH_PRODUCT)
    Call<SearchProduct> searchProductByText(@Query("searchText") String searchText);

    @GET(RCWebConstants.RC_PRODUCT_NAMES)
    Call<RootObject> productNames();

}
