package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.PopularProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 10/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class PopularItemsAdapter extends RecyclerView.Adapter<PopularItemsAdapter.ViewHolder> {
    private ArrayList<PopularProducts> popularProductsList;
    private Context mContext;

    public PopularItemsAdapter(Context mContext, ArrayList<PopularProducts> popularProductsList) {
        this.mContext = mContext;
        this.popularProductsList = popularProductsList;
    }

    @Override
    public PopularItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_prod_row_layout, viewGroup, false);
        return new PopularItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularItemsAdapter.ViewHolder viewHolder, int position) {

        PopularProducts popularProducts = popularProductsList.get(position);
        Picasso.with(mContext)
                .load(popularProducts.getProduct_info()
                        .getProduct_image_url())
                .placeholder(R.mipmap.ic_item)
                .into(viewHolder.mImageView);
        viewHolder.mRatingBar.setRating(Float.parseFloat(popularProducts.getUser_product_info().getProduct_avg_rating()));
        viewHolder.mTxtProductTitle.setText(popularProducts.getProduct_info().getProduct_title());
        viewHolder.mTxtProductPrice.setText("$"+popularProducts.getUser_product_info().getPrice_per_day()+"/day");
        viewHolder.mTxtProductRating.setText("(" + popularProducts.getUser_product_info().getProduct_avg_rating() + ")");
        viewHolder.mTxtRenterName.setText(popularProducts.getUser_info().getFirst_name()
                + " " + popularProducts.getUser_info().getLast_name());
    }

    @Override
    public int getItemCount() {
        return popularProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView mTxtProductTitle, mTxtProductPrice, mTxtProductRating, mTxtRenterName;
        private ImageView mImageView;
        private RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mRatingBar=(RatingBar)view.findViewById(R.id.ratingBarPopular) ;
            mImageView = (ImageView) view.findViewById(R.id.img_product);
            mTxtProductTitle = (TextView) view.findViewById(R.id.txt_product_title);
            mTxtProductPrice = (TextView) view.findViewById(R.id.txt_product_price);
            mTxtProductRating = (TextView) view.findViewById(R.id.txt_rating);
            mTxtRenterName = (TextView) view.findViewById(R.id.txt_renter_name);
        }
    }
}