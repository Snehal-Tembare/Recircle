package com.example.synerzip.recircle_android.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 20/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ResultActivity extends AppCompatActivity {

    private static final String DESCRIPTION_EXPRESSION = "^[A-Za-z]+([\\-\\w\\s\\d]+)$";
//    @BindView(R.id.img)
//    public ImageView mImgView;

    private String productId = "";
    private String manufacturerId = "";
    private String query = "";
    private String mFromDate = "";
    private String mToDate = "";
    private Bundle mIntent;

    private String mName;
    private String mPlace;
    private String mStartDate;
    private String mEndDate;

    private ArrayList<Products> productsArrayList;
    private SearchAdapter mSearchAdapter;

    private SearchUtility utility;

    private AwesomeValidation mValidation;

    private SearchProduct searchProduct;

    private AutocompleteAdapter autocompleteAdapter;


    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.layout_expanded)
    public LinearLayout mExpandLayout;

    @BindView(R.id.layout_header)
    public LinearLayout mHeaderLayout;

    @BindView(R.id.lv_searched_items)
    public RecyclerView mLvSearcedItems;

    @BindView(R.id.txt_name)
    public TextView mTxtName;

    @BindView(R.id.txt_place)
    public TextView mTxtPlace;

    @BindView(R.id.txt_start_date)
    public TextView mTxtStartDate;

    @BindView(R.id.txt_end_date)
    public TextView mTxtEndDate;

    @BindView(R.id.auto_txt_search_item_name)
    public AutoCompleteTextView mAutoTxtName;

    @BindView(R.id.edt_place)
    public EditText mEdtPlace;

    @BindView(R.id.edt_start_date)
    public EditText mEdtStartDate;

    @BindView(R.id.edt_end_date)
    public EditText mEdtEndDate;

    private SimpleDateFormat mSimpleDateFormatter;

    private DatePickerDialog mFromDatePickerDialog;

    private DatePickerDialog mToDatePickerDialog;

    SimpleDateFormat simpleDateFormat;
    private ProgressDialog mDialog;
    private ArrayList<ProductsData> productsDataList;
    private ArrayList<String> productItemList;
    private List<Product> productsCustomList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mValidation.addValidation(this, R.id.auto_txt_search_item_name, DESCRIPTION_EXPRESSION, R.string.err_Field_empty);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.loading));

        utility = new SearchUtility();

        mIntent = getIntent().getExtras();

        SearchProduct sd = mIntent.getParcelable(getString(R.string.search_product));
        productsArrayList = sd.getProducts();

        mName = mIntent.getString(getString(R.string.name), "");
        mPlace = mIntent.getString(getString(R.string.place), "");
        mStartDate = mIntent.getString(getString(R.string.start_date), "");
        mEndDate = mIntent.getString(getString(R.string.end_date), "");

        mTxtName.setText(mName);
        mTxtPlace.setText(mPlace);
        mTxtStartDate.setText(mStartDate);
        mTxtEndDate.setText(mEndDate);

        mSearchAdapter = new SearchAdapter(this, productsArrayList);
        mLvSearcedItems.setLayoutManager(new LinearLayoutManager(this));
        mLvSearcedItems.setAdapter(mSearchAdapter);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        mSimpleDateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        Calendar newCalendar = Calendar.getInstance();
        mFromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(selectedYear, selectedMonth, selectedDay);

                mFromDate = simpleDateFormat.format(newDate.getTime());

                mEdtStartDate.setText(mSimpleDateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mToDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(selectedYear, selectedMonth, selectedDay);

                mToDate = simpleDateFormat.format(newDate.getTime());

                mEdtEndDate.setText(mSimpleDateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    protected void onResume() {
        super.onResume();

        productItemList = new ArrayList<>();

        productsCustomList = new ArrayList<>();

        utility.populateAutoCompleteData();

        mAutoTxtName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) parent.getAdapter().getItem(position);
                if (product.getProduct_manufacturer_id() != null && !product.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = product.getProduct_manufacturer_id();
                }
                if (product.getProduct_id() != null && !product.getProduct_id().isEmpty()) {
                    productId = product.getProduct_id();
                }
                //hide keyboard after item click
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);

            }
        });

        ReadyCallbak readyCallbak = new ReadyCallbak() {
            @Override
            public void searchProductResult(SearchProduct sd) {
                searchProduct = sd;
                if (null != searchProduct) {
                    productsArrayList.clear();
                    productsArrayList.addAll(sd.getProducts());
                    mSearchAdapter.notifyDataSetChanged();
                    mDialog.cancel();
                } else {
                    mDialog.cancel();
                    RCLog.showToast(getApplicationContext(), "Something went wrong...");
                }
            }

            @Override
            public void allItemsResult(ArrayList<ProductsData> mProductsDataList) {
                productsDataList = mProductsDataList;
                if (null != productsDataList && 0 != productsDataList.size()) {

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
                            (ResultActivity.this, R.layout.activity_search, R.id.txtProductName, productsCustomList);
                    mAutoTxtName.setAdapter(autocompleteAdapter);

                } else {
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                }
            }
        };

        utility.setCallback(readyCallbak);
    }

    @OnClick(R.id.layout_header)
    public void expandView() {
        mExpandLayout.setVisibility(View.VISIBLE);
        mHeaderLayout.setVisibility(View.GONE);

        mAutoTxtName.setText(mName);
        mEdtPlace.setText(mPlace);
        mEdtStartDate.setText(mStartDate);
        mEdtEndDate.setText(mEndDate);

    }

    @OnClick(R.id.btn_search)
    public void collapse() {
        mExpandLayout.setVisibility(View.GONE);
        mHeaderLayout.setVisibility(View.VISIBLE);
        //TODO code yet to complete

        mName = mAutoTxtName.getText().toString();
        mTxtName.setText(mName);

        if (mValidation.validate()) {
            if (productId.equalsIgnoreCase("") && manufacturerId.equalsIgnoreCase("")) {
                query = mAutoTxtName.getText().toString();
            }
            mDialog.show();
            utility.search(productId, manufacturerId, query, mFromDate, mToDate);
        }
    }

    @OnClick(R.id.layout_expanded)
    public void collapseView(){
        mExpandLayout.setVisibility(View.GONE);
        mHeaderLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.edt_start_date)
    public void showStartDate(View v) {
        mFromDatePickerDialog.show();
    }

    @OnClick(R.id.edt_end_date)
    public void showEndDate(View v) {
        mToDatePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
