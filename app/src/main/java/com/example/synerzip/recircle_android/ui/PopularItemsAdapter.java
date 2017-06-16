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
    private OnItemClickListener onItemClickListener;

    public PopularItemsAdapter(Context mContext, ArrayList<Products> popularProductsList,
                               OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.popularProductsList = popularProductsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PopularItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_prod_row, viewGroup, false);
        return new PopularItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularItemsAdapter.ViewHolder viewHolder, int position) {

        Products popularProducts = popularProductsList.get(position);

        if (popularProducts.getUser_product_info().getUser_prod_images().size() != 0 &&
                popularProducts.getUser_product_info().getUser_prod_images().get(0)
                        .getUser_prod_image_url() != null) {
            Picasso.with(mContext)
                    .load(popularProducts.getUser_product_info()
                            .getUser_prod_images().get(0)
                            .getUser_prod_image_url())
                    .placeholder(R.mipmap.ic_item)
                    .into(viewHolder.mImageView);
        }
        if (null != popularProducts.getUser_product_info().getProduct_avg_rating() &&
                popularProducts.getUser_product_info().getUser_prod_reviews() != null
                && popularProducts.getUser_product_info().getUser_prod_reviews().size() != 0) {
            viewHolder.mRatingBar.setRating(Float.parseFloat(popularProducts.getUser_product_info()
                    .getProduct_avg_rating()));
        } else {
            viewHolder.mRatingBar.setVisibility(View.GONE);
        }

        viewHolder.mTxtProductTitle.setText(popularProducts.getProduct_info().getProduct_title());
        viewHolder.mTxtProductPrice.setText("$" + popularProducts.getUser_product_info().getPrice_per_day() + "/day");
        viewHolder.mTxtRenterName.setText(popularProducts.getUser_info().getFirst_name()
                + " " + popularProducts.getUser_info().getLast_name());

        viewHolder.bind(popularProductsList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (popularProductsList.size() > 6) {
            return 6;
        } else {
            return popularProductsList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtProductTitle, mTxtProductPrice, mTxtRenterName;
        private ImageView mImageView;
        private RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBarPopular);
            mImageView = (ImageView) view.findViewById(R.id.imgPopularProduct);
            mTxtProductTitle = (TextView) view.findViewById(R.id.txtPopProductTitle);
            mTxtProductPrice = (TextView) view.findViewById(R.id.txtPopProductPrice);
            mTxtRenterName = (TextView) view.findViewById(R.id.txtPopRenterName);
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