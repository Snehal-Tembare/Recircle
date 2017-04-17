package com.example.synerzip.recircle_android.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private ProgressDialog mDialog;

    private ArrayList<ProductsData> productsDataList;

    private ArrayList<String> productItemList;

    private List<Product> productsCustomList;

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

    @BindView(R.id.txt_dates)
    public TextView mTxtDates;

    @BindView(R.id.auto_txt_search_item_name)
    public AutoCompleteTextView mAutoProductName;

    @BindView(R.id.edt_place)
    public EditText mEdtPlace;

    @BindView(R.id.edt_enter_dates)
    public EditText mEdtDates;

    @BindView(R.id.txt_data_not_found)
    public TextView mTxtDataNotFound;

    private Date fromDate;
    private Date toDate;
    private String formatedFromDate;
    private String formatedToDate;

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
        mValidation.addValidation(this, R.id.auto_txt_search_item_name, DESCRIPTION_EXPRESSION, R.string.enter_product_name);
        mAutoProductName.setSingleLine();

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
        if (!mStartDate.equalsIgnoreCase("") && !mEndDate.equalsIgnoreCase("")) {
            mTxtDates.setText(mStartDate + "-" + mEndDate);
        }

        mSearchAdapter = new SearchAdapter(this, productsArrayList);
        mLvSearcedItems.setLayoutManager(new LinearLayoutManager(this));
        mLvSearcedItems.setAdapter(mSearchAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        productItemList = new ArrayList<>();

        productsCustomList = new ArrayList<>();

        utility.populateAutoCompleteData();

        mAutoProductName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) parent.getAdapter().getItem(position);
                if (product.getProduct_manufacturer_id() != null && !product.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = product.getProduct_manufacturer_id();
                }
                if (product.getProduct_id() != null && !product.getProduct_id().isEmpty()) {
                    productId = product.getProduct_id();
                }
                HideKeyboard.hideKeyBoard(ResultActivity.this);

            }
        });

        ReadyCallback readyCallback = new ReadyCallback() {
            @Override
            public void searchProductResult(SearchProduct sd) {
                searchProduct = sd;
                resetAll();
                if (null != searchProduct) {
                    if (searchProduct.getProducts().size() == 0) {
                        mDialog.cancel();
                        mLvSearcedItems.setVisibility(View.GONE);
                        mTxtDataNotFound.setVisibility(View.VISIBLE);
                    } else {
                        productsArrayList.clear();
                        productsArrayList.addAll(sd.getProducts());
                        mSearchAdapter.notifyDataSetChanged();
                        mDialog.cancel();
                        mLvSearcedItems.setVisibility(View.VISIBLE);
                        mTxtDataNotFound.setVisibility(View.GONE);
                    }
                } else {
                    mDialog.cancel();
                    RCLog.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
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
                    mAutoProductName.setAdapter(autocompleteAdapter);

                } else {
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                }
            }
        };

        utility.setCallback(readyCallback);

        mAutoProductName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                mAutoProductName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_input_delete, 0);

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mAutoProductName.getRight() - mAutoProductName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        mAutoProductName.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void resetAll() {
        query="";
        productId="";
        manufacturerId="";
        mFromDate="";
        mToDate="";
    }

    /**
     * get dates from CalendarActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String from = data.getStringExtra(getString(R.string.from_date));
                String to = data.getStringExtra(getString(R.string.to_date));
                DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
                try {
                    fromDate = formatter.parse(from.toString());
                    toDate = formatter.parse(to.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calFromDate = Calendar.getInstance();
                Calendar calToDate = Calendar.getInstance();
                calFromDate.setTime(fromDate);
                calToDate.setTime(toDate);
                CharSequence monthFromDate = android.text.format.DateFormat
                        .format(getString(R.string.month_format), fromDate);
                CharSequence monthToDate = android.text.format.DateFormat
                        .format(getString(R.string.month_format), toDate);

                formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate + ", " + calFromDate.get(Calendar.YEAR);
                formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);

                if (monthFromDate.equals(monthToDate)) {
                    formatedFromDate = calFromDate.get(Calendar.DATE) + "";
                    formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);
                } else if (!monthFromDate.equals(monthToDate) && !(calFromDate.get(Calendar.YEAR) == calToDate.get(Calendar.YEAR))) {
                    formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate + ", " + calFromDate.get(Calendar.YEAR);
                    formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);
                } else if (!monthFromDate.equals(monthToDate)) {
                    formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate;
                    formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);
                }

                mEdtDates.setText(formatedFromDate + " - " + formatedToDate);

            }
        }

    }

    @OnClick(R.id.layout_header)
    public void expandView() {
        mExpandLayout.setVisibility(View.VISIBLE);
        mHeaderLayout.setVisibility(View.GONE);

        mAutoProductName.setText(mName);
        mEdtPlace.setText(mPlace);
        mEdtDates.setText(mTxtDates.getText());

    }

    @OnClick(R.id.btn_search)
    public void collapse() {

        mName = mAutoProductName.getText().toString();
        mTxtName.setText(mName);

        mTxtDates.setText(mEdtDates.getText().toString());

        if (mValidation.validate()) {

            mExpandLayout.setVisibility(View.GONE);
            mHeaderLayout.setVisibility(View.VISIBLE);

            if (productId.equalsIgnoreCase("") && manufacturerId.equalsIgnoreCase("")) {
                query = mAutoProductName.getText().toString();
            }
            mDialog.show();
            utility.search(productId, manufacturerId, query, formatedFromDate, formatedToDate);
        }
    }

    @OnClick(R.id.layout_expanded)
    public void collapseView() {
        mExpandLayout.setVisibility(View.GONE);
        mHeaderLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.edt_enter_dates)
    public void btnEnterDates(View view) {
        Intent intent = new Intent(ResultActivity.this, CalendarActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
