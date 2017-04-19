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
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProdReview;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
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

public class DetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final String EXTRA_IMAGE = "extra_image";

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
    private float dayCount;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.img_main_image)
    public ImageView mImgMain;

    @BindView(R.id.recycler_images)
    public RecyclerView mRecyclerImages;

    @BindView(R.id.img_user)
    public ImageView mImgUser;

    @BindView(R.id.txt_product_name)
    public TextView mTxtProductName;

    @BindView(R.id.txt_user_name)
    public TextView mTxtUserName;

    @BindView(R.id.txt_price)
    public TextView mTxtPrice;

    @BindView(R.id.txt_datails_rating_count)
    public TextView mTxtDetailsRateCount;

    @BindView(R.id.edt_total_price)
    public EditText mEdtTotalPrice;

    @BindView(R.id.txt_total_label)
    public TextView mTxtTotalLabel;

    @BindView(R.id.ratingbar_details)
    public RatingBar mDetailsRating;

    @BindView(R.id.all_ratingsbar)
    public RatingBar mRatingAllAvg;

    @BindView(R.id.txt_all_reviews_count)
    public TextView mTxtAvgRatingCount;

    @BindView(R.id.imgbtn_help)
    public ImageButton mImgHelp;

    @BindView(R.id.expand_txt_description)
    public ExpandableTextView mTxtDecscriptionDetail;

    @BindView(R.id.txt_desc_see_more)
    public TextView mTxtDescSeeMore;

    @BindView(R.id.expand_txt_condition)
    public ExpandableTextView mTxtConditionDetail;

    @BindView(R.id.txt_condition_see_more)
    public TextView mTxtConditionSeeMore;

    @BindView(R.id.list_reviews)
    public RecyclerView mReViewReviews;

    @BindView(R.id.progress_bar)
    public RelativeLayout mProgressBar;

    @BindView(R.id.edt_enter_dates)
    public EditText mEdtDates;

    @BindView(R.id.scrollView)
    public NestedScrollView mScrollView;

    @BindView(R.id.txt_see_all_reviews)
    public TextView mTxtSeeAllReviews;

    @BindView(R.id.collapsible_toolbar)
    public CollapsingToolbarLayout mCollapsibleLayout;

    @BindView(R.id.appbarlayout)
    public AppBarLayout mAppBarLayout;

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
                            mReViewReviews.addItemDecoration(new DividerItemDecoration(DetailsActivity.this, LinearLayoutManager.VERTICAL));
                            mReViewReviews.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                            mReViewReviews.setAdapter(reviewsListAdapter);

                            mImageAdapter = new ImageAdapter(getApplicationContext(), selectedImgPosition, userProdImagesArrayList, new ImageAdapter.OnImageItemClickListener() {
                                @Override
                                public void onImageClick(int position, UserProdImages userProdImages) {

                                    Picasso.with(getApplicationContext())
                                            .load(userProdImages.getUser_prod_image_url())
                                            .into(mImgMain, new com.squareup.picasso.Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    Bitmap bitmap = ((BitmapDrawable) mImgMain.getDrawable()).getBitmap();
                                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                                        public void onGenerated(Palette palette) {
                                                            applyPalette(palette);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });

                                    View view = mRecyclerImages.getChildAt(position);

                                    view.setBackground(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.selected_image_background));

                                    for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                                        view = mRecyclerImages.getChildAt(i);
                                        if (i != position) {
                                            view.setBackground(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.custom_imageview));
                                        }
                                    }
                                    selectedImgPosition = position;
                                    mLayoutManager.scrollToPosition(position);

                                }
                            });

                            mLayoutManager = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

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

                            mTxtPrice.setText("$" + product.getUser_product_info().getPrice_per_day() + "/day");

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

        mImgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog loginDialog = new Dialog(DetailsActivity.this);
                loginDialog.setTitle(getString(R.string.log_in));
                loginDialog.setContentView(R.layout.log_in_dialog);
                loginDialog.show();
            }
        });

        mImgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ZoomActivity.class);
                intent.putExtra(getString(R.string.selected_image_position), selectedImgPosition);
                intent.putParcelableArrayListExtra(getString(R.string.image_urls_array), userProdImagesArrayList);
                startActivity(intent);
            }
        });


    }

    private void applyPalette(Palette palette) {
        int primaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        int primary = ContextCompat.getColor(this, R.color.colorPrimary);
        mCollapsibleLayout.setContentScrimColor(palette.getMutedColor(primary));
        mCollapsibleLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
        supportFinishAfterTransition();
        supportPostponeEnterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

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

                long diff = toDate.getTime() - fromDate.getTime();
                dayCount = (float) diff / (24 * 60 * 60 * 1000);

                float total = dayCount * Integer.parseInt(product.getUser_product_info().getPrice_per_day());
                if (total != 0) {
                    mEdtTotalPrice.setVisibility(View.VISIBLE);
                    mTxtTotalLabel.setVisibility(View.VISIBLE);
                    mEdtTotalPrice.setText(" $" + (int) total + "   ($"
                            + product.getUser_product_info().getPrice_per_day() +
                            " * " + (int) dayCount + getString(R.string.days) + ")");
                }

            }
        }
    }

    @OnClick(R.id.img_next)
    public void showNextImage() {
        mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
    }

    @OnClick(R.id.img_previous)
    public void showPreviousImage() {
        mLayoutManager.scrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition() - 1);
    }

    @OnClick(R.id.edt_enter_dates)
    public void openCalendar() {
        Intent intent = new Intent(DetailsActivity.this, CalendarActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.txt_see_store_address)
    public void scrollLayout() {
        mScrollView.scrollTo(0, mScrollView.getBottom());
    }

    @OnClick(R.id.layout_see_on_map)
    public void openMap() {
        startActivity(new Intent(DetailsActivity.this, MapActivity.class));
    }

    @OnClick(R.id.txt_see_all_reviews)
    public void showAllReviews() {
        Intent intent = new Intent(this, AllReviewsActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.all_reviews_list), userProdReviewArrayList);
        startActivity(intent);
    }

}
