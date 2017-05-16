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
public class PopularItemsAdapter extends RecyclerView.Adapter<PopularItemsAdapter.ViewHolder> {
    private ArrayList<Products> popularProductsList;
    private Context mContext;
    private OnItemClickListener onItemClikListner;

    public PopularItemsAdapter(Context mContext, ArrayList<Products> popularProductsList, OnItemClickListener onItemClikListner) {
        this.mContext = mContext;
        this.popularProductsList = popularProductsList;
        this.onItemClikListner = onItemClikListner;
    }

    @Override
    public PopularItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_prod_row, viewGroup, false);
        return new PopularItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularItemsAdapter.ViewHolder viewHolder, int position) {

        Products popularProducts = popularProductsList.get(position);
        Picasso.with(mContext)
                .load(popularProducts.getProduct_info()
                        .getProduct_image_url())
                .placeholder(R.mipmap.ic_item)
                .into(viewHolder.mImageView);
        if (null != popularProducts.getUser_product_info().getProduct_avg_rating() &&
                Float.parseFloat(popularProducts.getUser_product_info().getProduct_avg_rating()) != 0) {
            viewHolder.mRatingBar.setRating(Float.parseFloat(popularProducts.getUser_product_info().getProduct_avg_rating()));
            viewHolder.mTxtProductRating.setText("(" + popularProducts.getUser_product_info().getProduct_avg_rating() + ")");
        } else {
            viewHolder.mRatingBar.setVisibility(View.GONE);
            viewHolder.mTxtProductRating.setVisibility(View.GONE);
        }

        viewHolder.mTxtProductTitle.setText(popularProducts.getProduct_info().getProduct_title());
        viewHolder.mTxtProductPrice.setText("$" + popularProducts.getUser_product_info().getPrice_per_day() + "/day");
        viewHolder.mTxtRenterName.setText(popularProducts.getUser_info().getFirst_name()
                + " " + popularProducts.getUser_info().getLast_name());

        viewHolder.bind(popularProductsList.get(position), onItemClikListner);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView mTxtProductTitle, mTxtProductPrice, mTxtProductRating, mTxtRenterName;
        private ImageView mImageView;
        private RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBarPopular);
            mImageView = (ImageView) view.findViewById(R.id.imgPopularProduct);
            mTxtProductTitle = (TextView) view.findViewById(R.id.txtProductTitle);
            mTxtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            mTxtProductRating = (TextView) view.findViewById(R.id.txtRating);
            mTxtRenterName = (TextView) view.findViewById(R.id.txtRenterName);
        }

        public void bind(final Products popularProducts, final OnItemClickListener onItemClikListner) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClikListner.onItemClick(popularProducts);
                }
            });
        }
    }


}