package com.example.synerzip.recircle_android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.All_Product_Info;
import com.example.synerzip.recircle_android.models.ProductDetails;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public String query;
    public ArrayList<String> productItemList;

    public ArrayAdapter<String> adapter;

    private RCAPInterface service;

    private static String ID;
    private List<ProductDetails> productDetails;

    @BindView(R.id.txtAutocomplete)
    public AutoCompleteTextView productAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        productItemList = new ArrayList<>();

        query=productAutoComplete.getText().toString();

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<All_Product_Info> call = service.getProductDetails();

        call.enqueue(new Callback<All_Product_Info>() {
            @Override
            public void onResponse(Call<All_Product_Info> call, Response<All_Product_Info> response) {
                if (null != response) {
                    productDetails = response.body().getProductDetails();
                    Log.v("Output", "" + productDetails.size());
                    for (ProductDetails productDetails1 : productDetails) {
                        productItemList.add(productDetails1.getProduct_info().getProduct_title());
                        Log.v("Popular products", "" + productDetails1.getProduct_info().getProduct_title());
                    }
                } else {
                    RCLog.showToast(getApplicationContext(), "Product Not Found");
                }
            }

            @Override
            public void onFailure(Call<All_Product_Info> call, Throwable t) {
                Log.v(TAG, t.toString());
            }
        });
       adapter = new ArrayAdapter<>
                (this,android.R.layout.simple_list_item_1,productItemList);
        productAutoComplete.setAdapter(adapter);

        productAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetails pd = productDetails.get(position);
                ID = pd.getProduct_info().getProduct_manufacturer_id();
            }
        });
    }

    @OnClick(R.id.btn_click)
    public void callSearchApi() {
        Call<SearchProduct> call = service.searchProduct(ID);
        call.enqueue(new Callback<SearchProduct>() {
            @Override
            public void onResponse(Call<SearchProduct> call, Response<SearchProduct> response) {
                if (null != response && null != response.body()) {
                    ArrayList<Products> productsArrayList = response.body().getProducts();
                    for (Products products : productsArrayList) {
                        Log.v("Product from list", products.getProduct_info().getProduct_manufacturer_name());
                    }
                } else {
                    RCLog.showToast(getApplicationContext(), "Bad gateway");
                }
            }
            @Override
            public void onFailure(Call<SearchProduct> call, Throwable t) {

            }
        });
    }
}




