package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProductDetails;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Snehal Tembare on 9/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<UserProductDetails> productDetailsList;
    private Context mContext;
    private MyProfileActivity.OnItemClickListener onItemClickListener;

    public ItemAdapter(Context mContext, ArrayList<UserProductDetails> productDetailsList,
                       MyProfileActivity.OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.productDetailsList = productDetailsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_prod_row, viewGroup, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {

        final UserProductDetails productDetails = productDetailsList.get(position);

        if (productDetails.getUser_prod_images().size() != 0 &&
                productDetails.getUser_prod_images().get(0).getUser_prod_image_url() != null) {
            Picasso.with(mContext)
                    .load(productDetails.getUser_prod_images().get(0).getUser_prod_image_url())
                    .placeholder(R.drawable.ic_camera)
                    .into(holder.mImageView);
        }

        if (0 != productDetails.getProduct_avg_rating()) {
            holder.mRatingBar.setRating(productDetails.getProduct_avg_rating());
        } else {
            holder.mRatingBar.setVisibility(View.GONE);
        }

        holder.mTxtProductTitle.setText(productDetails.getProduct_title());
        holder.mTxtProductPrice.setText("$" + productDetails.getPrice_per_day() + "/" + mContext.getString(R.string.day));

        holder.mImgEditItemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileActivity.isItemEdit = true;
                Intent intent = new Intent((mContext), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.v("ItemAdapter",productDetails.getUser_product_id()+"");
                intent.putExtra(mContext.getString(R.string.product_id), productDetails.getUser_product_id());
                mContext.startActivity(intent);
            }
        });

        holder.bind(productDetailsList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (productDetailsList == null) {
            return 0;
        }
        return productDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtProductTitle;
        TextView mTxtProductPrice;
        ImageView mImageView;
        ImageView mImgEditItemDetails;
        RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            mImageView = (ImageView) view.findViewById(R.id.img_recent_product);
            mTxtProductTitle = (TextView) view.findViewById(R.id.txtProductTitle);
            mTxtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            view.findViewById(R.id.txtRenterName).setVisibility(View.GONE);
            mImgEditItemDetails = (ImageView) view.findViewById(R.id.img_edit_item);
            mImgEditItemDetails.setVisibility(View.VISIBLE);
        }

        public void bind(final UserProductDetails products, final MyProfileActivity.OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onProductClick(products);
                }
            });
        }
    }
}