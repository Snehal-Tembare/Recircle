package com.example.synerzip.recircle_android.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.All_Product_Info;
import com.example.synerzip.recircle_android.models.PopularProducts;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.ProductDetails;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.RootObject;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    private static final String TAG = "SearchActivity";

    public ArrayList<String> productItemList;

    AutocompleteAdapter autocompleteAdapter;

    private RCAPInterface service;

    @BindView(R.id.auto_txt_search_item_name)
    public AutoCompleteTextView productAutoComplete;

    public ArrayList<ProductsData> productsDataList;

    public List<Product> productsCustomList;

    private ArrayList<ProductDetails> productDetails;

    private ArrayList<String> allItemsList;

    public ArrayList<String> popularProdList;

    private PopularItemsAdapter mPopularItemsAdapter;

    private RecentItemsAdapter mRecentItemsAdapter;

    @BindView(R.id.cardRecyclerViewRecent)
    public RecyclerView mRecyclerViewRecent;

    @BindView(R.id.card_recycler_view_popular)
    public RecyclerView mRecyclerViewPopular;

    private ArrayList<PopularProducts> popularProducts;

    @BindView(R.id.edt_start_date)
    public EditText mEditTxtStartDate;

    @BindView(R.id.edt_end_date)
    public EditText mEditTxtEndDate;

    @BindView(R.id.txtHeaderOneContent)
    public TextView mTxtHeaderOne;

    @BindView(R.id.txtHeaderTwoContent)
    public TextView mTxtHeaderTwo;

    @BindView(R.id.txtHeaderThreeContent)
    public TextView mTxtHeaderThree;

    @BindView(R.id.imgDownArrowOne)
    public ImageView imgDownArrowOne;

    @BindView(R.id.imgDownArrowTwo)
    public ImageView imgDownArrowTwo;

    @BindView(R.id.imgDownArrowThree)
    public ImageView imgDownArrowThree;

    @BindView(R.id.imgUpArrowOne)
    public ImageView imgUpArrowOne;

    @BindView(R.id.imgUpArrowTwo)
    public ImageView imgUpArrowTwo;

    @BindView(R.id.imgUpArrowThree)
    public ImageView imgUpArrowThree;

    private AwesomeValidation awesomeValidation;

    private DatePickerDialog mFromDatePickerDialog;

    private DatePickerDialog mToDatePickerDialog;

    SimpleDateFormat mDateFormatter;

    private String manufacturerId = "";

    private String productId = "";

    public String query = "";

    private String mFromDate = "";

    private String mToDate = "";

    private static final String DESCRIPTION_EXPRESSION = "^[A-Za-z]+([\\-\\w\\s\\d]+)$";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        if (NetworkUtility.isNetworkAvailable(this)) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            //navigation drawer layout
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                            R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
            });
            navigationView.setNavigationItemSelectedListener(this);


            getAllProductDetails();
        } else {
            RCLog.showToast(this, getString(R.string.err_network_available));
        }
        // date picker
        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar newCalendar = Calendar.getInstance();
        mFromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(selectedYear, selectedMonth, selectedDay);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                mFromDate = simpleDateFormat.format(newDate.getTime());

                RCLog.showToast(SearchActivity.this, mFromDate);
                mEditTxtStartDate.setText(mDateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mToDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                mToDate = simpleDateFormat.format(newDate.getTime());
                RCLog.showToast(SearchActivity.this, mToDate);
                Log.v("enddate", mToDate + "");
                mEditTxtEndDate.setText(mDateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.auto_txt_search_item_name, DESCRIPTION_EXPRESSION, R.string.err_Field_empty);

    }//end onCreate()

    @OnClick(R.id.linearLayoutOne)
    public void headerOne(View view) {
        if (mTxtHeaderOne.getVisibility() == View.VISIBLE) {
            mTxtHeaderOne.setVisibility(View.GONE);
            imgUpArrowOne.setVisibility(View.GONE);
            imgDownArrowOne.setVisibility(View.VISIBLE);
        } else {
            mTxtHeaderOne.setVisibility(View.VISIBLE);
            imgUpArrowOne.setVisibility(View.VISIBLE);
            imgDownArrowOne.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.linearLayoutTwo)
    public void headerTwo(View view) {
        if (mTxtHeaderTwo.getVisibility() == View.VISIBLE) {
            mTxtHeaderTwo.setVisibility(View.GONE);
            imgUpArrowTwo.setVisibility(View.GONE);
            imgDownArrowTwo.setVisibility(View.VISIBLE);
        } else {
            mTxtHeaderTwo.setVisibility(View.VISIBLE);
            imgUpArrowTwo.setVisibility(View.VISIBLE);
            imgDownArrowTwo.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.linearLayoutThree)
    public void headerThree(View view) {
        if (mTxtHeaderThree.getVisibility() == View.VISIBLE) {
            mTxtHeaderThree.setVisibility(View.GONE);
            imgUpArrowThree.setVisibility(View.GONE);
            imgDownArrowThree.setVisibility(View.VISIBLE);

        } else {
            mTxtHeaderThree.setVisibility(View.VISIBLE);
            imgUpArrowThree.setVisibility(View.VISIBLE);
            imgDownArrowThree.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.edt_start_date)
    public void btnStartDate(View v) {
        mFromDatePickerDialog.show();
    }

    @OnClick(R.id.edt_end_date)
    public void btnEndDate(View v) {
        mToDatePickerDialog.show();
    }

    //get all product details
    public void getAllProductDetails() {
        popularProdList = new ArrayList<>();

        allItemsList = new ArrayList<>();

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<All_Product_Info> call = service.getProductDetails();

        call.enqueue(new Callback<All_Product_Info>() {
            @Override
            public void onResponse(Call<All_Product_Info> call, Response<All_Product_Info> response) {
                if (null != response) {
                    productDetails = response.body().getProductDetails();
                    popularProducts = response.body().getPopularProducts();
                    for (ProductDetails productDetails1 : productDetails) {
                        popularProdList.add(productDetails1.getProduct_info().getProduct_title());
                        allItemsList.add(productDetails1.getProduct_info().getProduct_title());
                    }
                } else {
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                }

                mRecentItemsAdapter = new RecentItemsAdapter(SearchActivity.this, productDetails);
                mRecyclerViewRecent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewRecent.setAdapter(mRecentItemsAdapter);

                mPopularItemsAdapter = new PopularItemsAdapter(SearchActivity.this, popularProducts);
                mRecyclerViewPopular.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewPopular.setAdapter(mPopularItemsAdapter);
            }

            @Override
            public void onFailure(Call<All_Product_Info> call, Throwable t) {
                Log.v(TAG, t.toString());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        productsCustomList = new ArrayList<>();

        productItemList = new ArrayList<>();

        query = productAutoComplete.getText().toString();

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<RootObject> call = service.productNames();

        call.enqueue(new Callback<RootObject>() {
            @Override
            public void onResponse(Call<RootObject> call, Response<RootObject> response) {
                if (null != response && null != response.body()) {

                    productsDataList = response.body().getProductsData();

                    for (int i = 0; i < productsDataList.size(); i++) {
                        productItemList.add(productsDataList.get(i).getProduct_manufacturer_name());
                        Product pd = new Product();
                        pd.setProduct_manufacturer_id(productsDataList.get(i).getProduct_manufacturer_id());
                        pd.setProduct_manufacturer_name(productsDataList.get(i).getProduct_manufacturer_name());
                        pd.setProduct_manufacturer_title(productsDataList.get(i).getProduct_manufacturer_name());

                        productsCustomList.add(pd);
                        ArrayList<Product> productsList = productsDataList.get(i).getProducts();

                        for (int j = 0; j < productsList.size(); j++) {
                            productItemList.add(productsDataList
                                    .get(i).getProduct_manufacturer_name()
                                    + " " + productsList.get(j).getProduct_title());

                            productsList.get(j).setProduct_manufacturer_title(productsDataList
                                    .get(i).getProduct_manufacturer_name()
                                    + " " + productsList.get(j).getProduct_title());
                            productsCustomList.add(productsList.get(j));
                        }
                    }

                    autocompleteAdapter = new AutocompleteAdapter
                            (SearchActivity.this, R.layout.activity_search, R.id.txtProductName, productsCustomList);
                    productAutoComplete.setAdapter(autocompleteAdapter);

                } else {
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                }
            }

            @Override
            public void onFailure(Call<RootObject> call, Throwable t) {
            }
        });

        productAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO functionality yet to be completed

                Product pd = (Product) parent.getAdapter().getItem(position);
                if (pd.getProduct_manufacturer_id() != null && !pd.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = pd.getProduct_manufacturer_id();
                }
                if (pd.getProduct_id() != null && !pd.getProduct_id().isEmpty()) {
                    productId = pd.getProduct_id();
                }
                //hide keyboard after item click
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);
            }
        });

    }//end onResume()

    @OnClick(R.id.btn_search)
    public void callSearchApi() {

        if (awesomeValidation.validate()) {

            if (productId.equalsIgnoreCase("") && manufacturerId.equalsIgnoreCase("")) {
                query = productAutoComplete.getText().toString();
            }
            if (!productId.equalsIgnoreCase("") || !manufacturerId.equalsIgnoreCase("") || !query.equalsIgnoreCase("")) {

                Call<SearchProduct> call = service.searchProduct(productId, manufacturerId, query, mFromDate, mToDate);
                call.enqueue(new Callback<SearchProduct>() {
                    @Override
                    public void onResponse(Call<SearchProduct> call, Response<SearchProduct> response) {
                        if (null != response && null != response.body()) {
                            Log.v(TAG, response.body() + "");
                            ArrayList<Products> productsArrayList = response.body().getProducts();
                            for (Products products : productsArrayList) {
                                RCLog.showToast(SearchActivity.this, products.getUser_product_info().getPrice_per_day() + "price per day");
                            }
                        } else {
                            RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchProduct> call, Throwable t) {

                    }
                });
            }
            startActivity(new Intent(this,ResultActivity.class));
        }
    }//end callSearchApi()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.launching, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}





