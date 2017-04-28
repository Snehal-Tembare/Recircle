package com.example.synerzip.recircle_android.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorTreeAdapter;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProdReview;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

public class Details extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final String EXTRA_IMAGE = "extra_image";
    public static boolean isFromNextActivity = false;
    public static int total;

    private RCAPInterface service;
    private Products product;

    private ArrayList<UserProdImages> userProdImagesArrayList;

    private ArrayList<UserProdReview> userProdReviewArrayList;

    private ReviewsListAdapter reviewsListAdapter;

    private int selectedImgPosition = 0;

    private ImageAdapter mImageAdapter;

    private LinearLayoutManager mLayoutManager;

    private Date fromDate;
    private Date toDate;
    private String formatedFromDate;
    private String formatedToDate;
    private int dayCount;

    private int pricePerDay;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.img_main_image)
    protected ImageView mImgMain;

    @BindView(R.id.recycler_images)
    protected RecyclerView mRecyclerImages;

    @BindView(R.id.img_user)
    protected ImageView mImgUser;

    @BindView(R.id.txt_product_name)
    protected TextView mTxtProductName;

    @BindView(R.id.txt_user_name)
    protected TextView mTxtUserName;

    @BindView(R.id.btn_price)
    protected Button mBtnPrice;

    @BindView(R.id.txt_datails_rating_count)
    protected TextView mTxtDetailsRateCount;

    @BindView(R.id.ratingbar_details)
    protected RatingBar mDetailsRating;

    @BindView(R.id.all_ratingsbar)
    protected RatingBar mRatingAllAvg;

    @BindView(R.id.txt_all_reviews_count)
    protected TextView mTxtAvgRatingCount;

    @BindView(R.id.imgbtn_help)
    protected ImageButton mImgHelp;

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

    @BindView(R.id.collapsible_toolbar)
    protected CollapsingToolbarLayout mCollapsibleLayout;

    @BindView(R.id.appbarlayout)
    protected AppBarLayout mAppBarLayout;

    @BindView(R.id.btn_select_dates)
    protected Button mBtnSelectDates;

    @BindView(R.id.details_parent_linear_layout)
    protected LinearLayout mParentLayout;

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

        service = ApiClient.getClient().create(RCAPInterface.class);
        Bundle bundle = getIntent().getExtras();
        String productId = bundle.getString(getString(R.string.product_id), "");

        final Call<Products> productsCall = service.getProductDetailsByID(productId);

        productsCall.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response != null) {
                        mProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        product = response.body();

                        if (product.getUser_product_info().getUser_prod_reviews() != null
                                && product.getUser_product_info().getUser_prod_reviews().size() != 0
                                && product.getUser_product_info().getUser_prod_images() != null
                                && product.getUser_product_info().getUser_prod_images().size() != 0) {

                            userProdReviewArrayList = product.getUser_product_info().getUser_prod_reviews();

                            userProdImagesArrayList = product.getUser_product_info().getUser_prod_images();

                            reviewsListAdapter = new ReviewsListAdapter(getApplicationContext(), userProdReviewArrayList);
                            if (userProdReviewArrayList.size() > 0) {
                                mTxtSeeAllReviews.setVisibility(View.VISIBLE);
                                mTxtSeeAllReviews.setText(getString(R.string.see_all_reviews) + " (" + userProdReviewArrayList.size() + ")");
                            }
                            mReViewReviews.setLayoutManager(new LinearLayoutManager(Details.this));
                            mReViewReviews.setAdapter(reviewsListAdapter);

                            mImageAdapter = new ImageAdapter(getApplicationContext(), selectedImgPosition, userProdImagesArrayList, new ImageAdapter.OnImageItemClickListener() {
                                @Override
                                public void onImageClick(int position, UserProdImages userProdImages) {

                                    Picasso.with(Details.this).load(userProdImages.getUser_prod_image_url()).into(mImgMain);

                                    View view = mRecyclerImages.getChildAt(position);

                                    view.setBackground(ContextCompat.getDrawable(Details.this, R.drawable.selected_image_background));

                                    for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                                        view = mRecyclerImages.getChildAt(i);
                                        if (i != position) {
                                            view.setBackground(ContextCompat.getDrawable(Details.this, R.drawable.custom_imageview));
                                        }
                                    }
                                    selectedImgPosition = position;
                                    mLayoutManager.scrollToPosition(position);

                                }
                            });

                            mLayoutManager = new LinearLayoutManager(Details.this, LinearLayoutManager.HORIZONTAL, false);

                            mRecyclerImages.setLayoutManager(mLayoutManager);
                            mRecyclerImages.setAdapter(mImageAdapter);

                            mTxtProductName.setText(product.getProduct_info().getProduct_title());

                            mCollapsibleLayout.setTitle(product.getProduct_info().getProduct_title());

                            Picasso.with(getApplicationContext()).load(product.getUser_info()
                                    .getUser_image_url()).into(mImgUser);

                            mTxtUserName.setText(product.getUser_info().getFirst_name() + " "
                                    + product.getUser_info().getLast_name());

                            mDetailsRating.setRating(Integer.parseInt(product.getUser_product_info().getProduct_avg_rating()));

                            mRatingAllAvg.setRating(Integer.parseInt(product.getUser_product_info().getProduct_avg_rating()));

                            mTxtDetailsRateCount.setText("(" + product.getUser_product_info().getProduct_avg_rating() + ")");

                            mTxtAvgRatingCount.setText("(" + product.getUser_product_info().getProduct_avg_rating() + ")");

                            mBtnPrice.setText("$" + product.getUser_product_info().getPrice_per_day() + "/day");

                            pricePerDay = Integer.parseInt(product.getUser_product_info().getPrice_per_day());

                            mTxtDecscriptionDetail.setText(product.getProduct_info().getProduct_description());

                            mTxtConditionDetail.setText(product.getUser_product_info().getUser_prod_desc());

                            Picasso.with(getApplicationContext())
                                    .load(product.getProduct_info().getProduct_image_url())
                                    .into(mImgMain);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });

        mTxtDecscriptionDetail.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int lineCount = mTxtDecscriptionDetail.getLayout().getLineCount();
                Log.v("Count", "onPreDraw" + lineCount);
                if (lineCount > 4) {
                    mTxtDescSeeMore.setVisibility(View.VISIBLE);
                } else {
                    mTxtDescSeeMore.setVisibility(View.GONE);
                }
                return true;
            }
        });

        mTxtConditionDetail.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int lineCount = mTxtDecscriptionDetail.getLayout().getLineCount();
                Log.v("Count", "onPreDraw" + lineCount);
                if (lineCount > 4) {
                    mTxtConditionSeeMore.setVisibility(View.VISIBLE);
                } else {
                    mTxtConditionSeeMore.setVisibility(View.GONE);
                }
                return true;
            }
        });

        mImgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog loginDialog = new Dialog(Details.this);
                loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loginDialog.setContentView(R.layout.activity_log_in);
                Toolbar toolbar = (Toolbar) loginDialog.findViewById(R.id.toolbar);
                toolbar.setVisibility(View.GONE);
                loginDialog.show();
            }
        });

        mImgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, ZoomActivity.class);
                intent.putExtra(getString(R.string.selected_image_position), selectedImgPosition);
                intent.putParcelableArrayListExtra(getString(R.string.image_urls_array), userProdImagesArrayList);
                startActivity(intent);
            }
        });


    }

    /**
     * OnClick of home button
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * Get selected dates
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
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

                long diff = toDate.getTime() - fromDate.getTime();
                dayCount = (int) diff / (24 * 60 * 60 * 1000);

                total = (int) Math.abs(dayCount) * Integer.parseInt(product.getUser_product_info().getPrice_per_day());

                if (total != 0) {
                    mBtnPrice.setText(" $" + (int) total);
                    mBtnPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_48, 0);
                    mBtnSelectDates.setText(getString(R.string.confirm));
                    isFromNextActivity = true;
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFromNextActivity) {
            mBtnSelectDates.setText(getString(R.string.confirm));
            if (RentInfoActivity.isDateChanged) {
                mBtnPrice.setText(" $" + total);
                mBtnPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_48, 0);
            }

        } else {
            mBtnSelectDates.setText(getString(R.string.select_dates));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        total = 0;
        RentInfoActivity.isDateChanged=false;
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
     * Opens Calendar to select dates
     */

    @OnClick(R.id.btn_select_dates)
    public void openCalendar() {

        if (mBtnSelectDates.getText().equals(getString(R.string.confirm))) {
            Intent infoIntent = new Intent(this, RentInfoActivity.class);
            infoIntent.putExtra(getString(R.string.product), product);
            infoIntent.putExtra(getString(R.string.from_date), formatedFromDate);
            infoIntent.putExtra(getString(R.string.to_date), formatedToDate);
            infoIntent.putExtra(getString(R.string.days_count), dayCount);
            infoIntent.putExtra(getString(R.string.total), total);

            startActivity(infoIntent);
        } else {
            Intent intent = new Intent(Details.this, CalendarActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    /**
     * Scroll to show store address
     */

    @OnClick(R.id.txt_see_store_address)
    public void scrollLayout() {
        mScrollView.scrollTo(0, mScrollView.getHeight());
    }

    /**
     * Opens Map for Store address
     */

    @OnClick(R.id.layout_see_on_map)
    public void openMap() {
        startActivity(new Intent(Details.this, MapActivity.class));
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
    public void showPriceInfo() {
        if (mBtnSelectDates.getText().equals(getString(R.string.confirm))) {
            Intent infoIntent = new Intent(this, RentInfoActivity.class);
            infoIntent.putExtra(getString(R.string.product), product);
            infoIntent.putExtra(getString(R.string.from_date), formatedFromDate);
            infoIntent.putExtra(getString(R.string.to_date), formatedToDate);
            infoIntent.putExtra(getString(R.string.days_count), dayCount);
            infoIntent.putExtra(getString(R.string.total), total);

            startActivity(infoIntent);
        } else {
            RCLog.showToast(getApplicationContext(), getString(R.string.select_dates));
        }

    }

}
