package com.example.synerzip.recircle_android.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.synerzip.recircle_android.R;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.EditProduct;
import com.example.synerzip.recircle_android.models.ListAnItemRequest;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.Base64Encryption;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Prajakta Patil on 24/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class ListItemSummaryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private SharedPreferences sharedPreferences;

    private String mAccessToken;
    private String mPassword;
    private String mUserId;
    private String mUserEmail;
    private String mUserLastName;
    private String mUserFirstName;
    private String mUserImage;
    private long mUserMobNo;

    private Products product;
    private boolean isLoggedIn;

    private RCAPInterface service;

    private String productId = "";
    private String editProductId = "";

    private ArrayList<Discounts> listDiscounts;

    @BindView(R.id.txt_five_days_disc)
    protected TextView mTxtDiscFiveDays;

    @BindView(R.id.txt_ten_days_disc)
    protected TextView mTxtDiscTenDays;

    @BindView(R.id.txtDiscounts)
    protected TextView mTxtDisc;

    private String mItemDesc, mProductTitle;

    private int mItemPrice, mMinRental;

    private long mZipcode;

    private int fromAustin;

    private ArrayList<UserProdImages> listUploadItemImage;

    private ArrayList<UserProductUnAvailability> mItemAvailability;

    private ArrayList<Date> unavailableDates;

    @BindView(R.id.txt_days_count)
    protected TextView mTxtDaysCount;

    @BindView(R.id.txt_item_price)
    protected TextView mTxtItemPrice;

    @BindView(R.id.txt_item_rental)
    protected TextView mTxtItemRental;

    private ArrayList<String> uploadGalleryImages;

    @BindView(R.id.recycler_view_images)
    protected RecyclerView mRecyclerView;

    private int selectedImgPosition = 0;

    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.txt_item_desc)
    protected TextView mTxtItemDesc;

    @BindView(R.id.txt_product_title)
    protected TextView mTxtProductTitle;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.layout_list_item_summary)
    protected LinearLayout mLinearLayout;

    @BindView(R.id.txt_show_dates)
    protected TextView mTxtShowDates;

    @BindView(R.id.btn_confirm_item)
    protected Button mTxtConfirmItem;

    private ListAnItemRequest listAnItemRequest;

    @BindView(R.id.viewDiscounts)
    protected View viewDiscounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_item_summary);
        ButterKnife.bind(this);

        if (MyProfileActivity.isItemEdit) {
            mTxtConfirmItem.setText(getString(R.string.edit));
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = bundle.getParcelable(getString(R.string.product));
            if (product != null) {
                editProductId = product.getUser_product_info().getUser_product_id();
            }
        }

        uploadGalleryImages = new ArrayList<>();

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);

        unavailableDates = new ArrayList<>();

        listUploadItemImage = new ArrayList<>();

        unavailableDates = AdditionalDetailsActivity.selectedDates;

        int datesCount;

        datesCount = AdditionalDetailsActivity.daysCount;
        if (datesCount != 0) {
            mTxtDaysCount.setText(datesCount + " days");
        } else {
            mTxtDaysCount.setText(getString(R.string.dates_not_selected));
            mTxtShowDates.setVisibility(View.GONE);
        }

        productId = ListItemFragment.productId;
        listDiscounts = ListItemFragment.listDiscounts;

        //TODO changes needed for ListAnItem api for discount ; the functionality should be dynamic
        if (listDiscounts.size() != 0) {

            viewDiscounts.setVisibility(View.VISIBLE);
            mTxtDisc.setVisibility(View.VISIBLE);

            if (listDiscounts.size() > 2) {
                mTxtDiscFiveDays.setVisibility(View.VISIBLE);
                mTxtDiscFiveDays.setText(getString(R.string.five_days));
                mTxtDiscTenDays.setVisibility(View.VISIBLE);
                mTxtDiscTenDays.setText(getString(R.string.ten_days));
            } else {
                if (listDiscounts.get(0).getDiscount_for_days() == 5) {
                    mTxtDiscFiveDays.setVisibility(View.VISIBLE);
                    mTxtDiscFiveDays.setText(getString(R.string.five_days));
                    mTxtDiscTenDays.setVisibility(View.GONE);
                } else {
                    if (listDiscounts.get(0).getDiscount_for_days() == 10) {
                        mTxtDiscTenDays.setVisibility(View.VISIBLE);
                        mTxtDiscTenDays.setText(getString(R.string.ten_days));
                        mTxtDiscFiveDays.setVisibility(View.GONE);
                    }
                }
            }
        }

        mItemAvailability = AdditionalDetailsActivity.mItemAvailability;

        mZipcode = AdditionalDetailsActivity.mZipcode;
        fromAustin = AdditionalDetailsActivity.fromAustin;
        mItemPrice = ListItemFragment.mItemPrice;
        mTxtItemPrice.setText("$ " + mItemPrice + "/day");
        mMinRental = ListItemFragment.mMinRental;
        mTxtItemRental.setText(mMinRental + " days");

        mItemDesc = AdditionalDetailsActivity.mItemDesc;
        mTxtItemDesc.setText(mItemDesc);
        mProductTitle = ListItemFragment.mProductName;

        if (productId.isEmpty()) {
            mTxtProductTitle.setText(ListItemFragment.mProductName);
        } else {
            mTxtProductTitle.setText(ListItemFragment.productTitle);
        }
        //TODO product images should be taken from amazon s3 bucket ; yet to be done

        UserProdImages mUserProdImages;

        mUserProdImages = new UserProdImages("https://s3.ap-south-1.amazonaws.com/recircleimages/1398934243000_1047081.jpg",
                "2017-02-04T13:13:09.000Z");
        listUploadItemImage.add(mUserProdImages);
        uploadGalleryImages = UploadImgActivity.listUploadGalleryImage;

        final ListItemImageAdapter mListItemImageAdapter = new ListItemImageAdapter
                (ListItemSummaryActivity.this, selectedImgPosition, uploadGalleryImages,
                        new ListItemImageAdapter.OnImageItemClickListener() {
                            @Override
                            public void onImageClick(int position, String userProdImages) {
                                View view = mRecyclerView.getChildAt(position);
                                view.setBackground(ContextCompat.getDrawable(ListItemSummaryActivity.this, R.drawable.selected_image_background));
                                //TODO recycler view scroll issue is yet to be solved
                                for (int i = 0; i < uploadGalleryImages.size(); i++) {
                                    view = mRecyclerView.getChildAt(i);

                                    if (i != position) {
                                        view.setBackground(ContextCompat.getDrawable(ListItemSummaryActivity.this, R.drawable.custom_imageview));
                                    }
                                }
                                selectedImgPosition = position;
                                mLayoutManager.scrollToPosition(position);
                            }
                        });

        mLayoutManager = new LinearLayoutManager(ListItemSummaryActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListItemImageAdapter);
    }

    /**
     * api call for list an item
     */
    private void getListAnItem() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayout.setAlpha((float) 0.6);
        if (productId == null) {
            mProductTitle = "";
            Log.v("---Disc_Sum_list_call", listDiscounts.size() + "");

            listAnItemRequest = new ListAnItemRequest(productId, mProductTitle, mItemPrice, mMinRental,
                    mItemDesc, listDiscounts, listUploadItemImage, mItemAvailability, mZipcode, fromAustin);
        } else {
            productId = "";
            listAnItemRequest = new ListAnItemRequest(productId, mProductTitle, mItemPrice, mMinRental,
                    mItemDesc, listDiscounts, listUploadItemImage, mItemAvailability, mZipcode, fromAustin);
        }

        if (ApiClient.getClient(ListItemSummaryActivity.this) != null) {
            service = ApiClient.getClient(ListItemSummaryActivity.this).create(RCAPInterface.class);

            if (MyProfileActivity.isItemEdit) {

                Log.v("Edit product data", ListItemFragment.editProduct.toString());

                Call<EditProduct> productsCall = service.editUserProductDetails("Bearer " + mAccessToken, ListItemFragment.editProduct);
                productsCall.enqueue(new Callback<EditProduct>() {
                    @Override
                    public void onResponse(Call<EditProduct> call, Response<EditProduct> response) {
                        if (response.isSuccessful()) {
                            RCLog.showToast(getApplicationContext(), getString(R.string.details_edited_successfully));
                            mProgressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(ListItemSummaryActivity.this, MyProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyProfileActivity.isItemEdit = false;
                            startActivity(intent);
                        } else if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                            mProgressBar.setVisibility(View.GONE);
                            RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.user_not_authenticated));
                        } else {
                            RCLog.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<EditProduct> call, Throwable t) {
                        RCLog.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                mTxtConfirmItem.setText(getString(R.string.list_this_item));
                Call<AllProductInfo> call = service.listAnItem("Bearer " + mAccessToken, listAnItemRequest);
                call.enqueue(new Callback<AllProductInfo>() {

                    @Override
                    public void onResponse(Call<AllProductInfo> call, Response<AllProductInfo> response) {
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);
                        if (response.isSuccessful()) {
                            RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.item_added));
                            Intent intent = new Intent(ListItemSummaryActivity.this, ListItemSuccessActivity.class);
                            String userProductId = response.body().getUser_product_id();
                            intent.putExtra(getString(R.string.product_id), userProductId);
                            startActivity(intent);
                        } else if (response.code() == RCWebConstants.RC_ERROR_CODE_BAD_REQUEST) {

                            RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.product_creation_failed));
                        } else if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                            mProgressBar.setVisibility(View.GONE);
                            RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.user_not_authenticated));
                            logInDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<AllProductInfo> call, Throwable t) {
                        RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.product_not_created));
                    }
                });
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Login again dialog
     */
    private void logInDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayout.setAlpha((float) 0.6);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_again_dialog);
        dialog.setTitle(getString(R.string.log_in_again));
        final EditText mEditTxtUserName = (EditText) dialog.findViewById(R.id.edit_login_email_dialog);
        final EditText mEditTxtPwd = (EditText) dialog.findViewById(R.id.edit_login_pwd_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_user_log_in_dialog);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setAlpha((float) 0.6);
                final String mUserName = mEditTxtUserName.getText().toString();
                final String mUserPwd = Base64Encryption.encrypt(mEditTxtPwd.getText().toString());
                LogInRequest logInRequest = new LogInRequest(mUserName, mUserPwd);

                if (ApiClient.getClient(ListItemSummaryActivity.this) != null) {
                    service = ApiClient.getClient(ListItemSummaryActivity.this).create(RCAPInterface.class);
                    Call<User> userCall = service.userLogIn(logInRequest);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            mProgressBar.setVisibility(View.GONE);
                            mLinearLayout.setAlpha((float) 1.0);

                            if (response.isSuccessful()) {
                                RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.user_logged_in));
                                mAccessToken = response.body().getToken();
                                mUserId = response.body().getUser_id();
                                mUserEmail = response.body().getEmail();
                                mUserFirstName = response.body().getFirst_name();
                                mUserEmail = response.body().getEmail();
                                mUserLastName = response.body().getLast_name();
                                mAccessToken = response.body().getToken();
                                mUserImage = response.body().getUser_image_url();
                                mUserMobNo = response.body().getUser_mob_no();

                                if (null != mUserId && null != mUserName &&
                                        null != mUserFirstName && null != mUserLastName && null != mAccessToken) {
                                    saveUserData();
                                }
                                dialog.dismiss();
                            } else {
                                if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                                    RCLog.showToast(ListItemSummaryActivity.this, getString(R.string.err_credentials));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            mLinearLayout.setAlpha((float) 1.0);
                        }
                    });
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        dialog.show();
    }

    private void saveUserData() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_PASSWORD, mPassword);
            editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, true);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_EMAIL, mUserEmail);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_FIRSTNAME, mUserFirstName);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_LASTNAME, mUserLastName);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_IMAGE, mUserImage);
            editor.putLong(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_MOB_NO, mUserMobNo);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * imagebutton to edit product details
     *
     * @param view
     */
    @OnClick(R.id.img_edit)
    public void imgEdit(View view) {
        finish();
    }

    /**
     * button for confirm item
     *
     * @param view
     */
    @OnClick(R.id.btn_confirm_item)
    public void btnConfirmItem(View view) {
        getListAnItem();
    }

    /**
     * textview to show unavailable dates
     *
     * @param view
     */
    @OnClick(R.id.txt_show_dates)
    public void txtShowDates(View view) {
        Intent intent = new Intent(ListItemSummaryActivity.this, ListCalendarSummaryActivity.class);
        startActivity(intent);
    }

    /**
     * action bar back button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
