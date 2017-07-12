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
 * Created by Prajakta Patil on 12/7/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private ArrayList<Products> popularProducts;
    private ArrayList<Products> recentProducts;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private String source;
    private final int MAX_ROWS_DISPLAY=6;

    public ProductsAdapter(Context mContext, String source,
                           ArrayList<Products> popularProducts,
                           ArrayList<Products> recentProducts,
                           OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.popularProducts = popularProducts;
        this.recentProducts = recentProducts;
        this.onItemClickListener = onItemClickListener;
        this.source = source;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_prod_row, viewGroup, false);
        return new ProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder viewHolder, int position) {
        if (source.equalsIgnoreCase("PopularProducts")) {
            Products pop = popularProducts.get(position);
            if (pop.getUser_product_info().getUser_prod_images().size() != 0 &&
                    pop.getUser_product_info().getUser_prod_images().get(0)
                            .getUser_prod_image_url() != null) {
                Picasso.with(mContext)
                        .load(pop.getUser_product_info()
                                .getUser_prod_images().get(0)
                                .getUser_prod_image_url())
                        .placeholder(R.mipmap.ic_item)
                        .into(viewHolder.mImageView);
            }
            if (null != pop.getUser_product_info().getProduct_avg_rating() &&
                    pop.getUser_product_info().getUser_prod_reviews() != null
                    && pop.getUser_product_info().getUser_prod_reviews().size() != 0) {
                viewHolder.mRatingBar.setRating(Float.parseFloat(pop.getUser_product_info()
                        .getProduct_avg_rating()));
            } else {
                viewHolder.mRatingBar.setVisibility(View.GONE);
            }

            viewHolder.mTxtProductTitle.setText(pop.getProduct_info().getProduct_title());
            viewHolder.mTxtProductPrice.setText("$" + pop.getUser_product_info().getPrice_per_day() + "/day");
            viewHolder.mTxtRenterName.setText(pop.getUser_info().getFirst_name()
                    + " " + pop.getUser_info().getLast_name());

            viewHolder.bind(pop, onItemClickListener);
        } else {
            Products recent = recentProducts.get(position);

            if (recent.getUser_product_info().getUser_prod_images().size() != 0 &&
                    recent.getUser_product_info().getUser_prod_images().get(0)
                            .getUser_prod_image_url() != null) {
                Picasso.with(mContext)
                        .load(recent.getUser_product_info()
                                .getUser_prod_images().get(0)
                                .getUser_prod_image_url())
                        .placeholder(R.mipmap.ic_item)
                        .into(viewHolder.mImageView);
            }
            if (null != recent.getUser_product_info().getProduct_avg_rating() &&
                    recent.getUser_product_info().getUser_prod_reviews() != null
                    && recent.getUser_product_info().getUser_prod_reviews().size() != 0) {
                viewHolder.mRatingBar.setRating(Float.parseFloat(recent.getUser_product_info()
                        .getProduct_avg_rating()));
            } else {
                viewHolder.mRatingBar.setVisibility(View.GONE);
            }

            viewHolder.mTxtProductTitle.setText(recent.getProduct_info().getProduct_title());
            viewHolder.mTxtProductPrice.setText("$" + recent.getUser_product_info().getPrice_per_day() + "/day");
            viewHolder.mTxtRenterName.setText(recent.getUser_info().getFirst_name()
                    + " " + recent.getUser_info().getLast_name());

            viewHolder.bind(recent, onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (source.equalsIgnoreCase("PopularProducts")) {
            if (popularProducts.size() > MAX_ROWS_DISPLAY) {
                return MAX_ROWS_DISPLAY;
            } else {
                return popularProducts.size();
            }
        } else {
            if (recentProducts.size() > MAX_ROWS_DISPLAY) {
                return MAX_ROWS_DISPLAY;
            } else {
                return recentProducts.size();
            }
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