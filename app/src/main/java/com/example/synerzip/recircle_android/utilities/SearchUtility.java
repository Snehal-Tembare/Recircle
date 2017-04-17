package com.example.synerzip.recircle_android.utilities;

import android.util.Log;

import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.RootObject;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.ReadyCallback;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snehal Tembare on 23/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class SearchUtility {
    private final String TAG = "SearchUtility";
    private RCAPInterface service;
    private SearchProduct sd;

    public ArrayList<ProductsData> productsDataList;
    private ReadyCallback callback;

    public void setCallback(ReadyCallback callback) {
        this.callback = callback;
    }

    public void search(String productId, String manufacturerId, String query, String mFromDate, String mToDate) {

        service = ApiClient.getClient().create(RCAPInterface.class);

        if (!productId.equalsIgnoreCase("") || !manufacturerId.equalsIgnoreCase("") || !query.equalsIgnoreCase("")) {

            Call<SearchProduct> call = service.searchProduct(productId, manufacturerId, query, mFromDate, mToDate);

            call.enqueue(new Callback<SearchProduct>() {
                @Override
                public void onResponse(Call<SearchProduct> call, Response<SearchProduct> response) {
                    if (null != response && response.code() == RCWebConstants.RC_SUCCESS_CODE
                            && null != response.body()) {
                        Log.v(TAG, response.body() + "");
                        sd = response.body();
                        callback.searchProductResult(sd);
                    } else {
                        callback.searchProductResult(new SearchProduct());
                    }
                }

                @Override
                public void onFailure(Call<SearchProduct> call, Throwable t) {

                }
            });
        }

    }

    public void populateAutoCompleteData() {

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<RootObject> call = service.productNames();

        call.enqueue(new Callback<RootObject>() {
            @Override
            public void onResponse(Call<RootObject> call, Response<RootObject> response) {
                if (null != response && null != response.body()) {

                    productsDataList = response.body().getProductsData();

                    callback.allItemsResult(productsDataList);
                } else {
                    callback.allItemsResult(new ArrayList<ProductsData>());
                }
            }

            @Override
            public void onFailure(Call<RootObject> call, Throwable t) {
            }
        });

    }

}
