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
import com.example.synerzip.recircle_android.models.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 10/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class RecentItemsAdapter extends RecyclerView.Adapter<RecentItemsAdapter.ViewHolder> {
    private ArrayList<Products> productDetailsList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public RecentItemsAdapter(Context mContext, ArrayList<Products> productDetailsList, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.productDetailsList = productDetailsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecentItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_prod_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentItemsAdapter.ViewHolder viewHolder, int position) {

        Products productDetails = productDetailsList.get(position);

        Picasso.with(mContext)
                .load(productDetails.getProduct_info().getProduct_image_url())
                .placeholder(R.mipmap.ic_item)
                .into(viewHolder.mImageView);

        if (null != productDetails.getUser_product_info().getProduct_avg_rating() &&
                productDetails.getUser_product_info().getUser_prod_reviews()!=null &&
                productDetails.getUser_product_info().getUser_prod_reviews().size() != 0) {
            viewHolder.mRatingBar.setRating(Float.parseFloat(productDetails.getUser_product_info().getProduct_avg_rating()));
            viewHolder.mTxtProductReviews.setText("(" + productDetails.getUser_product_info().getUser_prod_reviews().size() + ")");
        } else {
            viewHolder.mRatingBar.setVisibility(View.GONE);
            viewHolder.mTxtProductReviews.setVisibility(View.GONE);
        }

        viewHolder.mTxtProductTitle.setText(productDetails.getProduct_info().getProduct_title());
        viewHolder.mTxtProductPrice.setText("$" + productDetails.getUser_product_info().getPrice_per_day() + "/day");
        viewHolder.mTxtRenterName.setText(productDetails.getUser_info().getFirst_name()
                + " " + productDetails.getUser_info().getLast_name());

        viewHolder.bind(productDetailsList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        if(productDetailsList.size()>6){
            return 6;
        }
        else{
            return productDetailsList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtProductTitle, mTxtProductPrice, mTxtProductReviews, mTxtRenterName;
        ImageView mImageView;
        RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            mImageView = (ImageView) view.findViewById(R.id.img_recent_product);
            mTxtProductTitle = (TextView) view.findViewById(R.id.txtProductTitle);
            mTxtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            mTxtProductReviews = (TextView) view.findViewById(R.id.txtReviews);
            mTxtRenterName = (TextView) view.findViewById(R.id.txtRenterName);
        }

        public void bind(final Products products, final OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(products);
                }
            });
        }
    }
}