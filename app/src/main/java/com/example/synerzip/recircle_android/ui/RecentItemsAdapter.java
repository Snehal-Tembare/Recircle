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
import com.example.synerzip.recircle_android.models.ProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 10/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class RecentItemsAdapter extends RecyclerView.Adapter<RecentItemsAdapter.ViewHolder> {
    private ArrayList<ProductDetails> productDetailsList;
    private Context mContext;

    public RecentItemsAdapter(Context mContext, ArrayList<ProductDetails> productDetailsList) {
        this.mContext = mContext;
        this.productDetailsList = productDetailsList;
    }

    @Override
    public RecentItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_prod_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentItemsAdapter.ViewHolder viewHolder, int position) {

        ProductDetails productDetails = productDetailsList.get(position);

        Picasso.with(mContext)
                .load(productDetails.getProduct_info().getProduct_image_url())
                .placeholder(R.mipmap.ic_item)
                .into(viewHolder.mImageView);

        viewHolder.mRatingBar.setRating(Float.parseFloat(productDetails.getUser_product_info().getProduct_avg_rating()));
        viewHolder.mTxtProductTitle.setText(productDetails.getProduct_info().getProduct_title());
        viewHolder.mTxtProductPrice.setText("$" + productDetails.getUser_product_info().getPrice_per_day() + "/day");
        viewHolder.mTxtProductRating.setText("(" + productDetails.getUser_product_info().getProduct_avg_rating() + ")");
        viewHolder.mTxtRenterName.setText(productDetails.getUser_info().getFirst_name()
                + " " + productDetails.getUser_info().getLast_name());
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView mTxtProductTitle, mTxtProductPrice, mTxtProductRating, mTxtRenterName;
        ImageView mImageView;
        RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            mImageView = (ImageView) view.findViewById(R.id.img_recent_product);
            mTxtProductTitle = (TextView) view.findViewById(R.id.txtProductTitle);
            mTxtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            mTxtProductRating = (TextView) view.findViewById(R.id.txtRating);
            mTxtRenterName = (TextView) view.findViewById(R.id.txtRenterName);
        }
    }
}