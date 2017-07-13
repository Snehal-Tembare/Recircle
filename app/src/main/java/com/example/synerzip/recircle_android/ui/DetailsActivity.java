package com.example.synerzip.recircle_android.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProdReview;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.messages.UserQueAnsActivity;
import com.example.synerzip.recircle_android.ui.rentitem.RentInfoActivity;
import com.example.synerzip.recircle_android.utilities.AESEncryptionDecryption;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
import com.pkmmte.view.CircularImageView;
import com.example.synerzip.recircle_android.ui.rentitem.RentInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snehal Tembare on 31/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE = "extra_image";

    public static boolean isShowInfo;

    private SharedPreferences sharedPreferences;

    private String mAccessToken;

    private String mUserName;
    private String mPassword;
    private String mUserId;
    private String mUserEmail;
    private String mUserLastName;
    private String mUserFirstName;
    private String mUserImage;
    private long mUserMobNo;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.img_main_image)
    protected ImageView mImgMain;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerImages;

    @BindView(R.id.img_user)
    protected CircularImageView mImgUser;

    @BindView(R.id.txt_product_name)
    protected TextView mTxtProductName;

    @BindView(R.id.txt_user_name)
    protected TextView mTxtUserName;

    @BindView(R.id.btn_price)
    protected Button mBtnPrice;

    @BindView(R.id.txt_details_rating_count)
    protected TextView mTxtDetailsRateCount;

    @BindView(R.id.ratingbar_details)
    protected RatingBar mDetailsRating;

    @BindView(R.id.all_ratingsbar)
    protected RatingBar mRatingAllAvg;

    @BindView(R.id.txt_all_reviews_count)
    protected TextView mTxtAvgRatingCount;

    @BindView(R.id.txt_ask_question)
    protected TextView mTxtAskQuestion;

    @BindView(R.id.expand_txt_description)
    protected ExpandableTextView mTxtDecscriptionDetail;

    @BindView(R.id.txt_desc_see_more)
    protected TextView mTxtDescSeeMore;

    @BindView(R.id.expand_txt_condition)
    protected ExpandableTextView mTxtConditionDetail;

    @BindView(R.id.txt_condition_see_more)
    protected TextView mTxtConditionSeeMore;

    @BindView(R.id.list_reviews)
    protected RecyclerView mReViewReviews;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.scrollView)
    protected NestedScrollView mScrollView;

    @BindView(R.id.txt_see_all_reviews)
    protected TextView mTxtSeeAllReviews;

    @BindView(R.id.layout_renters_review)
    protected LinearLayout mLayoutRentersReview;

    @BindView(R.id.collapsible_toolbar)
    protected CollapsingToolbarLayout mCollapsibleLayout;

    @BindView(R.id.appbarlayout)
    protected AppBarLayout mAppBarLayout;

    @BindView(R.id.details_parent_linear_layout)
    protected LinearLayout mParentLayout;

    @BindView(R.id.layout_images)
    protected LinearLayout mLayoutImages;

    private RCAPInterface service;
    private Products product;
    private ArrayList<UserProdImages> userProdImagesArrayList;
    private ArrayList<UserProdReview> userProdReviewArrayList;
    private ArrayList<UserProductUnAvailability> userProductUnAvailabilities;
    private ReviewsListAdapter reviewsListAdapter;
    private int selectedImgPosition = 0;
    private ImageAdapter mImageAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoggedIn;
    private String productId;
    private Intent askQueActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        init();
    }


    /**
     * Initialize views
     */

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.recircle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mUserId = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);

        mProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ViewCompat.setTransitionName(findViewById(R.id.appbarlayout), EXTRA_IMAGE);

        mCollapsibleLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        mTxtDescSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtDecscriptionDetail.toggle();
                mTxtDescSeeMore.setText(mTxtDecscriptionDetail.isExpanded() ? R.string.see_more : R.string.view_less);
            }
        });

        mTxtConditionSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtConditionDetail.toggle();
                mTxtConditionSeeMore.setText(mTxtConditionDetail.isExpanded() ? R.string.see_more : R.string.view_less);
            }
        });

        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);

            Bundle bundle = getIntent().getExtras();
            productId = bundle.getString(getString(R.string.product_id), "");

            final Call<Products> productsCall = service.getProductDetailsByID(productId);

            productsCall.enqueue(new Callback<Products>() {
                @Override
                public void onResponse(Call<Products> call, Response<Products> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response != null) {
                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            product = response.body();

                            if (product != null) {

                                //Disable ask question for self listed item
                                if (mUserId.equalsIgnoreCase(product.getUser_info().getUser_id())) {
                                    mTxtAskQuestion.setVisibility(View.GONE);
                                } else {
                                    mTxtAskQuestion.setVisibility(View.VISIBLE);
                                }

                                if (product.getUser_product_info().getUser_prod_reviews() != null
                                        && product.getUser_product_info().getUser_prod_reviews().size() != 0) {
                                    userProdReviewArrayList = product.getUser_product_info().getUser_prod_reviews();
                                    mLayoutRentersReview.setVisibility(View.VISIBLE);
                                    reviewsListAdapter = new ReviewsListAdapter(getApplicationContext(), userProdReviewArrayList);

                                    if (userProdReviewArrayList.size() > 0) {
                                        mTxtSeeAllReviews.setVisibility(View.VISIBLE);
                                        mTxtSeeAllReviews.setText(getString(R.string.see_all_reviews) + " (" + userProdReviewArrayList.size() + ")");
                                    }
                                    mReViewReviews.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                                    mReViewReviews.setAdapter(reviewsListAdapter);
                                } else {
                                    mTxtSeeAllReviews.setVisibility(View.GONE);
                                    mLayoutRentersReview.setVisibility(View.GONE);
                                }
                                if (product.getUser_product_info().getUser_prod_images() != null
                                        && product.getUser_product_info().getUser_prod_images().size() > 1) {
                                    mLayoutImages.setVisibility(View.VISIBLE);
                                    userProdImagesArrayList = product.getUser_product_info().getUser_prod_images();

                                    mImageAdapter = new ImageAdapter(getApplicationContext(), selectedImgPosition, userProdImagesArrayList, new ImageAdapter.OnImageItemClickListener() {
                                        @Override
                                        public void onImageClick(int position, UserProdImages userProdImages) {

                                            Picasso.with(DetailsActivity.this)
                                                    .load(userProdImages.getUser_prod_image_url())
                                                    .placeholder(R.drawable.ic_camera)
                                                    .into(mImgMain);

                                            View view = mRecyclerImages.getChildAt(position);

                                            if (view != null) {
                                                view.setBackground(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.selected_image_background));
                                            }

                                            for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                                                view = mRecyclerImages.getChildAt(i);
                                                if (i != position) {
                                                    if (view != null) {
                                                        view.setBackground(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.custom_imageview));
                                                    }
                                                }
                                            }
                                            selectedImgPosition = position;
                                            mLayoutManager.scrollToPosition(position);

                                        }
                                    });

                                    mLayoutManager = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

                                    mRecyclerImages.setLayoutManager(mLayoutManager);
                                    mRecyclerImages.setAdapter(mImageAdapter);
                                } else {
                                    mLayoutImages.setVisibility(View.GONE);
                                }

                                if (product.getUser_product_info().getUser_prod_unavailability() != null
                                        && product.getUser_product_info().getUser_prod_unavailability().size() != 0) {
                                    userProductUnAvailabilities = product.getUser_product_info().getUser_prod_unavailability();
                                }

                                mTxtProductName.setText(product.getProduct_info().getProduct_title());

                                mCollapsibleLayout.setTitle(product.getProduct_info().getProduct_title());

                                Picasso.with(getApplicationContext())
                                        .load(product.getUser_info().getUser_image_url())
                                        .placeholder(R.drawable.ic_user).into(mImgUser);

                                mTxtUserName.setText(product.getUser_info().getFirst_name() + " "
                                        + product.getUser_info().getLast_name());

                                if (product.getUser_product_info().getProduct_avg_rating() != null) {
                                    mDetailsRating.setVisibility(View.VISIBLE);
                                    mRatingAllAvg.setVisibility(View.VISIBLE);
                                    mTxtDetailsRateCount.setVisibility(View.VISIBLE);
                                    mTxtAvgRatingCount.setVisibility(View.VISIBLE);

                                    mDetailsRating.setRating(Float.parseFloat(product.getUser_product_info().getProduct_avg_rating()));
                                    mRatingAllAvg.setRating(Float.parseFloat(product.getUser_product_info().getProduct_avg_rating()));
                                    mTxtDetailsRateCount.setText("(" + product.getUser_product_info().getProduct_avg_rating() + ")");
                                    mTxtAvgRatingCount.setText("(" + String.valueOf(product.getUser_product_info().getProduct_avg_rating()) + ")");
                                } else {
                                    mDetailsRating.setVisibility(View.GONE);
                                    mRatingAllAvg.setVisibility(View.GONE);
                                    mTxtDetailsRateCount.setVisibility(View.GONE);
                                    mTxtAvgRatingCount.setVisibility(View.GONE);
                                }

                                if (product.getUser_product_info().getPrice_per_day() != null) {
                                    mBtnPrice.setText(getString(R.string.rent_item_at) + product.getUser_product_info().getPrice_per_day() + "/day");
                                }

                                if (product.getProduct_info().getProduct_description() != null) {
                                    mTxtDecscriptionDetail.setText(product.getProduct_info().getProduct_description());
                                }

                                if (product.getUser_product_info().getUser_prod_desc() != null) {
                                    mTxtConditionDetail.setText(product.getUser_product_info().getUser_prod_desc());
                                }

                                if (product.getUser_product_info().getUser_prod_images() != null
                                        && product.getUser_product_info().getUser_prod_images().size() != 0) {
                                    Picasso.with(getApplicationContext())
                                            .load(product.getUser_product_info()
                                                    .getUser_prod_images().get(0).getUser_prod_image_url())
                                            .placeholder(R.drawable.ic_camera)
                                            .into(mImgMain);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Products> call, Throwable t) {

                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

        mTxtDecscriptionDetail.getViewTreeObserver().

                addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        final int lineCount = mTxtDecscriptionDetail.getLayout().getLineCount();
                        if (lineCount > 4) {
                            mTxtDescSeeMore.setVisibility(View.VISIBLE);
                        } else {
                            mTxtDescSeeMore.setVisibility(View.GONE);
                        }
                        return true;
                    }
                });

        mTxtConditionDetail.getViewTreeObserver().

                addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        final int lineCount = mTxtDecscriptionDetail.getLayout().getLineCount();
                        if (lineCount > 4) {
                            mTxtConditionSeeMore.setVisibility(View.VISIBLE);
                        } else {
                            mTxtConditionSeeMore.setVisibility(View.GONE);
                        }
                        return true;
                    }
                });

        mTxtAskQuestion.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                askQueActivityIntent = new Intent(DetailsActivity.this, UserQueAnsActivity.class);
                askQueActivityIntent.putExtra(getString(R.string.product_id), productId);
                askQueActivityIntent.putExtra(getString(R.string.product_title), product.getProduct_info().getProduct_title());

                if (!isLoggedIn) {
                    RCLog.showToast(DetailsActivity.this, getString(R.string.user_must_login));
                    logInDialog();
                } else {
                    startActivity(askQueActivityIntent);
                }

            }
        });

        mImgMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ZoomActivity.class);
                intent.putExtra(getString(R.string.selected_image_position), selectedImgPosition);
                intent.putParcelableArrayListExtra(getString(R.string.image_urls_array), userProdImagesArrayList);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Disable ask question for self listed item
        if (product != null) {
            if (mUserId.equalsIgnoreCase(product.getUser_info().getUser_id())) {
                mTxtAskQuestion.setVisibility(View.GONE);
            } else {
                mTxtAskQuestion.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Login again dialog
     */
    private void logInDialog() {
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
                mParentLayout.setAlpha((float) 0.6);
                final String mLogInUserName = mEditTxtUserName.getText().toString();
                final String mLogInPwd = mEditTxtPwd.getText().toString();
                LogInRequest logInRequest = new LogInRequest(mLogInUserName, mLogInPwd);

                if (ApiClient.getClient(DetailsActivity.this) != null) {
                    service = ApiClient.getClient(DetailsActivity.this).create(RCAPInterface.class);

                    Call<User> userCall = service.userLogIn(logInRequest);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            mProgressBar.setVisibility(View.GONE);
                            mParentLayout.setAlpha((float) 1.0);

                            if (response.isSuccessful()) {
                                mAccessToken = response.body().getToken();
                                mUserId = response.body().getUser_id();
                                mUserName = response.body().getEmail();
                                mUserFirstName = response.body().getFirst_name();
                                mUserEmail = response.body().getEmail();
                                mUserLastName = response.body().getLast_name();
                                mAccessToken = response.body().getToken();
                                mUserImage = response.body().getUser_image_url();
                                mUserMobNo = response.body().getUser_mob_no();

                                if (null != mUserId && null != mLogInUserName &&
                                        null != mUserFirstName && null != mUserLastName && null != mAccessToken) {
                                    saveUserData();
                                }
                                dialog.dismiss();

                                startActivity(askQueActivityIntent);
                            } else {
                                if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                                    RCLog.showToast(DetailsActivity.this, getString(R.string.err_credentials));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            mParentLayout.setAlpha((float) 1.0);
                        }
                    });
                } else {
                    RCLog.showLongToast(DetailsActivity.this, getString(R.string.check_nw_connectivity));
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
            String encryptedPassword = AESEncryptionDecryption.encrypt(android_id, mPassword);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_PASSWORD, encryptedPassword);
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
     * action bar back button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CalendarActivity.isDateSelected = false;
        RentInfoActivity.isDateEdited = false;

        if (CalendarActivity.selectedDates != null && CalendarActivity.selectedDates.size() != 0) {
            CalendarActivity.selectedDates.clear();
        }
    }

    /**
     * Show next Image
     */

    @OnClick(R.id.img_next)
    public void showNextImage() {
        mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
    }

    /**
     * Show previous Image
     */

    @OnClick(R.id.img_previous)
    public void showPreviousImage() {
        mLayoutManager.scrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition() - 1);
    }

    /**
     * Scroll to show store address
     */

    @OnClick(R.id.txt_see_store_address)
    public void scrollLayout() {
        mScrollView.scrollTo(0, mScrollView.getBottom());
    }

    /**
     * Opens Map for Store address
     */

    @OnClick(R.id.layout_see_on_map)
    public void openMap() {
        startActivity(new Intent(DetailsActivity.this, MapActivity.class));
    }

    /**
     * Opens AllReviewsActivity
     */

    @OnClick(R.id.txt_see_all_reviews)
    public void showAllReviews() {
        Intent intent = new Intent(this, AllReviewsActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.all_reviews_list), userProdReviewArrayList);
        startActivity(intent);
    }

    /**
     * Show Rent Information
     */
    @OnClick(R.id.btn_price)
    public void showCalendarActivity() {
        isShowInfo = true;
        Intent intent = new Intent(DetailsActivity.this, CalendarActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.unavail_dates), userProductUnAvailabilities);
        intent.putExtra(getString(R.string.product), product);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(DetailsActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
