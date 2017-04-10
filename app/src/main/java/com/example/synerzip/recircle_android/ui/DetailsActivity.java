package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.example.synerzip.recircle_android.utilities.RCLog;
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

    private RCAPInterface service;
    private Products product;

    private ArrayList<UserProdImages> userProdImagesArrayList;

    private ArrayList<UserProdReview> userProdReviewArrayList;

    private ReviewsListAdapter reviewsListAdapter;

    private String link;
    private int selectedImgPosition=0;

    private ImageAdapter mImageAdapter;

    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.img_main_image)
    public ImageView mImgMain;

    @BindView(R.id.recycler_images)
    public RecyclerView mRecyclerImages;

    @BindView(R.id.txt_loading_images)
    public TextView mTxtLoadingImages;

    @BindView(R.id.img_user)
    public ImageView mImgUser;

    @BindView(R.id.txt_product_name)
    public TextView mTxtProductName;

    @BindView(R.id.txt_user_name)
    public TextView mTxtUserName;

    @BindView(R.id.txt_price)
    public TextView mTxtPrice;

    @BindView(R.id.txt_avg_rating)
    public TextView mTxtAvgRating;

    @BindView(R.id.edt_total_price)
    public EditText mEdtTotalPrice;

    @BindView(R.id.ratingbar_details)
    public RatingBar mRatingBar;

    @BindView(R.id.all_rating_avg)
    public RatingBar mRatingAllAvg;

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

    @BindView(R.id.img_previous)
    public ImageView mImgPrev;

    @BindView(R.id.img_next)
    public ImageView mImgNext;

    @BindView(R.id.progress_bar)
    public RelativeLayout mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.recircle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mProgressBar.setVisibility(View.VISIBLE);

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
        String ID = bundle.getString(getString(R.string.product_id), "");

        final Call<Products> productsCall = service.getProductDetailsByID(ID);
        productsCall.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if (response.body() != null) {

                    mTxtLoadingImages.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);

                    product = response.body();

                    userProdReviewArrayList = product.getUser_product_info().getUser_prod_reviews();

                    userProdImagesArrayList = product.getUser_product_info().getUser_prod_images();

                    reviewsListAdapter = new ReviewsListAdapter(getApplicationContext(), userProdReviewArrayList);
                    mReViewReviews.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                    mReViewReviews.setAdapter(reviewsListAdapter);

                    mImageAdapter = new ImageAdapter(getApplicationContext(), selectedImgPosition, userProdImagesArrayList, new ImageAdapter.OnImageItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onImageClick(int position, UserProdImages userProdImages) {

                            Picasso.with(getApplicationContext())
                                    .load(userProdImages.getUser_prod_image_url())
                                    .into(mImgMain);

                            View view = mRecyclerImages.getChildAt(position);

                            view.setBackground(getDrawable(R.drawable.selected_image_background));

                            for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                                view = mRecyclerImages.getChildAt(i);
                                if (i != position) {
                                    view.setBackground(getDrawable(R.drawable.custom_imageview));
                                }
                            }
                            link=userProdImages.getUser_prod_image_url();
                            selectedImgPosition=position;
                            mLayoutManager.scrollToPosition(position);

                        }
                    });

                    mLayoutManager=new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

                    mRecyclerImages.setLayoutManager(mLayoutManager);
                    mRecyclerImages.setAdapter(mImageAdapter);

                    mTxtProductName.setText(product.getProduct_info().getProduct_title());
                    Picasso.with(getApplicationContext()).load(product.getUser_info()
                            .getUser_image_url()).into(mImgUser);

                    mTxtUserName.setText(product.getUser_info().getFirst_name() + " "
                            + product.getUser_info().getLast_name());

                    mRatingBar.setRating(Integer.parseInt(product.getUser_product_info().getProduct_avg_rating()));

                    mRatingAllAvg.setRating(Integer.parseInt(product.getUser_product_info().getProduct_avg_rating()));

                    mTxtAvgRating.setText("(" + product.getUser_product_info().getProduct_avg_rating() + ")");
                    mTxtPrice.setText("$" + product.getUser_product_info().getPrice_per_day() + "/day");

                    //To calculate total for now gave 2days
                    mEdtTotalPrice.setText("$" + String.valueOf(Integer.parseInt(product.getUser_product_info().getPrice_per_day()) * 2)
                            + "  $" + product.getUser_product_info().getPrice_per_day() + "*2days");

                    mTxtDecscriptionDetail.setText(product.getProduct_info().getProduct_description());

                    mTxtConditionDetail.setText(product.getUser_product_info().getUser_prod_desc());

                    link = product.getProduct_info().getProduct_image_url();

                    Picasso.with(getApplicationContext())
                            .load(product.getProduct_info().getProduct_image_url())
                            .into(mImgMain);

                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });

        mImgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCLog.showToast(getApplicationContext(), "Open LogIn activity");

            }
        });

        mImgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsActivity.this, ZoomActivity.class);
                intent.putExtra(getString(R.string.selected_image_position),selectedImgPosition);
                intent.putParcelableArrayListExtra(getString(R.string.image_urls_array),userProdImagesArrayList);
                startActivity(intent);
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    @OnClick(R.id.img_next)
    public void showNext(){
        mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition()+1);
    }

    @OnClick(R.id.img_previous)
    public void shoePrevious(){
        mLayoutManager.scrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition()-1);

    }


}
