package com.example.synerzip.recircle_android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String DESCRIPTION_EXPRESSION = "^[A-Za-z]+([\\-\\w\\s\\d]+)$";
    private static final int REQUEST_CODE = 1;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    private static final String TAG = "SearchActivity";

    private ArrayList<String> productItemList;

    private AutocompleteAdapter mAutocompleteAdapter;

    private RCAPInterface service;

    @BindView(R.id.auto_txt_search_item_name)
    protected AutoCompleteTextView mProductAutoComplete;

    private ArrayList<ProductsData> productsDataList;

    private List<Product> productsCustomList;

    private ArrayList<Products> productDetailsList;

    private PopularItemsAdapter mPopularItemsAdapter;

    private RecentItemsAdapter mRecentItemsAdapter;

    @BindView(R.id.cardRecyclerViewRecent)
    protected RecyclerView mRecyclerViewRecent;

    @BindView(R.id.card_recycler_view_popular)
    protected RecyclerView mRecyclerViewPopular;

    private ArrayList<Products> popularProducts;

    @BindView(R.id.txtHeaderOneContent)
    protected TextView mTxtHeaderOne;

    @BindView(R.id.txtHeaderTwoContent)
    protected TextView mTxtHeaderTwo;

    @BindView(R.id.txtHeaderThreeContent)
    protected TextView mTxtHeaderThree;

    @BindView(R.id.imgDownArrowOne)
    protected ImageView imgDownArrowOne;

    @BindView(R.id.imgDownArrowTwo)
    protected ImageView imgDownArrowTwo;

    @BindView(R.id.imgDownArrowThree)
    protected ImageView imgDownArrowThree;

    @BindView(R.id.imgUpArrowOne)
    protected ImageView imgUpArrowOne;

    @BindView(R.id.imgUpArrowTwo)
    protected ImageView imgUpArrowTwo;

    @BindView(R.id.imgUpArrowThree)
    protected ImageView imgUpArrowThree;

    @BindView(R.id.edt_place)
    protected EditText mEdtPlace;

    @BindView(R.id.lists_layout)
    protected LinearLayout mListsLayout;

    private AwesomeValidation awesomeValidation;

    private String manufacturerId = "";

    private String productId = "";

    private String query = "";

    private String mFromDate = "";

    private String mToDate = "";

    private Intent mIntent;

    private String mName;

    private SearchUtility utility;

    private SearchProduct searchProduct;

    private Date fromDate, toDate;

    @BindView(R.id.edt_enter_dates)
    protected EditText mEditTxtDate;

    private String formatedFromDate, formatedToDate = "";

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.parent_layout)
    protected LinearLayout mLinearLayout;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean isLoggedIn=false;

    private String mUserFirstName = "";
    private String mAccessToken;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        utility = new SearchUtility();
        mProductAutoComplete.setSingleLine();

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, "");
        mUserFirstName = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_FIRST_USERNAME, mUserFirstName);

        //navigation drawer layout
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        mNavigationView.bringToFront();
        mNavigationView.setNavigationItemSelectedListener(this);

        Menu menu = mNavigationView.getMenu();
        MenuItem nav_loggedInAs = menu.findItem(R.id.nav_logedInAs);

        if (isLoggedIn) {
            menu.removeItem(R.id.nav_logIn_signUp);
            nav_loggedInAs.setVisible(true);
            nav_loggedInAs.setTitle(getString(R.string.logged_in_as) + mUserFirstName);
        } else {
            menu.removeItem(R.id.nav_messages);
            menu.removeItem(R.id.nav_rentals);
            menu.removeItem(R.id.nav_items);
            menu.removeItem(R.id.nav_payments);
            menu.removeItem(R.id.nav_logout);
            menu.removeItem(R.id.nav_logedInAs);
        }
        //check network availability
        if (NetworkUtility.isNetworkAvailable(this)) {

            getAllProductDetails();
        } else {
            RCLog.showToast(this, getString(R.string.err_network_available));
        }
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.auto_txt_search_item_name, DESCRIPTION_EXPRESSION, R.string.enter_product_name);

    }//onCreate()

    @OnClick(R.id.edt_enter_dates)
    public void btnEnterDates(View view) {
        Intent intent = new Intent(SearchActivity.this, CalendarActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * get dates from CalendarActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                mEditTxtDate.setText(formatedFromDate + " - " + formatedToDate);
            }
        }
    }

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

    /**
     * get all product details
     */
    public void getAllProductDetails() {

        productDetailsList = new ArrayList<>();
        popularProducts = new ArrayList<>();

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<AllProductInfo> call = service.getProductDetails();

        call.enqueue(new Callback<AllProductInfo>() {
            @Override
            public void onResponse(Call<AllProductInfo> call, Response<AllProductInfo> response) {

                if (response.isSuccessful()) {
                    mListsLayout.setVisibility(View.VISIBLE);
                    if (null != response && null != response.body()) {

                        if (response.body().getProductDetails() != null
                                && response.body().getProductDetails().size() != 0
                                && response.body().getPopularProducts() != null
                                && response.body().getPopularProducts().size() != 0) {

                            productDetailsList = response.body().getProductDetails();
                            popularProducts = response.body().getPopularProducts();
                            bindData();
                        }
                    }
                } else {
                    mListsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllProductInfo> call, Throwable t) {
                Log.v(TAG, t.toString());
            }
        });


    }

    private void bindData() {
        mRecentItemsAdapter = new RecentItemsAdapter(SearchActivity.this, productDetailsList, new OnItemClickListener() {
            @Override
            public void onItemClick(Products product) {
                Intent detailsIntent = new Intent(SearchActivity.this, DetailsActivity.class);
                detailsIntent.putExtra(getString(R.string.product_id),
                        product.getUser_product_info().getUser_product_id());

                Log.v("***Product Id:", product.getUser_product_info().getUser_product_id());
                startActivity(detailsIntent);
            }
        });

        mRecyclerViewRecent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewRecent.setAdapter(mRecentItemsAdapter);

        mPopularItemsAdapter = new PopularItemsAdapter(SearchActivity.this, popularProducts, new OnItemClickListener() {
            @Override
            public void onItemClick(Products product) {
                Log.v(TAG, "onItemClick" + product.getUser_product_info().getUser_product_id());

                Intent detailsIntent = new Intent(SearchActivity.this, DetailsActivity.class);
                detailsIntent.putExtra(getString(R.string.product_id),
                        product.getUser_product_info().getUser_product_id());
                startActivity(detailsIntent);
            }
        });

        mRecyclerViewPopular.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewPopular.setAdapter(mPopularItemsAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        productsCustomList = new ArrayList<>();

        productItemList = new ArrayList<>();

        service = ApiClient.getClient().create(RCAPInterface.class);

        utility.populateAutoCompleteData();

        ReadyCallback readyCallback = new ReadyCallback() {
            @Override
            public void searchProductResult(SearchProduct sd) {
                searchProduct = sd;
                resetAll();
                if (null != searchProduct && searchProduct.getProducts() != null
                        && searchProduct.getProducts().size() != 0) {
                    mIntent = new Intent(SearchActivity.this, ResultActivity.class);
                    mIntent.putExtra(getString(R.string.search_product), searchProduct);
                    mIntent.putExtra(getString(R.string.name), mName);
                    mIntent.putExtra(getString(R.string.place), getString(R.string.city_name));

                    if (!mEditTxtDate.getText().toString().equalsIgnoreCase("")) {
                        mIntent.putExtra(getString(R.string.start_date), formatedFromDate);
                        mIntent.putExtra(getString(R.string.end_date), formatedToDate);
                    }
                    mProgressBar.setVisibility(View.GONE);
                    mDrawerLayout.setAlpha((float) 1.0);
                    startActivity(mIntent);
                    mProductAutoComplete.setText("");
                    mEditTxtDate.setText("");
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mDrawerLayout.setAlpha((float) 1.0);
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
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

                    mAutocompleteAdapter = new AutocompleteAdapter
                            (SearchActivity.this, R.layout.activity_search, R.id.txtProductName, productsCustomList);
                    mProductAutoComplete.setAdapter(mAutocompleteAdapter);

                }
            }
        };

        utility.setCallback(readyCallback);

        mProductAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product = (Product) parent.getAdapter().getItem(position);
                if (product.getProduct_manufacturer_id() != null && !product.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = product.getProduct_manufacturer_id();
                }
                if (product.getProduct_id() != null && !product.getProduct_id().isEmpty()) {
                    productId = product.getProduct_id();
                }
                HideKeyboard.hideKeyBoard(SearchActivity.this);
            }
        });

    }//onResume()

    /**
     * Clear all fields
     */
    private void resetAll() {
        query = "";
        productId = "";
        manufacturerId = "";
        mFromDate = "";
        mToDate = "";
    }

    /**
     * button for search product
     */
    @OnClick(R.id.btn_search)
    public void callSearchApi() {
        mName = mProductAutoComplete.getText().toString();
        if (awesomeValidation.validate()) {

            if (productId.equalsIgnoreCase("") && manufacturerId.equalsIgnoreCase("")) {
                query = mProductAutoComplete.getText().toString();
            }
            mProgressBar.setVisibility(View.VISIBLE);
            mDrawerLayout.setAlpha((float) 0.6);
            utility.search(productId, manufacturerId, query, mFromDate, mToDate);
        }

    }//end callSearchApi()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.launching, menu);
        return true;
    }

    /**
     * navigation drawer item selection
     *
     * @param item
     * @return
     */
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_listItem:
                setVisible(true);
                startActivity(new Intent(SearchActivity.this, ListAnItemActivity.class));
                break;
            case R.id.nav_logIn_signUp:
                startActivity(new Intent(SearchActivity.this, LogInActivity.class));
                break;

            case R.id.nav_logout: {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.logout_alert_title));
                alertDialog.setPositiveButton(getResources().getString(R.string.logout), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        dialogInterface.dismiss();
                        clearData();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
                        editor.apply();
                        finish();
                        Intent intent = new Intent(SearchActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                break;
            }
            //TODO functionality yet to be completed for below menu items
            case R.id.nav_messages:
                setVisible(true);
                RCLog.showToast(SearchActivity.this, TAG);
                break;
            case R.id.nav_items:
                RCLog.showToast(SearchActivity.this, TAG);
                break;
            case R.id.nav_payments:
                RCLog.showToast(SearchActivity.this, TAG);
                break;
            case R.id.nav_rentals:
                if (isLoggedIn) {
                    startActivity(new Intent(this, AllRequestsActivity.class));
                } else {
                    RCLog.showToast(SearchActivity.this, getString(R.string.user_must_login));
                    logInDialog();
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logInDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_again_dialog);
        dialog.setTitle(getString(R.string.log_in));
        final EditText mEditTxtUserName = (EditText) dialog.findViewById(R.id.edit_login_email_dialog);
        final EditText mEditTxtPwd = (EditText) dialog.findViewById(R.id.edit_login_pwd_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_user_log_in_dialog);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setAlpha((float) 0.6);

                final String mUserName = mEditTxtUserName.getText().toString();
                final String mUserPwd = mEditTxtPwd.getText().toString();
                LogInRequest logInRequest = new LogInRequest(mUserName, mUserPwd);

                service = ApiClient.getClient().create(RCAPInterface.class);
                Call<User> userCall = service.userLogIn(logInRequest);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);

                        if (response.isSuccessful()) {
                            mAccessToken = response.body().getToken();
                            sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
                            editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, true);
                            editor.apply();
                            isLoggedIn = true;
                            dialog.dismiss();
                        } else {
                            if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                                RCLog.showToast(SearchActivity.this, getString(R.string.err_credentials));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);
                    }
                });
            }
        });

        dialog.show();
    }


    /**
     * clears Application data
     */
    private void clearData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
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

    /**
     * navigation drawer back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        editor.clear();
        editor.apply();
    }
}








