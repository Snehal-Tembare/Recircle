package com.example.synerzip.recircle_android.utilities;

import android.content.Context;
import android.view.View;

import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.RootProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.ListItemFragment;
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
    private RCAPInterface service;
    private SearchProduct sd;
    private Context mContext;

    public ArrayList<ProductsData> productsDataList;
    private ReadyCallback callback;

    public SearchUtility(Context context) {
        mContext = context;
    }

    public void setCallback(ReadyCallback callback) {
        this.callback = callback;
    }

    public void search(String productId, String manufacturerId, String query, String mFromDate, String mToDate) {

        if (ApiClient.getClient(mContext) != null) {
            service = ApiClient.getClient(mContext).create(RCAPInterface.class);

            if (!productId.equalsIgnoreCase("") || !manufacturerId.equalsIgnoreCase("")
                    || !query.equalsIgnoreCase("")) {

                Call<SearchProduct> call = service.searchProduct(productId, manufacturerId, query, mFromDate, mToDate);

                call.enqueue(new Callback<SearchProduct>() {
                    @Override
                    public void onResponse(Call<SearchProduct> call, Response<SearchProduct> response) {
                        if (response.isSuccessful()) {
                            if (null != response && response.code() == RCWebConstants.RC_SUCCESS_CODE
                                    && null != response.body()) {
                                sd = response.body();
                                callback.searchProductResult(sd);
                            }
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
    }

    public void populateAutoCompleteData() {

        if (ApiClient.getClient(mContext) != null) {
            service = ApiClient.getClient(mContext).create(RCAPInterface.class);

            Call<RootProductsData> call = service.productNames();

            call.enqueue(new Callback<RootProductsData>() {
                @Override
                public void onResponse(Call<RootProductsData> call, Response<RootProductsData> response) {
                    if (response.isSuccessful()) {
                        if (null != response && null != response.body()
                                && response.body().getProductsData() != null
                                && response.body().getProductsData().size() != 0) {

                            productsDataList = response.body().getProductsData();

                            callback.allItemsResult(productsDataList);
                        } else {
                            callback.allItemsResult(productsDataList);
                        }
                    } else {
                        callback.allItemsResult(new ArrayList<ProductsData>());
                    }
                }

                @Override
                public void onFailure(Call<RootProductsData> call, Throwable t) {
                }
            });
        }
    }

}


